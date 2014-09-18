package com.easyinsight.users;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 9/16/14
 * Time: 10:38 AM
 */
public class ReactivationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String activationKey = req.getParameter("activationKey");
        new ReactivationAccount().setupAccount(activationKey, req, resp);
    }
}
