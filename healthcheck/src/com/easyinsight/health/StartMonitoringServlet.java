package com.easyinsight.health;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 2/6/11
 * Time: 3:18 PM
 */
public class StartMonitoringServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HealthChecker.instance().setActive(true);
        resp.getOutputStream().write("Monitoring Started.".getBytes());
        resp.getOutputStream().flush();
    }
}
