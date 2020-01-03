package com.github.skjolber.csv.strings.sampletime;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Timeout;
import org.openjdk.jmh.annotations.Warmup;

import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.BenchmarkConstants;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SampleTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(time=BenchmarkConstants.warmupTimeInSeconds, timeUnit=TimeUnit.SECONDS,iterations=1)
@Measurement(time=BenchmarkConstants.measurementTimeInSeconds, timeUnit=TimeUnit.SECONDS,iterations=1)
@Timeout(timeUnit=TimeUnit.MINUTES, time=60)
public class SampleTimeQuotedGtfsStopTimeStrings extends AbstractCsvSampleTimeStringsParserBenchmark {
	
	public SampleTimeQuotedGtfsStopTimeStrings() {
		this.dataFile = DataFile.GTFS_STOPTIME;
	}

}
