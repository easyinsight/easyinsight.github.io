package com.easyinsight.api;

import java.io.InputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 1, 2009
 * Time: 4:31:31 PM
 */
public class CountingInputStream extends InputStream {
    private int i = 0;
    private InputStream decorated;

    public CountingInputStream(InputStream is) {
        decorated = is;
    }
    public int read() throws IOException {
        int readValue = decorated.read();
        if(readValue != -1)
            i++;
        return readValue;
    }

    public int getCount() {
        return i;
    }
}
