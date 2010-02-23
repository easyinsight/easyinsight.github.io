package com.easyinsight.benchmark;

import com.easyinsight.api.UncheckedPublishService;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User: jamesboe
 * Date: Feb 22, 2010
 * Time: 3:47:25 PM
 */
public class InternalPublishService extends UncheckedPublishService {

    private long accountID;
    private long userID;

    public InternalPublishService() {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement accountQueryStmt = conn.prepareStatement("SELECT ACCOUNT_ID FROM ACCOUNT WHERE ACCOUNT_TYPE = ?");
            accountQueryStmt.setLong(1, Account.ADMINISTRATOR);
            ResultSet accountRS = accountQueryStmt.executeQuery();
            accountRS.next();
            accountID = accountRS.getLong(1);
            PreparedStatement userXQueryStmt = conn.prepareStatement("SELECT USER_ID FROM USER WHERE USER.username = ?");
            userXQueryStmt.setString(1, "jboe");
            ResultSet userRS = userXQueryStmt.executeQuery();
            userRS.next();
            userID = userRS.getLong(1);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    @Override
    protected long getAccountID() {
        return accountID;
    }

    @Override
    protected long getUserID() {
        return userID;
    }
}
