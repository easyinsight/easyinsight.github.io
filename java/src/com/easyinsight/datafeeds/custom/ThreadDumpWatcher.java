package com.easyinsight.datafeeds.custom;

import com.easyinsight.cache.MemCachedManager;
import com.easyinsight.logging.LogClass;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 2/11/14
 * Time: 9:58 AM
 */
public class ThreadDumpWatcher {

    private static ThreadDumpWatcher watcher;

    public static void initialize() {
        watcher = new ThreadDumpWatcher();
        watcher.start();
    }

    public static ThreadDumpWatcher instance() {
        return watcher;
    }

    private Thread thread;
    private boolean running = false;
    private boolean doneRunning = false;

    private void start() {
        try {
            running = true;
            final String host = InetAddress.getLocalHost().getHostName();
            thread = new Thread(new Runnable() {

                public void run() {
                    try {
                        while (running) {
                            Thread.sleep(2000);
                            Object request = MemCachedManager.instance().get("threadDump");
                            if (request != null) {
                                Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();

                                Map<Long, StackTraceElement[]> copy = new HashMap<Long, StackTraceElement[]>();
                                for (Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
                                    copy.put(entry.getKey().getId(), entry.getValue());
                                }
                                MemCachedManager.instance().add("threadDump" + host, 10000, copy);
                            }
                        }
                    } catch (InterruptedException ie) {
                        // no worries
                    }
                    doneRunning = true;
                }
            });
            thread.start();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }

    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
        int limit = 0;
        while (!doneRunning && limit < 10) {
            System.out.println("Waiting for running to finish...");
            try {
                Thread.sleep(1000);
                limit++;
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
}
