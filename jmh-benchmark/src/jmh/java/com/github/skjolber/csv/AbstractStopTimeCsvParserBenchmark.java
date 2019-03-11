package com.github.skjolber.csv;

import org.openjdk.jmh.annotations.Setup;

import com.github.skjolber.stcsv.CsvMapper;

public abstract class AbstractStopTimeCsvParserBenchmark extends AbstractCsvParserBenchmark<StopTime> {
	
	@Setup
	public void setupSesseltjonnaCsv() throws Exception {
		
		plain = CsvMapper.builder(StopTime.class)
			.stringField("stop_id")
				.required()
			.stringField("trip_id")
				.required()
			.stringField("arrival_time")
			    //.consumer((object, array, start, end) -> object.setArrivalTime(StopTimeFieldMappingFactory.getStringAsSeconds(new String(array, start, end - start))))
				.optional()
			.stringField("departure_time")
				//.consumer((object, array, start, end) -> object.setDepartureTime(StopTimeFieldMappingFactory.getStringAsSeconds(new String(array, start, end - start))))
				.optional()
			.integerField("timepoint")
				.optional()
			.integerField("stop_sequence")
				.optional()
			.stringField("stop_headsign")
				.optional()
			.stringField("route_short_name")
				.optional()
			.integerField("pickup_type")
				.optional()
			.integerField("drop_off_type")
				.optional()
			.doubleField("shape_dist_traveled")
				.optional()
			.stringField("fare_period_id")
				.optional()
			.build();
		
		quotes = CsvMapper.builder(StopTime.class)
				.stringField("stop_id")
					.quoted()
					.required()
				.stringField("trip_id")
					.quoted()
					.required()
				.stringField("arrival_time")
				    //.consumer((object, array, start, end) -> object.setArrivalTime(StopTimeFieldMappingFactory.getStringAsSeconds(new String(array, start, end - start))))
					.quoted()
					.optional()
				.stringField("departure_time")
					//.consumer((object, array, start, end) -> object.setDepartureTime(StopTimeFieldMappingFactory.getStringAsSeconds(new String(array, start, end - start))))
					.quoted()
					.optional()
				.integerField("timepoint")
					.quoted()
					.optional()
				.integerField("stop_sequence")
					.quoted()
					.optional()
				.stringField("stop_headsign")
					.quoted()
					.optional()
				.stringField("route_short_name")
					.quoted()
					.optional()
				.integerField("pickup_type")
					.quoted()
					.optional()
				.integerField("drop_off_type")
					.quoted()
					.optional()
				.doubleField("shape_dist_traveled")
					.quoted()
					.optional()
				.stringField("fare_period_id")
					.quoted()
					.optional()
				.build();		
		
	}

}
