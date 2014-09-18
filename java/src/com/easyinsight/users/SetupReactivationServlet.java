package com.easyinsight.users;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;

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
public class SetupReactivationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SecurityUtil.populateThreadLocalFromSession(req);
        try {
            SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
            long accountID = Long.parseLong(req.getParameter("id"));
            EIConnection conn = Database.instance().getConnection();
            try {
                conn.setAutoCommit(false);
                new ReactivationAccount().generate(accountID, conn);
                conn.commit();
            } catch (Exception e) {
                LogClass.error(e);
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
                Database.closeConnection(conn);
            }
        } finally {
            SecurityUtil.clearThreadLocal();
        }
    }
}
