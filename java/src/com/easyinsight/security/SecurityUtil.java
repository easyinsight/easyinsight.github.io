package com.easyinsight.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.users.UserService;
import com.easyinsight.users.UserServiceResponse;
import com.easyinsight.users.User;
import com.easyinsight.users.Account;
import com.easyinsight.logging.LogClass;
import com.easyinsight.logging.SecurityLogger;
import org.hibernate.Session;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * User: James Boe
 * Date: Aug 12, 2008
 * Time: 10:26:07 AM
 */
public class SecurityUtil {

    private static ISecurityProvider securityProvider;
    private static ThreadLocal<UserPrincipal> threadLocal = new ThreadLocal<UserPrincipal>();

    public static void populateThreadLocal(String userName, long userID, long accountID, int accountType, boolean accountAdmin, int firstDayOfWeek, String persona) {
        threadLocal.set(new UserPrincipal(userName, accountID, userID, accountType, accountAdmin, firstDayOfWeek, persona));
    }

    public static void populateSession(HttpSession session, UserServiceResponse userServiceResponse) {
        session.setAttribute("userName", userServiceResponse.getUserName());
        session.setAttribute("userID", userServiceResponse.getUserID());
        session.setAttribute("accountID", userServiceResponse.getAccountID());
        session.setAttribute("accountAdmin", userServiceResponse.isAccountAdmin());
        session.setAttribute("accountID", userServiceResponse.getAccountID());
        session.setAttribute("dayOfWeek", userServiceResponse.getFirstDayOfWeek());
        session.setAttribute("persona", userServiceResponse.getPersonaName());
        session.setAttribute("accountType", userServiceResponse.getAccountType());
    }

    public static void populateThreadLocalFromSession(HttpServletRequest request) {

        String embedKey = request.getParameter("embedKey");
        boolean usingEmbed = false;
        if (embedKey != null) {
            Session session = Database.instance().createSession();
            List results = session.createQuery("from User where userKey = ?").setString(0, embedKey).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                populateThreadLocal(user.getUserName(), user.getUserID(), user.getAccount().getAccountID(), user.getAccount().getAccountType(),
                        user.isAccountAdmin(), user.getAccount().getFirstDayOfWeek(), null);
                usingEmbed = true;
            }
        }

        if (!usingEmbed) {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("userName");
            if (userName != null) {
                Long userID = (Long) session.getAttribute("userID");
                Long accountID = (Long) session.getAttribute("accountID");
                Integer accountType = (Integer) session.getAttribute("accountType");
                Integer dayOfWeek = (Integer) session.getAttribute("dayOfWeek");
                String persona = (String) session.getAttribute("persona");
                Boolean accountAdmin = (Boolean) session.getAttribute("accountAdmin");
                if (dayOfWeek == null) {
                    dayOfWeek = 1;
                }
                com.easyinsight.security.SecurityUtil.populateThreadLocal(userName, userID,
                        accountID, accountType, accountAdmin, dayOfWeek, persona);
            }
        }

    }

    public static void setSecurityProvider(ISecurityProvider securityProvider) {
        SecurityUtil.securityProvider = securityProvider;
    }

    public static ISecurityProvider getSecurityProvider() {
        return SecurityUtil.securityProvider;
    }

    public static int getFirstDayOfWeek() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if (userPrincipal == null)
            userPrincipal = threadLocal.get();
        if (userPrincipal == null) {
            return Calendar.SUNDAY;
        }
        return userPrincipal.getFirstDayOfWeek();
    }

    public static boolean isAccountReports() {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT TEST_ACCOUNT_VISIBLE FROM USER WHERE USER_ID = ?");
            ps.setLong(1, getUserID());
            ResultSet rs = ps.executeQuery();
            boolean ret = false;
            if (rs.next()) {
                ret = rs.getBoolean(1);
            }
            ps.close();
            return ret;
        } catch (Exception e) {
            LogClass.error(e);
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static boolean isAccountReports(EIConnection conn) throws SQLException {
        PreparedStatement ps = conn.prepareStatement("SELECT TEST_ACCOUNT_VISIBLE FROM USER WHERE USER_ID = ?");
        ps.setLong(1, getUserID());
        ResultSet rs = ps.executeQuery();
        boolean ret = false;
        if (rs.next()) {
            ret = rs.getBoolean(1);
        }
        ps.close();
        return ret;
    }

    public static boolean isAccountAdmin() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if (userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.isAccountAdmin();
    }

    public static String getUserName() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if (userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.getUserName();
    }

    public static void authorizeAccountAdmin() {
        if (!isAccountAdmin()) {
            throw new SecurityException();
        }
    }

    public static long authenticate(String userName, String password) {
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse.getUserID();
        } else {
            SecurityLogger.error("Unsuccessful login, user: " + userName);
            throw new SecurityException();
        }
    }

    public static UserServiceResponse authenticateKeys(String key, String secretKey) {
        UserServiceResponse userServiceResponse;
        if (key == null || secretKey == null) {
            throw new SecurityException();
        }
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            conn.setAutoCommit(false);
            List results = session.createQuery("from User where userKey = ?").setString(0, key).list();
            if (results.size() > 0) {
                User user = (User) results.get(0);
                if (!secretKey.equals(user.getUserSecretKey())) {
                    throw new SecurityException();
                }
                Account account = user.getAccount();
                if (!Arrays.asList(Account.TRIAL, Account.ACTIVE).contains(account.getAccountState())) {
                    return new UserServiceResponse(false, "This account is not active. Please log in to re-activate your account.");
                }
                userServiceResponse = UserServiceResponse.createResponse(user, session, conn);
            } else {
                throw new SecurityException();
            }
            conn.commit();
        } catch (SecurityException se) {
            conn.rollback();
            throw se;
        } catch (Exception e) {
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            session.close();
            Database.closeConnection(conn);
        }
        return userServiceResponse;
    }

    public static UserServiceResponse authenticateToResponse(String userName, String password) {
        UserServiceResponse userServiceResponse = new UserService().authenticateWithEncrypted(userName, password);
        if (userServiceResponse.isSuccessful()) {
            return userServiceResponse;
        } else {
            SecurityLogger.error("Unsuccessful login, user: " + userName);
            throw new SecurityException();
        }
    }

    public static long getUserID() {
        return getUserID(true);
    }

    public static long getUserID(boolean required) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                if (required) {
                    SecurityLogger.error("Could not retrieve user principal.");
                    throw new SecurityException(SecurityException.LOGIN_REQUIRED);
                } else
                    return 0;
            } else {
                return userPrincipal.getUserID();
            }
        } else {
            return userPrincipal.getUserID();
        }
    }

    public static void authorizeAccountTier(int requiredTier) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                SecurityLogger.error("Could not retrieve user principal.");
                throw new SecurityException();
            }
        } else {
            if (userPrincipal.getAccountType() < requiredTier) {
                SecurityLogger.error("User " + userPrincipal.getUserName() + " could not access tier - " + requiredTier + "; their tier is " + userPrincipal.getAccountType());
                throw new SecurityException();
            }
        }
    }

    public static void authorizeScorecard(long scorecardID) {
        long userID = SecurityUtil.getUserID();
        authorizeScorecard(scorecardID, userID);
    }

    public static void authorizeScorecard(long scorecardID, long userID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT USER.USER_ID, ACCOUNT_ID, ACCOUNT_VISIBLE FROM SCORECARD, USER WHERE SCORECARD_ID = ? AND " +
                    "USER.USER_ID = SCORECARD.USER_ID");
            queryStmt.setLong(1, scorecardID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {

                long scorecardUserID = rs.getLong(1);
                boolean accountVisible = rs.getBoolean(3);
                long accountID = rs.getLong(2);
                if (scorecardUserID != userID) {
                    if (accountVisible || isAccountReports()) {
                        if (accountID != getAccountID()) {
                            throw new SecurityException();
                        }
                    } else {
                        PreparedStatement queryGroupStmt = conn.prepareStatement("SELECT GROUP_TO_USER_JOIN.group_to_user_join_id FROM " +
                                "SCORECARD, GROUP_TO_USER_JOIN WHERE SCORECARD.group_id = GROUP_TO_USER_JOIN.group_id AND " +
                                "GROUP_TO_USER_JOIN.user_id = ? AND scorecard.scorecard_id = ?");
                        queryGroupStmt.setLong(1, userID);
                        queryGroupStmt.setLong(2, scorecardID);
                        ResultSet groupRS = queryGroupStmt.executeQuery();
                        if (!groupRS.next()) {
                            throw new SecurityException();
                        }
                    }
                }
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new SecurityException();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static JSONObject getUserJSON(Connection conn, HttpServletRequest request) throws JSONException {
        long userID = getUserID(false);
        String embedKey = request.getParameter("embedKey");
        JSONObject jo = new JSONObject();
        if (userID > 0) {
            Session session = Database.instance().createSession(conn);

            try {
                User u = (User) session.createQuery("from User where userID = ?").setLong(0, userID).list().get(0);
                jo.put("name", u.getUserName());
                jo.put("designer", embedKey != null || u.isAnalyst());
            } finally {
                session.close();
            }

        } else {
            jo.put("name", (Object) null);
            jo.put("designer", false);
        }
        jo.put("embedKey", embedKey);

        return jo;
    }

    public static void authorizeKPI(long kpiID) {
        Connection conn = Database.instance().getConnection();
        long userID = SecurityUtil.getUserID();
        try {

            // HOW DO I DO THIS
            PreparedStatement queryStmt = conn.prepareStatement("SELECT OWNER " +
                    "FROM KPI_ROLE WHERE " +
                    "KPI_ROLE.kpi_id = ? AND KPI_ROLE.USER_ID = ?");

            queryStmt.setLong(1, kpiID);
            queryStmt.setLong(2, userID);
            ResultSet rs = queryStmt.executeQuery();
            if (!rs.next()) {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new SecurityException();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static void changeAccountType(int accountType) {
        if (securityProvider != null && securityProvider.getUserPrincipal() != null) {
            securityProvider.getUserPrincipal().setAccountType(accountType);
        }
    }

    public static int getAccountTier() {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                SecurityLogger.error("Could not retrieve user principal.");
                throw new SecurityException();
            } else {
                return userPrincipal.getAccountType();
            }
        } else {
            return userPrincipal.getAccountType();
        }
    }

    public static long getAccountID(boolean required) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null && required) {
                throw new SecurityException();
            } else if (userPrincipal != null) {
                return userPrincipal.getAccountID();
            } else {
                return 0;
            }
        } else {
            return userPrincipal.getAccountID();
        }
    }

    public static long getAccountID() {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                throw new SecurityException();
            } else {
                return userPrincipal.getAccountID();
            }
        } else {
            return userPrincipal.getAccountID();
        }
    }

    public static void authorizeFeed(long feedID, int requestedRole) {
        long userID = getUserID();
        int existingRole = getRole(userID, feedID);
        if (existingRole > requestedRole) {
            throw new SecurityException();
        }
    }

    public static int getUserRoleToFeed(long feedID) {
        long userID = getUserID();
        return getRole(userID, feedID);
    }

    private static int getInsightRole(long userID, long insightID) {
        Connection conn = Database.instance().getConnection();
        try {
            int role = Roles.NONE;
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT RELATIONSHIP_TYPE FROM USER_TO_ANALYSIS WHERE " +
                    "USER_ID = ? AND ANALYSIS_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, insightID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                role = Roles.OWNER;
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("SELECT group_to_user_join.binding_type FROM group_to_insight, group_to_user_join WHERE " +
                        "group_to_user_join.group_id = group_to_insight.group_id AND group_to_user_join.user_id = ? AND group_to_insight.insight_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, insightID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    role = groupRS.getInt(1);
                } else {

                    // this block of code is for reports which are included as part of "include reports" on groups per data source

                    PreparedStatement lastChanceStmt = conn.prepareStatement("SELECT analysis.ANALYSIS_ID FROM ANALYSIS, group_to_user_join," +
                            "community_group, upload_policy_groups WHERE " +
                            "analysis.analysis_id = ? AND analysis.data_feed_id = upload_policy_groups.feed_id AND upload_policy_groups.group_id = community_group.community_group_id AND " +
                            "community_group.data_source_include_report = ? AND community_group.community_group_id = group_to_user_join.group_id AND group_to_user_join.user_id = ?");
                    lastChanceStmt.setLong(1, insightID);
                    lastChanceStmt.setBoolean(2, true);
                    lastChanceStmt.setLong(3, userID);
                    ResultSet lastChanceRS = lastChanceStmt.executeQuery();
                    if (lastChanceRS.next()) {
                        role = Roles.SUBSCRIBER;
                    } else {
                        role = Integer.MAX_VALUE;
                    }
                    lastChanceStmt.close();
                    return role;
                }
                groupQueryStmt.close();
            }
            existingLinkQuery.close();
            return role;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static int getRole(long userID, long feedID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT ROLE FROM UPLOAD_POLICY_USERS WHERE " +
                    "USER_ID = ? AND FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                PreparedStatement groupQueryStmt = conn.prepareStatement("SELECT group_to_user_join.binding_type FROM upload_policy_groups, group_to_user_join WHERE " +
                        "group_to_user_join.group_id = upload_policy_groups.group_id AND group_to_user_join.user_id = ? AND upload_policy_groups.feed_id = ?");
                groupQueryStmt.setLong(1, userID);
                groupQueryStmt.setLong(2, feedID);
                ResultSet groupRS = groupQueryStmt.executeQuery();
                if (groupRS.next()) {
                    return groupRS.getInt(1);
                } else {
                    PreparedStatement accountQueryStmt = conn.prepareStatement("SELECT role FROM upload_policy_users, user, data_feed WHERE " +
                            "data_feed.data_feed_id = ? AND data_feed.data_feed_id = upload_policy_users.feed_id AND " +
                            "data_feed.account_visible = ? AND upload_policy_users.user_id = user.user_id AND " +
                            "user.account_id = ?");
                    accountQueryStmt.setLong(1, feedID);
                    accountQueryStmt.setBoolean(2, true);
                    accountQueryStmt.setLong(3, getAccountID());
                    ResultSet accountRS = accountQueryStmt.executeQuery();
                    if (accountRS.next()) {
                        return Roles.OWNER;
                    } else {
                        PreparedStatement forTimeBeingStmt = conn.prepareStatement("SELECT account_id FROM user, data_feed, upload_policy_users WHERE " +
                                "data_feed.data_feed_id = ? AND data_feed.data_feed_id = upload_policy_users.feed_id AND upload_policy_users.user_id = user.user_id");
                        forTimeBeingStmt.setLong(1, feedID);
                        ResultSet hackRS = forTimeBeingStmt.executeQuery();
                        if (hackRS.next()) {
                            long accountID = hackRS.getLong(1);
                            if (accountID == getAccountID()) {
                                return Roles.OWNER;
                            } else {
                                return Integer.MAX_VALUE;
                            }
                        } else {
                            return Integer.MAX_VALUE;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private static int getRoleForGroup(long userID, long groupID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT BINDING_TYPE FROM GROUP_TO_USER_JOIN WHERE " +
                    "USER_ID = ? AND GROUP_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, groupID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                if (isAccountAdmin()) {
                    PreparedStatement getGroupAccountIDStmt = conn.prepareStatement("SELECT GROUP_ACCOUNT_ID FROM COMMUNITY_GROUP WHERE GROUP_ACCOUNT_ID = ?");
                    getGroupAccountIDStmt.setLong(1, getAccountID());
                    ResultSet blahRS = getGroupAccountIDStmt.executeQuery();
                    if (blahRS.next()) {
                        return Roles.OWNER;
                    } else {
                        PreparedStatement baseGroupStmt = conn.prepareStatement("SELECT BINDING_TYPE FROM GROUP_TO_USER_JOIN, USER WHERE " +
                                "GROUP_TO_USER_JOIN.USER_ID = USER.USER_ID AND USER.ACCOUNT_ID = ? AND GROUP_ID = ?");
                        baseGroupStmt.setLong(1, getAccountID());
                        baseGroupStmt.setLong(2, groupID);
                        ResultSet groupRS = baseGroupStmt.executeQuery();
                        if (groupRS.next()) {
                            return Roles.OWNER;
                        }
                    }
                }
                return Integer.MAX_VALUE;
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static int authorizeReport(long reportID, int neededRole) {
        int role = authorizeInsight(reportID);
        if (role > neededRole) {
            throw new SecurityException();
        }
        return role;
    }

    public static int authorizeInsight(long insightID) {
        boolean accountVisibility = false;
        boolean publicVisibility = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT ACCOUNT_VISIBLE, PUBLICLY_VISIBLE FROM ANALYSIS WHERE ANALYSIS_ID = ?");
            authorizeStmt.setLong(1, insightID);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                accountVisibility = rs.getBoolean(1);
                publicVisibility = rs.getBoolean(2);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                if (!publicVisibility) {
                    throw new SecurityException();
                } else {
                    return Roles.PUBLIC;
                }
            }
        }

        if (accountVisibility && isAccountReports()) {
            conn = Database.instance().getConnection();
            try {
                PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, USER_TO_ANALYSIS WHERE USER.USER_ID = " +
                        "user_to_analysis.user_id AND user_to_analysis.analysis_id = ?");
                query.setLong(1, insightID);
                ResultSet rs = query.executeQuery();
                if (rs.next()) {
                    long accountID = rs.getLong(1);
                    if (accountID == userPrincipal.getAccountID()) {
                        return Roles.OWNER;
                        // all good
                    } else {
                        if (!publicVisibility) {
                            throw new SecurityException();
                        } else {
                            return Roles.SUBSCRIBER;
                        }
                    }
                } else {
                    if (!publicVisibility) {
                        throw new SecurityException();
                    } else {
                        return Roles.SUBSCRIBER;
                    }
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } else {
            int role = getInsightRole(userPrincipal.getUserID(), insightID);
            if (role != Roles.OWNER && role != Roles.SUBSCRIBER && role != Roles.EDITOR) {
                if (!publicVisibility) {
                    throw new SecurityException();
                } else {
                    return Roles.PUBLIC;
                }
            }
            return role;
        }
    }

    public static void authorizeDashboard(long dashboardID, int neededRole) {
        int role = authorizeDashboard(dashboardID);
        if (role > neededRole) {
            throw new SecurityException();
        }
    }

    public static int authorizeDashboard(long dashboardID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD.ACCOUNT_VISIBLE, DASHBOARD.PUBLIC_VISIBLE FROM DASHBOARD WHERE " +
                    "dashboard.dashboard_id = ?");
            queryStmt.setLong(1, dashboardID);

            boolean accountVisible;
            boolean publicVisible;

            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                accountVisible = rs.getBoolean(1);
                publicVisible = rs.getBoolean(2);
            } else {
                throw new SecurityException();
            }

            UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
            if (userPrincipal == null) {
                userPrincipal = threadLocal.get();
                if (userPrincipal == null) {
                    if (!publicVisible) {
                        throw new SecurityException();
                    } else {
                        return Roles.PUBLIC;
                    }
                }
            }

            PreparedStatement userStmt = conn.prepareStatement("SELECT USER_TO_DASHBOARD.USER_ID, USER.ACCOUNT_ID FROM " +
                    "user_to_dashboard, user WHERE user_to_dashboard.user_id = user.user_id AND user_to_dashboard.dashboard_id = ?");
            userStmt.setLong(1, dashboardID);
            ResultSet userRS = userStmt.executeQuery();
            int role = Roles.NONE;
            while (userRS.next()) {
                long userID = userRS.getLong(1);
                long accountID = userRS.getLong(2);
                if (userID == SecurityUtil.getUserID()) {
                    role = Math.min(Roles.OWNER, role);
                } else if ((accountVisible && isAccountReports(conn)) && accountID == SecurityUtil.getAccountID()) {
                    role = Math.min(Roles.OWNER, role);
                }
            }
            if (role != Roles.NONE) {
                return role;
            }
            PreparedStatement groupQueryStmt = conn.prepareStatement("SELECT group_to_user_join.binding_type FROM group_to_dashboard, group_to_user_join WHERE " +
                    "group_to_user_join.group_id = group_to_dashboard.group_id AND group_to_user_join.user_id = ? AND group_to_dashboard.dashboard_id = ?");
            groupQueryStmt.setLong(1, SecurityUtil.getUserID());
            groupQueryStmt.setLong(2, dashboardID);
            ResultSet groupRS = groupQueryStmt.executeQuery();
            if (groupRS.next()) {
                return groupRS.getInt(1);
            } else {
                PreparedStatement lastChanceStmt = conn.prepareStatement("SELECT dashboard.DASHBOARD_ID FROM dashboard, group_to_user_join," +
                        "community_group, upload_policy_groups WHERE " +
                        "dashboard.dashboard_id = ? AND dashboard.data_source_id = upload_policy_groups.feed_id AND upload_policy_groups.group_id = community_group.community_group_id AND " +
                        "community_group.data_source_include_report = ? AND community_group.community_group_id = group_to_user_join.group_id AND group_to_user_join.user_id = ?");
                lastChanceStmt.setLong(1, dashboardID);
                lastChanceStmt.setBoolean(2, true);
                lastChanceStmt.setLong(3, SecurityUtil.getUserID());
                ResultSet lastChanceRS = lastChanceStmt.executeQuery();
                if (lastChanceRS.next()) {
                    role = Roles.SUBSCRIBER;
                } else {
                    role = Roles.NONE;
                }
                lastChanceStmt.close();
            }

            if (role == Roles.NONE) {
                throw new SecurityException();
            }
            return role;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new SecurityException();
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static int authorizeGroup(long groupID, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if(userPrincipal == null) {
                throw new SecurityException();
            }
        }

        int role = getRoleForGroup(userPrincipal.getUserID(), groupID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
        return role;
    }

    public static long authorizeGroupByKey(String urlKey, int requiredRole) {
        UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
        if (userPrincipal == null) {
            userPrincipal = threadLocal.get();
            if (userPrincipal == null) {
                throw new SecurityException();
            }
        }
        long groupID;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement idStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP_ID FROM COMMUNITY_GROUP WHERE URL_KEY = ?");
            idStmt.setString(1, urlKey);
            ResultSet rs = idStmt.executeQuery();
            if (rs.next()) {
                groupID = rs.getLong(1);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        int role = getRoleForGroup(userPrincipal.getUserID(), groupID);
        if (role > requiredRole) {
            throw new SecurityException();
        }
        return groupID;
    }

    public static void authorizeFeedAccess(long dataFeed) {
        if (getAccountTier() == Account.ADMINISTRATOR) {
            return;
        }
        // retrieve feed policy
        boolean accountVisible = false;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT ACCOUNT_VISIBLE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            authorizeStmt.setLong(1, dataFeed);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                accountVisible = rs.getBoolean(1);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

        if (accountVisible || isAccountReports()) {
            conn = Database.instance().getConnection();
            try {
                PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, UPLOAD_POLICY_USERS WHERE USER.USER_ID = " +
                        "UPLOAD_POLICY_USERS.user_id AND UPLOAD_POLICY_USERS.feed_id = ?");
                query.setLong(1, dataFeed);
                ResultSet rs = query.executeQuery();
                if (rs.next()) {
                    long accountID = rs.getLong(1);
                    if (accountID == getAccountID()) {
                        // all good
                    } else {
                        throw new SecurityException();
                    }
                } else {
                    throw new SecurityException();
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } else {
            UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
            if (userPrincipal == null) {
                userPrincipal = threadLocal.get();
                if (userPrincipal == null) {
                    throw new SecurityException(SecurityException.LOGIN_REQUIRED);
                }
            }
            int role = getRole(userPrincipal.getUserID(), dataFeed);
            if (role > Roles.SUBSCRIBER) {
                throw new SecurityException();
            }
        }
    }

    public static long authorizeFeedAccess(String urlKey) {
        // retrieve feed policy
        boolean accountVisible = false;
        long dataFeed;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement authorizeStmt = conn.prepareStatement("SELECT DATA_FEED_ID, ACCOUNT_VISIBLE FROM DATA_FEED WHERE DATA_FEED.API_KEY = ?");
            authorizeStmt.setString(1, urlKey);
            ResultSet rs = authorizeStmt.executeQuery();
            if (rs.next()) {
                dataFeed = rs.getLong(1);
                accountVisible = rs.getBoolean(2);
            } else {
                throw new SecurityException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        if (accountVisible && isAccountReports()) {
            conn = Database.instance().getConnection();
            try {
                PreparedStatement query = conn.prepareStatement("SELECT ACCOUNT_ID FROM USER, UPLOAD_POLICY_USERS WHERE USER.USER_ID = " +
                        "UPLOAD_POLICY_USERS.user_id AND UPLOAD_POLICY_USERS.feed_id = ?");
                query.setLong(1, dataFeed);
                ResultSet rs = query.executeQuery();
                if (rs.next()) {
                    long accountID = rs.getLong(1);
                    if (accountID == getAccountID()) {
                        // all good
                    } else {
                        throw new SecurityException();
                    }
                } else {
                    throw new SecurityException();
                }
            } catch (SQLException e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            } finally {
                Database.closeConnection(conn);
            }
        } else {
            UserPrincipal userPrincipal = securityProvider.getUserPrincipal();
            if (userPrincipal == null) {
                userPrincipal = threadLocal.get();
                if (userPrincipal == null) {
                    throw new SecurityException(SecurityException.LOGIN_REQUIRED);
                }
            }
            int role = getRole(userPrincipal.getUserID(), dataFeed);
            if (role > Roles.SUBSCRIBER) {
                throw new SecurityException();
            }
        }
        return dataFeed;
    }

    public static void clearThreadLocal() {
        threadLocal.remove();
    }

    public static String getPersonaName() {
        UserPrincipal userPrincipal = getSecurityProvider().getUserPrincipal();
        if (userPrincipal == null)
            userPrincipal = threadLocal.get();
        return userPrincipal.getPersonaName();
    }
}
