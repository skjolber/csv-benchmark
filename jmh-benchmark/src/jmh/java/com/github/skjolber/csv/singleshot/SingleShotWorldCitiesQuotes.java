package com.github.skjolber.csv.singleshot;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.github.skjolber.csv.AbstractCityCsvParserBenchmark;
import com.github.skjolber.csv.DataFile;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SingleShotWorldCitiesQuotes extends AbstractCityCsvParserBenchmark {
	
	public SingleShotWorldCitiesQuotes() {
		this.dataFile = DataFile.WORLD_CITIES_WITH_QUOTES;
	}

}
