package com.github.skjolber.csv.strings.singleshot;

import java.io.IOException;
import java.io.Reader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.param.CsvParam;

/**
 * 
 * Attempt to include as much as possible of the classloading within the method.
 * 
 */

public abstract class AbstractSingleShotCsvStringsParserBenchmark {
	
	protected DataFile dataFile;
	
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
    	org.simpleflatmapper.csv.CsvParser.DSL dsl = org.simpleflatmapper.csv.CsvParser.bufferSize(4 * 1024);
    	try(Reader reader = getReader(csvParam)) {
    		org.simpleflatmapper.lightningcsv.CsvReader csvReader = dsl.reader(reader);
            csvReader.parseRow(org.simpleflatmapper.lightningcsv.parser.StringArrayCellConsumer.newInstance(vals -> {
            	org.simpleflatmapper.lightningcsv.parser.CellConsumer cellConsumer = new org.simpleflatmapper.lightningcsv.parser.StringArrayCellConsumerNoCopyFixedLength<>(blackhole::consume, vals.length);
                blackhole.consume(vals);
                csvReader.parseAll(cellConsumer);
            }));
        }
    }

    @Benchmark
    public void sesseltjonnaCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {
        try(Reader reader = getReader(csvParam)) {
        	com.github.skjolber.stcsv.CsvReader<String[]> csvReader = com.github.skjolber.stcsv.sa.StringArrayCsvReader.builder().build(reader);
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
    	com.univocity.parsers.csv.CsvParserSettings settings = new com.univocity.parsers.csv.CsvParserSettings();

        //turning off features enabled by default
        settings.setIgnoreLeadingWhitespaces(false);
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setSkipEmptyLines(false);
        settings.setColumnReorderingEnabled(false);
        settings.setReadInputOnSeparateThread(false);

        settings.setProcessor(new com.univocity.parsers.common.processor.AbstractRowProcessor() {
            @Override
            public void rowProcessed(String[] row, com.univocity.parsers.common.ParsingContext context) {
                blackhole.consume(row);
            }
        });

        com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
        try(Reader reader = getReader(csvParam)) {
            parser.parse(reader);
        }
    }    
    
	private Reader getReader(CsvParam csvParam) throws IOException {
		return csvParam.getReader(dataFile);
	}

}
