package com.easyinsight.users;

import com.easyinsight.database.EIConnection;
import com.easyinsight.util.RandomTextGenerator;
import org.jetbrains.annotations.Nullable;
import org.hibernate.Session;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

/**
 * User: James Boe
 * Date: Aug 16, 2008
 * Time: 1:04:33 PM
 */
public class InternalUserService {

    @Nullable
    public UserServiceResponse validateCookie(String cookie, String userName, EIConnection conn, Session session) throws SQLException {

        PreparedStatement queryStmt = conn.prepareStatement("SELECT USER.USER_ID FROM USER_SESSION, USER WHERE USER_SESSION.user_id = user.user_id and " +
                "user_session.session_number = ? AND user.username = ?");
        queryStmt.setString(1, cookie);
        queryStmt.setString(2, userName);
        ResultSet rs = queryStmt.executeQuery();
        long userID = 0;
        if (rs.next()) {
            userID = rs.getLong(1);
        }
        queryStmt.close();
        if (userID == 0) {
            return null;
        }

        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM USER_SESSION WHERE user_session.session_number = ? and user_session.user_id = ?");
        deleteStmt.setString(1, cookie);
        deleteStmt.setLong(2, userID);
        deleteStmt.execute();
        deleteStmt.close();

        User user = (User) session.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
        UserServiceResponse response = UserServiceResponse.createResponse(user, session, conn);
        return response;
    }

    public String createCookie(long userID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            return createCookie(userID, conn);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public String createCookie(long userID, EIConnection conn) throws SQLException {
        String sessionCookie = RandomTextGenerator.generateText(30);
        PreparedStatement saveCookieStmt = conn.prepareStatement("INSERT INTO USER_SESSION (USER_ID, SESSION_NUMBER," +
                "USER_SESSION_DATE) VALUES (?, ?, ?)");
        saveCookieStmt.setLong(1, userID);
        saveCookieStmt.setString(2, sessionCookie);
        saveCookieStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        saveCookieStmt.execute();
        return sessionCookie;
    }

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
