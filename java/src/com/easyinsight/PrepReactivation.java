package com.easyinsight;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.users.Account;
import com.easyinsight.users.ReactivatableUser;
import com.easyinsight.users.User;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/4/13
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrepReactivation {

    public static void main(String[] args) {
        Database.initialize();
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("SELECT ACCOUNT.ACCOUNT_ID FROM ACCOUNT, (SELECT ACCOUNT_ID, MAX(TRANSACTION_TIME) AS TMP FROM ACCOUNT_CREDIT_CARD_BILLING_INFO GROUP BY ACCOUNT_ID) TMP  WHERE ACCOUNT.CREATION_DATE < ? AND ACCOUNT.ACCOUNT_ID = TMP.ACCOUNT_ID AND TMP.TMP < ?");
            Date d = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.MONTH, -1);
            d = c.getTime();
            Calendar cc = Calendar.getInstance();
            cc.set(Calendar.YEAR, 2012);
            cc.set(Calendar.MONTH, 11);
            cc.set(Calendar.DAY_OF_MONTH, Calendar.DECEMBER);
            System.out.println(cc.getTime());
            ps.setDate(1, new java.sql.Date(d.getTime()));
            ps.setDate(2, new java.sql.Date(cc.getTime().getTime()));
            ResultSet rs = ps.executeQuery();

            Transaction t = session.beginTransaction();
            while (rs.next()) {
                long accountID = rs.getLong(1);
                Account a = (Account) session.get(Account.class, accountID);
                for (User u : a.getUsers()) {
                    if (!u.getEmail().endsWith("_reactivate")) {
                        ReactivatableUser uu = new ReactivatableUser();
                        uu.setEmail(u.getEmail());
                        uu.setFirstName(u.getFirstName());
                        uu.setLastName(u.getName());
                        uu.setOldUser(u);
                        uu.setReactivationKey(RandomTextGenerator.generateText(20));
                        uu.setCompany(a.getName());
                        u.setUserName(u.getUserName() + "_reactivate");
                        u.setEmail(u.getEmail() + "_reactivate");
                        session.save(uu);
                        session.save(u);
                    }
                }
                a.setName(a.getName() + "_reactivate");
                session.save(a);
            }
            t.commit();
        } catch (SQLException e) {

            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
}
