package com.easyinsight.health;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 2/6/11
 * Time: 3:15 PM
 */
public class CurrentMonitoringServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Active State = ").append(HealthChecker.instance().isActive()).append("\r\n");
        stringBuilder.append("Error Count = ").append(HealthChecker.instance().getFailures()).append("\r\n");
        resp.getOutputStream().write(stringBuilder.toString().getBytes());
        resp.getOutputStream().flush();
    }
}
