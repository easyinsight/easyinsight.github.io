package com.easyinsight.html;

import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jamesboe
 * Date: 10/31/14
 * Time: 2:53 PM
 */
public class DoneServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Done!");
        JSONObject out = new JSONObject();
        try {
            out.put("done", true);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        resp.setContentType("application/json");
        resp.getOutputStream().write(out.toString().getBytes());
        resp.getOutputStream().flush();
    }
}
