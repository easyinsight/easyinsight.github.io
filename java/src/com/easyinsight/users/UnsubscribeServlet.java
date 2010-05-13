package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: May 11, 2010
 * Time: 8:42:48 AM
 */
public class UnsubscribeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        String unsubscribeToken = req.getParameter("user");
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USER_ID FROM user_unsubscribe_key where " +
                    "unsubscribe_key = ?");
            queryStmt.setString(1, unsubscribeToken);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long userID = rs.getLong(1);
                PreparedStatement changeStmt = conn.prepareStatement("UPDATE USER SET OPT_IN_EMAIL = ? WHERE USER_ID = ?");
                changeStmt.setLong(1, userID);
                changeStmt.executeUpdate();
                response.setContentType("text/html");
                response.setStatus(HttpServletResponse.SC_OK);
                response.getOutputStream().print("You will no longer receive the newsletter.");
                response.getOutputStream().close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
