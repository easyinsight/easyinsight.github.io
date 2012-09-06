package com.easyinsight.health;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Date;
import java.util.Timer;

/**
 * User: jamesboe
 * Date: 1/29/11
 * Time: 10:46 PM
 */
public class HealthCheckServlet extends HttpServlet {

    private Timer timer;

    public void init(ServletConfig servletConfig) throws ServletException {
        timer = new Timer();
        HealthChecker.initialize();
        SeleniumChecker.initialize();
        timer.schedule(HealthChecker.instance(), new Date(), 60000);
        timer.schedule(SeleniumChecker.instance(), new Date(), 60000);
        System.out.println("Scheduled timer");
    }

    @Override
    public void destroy() {
        super.destroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
