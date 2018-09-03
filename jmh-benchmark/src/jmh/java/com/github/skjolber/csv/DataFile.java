package com.github.skjolber.csv;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
public enum DataFile {

	WORLD_CITIES(City.class, "worldcitiespop.zip", false, StandardCharsets.UTF_8),
	WORLD_CITIES_WITH_QUOTES(City.class, "worldcitiespop.zip", true, StandardCharsets.UTF_8),
	GTFS_TRIP(Trip.class, "trips.zip", false, StandardCharsets.UTF_8),
	GTFS_TRIP_WITH_QUOTES(Trip.class, "trips.zip", true, StandardCharsets.UTF_8);
	
	private Class<?> cls;
	private String inputFile;
	private boolean quotes;
	private Charset charset;
	
	private DataFile(Class<?> cls, String inputFile, boolean quotes, Charset charset) {
		this.cls = cls;
		this.inputFile = inputFile;
		this.quotes = quotes;
		this.charset = charset;
	}
	
	public File getOutputFile(int numberOfRows) {
		return new File(getFileDirectory() + File.separator + inputFile + String.format("-%s-%d.txt", quotes ? "quoted" : "plain", numberOfRows));
	}
	
	private static String getFileDirectory() {
		return System.getProperty("csv.dir", System.getProperty("java.io.tmpdir"));
	}
	
	public <T> Class<T> getCls() {
		return (Class<T>) cls;
	}
	
	public File getInputFile() {
		return new File("jmh-benchmark/src/jmh/resources/" + inputFile);
	}
	
	public boolean isQuotes() {
		return quotes;
	}
	
	public Charset getCharset() {
		return charset;
	}
}
