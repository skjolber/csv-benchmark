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

public abstract class AbstractParser {

	private final String name;
	private int rowCount;
	private int blackhole; //something to keep values from processed objects to avoid unwanted JIT's dead code removal

	protected AbstractParser(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	
	protected boolean process(Object row) {
		if(row == null){
			return false;
		}
		blackhole +=  System.identityHashCode(row);
		rowCount++;
		return true;
	}
	
	public void resetRowCount(){
		rowCount = 0;
	}

	public int getRowCount() {
		return rowCount;
	}

	public String getBlackhole(){
		return String.valueOf(blackhole);
	}

	public abstract void processRows(Reader reader) throws Exception;

	public abstract List<String[]> parseRows(Reader reader) throws Exception;

}
