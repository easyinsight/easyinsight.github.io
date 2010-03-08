package com.easyinsight.database.migration;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.users.Account;
import com.easyinsight.users.AccountActivityStorage;
import com.easyinsight.users.AccountActivity;
import com.easyinsight.logging.LogClass;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Apr 27, 2009
 * Time: 11:25:01 AM
 */
public class Migrate60to61 implements Migration {

    public boolean needToRun() {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT COUNT(*) FROM ACCOUNT_ACTIVITY");
            ResultSet rs = queryStmt.executeQuery();
            return !rs.next();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void migrate() {
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            Calendar trialCal = Calendar.getInstance();
            trialCal.set(Calendar.MONTH, Calendar.MAY);
            trialCal.set(Calendar.DAY_OF_MONTH, 1);
            trialCal.set(Calendar.HOUR_OF_DAY, 0);
            trialCal.set(Calendar.MINUTE, 0);
            trialCal.set(Calendar.SECOND, 0);

            Calendar activeCal = Calendar.getInstance();
            activeCal.set(Calendar.MONTH, Calendar.JUNE);
            activeCal.set(Calendar.DAY_OF_MONTH, 1);
            activeCal.set(Calendar.HOUR_OF_DAY, 0);
            activeCal.set(Calendar.MINUTE, 0);
            activeCal.set(Calendar.SECOND, 0);

            AccountActivityStorage accountActivityStorage = new AccountActivityStorage();
            List<Account> accounts = session.createQuery("from Account").list();
            for (Account account : accounts) {
                // create the beta entry
                AccountActivity accountActivity = new AccountActivity(account.getAccountType(), new Date(), account.getAccountID(),
                        account.getUsers().size(), AccountActivity.ACCOUNT_CREATED, "", account.getMaxSize(), account.getMaxUsers(),
                        Account.BETA);
                accountActivityStorage.saveAccountActivity(accountActivity);
                // create the "official" trial start date
                AccountActivity trialActivity = new AccountActivity(account.getAccountType(), trialCal.getTime(), account.getAccountID(),
                        account.getUsers().size(), AccountActivity.ACCOUNT_CREATED, "", account.getMaxSize(), account.getMaxUsers(),
                        Account.TRIAL);
                accountActivityStorage.saveAccountActivity(trialActivity);
                // create the activation date
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 30);
                new AccountActivityStorage().saveAccountTimeChange(account.getAccountID(), Account.ACTIVE, cal.getTime(), conn);
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.instance().closeConnection(conn);
        }

    }
}
