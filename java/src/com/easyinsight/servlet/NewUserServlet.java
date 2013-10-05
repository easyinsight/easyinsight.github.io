package com.easyinsight.servlet;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * User: jamesboe
 * Date: 10/2/13
 * Time: 1:39 PM
 */
public class NewUserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String token = req.getParameter("token");
            EIConnection conn = Database.instance().getConnection();
            try {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT USER_ID FROM new_user_link WHERE TOKEN = ?");
                queryStmt.setString(1, token);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    long userID = rs.getLong(1);
                    PreparedStatement getUserStmt = conn.prepareStatement("SELECT EMAIL, PASSWORD FROM USER WHERE USER_ID = ?");
                    getUserStmt.setLong(1, userID);
                    ResultSet userRS = getUserStmt.executeQuery();
                    userRS.next();
                    String email = userRS.getString(1);
                    String password = userRS.getString(2);
                    UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted2(email, password);
                    req.getSession().invalidate();
                    HttpSession session  = req.getSession(true);
                    SecurityUtil.populateSession(session, userServiceResponse);
                    UserService.checkAccountStateOnLogin(session, userServiceResponse, req, resp, "/app");
                }
            } finally {
                Database.closeConnection(conn);
            }
        } catch (Exception e) {
            LogClass.error(e);
            resp.sendRedirect("");
        }
    }
}
