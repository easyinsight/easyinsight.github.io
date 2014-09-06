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

    private static final String USERS_IN_ACCOUNT = "SELECT USER_ID, USERNAME, EMAIL, NAME, ACCOUNT_ID, FIRST_NAME, USER_KEY, ANALYST FROM USER WHERE ACCOUNT_ID = ?";

    public List<UserStub> getUserStubs(boolean includeUser) {
        Connection conn = Database.instance().getConnection();
        try {
            List<UserStub> userStubs = new ArrayList<UserStub>();            
            
            PreparedStatement usersStmt = conn.prepareStatement(USERS_IN_ACCOUNT);
            usersStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet userAccountRS = usersStmt.executeQuery();
            while (userAccountRS.next()) {
                UserStub userStub = new UserStub(userAccountRS.getLong(1), userAccountRS.getString(2), userAccountRS.getString(3),
                        userAccountRS.getString(4), userAccountRS.getLong(5), userAccountRS.getString(6), userAccountRS.getBoolean("analyst"));
                if (SecurityUtil.isAccountAdmin()) {
                    userStub.setUserKey(userAccountRS.getString(7));
                }
                userStubs.add(userStub);
            }
            usersStmt.close();

            if (includeUser) {
                UserStub userStub = new UserStub();
                userStub.setUserID(SecurityUtil.getUserID());
                userStubs.remove(userStub);
            }

            List<UserStub> stubs = new ArrayList<UserStub>(userStubs);

            Collections.sort(stubs, new Comparator<UserStub>() {

                public int compare(UserStub userStub, UserStub userStub1) {
                    return userStub.displayName().toLowerCase().compareTo(userStub1.displayName().toLowerCase());
                }
            });

            return stubs;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }
}
