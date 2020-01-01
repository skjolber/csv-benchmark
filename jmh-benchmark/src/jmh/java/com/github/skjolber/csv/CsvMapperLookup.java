package com.github.skjolber.csv;

import com.github.skjolber.stcsv.databinder.CsvMapper;

/**
 * Simple utility to avoid repeating benchmark classes too much. 
 * 
 */

public class CsvMapperLookup {

	public static <T> com.github.skjolber.stcsv.databinder.CsvMapper<T> getMapper(Class<T> cls, boolean quoted) {
		if(cls == City.class) {
			if(quoted) {
				return (CsvMapper<T>) getCityParserForQuotes();
			}
			return (CsvMapper<T>) getCityParserForPlain();
		} else if(cls == StopTime.class) {
			if(quoted) {
				return (CsvMapper<T>) getStopTimeParserForQuotes();
			}
			return (CsvMapper<T>) getStopTimeParserForPlain();
		} else if(cls == Trip.class) {
			if(quoted) {
				return (CsvMapper<T>) getTripParserForQuotes();
			}
			return (CsvMapper<T>) getTripParserForPlain();
		}
		throw new RuntimeException();
	}
	
	
	protected static  com.github.skjolber.stcsv.databinder.CsvMapper<Trip> getTripParserForPlain() {
		return com.github.skjolber.stcsv.databinder.CsvMapper.builder(Trip.class)
			.stringField("route_id")
				.required()
			.stringField("service_id")
				.required()
			.stringField("trip_id")
				.required()
			.stringField("trip_headsign")
				.optional()
			.integerField("direction_id")
				.optional()
			.stringField("shape_id")
				.optional()
			.integerField("wheelchair_accessible")
				.optional()
			.build();
	}
	
	
	protected static  com.github.skjolber.stcsv.databinder.CsvMapper<Trip> getTripParserForQuotes() {
		return com.github.skjolber.stcsv.databinder.CsvMapper.builder(Trip.class)
			.stringField("route_id")
				.quoted()
				.required()
			.stringField("service_id")
				.quoted()
				.required()
			.stringField("trip_id")
				.quoted()
				.required()
			.stringField("trip_headsign")
				.quoted()
				.optional()
			.integerField("direction_id")
				.quoted()
				.optional()
			.stringField("shape_id")
				.quoted()
				.optional()
			.integerField("wheelchair_accessible")
				.quoted()
				.optional()
			.build();
	}	
	
	protected static  com.github.skjolber.stcsv.databinder.CsvMapper<City> getCityParserForPlain() {
		return com.github.skjolber.stcsv.databinder.CsvMapper.builder(City.class)
			.stringField("Country")
				.required()
			.stringField("City")
				.required()
			.stringField("AccentCity")
				.required()
			.stringField("Region")
				.optional()
			.longField("Population")
				.optional()
			.doubleField("Latitude")
				.optional()
			.doubleField("Longitude")
				.optional()
			.build();
	}
	
	
	protected static  com.github.skjolber.stcsv.databinder.CsvMapper<City> getCityParserForQuotes() {
		return com.github.skjolber.stcsv.databinder.CsvMapper.builder(City.class)
			.stringField("Country")
				.quoted()
				.required()
			.stringField("City")
				.quoted()
				.required()
			.stringField("AccentCity")
				.quoted()
				.required()
			.stringField("Region")
				.quoted()
				.optional()
			.longField("Population")
				.quoted()
				.optional()
			.doubleField("Latitude")
				.quoted()
				.optional()
			.doubleField("Longitude")
				.quoted()
				.optional()
			.build();
	}
	
	
	protected static  com.github.skjolber.stcsv.databinder.CsvMapper<StopTime> getStopTimeParserForPlain() {
		return com.github.skjolber.stcsv.databinder.CsvMapper.builder(StopTime.class)
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
	}

	
	protected static  com.github.skjolber.stcsv.databinder.CsvMapper<StopTime> getStopTimeParserForQuotes() {
		return com.github.skjolber.stcsv.databinder.CsvMapper.builder(StopTime.class)
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
