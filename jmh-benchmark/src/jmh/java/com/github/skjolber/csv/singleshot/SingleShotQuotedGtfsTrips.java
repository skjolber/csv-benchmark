package com.github.skjolber.csv.singleshot;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import com.github.skjolber.csv.AbstractTripCsvParserBenchmark;
import com.github.skjolber.csv.DataFile;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SingleShotQuotedGtfsTrips extends AbstractTripCsvParserBenchmark {
	
	public SingleShotQuotedGtfsTrips() {
		this.dataFile = DataFile.GTFS_TRIP_WITH_QUOTES;
	}
	
}
