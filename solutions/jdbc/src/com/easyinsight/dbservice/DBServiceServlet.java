package com.easyinsight.dbservice;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Timer;
import java.util.Calendar;
import java.util.Date;

/**
 * User: James Boe
 * Date: Mar 22, 2009
 * Time: 10:28:46 PM
 */
public class DBServiceServlet extends HttpServlet {

    private Timer timer;

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        startTasks();
    }

    private void startTasks() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date midnight = cal.getTime();
        timer = new Timer();
        timer.schedule(new DBTask(), midnight, 24 * 60 * 60 * 1000);
        //timer.schedule(new DBTask(), new Date(), 60000);
    }

    @Override
    public void destroy() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
