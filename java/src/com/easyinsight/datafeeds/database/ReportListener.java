package com.easyinsight.datafeeds.database;

import com.easyinsight.analysis.AsyncReport;
import com.easyinsight.logging.LogClass;

/**
 * User: jamesboe
 * Date: 12/3/12
 * Time: 10:03 PM
 */
public class ReportListener implements Runnable {

    private boolean running;

    private static ReportListener instance;

    public static ReportListener instance() {
        return instance;
    }

    public static void initialize() {
        instance = new ReportListener();
        thread = new Thread(instance);
        thread.setName("Report Listener");
        thread.start();
    }

    public void stop() {
        running = false;
        thread.interrupt();
    }

    private static Thread thread;

    public void blah() throws Exception {
        running = true;

        while (running) {
            try {
                new AsyncReport().claimAndRun();
            } catch (Exception e) {
                LogClass.error(e);
            }
        }
    }

    public void run() {
        try {
            blah();
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
