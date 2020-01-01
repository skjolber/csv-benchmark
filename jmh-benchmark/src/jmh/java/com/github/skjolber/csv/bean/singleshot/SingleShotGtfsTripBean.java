package com.github.skjolber.csv.bean.singleshot;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.Trip;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SingleShotGtfsTripBean extends AbstractSingleShotBeanBenchmark<Trip> {
	
	public SingleShotGtfsTripBean() {
		this.dataFile = DataFile.GTFS_TRIP;
	}

}
