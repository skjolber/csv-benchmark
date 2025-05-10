/*******************************************************************************
 * Copyright 2014 uniVocity Software Pty Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package parser;

import java.io.Reader;
import java.util.List;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

public class UnivocityParser extends AbstractParser {

	public UnivocityParser() {
		super("uniVocity CSV parser");
	}

	@Override
	public void processRows(final Reader input) {
		CsvParserSettings settings = new CsvParserSettings();
		
		//turning off features enabled by default
		settings.setIgnoreLeadingWhitespaces(false);
		settings.setIgnoreTrailingWhitespaces(false);
		settings.setSkipEmptyLines(false);
		settings.setColumnReorderingEnabled(false);

		settings.setRowProcessor(new AbstractRowProcessor() {
			@Override
			public void rowProcessed(String[] row, ParsingContext context) {
				process(row);
			}
		});
		
		CsvParser parser = new CsvParser(settings);
		parser.parse(input);
	}

	@Override
	public List<String[]> parseRows(final Reader input) {

		CsvParserSettings settings = new CsvParserSettings();
		CsvParser parser = new CsvParser(settings);

		return parser.parseAll(input);
	}

}
