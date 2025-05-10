package parser;


import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.simpleflatmapper.csv.CsvParser;

public class SimpleFlatMapperParser extends AbstractParser {
	public SimpleFlatMapperParser() {

		super("SimpleFlatMapper CSV parser");
	}

	@Override
	public void processRows(final Reader input) throws Exception {
		final Iterator<String[]> it = CsvParser.iterator(input);
		while(it.hasNext()) {
			process(it.next());
		}
	}

	@Override
	public List<String[]> parseRows(final Reader input) throws Exception {
		final List<String[]> list = new ArrayList<String[]>();
		final Iterator<String[]> it = CsvParser.iterator(input);
		while(it.hasNext()) {
			list.add(it.next());
		}
		return list;
	}
}
