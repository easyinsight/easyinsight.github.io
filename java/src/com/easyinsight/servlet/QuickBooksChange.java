package com.easyinsight.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 15, 2009
 * Time: 5:24:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuickBooksChange extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] bytes = new byte[request.getContentLength()];
        request.getInputStream().read(bytes);
        String s = new String(bytes);
        String[] params = s.split("&");
        Map<String, String> paramMap = new HashMap<String, String>();
        for(String param : params) {
            param.split("=");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
}
