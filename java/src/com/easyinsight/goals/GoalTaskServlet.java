package com.easyinsight.goals;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Timer;
import java.util.Calendar;

/**
 * User: James Boe
 * Date: Feb 13, 2009
 * Time: 11:07:35 AM
 */
public class GoalTaskServlet extends HttpServlet {

    private Timer timer;

    public void init(ServletConfig servletConfig) throws ServletException {
        timer = new Timer();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        timer.schedule(new GoalUpdateTask(), cal.getTime(), (24 * 60 * 60 * 1000));
    }

    @Override
    public void destroy() {
        super.destroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
