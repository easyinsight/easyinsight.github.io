package com.easyinsight.servlet;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.email.AccountMemberInvitation;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

/**
 * User: jamesboe
 * Date: Nov 16, 2010
 * Time: 2:27:00 PM
 */
public class AccountActivationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String startURL = null;
        boolean okay = false;
        String activationID = req.getParameter("activationID");
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID, TARGET_URL FROM ACCOUNT_ACTIVATION WHERE ACTIVATION_KEY = ?");
            query.setString(1, activationID);
            ResultSet rs = query.executeQuery();
            if (rs.next()) {
                long accountID = rs.getLong(1);
                startURL = rs.getString(2);
                PreparedStatement accountTypeStmt = conn.prepareStatement("SELECT ACCOUNT_TYPE FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                accountTypeStmt.setLong(1, accountID);
                ResultSet typeRS = accountTypeStmt.executeQuery();
                typeRS.next();
                int accountType = typeRS.getInt(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT SET ACCOUNT_STATE = ? WHERE ACCOUNT_ID = ?");
                if (accountType == Account.PERSONAL) {
                    updateStmt.setInt(1, Account.ACTIVE);
                } else {
                    updateStmt.setInt(1, Account.TRIAL);
                }
                updateStmt.setLong(2, accountID);
                updateStmt.executeUpdate();

                okay = true;
                PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM ACCOUNT_ACTIVATION WHERE ACTIVATION_KEY = ?");
                clearStmt.setString(1, activationID);
                clearStmt.executeUpdate();

                if (accountType != Account.PERSONAL) {
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, 30);
                    new AccountActivityStorage().saveAccountTimeChange(accountID, Account.ACTIVE, cal.getTime(), conn);
                }
                PreparedStatement queryStmt = conn.prepareStatement("SELECT EMAIL FROM USER WHERE ACCOUNT_ID = ?");
                queryStmt.setLong(1, accountID);
                ResultSet userRS = queryStmt.executeQuery();
                if (userRS.next()) {
                    final String email = userRS.getString(1);
                    new Thread(new Runnable() {

                        public void run() {
                            new AccountMemberInvitation().sendWelcomeEmail(email);
                        }
                    }).start();

                }
            } else {
                resp.getWriter().write("This activation key was no longer valid. You can log into the application directly at https://www.easy-insight.com/app.");
                resp.getWriter().close();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        if (okay) {
            resp.sendRedirect(startURL);
        }
    }
}
