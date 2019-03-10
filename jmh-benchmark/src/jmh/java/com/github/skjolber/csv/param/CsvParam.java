package com.github.skjolber.csv.param;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.CharArrayReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.util.CheckedConsumer;

import com.github.skjolber.csv.DataFile;

@State(Scope.Benchmark)
public class CsvParam {

	//@Param(value={"1", "10","1000", "2500", "5000", "10000", "25000", "50000", "100000", "250000", "500000", "1000000"})
	// @Param(value={"1", "10","100", "250", "500", "1000", "2500", "5000", "10000", "25000", "50000", "100000", "250000", "500000", "1000000"})
	//@Param(value={"1", "10","100", "250", "500", "1000", "2500", "5000", "10000", "25000", "50000", "100000", "250000", "500000", "1000000"})
	//@Param(value={"10000", "25000", "50000", "100000", "250000", "500000", "1000000"}) 
	@Param(value={"10000"})
	public int nbRows = 1;

	public ExecutorService executorService;

	protected final static Map<File, char[]> characters = new ConcurrentHashMap<>();;
	protected final static Map<File, byte[]> bytes = new ConcurrentHashMap<>();;

	@Setup
	public void setUp() throws IOException {

		for (DataFile dataFile : DataFile.values()) {
			File inputFile = dataFile.getInputFile();
			File outputFile = dataFile.getOutputFile(nbRows);
			if(!outputFile.exists()) {
				rewriteFile(nbRows, outputFile, inputFile, dataFile.getCharset(), dataFile.isQuotes() ? CsvParam::getQuotesRewriter : CsvParam::getRewriter);
			}
			
			FileInputStream in = new FileInputStream(outputFile);
			try {
				byte[] byteArray = IOUtils.toByteArray(in);

				characters.put(outputFile, new String(byteArray, StandardCharsets.UTF_8).toCharArray());	
				
				bytes.put(outputFile, byteArray);
			} finally {
				in.close();
			}			
		}		
		
		executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);
			}
		});
	}

	@TearDown
	public void tearDown() {
		executorService.shutdown();
	}

	public Reader getReader(DataFile input) throws IOException {
		return new CharArrayReader(characters.get(input.getOutputFile(nbRows)));
	}

	public InputStream getInputStream(DataFile input) throws IOException {
		return new ByteArrayInputStream(bytes.get(input.getOutputFile(nbRows)));
	}

	private static void rewriteFile(int nbRows, File file, File fileZip, Charset charset, Function<Writer, CheckedConsumer<String[]>> rewriterFunction) throws IOException {
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				Writer writer = new OutputStreamWriter(bos, "UTF-8")) {

			CheckedConsumer<String[]> rewriter = rewriterFunction.apply(writer);
			
			CheckedConsumerWithCounter checkedConsumerWithCounter = new CheckedConsumerWithCounter(rewriter);
			
			while(checkedConsumerWithCounter.getCount() < nbRows) {
				ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
				ZipEntry zipEntry = zis.getNextEntry();
				while(zipEntry != null){
					BufferedInputStream bis = new BufferedInputStream(zis);
	
					CsvParser.DSL dsl = CsvParser.dsl().quote('"');
					org.simpleflatmapper.lightningcsv.CsvReader reader = dsl.reader(new InputStreamReader(bis, charset));
	
					if(checkedConsumerWithCounter.getCount() > 0) {
						reader.skipRows(1);
					}
					reader.read(checkedConsumerWithCounter, nbRows - checkedConsumerWithCounter.getCount());
					
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
				zis.close();
			}
		}
	}
	
	private static class CheckedConsumerWithCounter implements CheckedConsumer<String[]> {

		private int count = 0;
		
		private CheckedConsumer<String[]> delegate;
		
		public CheckedConsumerWithCounter(CheckedConsumer<String[]> delegate) {
			super();
			this.delegate = delegate;
		}

		@Override
		public void accept(String[] t) throws Exception {
			delegate.accept(t);
			count++;
		}
		
		public int getCount() {
			return count;
		}
		
	};

	private static CheckedConsumer<String[]> getQuotesRewriter(Writer writer) {
		return (row) -> {
			for (int i = 0; i < row.length; i++) {
				String cell = row[i];
				if (i > 0) {
					writer.write(",");
				}
				writer.write("\"");

				for (int j = 0; j < cell.length(); j++) {
					char c = cell.charAt(j);
					if (c == '"') {
						writer.append('"');
					}
					writer.append(c);
				}
				writer.write("\"");
			}
			writer.write("\n");
		};
	}

	private static CheckedConsumer<String[]> getRewriter(Writer writer) {
		return (row) -> {
			for (int i = 0; i < row.length; i++) {
				String cell = row[i];
				if (i > 0) {
					writer.write(",");
				}
				writer.write(cell.replace("\"", "").replace("\n", "").replace(",", ""));
			}
			writer.write("\n");
		};
	}

}
