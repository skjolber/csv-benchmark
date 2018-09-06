package com.github.skjolber.csv;

import org.openjdk.jmh.annotations.Setup;

import com.github.skjolber.stcsv.CsvClassMapping;


public abstract class AbstractTripCsvParserBenchmark extends AbstractCsvParserBenchmark<Trip> {
	
	@Setup
	public void setupSesseltjonnaCsv() throws Exception {
		
		plain = CsvClassMapping.builder(Trip.class)
			.stringField("route_id")
				.setter(Trip::setRouteId)
				.required()
			.stringField("service_id")
				.setter(Trip::setServiceId)
				.required()
			.stringField("trip_id")
				.setter(Trip::setTripId)
				.required()
			.stringField("trip_headsign")
				.setter(Trip::setTripHeadsign)
				.optional()
			.integerField("direction_id")
				.setter(Trip::setDirectionId)
				.optional()
			.stringField("shape_id")
				.setter(Trip::setShapeId)
				.optional()
			.integerField("wheelchair_accessible")
				.setter(Trip::setWheelchairAccessible)
				.optional()
			.build();
		
		quotes = CsvClassMapping.builder(Trip.class)
				.stringField("route_id")
					.setter(Trip::setRouteId)
					.quoted()
					.required()
				.stringField("service_id")
					.setter(Trip::setServiceId)
					.quoted()
					.required()
				.stringField("trip_id")
					.setter(Trip::setTripId)
					.quoted()
					.required()
				.stringField("trip_headsign")
					.setter(Trip::setTripHeadsign)
					.quoted()
					.optional()
				.integerField("direction_id")
					.setter(Trip::setDirectionId)
					.quoted()
					.optional()
				.stringField("shape_id")
					.setter(Trip::setShapeId)
					.quoted()
					.optional()
				.integerField("wheelchair_accessible")
					.setter(Trip::setWheelchairAccessible)
					.quoted()
					.optional()
				.build();
		
	}

}
