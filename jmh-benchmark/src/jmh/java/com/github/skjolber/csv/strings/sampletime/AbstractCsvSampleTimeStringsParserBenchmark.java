package com.github.skjolber.csv.strings.sampletime;

import java.io.IOException;
import java.io.Reader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.csv.CsvParser;
import org.simpleflatmapper.lightningcsv.CsvReader;
import org.simpleflatmapper.lightningcsv.parser.CellConsumer;
import org.simpleflatmapper.lightningcsv.parser.StringArrayCellConsumer;
import org.simpleflatmapper.lightningcsv.parser.StringArrayCellConsumerNoCopyFixedLength;

import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.param.CsvParam;
import com.github.skjolber.stcsv.sa.StringArrayCsvReader;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;

public abstract class AbstractCsvSampleTimeStringsParserBenchmark {
	
	protected DataFile dataFile;
	protected CsvParser.DSL dsl;
	protected CsvParserSettings settings;
	
    @Setup
    public void setupSimpleFlatMapper(Blackhole blackhole, CsvParam csvParam) {
        dsl = CsvParser.bufferSize(4 * 1024);
        
        settings = new CsvParserSettings();

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
    }
	/*
    @Benchmark
    public void sfmCallback(Blackhole blackhole, CsvParam csvParam) throws IOException {
    	CsvParser.DSL dsl = CsvParser.bufferSize(4 * 1024);
        try(Reader reader = getReader(csvParam)) {
            dsl.reader(reader).read(blackhole::consume);
        }
    }

    @Benchmark
    public void sfmIterate(Blackhole blackhole, CsvParam csvParam) throws IOException {
    	CsvParser.DSL dsl = CsvParser.bufferSize(4 * 1024);
        try(Reader reader = getReader(csvParam)) {
            for(String[] row : dsl.reader(reader)) {
                blackhole.consume(row);
            }
        }
    }
    */

	// using the 'same array' sfm benchmark seems to be fastest
    @Benchmark
    public void simpleFlatMapper(Blackhole blackhole, CsvParam csvParam) throws IOException {
    	try(Reader reader = getReader(csvParam)) {
            CsvReader csvReader = dsl.reader(reader);
            csvReader.parseRow(StringArrayCellConsumer.newInstance(vals -> {
                CellConsumer cellConsumer = new StringArrayCellConsumerNoCopyFixedLength<>(blackhole::consume, vals.length);
                blackhole.consume(vals);
                csvReader.parseAll(cellConsumer);
            }));
        }
    }

    @Benchmark
    public void sesseltjonnaCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {
        try(Reader reader = getReader(csvParam)) {
        	com.github.skjolber.stcsv.CsvReader<String[]> csvReader = StringArrayCsvReader.builder().build(reader);
            String[] next;
            do {
                next = csvReader.next();
                if(next == null) {
                    break;
                }
                blackhole.consume(next);
            } while(true);
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

}
