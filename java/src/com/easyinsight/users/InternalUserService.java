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
        Session session = Database.instance().createSession();
        try {
            User user = null;
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userName = ?").setString(0, userName).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            }
            if (results.size() > 0) {
                user = (User) results.get(0);

            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }

    public User retrieveUser(long userID) {
        Session session = Database.instance().createSession();
        try {
            User user = null;
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
            }
            return user;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            session.close();
        }
    }
}
