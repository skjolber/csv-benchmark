package com.github.skjolber.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.infra.Blackhole;
import org.simpleflatmapper.csv.CsvParser;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.skjolber.csv.param.CsvParam;
import com.github.skjolber.csv.scan.CsvClassFactory;
import com.github.skjolber.csv.scan.CsvClassMapping;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.BeanProcessor;
import com.univocity.parsers.csv.CsvParserSettings;

public abstract class AbstractCsvParserBenchmark<T> {
	
	protected CsvClassMapping<T> plain;
	protected CsvClassMapping<T> quotes;
	
	protected CsvParser.DSL dsl;
	protected CsvParser.MapToDSL<T> mapToDSL;

	protected CsvMapper csvMapperToStringArray;
	protected ObjectReader cityReader;
	
	protected DataFile dataFile;
    
    @Setup
    public void setupJackson() {
        csvMapperToStringArray = new CsvMapper();
        csvMapperToStringArray.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY);

        CsvMapper csvMapperToCity = new CsvMapper();

        csvMapperToCity.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();

        cityReader = csvMapperToCity.readerFor(dataFile.getCls()).with(bootstrapSchema);
    }    
    
    @Setup
    public void setupSimpleFlatMapper() {
        dsl = CsvParser.bufferSize(4 * 1024);
        mapToDSL = dsl.mapTo(dataFile.getCls());
    }

    @Benchmark
    public void sesseltjonnaCsv(Blackhole blackhole, CsvParam csvParam) throws Exception {
    	Reader reader = getReader(csvParam);
    	try {
			CsvClassFactory<T> factory;
			if(!dataFile.isQuotes()) {
				factory = plain.create(reader);
			} else {
				factory = quotes.create(reader);
			}
	
			do {
				T city = factory.next();
				if(city == null) {
					break;
				}
				blackhole.consume(city);
			} while(true);
    	} finally {
    		reader.close();
    	}
    }

    @Benchmark
    public void simpleFlatMapper(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = getReader(csvParam)) {
            mapToDSL.forEach(reader, blackhole::consume);
        }
    }
    
    //@Benchmark
    public void univocityConcurrent(Blackhole blackhole, CsvParam csvParam) throws IOException {
        CsvParserSettings settings = new CsvParserSettings();
        
        //turning off features enabled by default
        settings.setIgnoreLeadingWhitespaces(false);
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setSkipEmptyLines(false);
        settings.setColumnReorderingEnabled(false);
        settings.setReadInputOnSeparateThread(true);

        settings.setProcessor(new BeanProcessor<T>(dataFile.getCls()) {
            @Override
            public void beanProcessed(T bean, ParsingContext context) {
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
        CsvParserSettings settings = new CsvParserSettings();

        //turning off features enabled by default
        settings.setIgnoreLeadingWhitespaces(false);
        settings.setIgnoreTrailingWhitespaces(false);
        settings.setSkipEmptyLines(false);
        settings.setColumnReorderingEnabled(false);
        settings.setReadInputOnSeparateThread(false);

        settings.setProcessor(new BeanProcessor<T>(dataFile.getCls()) {
            @Override
            public void beanProcessed(T bean, ParsingContext context) {
                blackhole.consume(bean);
            }
        });

        com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
        try(Reader reader = getReader(csvParam)) {
            parser.parse(reader);
        }
    }
    
    //@Benchmark
    public void jackson(Blackhole blackhole, CsvParam csvParam) throws IOException {
        try(Reader reader = getReader(csvParam)) {
            MappingIterator<City> iterator = cityReader.readValues(reader);

            while (iterator.hasNext()) {
                blackhole.consume(iterator.next());
            }
        }
    }

	private Reader getReader(CsvParam csvParam) throws IOException {
		return csvParam.getReader(dataFile);
	}
    
	private InputStream getInputStream(CsvParam csvParam) throws IOException {
		return csvParam.getInputStream(dataFile);
	}

}
