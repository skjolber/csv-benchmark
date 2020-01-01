package com.github.skjolber.csv.bean.sampletime;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.csv.CsvParser;

import com.github.skjolber.csv.CsvMapperLookup;
import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.param.CsvParam;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;

public abstract class AbstractSampleTimeBeanBenchmark<T> {
	
	protected DataFile dataFile;
	protected com.github.skjolber.stcsv.databinder.CsvMapper<T> mapper;
	protected org.simpleflatmapper.csv.CsvParser.MapToDSL<T> mapToDsl;
	protected CsvParserSettings settings;
	
    @Setup
    public void setupSimpleFlatMapper(Blackhole blackhole, CsvParam csvParam) {
    	this.mapToDsl = getMapToDsl();
        this.settings = getSettings(blackhole);
        this.mapper = CsvMapperLookup.getMapper(dataFile.getCls(), dataFile.isQuotes());
    }

	private CsvParserSettings getSettings(Blackhole blackhole) {
		CsvParserSettings settings = new CsvParserSettings();

        //turning off features enabled by default
        settings.setIgnoreLeadingWhitespaces(false);
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setSkipEmptyLines(false);
        settings.setColumnReorderingEnabled(false);
        settings.setReadInputOnSeparateThread(false);

        settings.setProcessor(new AbstractRowProcessor() {
            @Override
            public void rowProcessed(String[] row, ParsingContext context) {
                blackhole.consume(row);
            }
        });
        return settings;
	}	
	
    @Benchmark
    public void sesseltjonnaCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {
    	Reader reader = getReader(csvParam);
    	try {
    		com.github.skjolber.stcsv.CsvReader<T> factory = mapper.create(reader);
	
			do {
				T bean = factory.next();
				if(bean == null) {
					break;
				}
				blackhole.consume(bean);
			} while(true);
    	} finally {
    		reader.close();
    	}
    }


    @Benchmark
    public void simpleFlatMapper(Blackhole blackhole, CsvParam csvParam) throws IOException {
         try(Reader reader = getReader(csvParam)) {
        	 mapToDsl.forEach(reader, blackhole::consume);
        }
    }

    //@Benchmark
    public void univocityConcurrent(Blackhole blackhole, CsvParam csvParam) throws IOException {
        com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
        try(InputStream in = getInputStream(csvParam)) {
            parser.parse(in);
        }        
    }
    
    @Benchmark
    public void univocity(Blackhole blackhole, CsvParam csvParam) throws IOException {
        com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
        try(Reader reader = getReader(csvParam)) {
            parser.parse(reader);
        }
    }

	private Reader getReader(CsvParam csvParam) throws IOException {
		return csvParam.getReader(dataFile);
	}
    
	private InputStream getInputStream(CsvParam csvParam) throws IOException {
		return csvParam.getInputStream(dataFile);
	}

	protected org.simpleflatmapper.csv.CsvParser.MapToDSL<T> getMapToDsl() {
		org.simpleflatmapper.csv.CsvParser.DSL dsl = CsvParser.bufferSize(4 * 1024);
    	org.simpleflatmapper.csv.CsvParser.MapToDSL<T> mapToDSL = dsl.mapTo(dataFile.getCls());
		return mapToDSL;
	}	
}
