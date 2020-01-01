package com.github.skjolber.csv.bean.singleshot;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

import com.github.skjolber.csv.CsvMapperLookup;
import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.param.CsvParam;

/**
 * Attempt to include as much as possible of the classloading within the method.
 * 
 */

public abstract class AbstractSingleShotBeanBenchmark<T> {
	
	protected DataFile dataFile;

    @Benchmark
    public void sesseltjonnaCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {
    	Reader reader = getReader(csvParam);
    	try {
    		com.github.skjolber.stcsv.CsvReader<T> factory = (com.github.skjolber.stcsv.CsvReader<T>) CsvMapperLookup.getMapper(dataFile.getCls(), dataFile.isQuotes()).create(reader);
	
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
    	org.simpleflatmapper.csv.CsvParser.MapToDSL<T> mapToDSL = getMapToDsl();
    	
        try(Reader reader = getReader(csvParam)) {
            mapToDSL.forEach(reader, blackhole::consume);
        }
    }

	protected org.simpleflatmapper.csv.CsvParser.MapToDSL<T> getMapToDsl() {
		org.simpleflatmapper.csv.CsvParser.DSL dsl = org.simpleflatmapper.csv.CsvParser.bufferSize(4 * 1024);
    	org.simpleflatmapper.csv.CsvParser.MapToDSL<T> mapToDSL = dsl.mapTo(dataFile.getCls());
		return mapToDSL;
	}
    
    //@Benchmark
    public void univocityConcurrent(Blackhole blackhole, CsvParam csvParam) throws IOException {
    	com.univocity.parsers.csv.CsvParserSettings settings = new com.univocity.parsers.csv.CsvParserSettings();
        
        //turning off features enabled by default
        settings.setIgnoreLeadingWhitespaces(false);
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setSkipEmptyLines(false);
        settings.setColumnReorderingEnabled(false);
        settings.setReadInputOnSeparateThread(true);

        settings.setProcessor(new com.univocity.parsers.common.processor.BeanProcessor<T>(dataFile.getCls()) {
            @Override
            public void beanProcessed(T bean, com.univocity.parsers.common.ParsingContext context) {
                blackhole.consume(bean);
            }
        });

        com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
        try(InputStream in = getInputStream(csvParam)) {
            parser.parse(in);
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

        settings.setProcessor(new com.univocity.parsers.common.processor.BeanProcessor<T>(dataFile.getCls()) {
            @Override
            public void beanProcessed(T bean, com.univocity.parsers.common.ParsingContext context) {
                blackhole.consume(bean);
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
    
	private InputStream getInputStream(CsvParam csvParam) throws IOException {
		return csvParam.getInputStream(dataFile);
	}

}
