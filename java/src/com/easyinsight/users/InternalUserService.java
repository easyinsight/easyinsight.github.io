package com.easyinsight.users;

import org.jetbrains.annotations.Nullable;
import org.hibernate.Session;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 1:04:33 PM
 */
public class InternalUserService {
    
    @Nullable
    public User retrieveUser(String userName) {
        try {
            User user = null;
            Session session = Database.instance().createSession();
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userName = ?").setString(0, userName).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
                user.setLicenses(new ArrayList<SubscriptionLicense>());
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public User retrieveUser(long userID) {
        try {
            User user = null;
            Session session = Database.instance().createSession();
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
                user.setLicenses(new ArrayList<SubscriptionLicense>());
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
