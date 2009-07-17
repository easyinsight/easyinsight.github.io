package com.easyinsight.servlet;

import com.easyinsight.PasswordStorage;
import com.easyinsight.database.EIConnection;
import com.easyinsight.database.Database;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: Jul 15, 2009
 * Time: 3:54:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuickBooksSubscribe extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        byte[] bytes = new byte[request.getContentLength()];
        request.getInputStream().read(bytes);
        String s = new String(bytes);
        String[] params = s.split("&");
        Map<String, String> paramMap = new HashMap<String, String>();
        for(String param : params) {
            String[] paramValue = param.split("=");
            if(paramValue.length > 1)
                paramMap.put(paramValue[0], paramValue[1]);
            else if(paramValue.length > 0)
                paramMap.put(paramValue[0], null);
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PasswordStorage.setSessionTicket(paramMap.get("sessiontkt"), Long.parseLong(request.getParameter("appdata")), conn);
            conn.commit();
        }
        catch(SQLException e) {
            conn.rollback();
            throw new RuntimeException(e);
        }
        finally {
            Database.closeConnection(conn);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
}
