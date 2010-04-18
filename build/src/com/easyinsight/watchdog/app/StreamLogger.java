package com.easyinsight.watchdog.app;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Apr 17, 2010
 * Time: 4:20:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamLogger extends Thread {
    private InputStream stream;
    private String label;

    public StreamLogger(InputStream stream, String label) {
        super();
        this.stream = stream;
        this.label = label;
    }

    public InputStream getStream() {
        return stream;
    }

    public void setStream(InputStream stream) {
        this.stream = stream;
    }

    @Override
    public void run() {
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader buf = new BufferedReader(reader);
        String line;
        try {
        while ( (line = buf.readLine()) != null)
            System.out.println(label + ":" + line);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static void logProcess(Process p, String label) {
        new StreamLogger(p.getInputStream(), label).start();
        new StreamLogger(p.getErrorStream(), "error: " + label).start();
    }
}

