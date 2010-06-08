package com.easyinsight.email;

import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;

import java.util.*;
import java.sql.*;

/**
 * User: James Boe
 * Date: Aug 17, 2008
 * Time: 11:14:06 PM
 */
public class ShareService {

    private static final String USERS_IN_ACCOUNT = "SELECT USER_ID, USERNAME, EMAIL, NAME, ACCOUNT_ID, FIRST_NAME FROM USER WHERE ACCOUNT_ID = ?";

    public List<UserStub> getUserStubs(boolean includeUser) {
        Connection conn = Database.instance().getConnection();
        try {
            List<UserStub> userStubs = new ArrayList<UserStub>();            
            
            PreparedStatement usersStmt = conn.prepareStatement(USERS_IN_ACCOUNT);
            usersStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet userAccountRS = usersStmt.executeQuery();
            while (userAccountRS.next()) {
                UserStub userStub = new UserStub(userAccountRS.getLong(1), userAccountRS.getString(2), userAccountRS.getString(3),
                        userAccountRS.getString(4), userAccountRS.getLong(5), userAccountRS.getString(6));
                userStubs.add(userStub);
            }

            if (includeUser) {
                UserStub userStub = new UserStub();
                userStub.setUserID(SecurityUtil.getUserID());
                userStubs.remove(userStub);
            }

            return new ArrayList<UserStub>(userStubs);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
