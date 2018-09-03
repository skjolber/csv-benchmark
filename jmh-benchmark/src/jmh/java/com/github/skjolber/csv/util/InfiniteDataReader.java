package com.github.skjolber.csv.util;

import java.io.Reader;

public class InfiniteDataReader extends Reader {

    private final char[] data;
    private int pos;

    public InfiniteDataReader(final String data) {
        this.data = data.toCharArray();
    }

    @Override
    public int read(final char[] cbuf, int off, int len) {
    	
    	int internalPosition = pos % data.length;
    	if(internalPosition % data.length + len > data.length) {
    		int endSegmentLength = data.length - internalPosition;
    		System.arraycopy(data, pos, cbuf, off, endSegmentLength);
    		int startSegmentLength = Math.min(data.length, len - endSegmentLength);
    		System.arraycopy(data, 0, cbuf, off + endSegmentLength, startSegmentLength);
    		
    		pos += endSegmentLength + startSegmentLength;
    	} else {
    		System.arraycopy(data, off, cbuf, off, len);
    		
    		pos += len;
    	}
    	
    	return len;
    }

    @Override
    public void close() {
        // NOP
    }

}
