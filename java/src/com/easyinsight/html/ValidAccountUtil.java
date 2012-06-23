package com.easyinsight.html;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.users.Account;
import org.apache.http.HttpRequest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: 6/16/12
 * Time: 11:04 AM
 */
public class ValidAccountUtil {
    public static boolean validateAccount(HttpRequest request) {
        String alternateDestination = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT account_state FROM ACCOUNT WHERE account_id = ?");
            //ps.setLong(1, (Long) session.getAttribute("accountID"));
            ResultSet rs = ps.executeQuery();
            rs.next();
            int accountState = rs.getInt(1);
            if (accountState == Account.CLOSED) {
                alternateDestination = "/app/billing/index.jsp";
            } else if (accountState == Account.DELINQUENT) {
                alternateDestination = "/app/billing/index.jsp";
            } else if (accountState == Account.BILLING_FAILED) {
                alternateDestination = "/app/billing/index.jsp";
            } else if (accountState == Account.INACTIVE) {
                alternateDestination = "/app/html/reactivate.jsp";
            }
            ps.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }
        return false;
    }
}
