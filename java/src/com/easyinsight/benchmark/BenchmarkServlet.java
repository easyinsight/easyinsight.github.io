package com.easyinsight.benchmark;

import com.easyinsight.admin.AdminService;
import com.easyinsight.logging.LogClass;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * User: jamesboe
 * Date: Feb 22, 2010
 * Time: 10:02:55 PM
 */
public class BenchmarkServlet extends HttpServlet {

    private Timer timer;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    BenchmarkManager.internalMeasurement(new AdminService().getHealthInfo());
                } catch (Exception e) {
                    LogClass.error(e);
                }
            }
        }, new Date(), 1000 * 60 * 60 * 4);
    }

    @Override
    public void destroy() {
        super.destroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
