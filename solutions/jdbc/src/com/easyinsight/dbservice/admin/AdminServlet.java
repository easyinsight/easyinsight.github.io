package com.easyinsight.dbservice.admin;

import com.easyinsight.dbservice.DBTask;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * User: James Boe
 * Date: Jan 17, 2009
 * Time: 1:42:35 PM
 */
public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        out.println("<HTML>");
        out.println("<HEAD><TITLE>Easy Insight Database Service Administration</TITLE></HEAD>");
        out.println("<body scroll=\"no\">");
        out.println("<form action=\"post\" action=\"/forcerun\"></form>");
        out.println("</body>");
        out.println("</HTML>");
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse response) throws ServletException, IOException {

        /*resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<HTML>");
        out.println("<HEAD><TITLE>Response</TITLE></HEAD>");
        out.println("<body scroll=\"no\">");
        out.println("this is the result of the POST...");
        out.println("</body>");
        out.println("</HTML>");*/
        if ("forcerun".equals(httpServletRequest.getParameter("eiaction"))) {
            new DBTask().run();
        }
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
