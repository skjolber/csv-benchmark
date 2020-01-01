package com.github.skjolber.csv.bean.singleshot;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import com.github.skjolber.csv.DataFile;
import com.github.skjolber.csv.StopTime;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class SingleShotGtfsStopTimeBean extends AbstractSingleShotBeanBenchmark<StopTime> {
	
	public SingleShotGtfsStopTimeBean() {
		this.dataFile = DataFile.GTFS_STOPTIME;
	}

}
