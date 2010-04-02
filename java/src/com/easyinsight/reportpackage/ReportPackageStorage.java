package com.easyinsight.reportpackage;

import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.email.UserStub;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;
import com.easyinsight.util.RandomTextGenerator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Dec 8, 2009
 * Time: 11:09:03 PM
 */
public class ReportPackageStorage {
    public long saveReportPackage(ReportPackage reportPackage) {
        if (reportPackage.getReportPackageID() > 0) {
            SecurityUtil.authorizePackage(reportPackage.getReportPackageID());
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long reportPackageID = saveReportPackage(reportPackage, conn);
            conn.commit();
            return reportPackageID;
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public long saveReportPackage(ReportPackage reportPackage, EIConnection conn) throws SQLException {
        if (reportPackage.getReportPackageID() > 0) {
            SecurityUtil.authorizePackage(reportPackage.getReportPackageID());
        }
        if (reportPackage.getUrlKey() == null) {
            reportPackage.setUrlKey(RandomTextGenerator.generateText(20));
        }
        if (reportPackage.getDateCreated() == null) {
            reportPackage.setDateCreated(new java.util.Date());
        }
        long reportPackageID;
            if (reportPackage.getReportPackageID() == 0) {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO REPORT_PACKAGE (PACKAGE_NAME, CONNECTION_VISIBLE," +
                        "PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE, DATA_SOURCE_ID, DATE_CREATED, AUTHOR_NAME, DESCRIPTION, LIMITED_SOURCE, TEMPORARY_PACKAGE, URL_KEY) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, reportPackage.getName());
                insertStmt.setBoolean(2, reportPackage.isConnectionVisible());
                insertStmt.setBoolean(3, reportPackage.isPubliclyVisible());
                insertStmt.setBoolean(4, reportPackage.isMarketplaceVisible());
                if (reportPackage.getDataSourceID() > 0) {
                    insertStmt.setLong(5, reportPackage.getDataSourceID());
                } else {
                    insertStmt.setNull(5, Types.BIGINT);
                }
                insertStmt.setDate(6, new java.sql.Date(reportPackage.getDateCreated().getTime()));
                insertStmt.setString(7, reportPackage.getAuthorName());
                insertStmt.setString(8, reportPackage.getDescription());
                insertStmt.setBoolean(9, reportPackage.isSingleDataSource());
                insertStmt.setBoolean(10, reportPackage.isTemporaryPackage());
                insertStmt.setString(11, reportPackage.getUrlKey());
                insertStmt.execute();
                reportPackageID = Database.instance().getAutoGenKey(insertStmt);
                reportPackage.setReportPackageID(reportPackageID);
            } else {
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE REPORT_PACKAGE SET PACKAGE_NAME = ?, CONNECTION_VISIBLE = ?," +
                        "PUBLICLY_VISIBLE = ?, MARKETPLACE_VISIBLE = ?, DATA_SOURCE_ID = ?, DATE_CREATED = ?, AUTHOR_NAME = ?, DESCRIPTION = ?," +
                        "LIMITED_SOURCE = ?, TEMPORARY_PACKAGE = ?, URL_KEY = ? WHERE REPORT_PACKAGE_ID = ?");
                updateStmt.setString(1, reportPackage.getName());
                updateStmt.setBoolean(2, reportPackage.isConnectionVisible());
                updateStmt.setBoolean(3, reportPackage.isPubliclyVisible());
                updateStmt.setBoolean(4, reportPackage.isMarketplaceVisible());
                if (reportPackage.getDataSourceID() > 0) {
                    updateStmt.setLong(5, reportPackage.getDataSourceID());
                } else {
                    updateStmt.setNull(5, Types.BIGINT);
                }
                updateStmt.setDate(6, new java.sql.Date(reportPackage.getDateCreated().getTime()));
                updateStmt.setString(7, reportPackage.getAuthorName());
                updateStmt.setString(8, reportPackage.getDescription());
                updateStmt.setBoolean(9, reportPackage.isSingleDataSource());
                updateStmt.setBoolean(10, reportPackage.isTemporaryPackage());
                updateStmt.setString(11, reportPackage.getUrlKey());
                updateStmt.setLong(12, reportPackage.getReportPackageID());
                updateStmt.executeUpdate();
                reportPackageID = reportPackage.getReportPackageID();
            }
            saveUsers(reportPackage, conn);
            PreparedStatement clearReportsStmt = conn.prepareStatement("DELETE FROM REPORT_PACKAGE_TO_REPORT WHERE REPORT_PACKAGE_ID = ?");
            clearReportsStmt.setLong(1, reportPackage.getReportPackageID());
            clearReportsStmt.executeUpdate();
            int i = 0;
            PreparedStatement insertReportStmt = conn.prepareStatement("INSERT INTO REPORT_PACKAGE_TO_REPORT (REPORT_ID, REPORT_PACKAGE_ID, REPORT_ORDER) VALUES (?, ?, ?)");
            for (InsightDescriptor insightDescriptor : reportPackage.getReports()) {
                insertReportStmt.setLong(1, insightDescriptor.getId());
                insertReportStmt.setLong(2, reportPackageID);
                insertReportStmt.setInt(3, i++);
                insertReportStmt.execute();
            }

            return reportPackageID;

    }

    public ReportPackage getReportPackage(long packageID) {
        ReportPackage reportPackage = null;
        SecurityUtil.authorizePackage(packageID);
        EIConnection conn = Database.instance().getConnection();
        try {
            reportPackage = getReportPackage(packageID, conn);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return reportPackage;
    }

    public ReportPackage getReportPackage(long packageID, EIConnection conn) throws SQLException {
        ReportPackage reportPackage = null;        

            PreparedStatement queryStmt = conn.prepareStatement("SELECT PACKAGE_NAME, CONNECTION_VISIBLE, PUBLICLY_VISIBLE, MARKETPLACE_VISIBLE," +
                    "DATA_SOURCE_ID, DATE_CREATED, AUTHOR_NAME, DESCRIPTION, LIMITED_SOURCE, TEMPORARY_PACKAGE, URL_KEY FROM " +
                    "REPORT_PACKAGE WHERE REPORT_PACKAGE_ID = ?");
            queryStmt.setLong(1, packageID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String packageName = rs.getString(1);
                boolean connectionVisible = rs.getBoolean(2);
                boolean publiclyVisible = rs.getBoolean(3);
                boolean marketplaceVisible = rs.getBoolean(4);
                long dataSourceID = rs.getLong(5);
                if (rs.wasNull()) dataSourceID = 0;
                Date dateCreated = rs.getDate(6);
                String authorName = rs.getString(7);
                String description = rs.getString(8);
                boolean limitedSource = rs.getBoolean(9);
                boolean temporaryPackage = rs.getBoolean(10);
                String urlKey = rs.getString(11);
                PreparedStatement getReportsStmt = conn.prepareStatement("SELECT REPORT_ID, TITLE, REPORT_TYPE, DATA_FEED_ID FROM REPORT_PACKAGE_TO_REPORT, ANALYSIS WHERE " +
                        "REPORT_PACKAGE_TO_REPORT.REPORT_ID = ANALYSIS.ANALYSIS_ID AND REPORT_PACKAGE_TO_REPORT.REPORT_PACKAGE_ID = ? ORDER BY REPORT_ORDER ASC");
                getReportsStmt.setLong(1, packageID);
                ResultSet reportRS = getReportsStmt.executeQuery();
                List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
                while (reportRS.next()) {
                    reports.add(new InsightDescriptor(reportRS.getLong(1), reportRS.getString(2), reportRS.getLong(4), reportRS.getInt(3)));
                }
                reportPackage = new ReportPackage();
                reportPackage.setReportPackageID(packageID);
                reportPackage.setName(packageName);
                reportPackage.setConnectionVisible(connectionVisible);
                reportPackage.setMarketplaceVisible(marketplaceVisible);
                reportPackage.setPubliclyVisible(publiclyVisible);
                reportPackage.setDataSourceID(dataSourceID);
                reportPackage.setDateCreated(dateCreated);
                reportPackage.setAuthorName(authorName);
                reportPackage.setDescription(description);
                reportPackage.setSingleDataSource(limitedSource);
                reportPackage.setTemporaryPackage(temporaryPackage);
                reportPackage.setReports(reports);
                reportPackage.setUrlKey(urlKey);
                populateUsers(reportPackage, conn);
            }

        return reportPackage;
    }

    public ReportPackageResponse openPackageIfPossible(String urlKey) {
        ReportPackageResponse feedResponse;
        try {
            try {
                long packageID = SecurityUtil.authorizePackage(urlKey);
                //long userID = SecurityUtil.getUserID();
                ReportPackageDescriptor reportPackageDescriptor = getDescriptor(packageID);
                feedResponse = new ReportPackageResponse(ReportPackageResponse.SUCCESS, reportPackageDescriptor);
            } catch (com.easyinsight.security.SecurityException e) {
                if (e.getReason() == SecurityException.LOGIN_REQUIRED)
                    feedResponse = new ReportPackageResponse(ReportPackageResponse.NEED_LOGIN, null);
                else
                    feedResponse = new ReportPackageResponse(ReportPackageResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedResponse;
    }

    public void deleteReportPackage(long packageID) {
        EIConnection conn = Database.instance().getConnection();
        SecurityUtil.authorizePackage(packageID);
        try {
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM REPORT_PACKAGE WHERE REPORT_PACKAGE_ID = ?");
            deleteStmt.setLong(1, packageID);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void saveUsers(ReportPackage reportPackage, EIConnection conn) throws SQLException {
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM USER_TO_REPORT_PACKAGE WHERE REPORT_PACKAGE_ID = ?");
        clearStmt.setLong(1, reportPackage.getReportPackageID());
        clearStmt.executeUpdate();
        for (FeedConsumer feedConsumer : reportPackage.getAdministrators()) {
            PreparedStatement addUserStmt = conn.prepareStatement("INSERT INTO USER_TO_REPORT_PACKAGE (REPORT_PACKAGE_ID, USER_ID, ROLE) VALUES (?, ?, ?)");
            if (feedConsumer instanceof UserStub) {
                UserStub userStub = (UserStub) feedConsumer;
                addUserStmt.setLong(1, reportPackage.getReportPackageID());
                addUserStmt.setLong(2, userStub.getUserID());
                addUserStmt.setInt(3, Roles.OWNER);
                addUserStmt.execute();
            }
        }
        for (FeedConsumer feedConsumer : reportPackage.getConsumers()) {
            PreparedStatement addUserStmt = conn.prepareStatement("INSERT INTO USER_TO_REPORT_PACKAGE (REPORT_PACKAGE_ID, USER_ID, ROLE) VALUES (?, ?, ?)");
            if (feedConsumer instanceof UserStub) {
                UserStub userStub = (UserStub) feedConsumer;
                addUserStmt.setLong(1, reportPackage.getReportPackageID());
                addUserStmt.setLong(2, userStub.getUserID());
                addUserStmt.setInt(3, Roles.SUBSCRIBER);
                addUserStmt.execute();
            }
        }
    }

    private void populateUsers(ReportPackage reportPackage, Connection conn) throws SQLException {
        List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();
        List<FeedConsumer> consumers = new ArrayList<FeedConsumer>();
        PreparedStatement getUsersStmt = conn.prepareStatement("SELECT USER.USER_ID, role, USER.email, USER.name, USER.username, USER.account_id FROM " +
                "USER_TO_REPORT_PACKAGE, USER WHERE REPORT_PACKAGE_ID = ? AND USER_TO_REPORT_PACKAGE.USER_ID = USER.USER_ID");
        getUsersStmt.setLong(1, reportPackage.getReportPackageID());
        ResultSet userRS = getUsersStmt.executeQuery();
        while (userRS.next()) {
            long userID = userRS.getLong(1);
            int role = userRS.getInt(2);
            String email = userRS.getString(3);
            String fullName = userRS.getString(4);
            String userName = userRS.getString(5);
            long accountID = userRS.getLong(6);
            if (role == Roles.OWNER) {
                administrators.add(new UserStub(userID, userName, email, fullName, accountID));
            } else {
                consumers.add(new UserStub(userID, userName, email, fullName, accountID));
            }
        }
        /*PreparedStatement getGroupsStmt = conn.prepareStatement("SELECT COMMUNITY_GROUP.COMMUNITY_GROUP_ID, ROLE, COMMUNITY_GROUP.name " +
                "FROM GOAL_TREE_TO_GROUP, COMMUNITY_GROUP WHERE GOAL_TREE_ID = ? AND " +
                "COMMUNITY_GROUP.COMMUNITY_GROUP_ID = GOAL_TREE_TO_GROUP.GROUP_ID");
        getGroupsStmt.setLong(1, reportPackage.getReportPackageID());
        ResultSet groupRS = getGroupsStmt.executeQuery();
        while (groupRS.next()) {
            long groupID = groupRS.getLong(1);
            int role = groupRS.getInt(2);
            String name = groupRS.getString(3);
            if (role == Roles.OWNER) {
                administrators.add(new GroupDescriptor(name, groupID, 0, null));
            } else {
                consumers.add(new GroupDescriptor(name, groupID, 0, null));
            }
        }*/
        reportPackage.setAdministrators(administrators);
        reportPackage.setConsumers(consumers);
    }

    private ReportPackageDescriptor getDescriptor(long packageID) {
        EIConnection conn = Database.instance().getConnection();
        SecurityUtil.authorizePackage(packageID);
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT PACKAGE_NAME FROM REPORT_PACKAGE WHERE REPORT_PACKAGE_ID = ?");
            queryStmt.setLong(1, packageID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                ReportPackageDescriptor reportPackageDescriptor = new ReportPackageDescriptor();
                reportPackageDescriptor.setId(packageID);
                reportPackageDescriptor.setName(rs.getString(1));
                return reportPackageDescriptor;
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        throw new RuntimeException();
    }

    public void keepPackage(long packageID) {
        SecurityUtil.authorizePackage(packageID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updatePackageStmt = conn.prepareStatement("UPDATE REPORT_PACKAGE SET TEMPORARY_PACKAGE = ? WHERE REPORT_PACKAGE_ID = ?");
            updatePackageStmt.setBoolean(1, false);
            updatePackageStmt.setLong(2, packageID);
            updatePackageStmt.executeUpdate();
            PreparedStatement getReportsStmt = conn.prepareStatement("SELECT REPORT_ID FROM REPORT_PACKAGE_TO_REPORT WHERE REPORT_PACKAGE_ID = ?");
            getReportsStmt.setLong(1, packageID);
            ResultSet reportRS = getReportsStmt.executeQuery();
            while (reportRS.next()) {
                long reportID = reportRS.getLong(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET TEMPORARY_REPORT = ? WHERE ANALYSIS_ID = ?");
                updateStmt.setBoolean(1, false);
                updateStmt.setLong(2, reportID);
                updateStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<ReportPackageDescriptor> getReportPackagesForUser() {
        List<ReportPackageDescriptor> packageList = new ArrayList<ReportPackageDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT PACKAGE_NAME, REPORT_PACKAGE.REPORT_PACKAGE_ID FROM REPORT_PACKAGE, USER_TO_REPORT_PACKAGE WHERE USER_ID = ? AND " +
                    "REPORT_PACKAGE.report_package_id = user_to_report_package.report_package_id AND TEMPORARY_PACKAGE = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setBoolean(2, false);
            ResultSet packageRS = queryStmt.executeQuery();
            while (packageRS.next()) {
                ReportPackageDescriptor descriptor = new ReportPackageDescriptor();
                descriptor.setId(packageRS.getLong(2));
                descriptor.setName(packageRS.getString(1));
                packageList.add(descriptor);
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return packageList;
    }
}
