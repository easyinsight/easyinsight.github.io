package com.easyinsight.dashboard;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.core.RolePrioritySet;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.FeedRegistry;
import com.easyinsight.email.UserStub;
import com.easyinsight.security.Roles;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 5:41:34 PM
 */
public class DashboardStorage {

    public String urlKeyForID(long id) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT URL_KEY FROM DASHBOARD WHERE DASHBOARD_ID = ?");
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void saveDashboard(Dashboard dashboard) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            saveDashboard(dashboard, conn);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public RolePrioritySet<DashboardDescriptor> getDashboardForGroup(long groupID, EIConnection conn) throws SQLException {
        RolePrioritySet<DashboardDescriptor> descriptors = new RolePrioritySet<DashboardDescriptor>();
        PreparedStatement userGroupStmt = conn.prepareStatement("select DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id, dashboard.account_visible FROM dashboard, " +
                "group_to_dashboard WHERE " +
                "dashboard.dashboard_id = group_to_dashboard.dashboard_id and group_to_dashboard.group_id = ?");
        userGroupStmt.setLong(1, groupID);
        ResultSet groupRS = userGroupStmt.executeQuery();
        while (groupRS.next()) {
            descriptors.add(new DashboardDescriptor(groupRS.getString(2), groupRS.getLong(1), groupRS.getString(3), groupRS.getLong(4), Roles.SUBSCRIBER, null, groupRS.getBoolean(5)));
        }
        userGroupStmt.close();
        return descriptors;
    }

    public RolePrioritySet<DashboardDescriptor> getDashboards(long userID, long accountID, EIConnection conn) throws SQLException {
        RolePrioritySet<DashboardDescriptor> dashboards = new RolePrioritySet<DashboardDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id, dashboard.account_visible, dashboard.folder from " +
                "dashboard, user_to_dashboard, user where user.account_id = ? and dashboard.dashboard_id = user_to_dashboard.dashboard_id and " +
                "dashboard.temporary_dashboard = ? and dashboard.account_visible = ? and user_to_dashboard.user_id = user.user_id");
        PreparedStatement ownerStmt = conn.prepareStatement("SELECT user.first_name, user.name from user, user_to_dashboard where " +
                "user.user_id = user_to_dashboard.user_id and user_to_dashboard.dashboard_id = ?");
        queryStmt.setLong(1, accountID);
        queryStmt.setBoolean(2, false);
        queryStmt.setBoolean(3, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            ownerStmt.setLong(1, rs.getLong(1));
            ResultSet ownerRS = ownerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            dashboards.add(new DashboardDescriptor(rs.getString(2), rs.getLong(1), rs.getString(3), rs.getLong(4), Roles.SHARER, name, rs.getBoolean(5), rs.getInt(6)));
        }
        queryStmt.close();
        PreparedStatement ueryAccountStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id, " +
                "dashboard.account_visible, dashboard.folder from " +
                "dashboard, user_to_dashboard where user_id = ? and dashboard.dashboard_id = user_to_dashboard.dashboard_id and " +
                "dashboard.temporary_dashboard = ?");
        ueryAccountStmt.setLong(1, userID);
        ueryAccountStmt.setBoolean(2, false);
        ResultSet accountRS = ueryAccountStmt.executeQuery();
        while (accountRS.next()) {
            ownerStmt.setLong(1, accountRS.getLong(1));
            ResultSet ownerRS = ownerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            dashboards.add(new DashboardDescriptor(accountRS.getString(2), accountRS.getLong(1), accountRS.getString(3), accountRS.getLong(4), Roles.OWNER, name, accountRS.getBoolean(5),
                    accountRS.getInt(6)));
        }
        ueryAccountStmt.close();

        PreparedStatement dashboardGroupStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.data_source_id, dashboard.URL_KEY, group_to_user_join.binding_type, " +
                "dashboard.creation_date, dashboard.account_visible, dashboard.folder FROM dashboard, group_to_user_join," +
                "group_to_dashboard WHERE " +
                "dashboard.dashboard_id = group_to_dashboard.dashboard_id and group_to_dashboard.group_id = group_to_user_join.group_id and group_to_user_join.user_id = ? and dashboard.temporary_dashboard = ?");
        dashboardGroupStmt.setLong(1, userID);
        dashboardGroupStmt.setBoolean(2, false);
        ResultSet dashboardRS = dashboardGroupStmt.executeQuery();
        while (dashboardRS.next()) {
            ownerStmt.setLong(1, dashboardRS.getLong(1));
            ResultSet ownerRS = ownerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            dashboards.add(new DashboardDescriptor(dashboardRS.getString(2), dashboardRS.getLong(1),  dashboardRS.getString(4), dashboardRS.getLong(3), Roles.SUBSCRIBER, name,
                    dashboardRS.getBoolean(7), dashboardRS.getInt(8)));
        }
        ownerStmt.close();
        dashboardGroupStmt.close();
        return dashboards;
    }

    public RolePrioritySet<DashboardDescriptor> getDashboardsForDataSource(long userID, long accountID, EIConnection conn, long dataSourceID) throws SQLException {
        RolePrioritySet<DashboardDescriptor> dashboards = new RolePrioritySet<DashboardDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id, dashboard.account_visible, dashboard.folder from " +
                "dashboard, user_to_dashboard, user where user.account_id = ? and dashboard.dashboard_id = user_to_dashboard.dashboard_id and " +
                "dashboard.temporary_dashboard = ? and dashboard.account_visible = ? and user_to_dashboard.user_id = user.user_id and dashboard.data_source_id = ?");
        PreparedStatement ownerStmt = conn.prepareStatement("SELECT user.first_name, user.name from user, user_to_dashboard where " +
                "user.user_id = user_to_dashboard.user_id and user_to_dashboard.dashboard_id = ?");
        queryStmt.setLong(1, accountID);
        queryStmt.setBoolean(2, false);
        queryStmt.setBoolean(3, true);
        queryStmt.setLong(4, dataSourceID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            ownerStmt.setLong(1, rs.getLong(1));
            ResultSet ownerRS = ownerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            dashboards.add(new DashboardDescriptor(rs.getString(2), rs.getLong(1), rs.getString(3), rs.getLong(4), Roles.SHARER, name, rs.getBoolean(5), rs.getInt(6)));
        }
        queryStmt.close();
        PreparedStatement ueryAccountStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id, " +
                "dashboard.account_visible, dashboard.folder from " +
                "dashboard, user_to_dashboard where user_id = ? and dashboard.dashboard_id = user_to_dashboard.dashboard_id and " +
                "dashboard.temporary_dashboard = ? and dashboard.data_source_id = ?");
        ueryAccountStmt.setLong(1, userID);
        ueryAccountStmt.setBoolean(2, false);
        ueryAccountStmt.setLong(3, dataSourceID);
        ResultSet accountRS = ueryAccountStmt.executeQuery();
        while (accountRS.next()) {
            ownerStmt.setLong(1, accountRS.getLong(1));
            ResultSet ownerRS = ownerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            dashboards.add(new DashboardDescriptor(accountRS.getString(2), accountRS.getLong(1), accountRS.getString(3), accountRS.getLong(4), Roles.OWNER, name, accountRS.getBoolean(5),
                    accountRS.getInt(6)));
        }
        ueryAccountStmt.close();

        PreparedStatement dashboardGroupStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.data_source_id, dashboard.URL_KEY, group_to_user_join.binding_type, " +
                "dashboard.creation_date, dashboard.account_visible, dashboard.folder FROM dashboard, group_to_user_join," +
                "group_to_dashboard WHERE " +
                "dashboard.dashboard_id = group_to_dashboard.dashboard_id and group_to_dashboard.group_id = group_to_user_join.group_id and group_to_user_join.user_id = ? and dashboard.temporary_dashboard = ? and " +
                "dashboard.data_source_id = ?");
        dashboardGroupStmt.setLong(1, userID);
        dashboardGroupStmt.setBoolean(2, false);
        dashboardGroupStmt.setLong(3, dataSourceID);
        ResultSet dashboardRS = dashboardGroupStmt.executeQuery();
        while (dashboardRS.next()) {
            ownerStmt.setLong(1, dashboardRS.getLong(1));
            ResultSet ownerRS = ownerStmt.executeQuery();
            String name;
            if (ownerRS.next()) {
                String firstName = ownerRS.getString(1);
                String lastName = ownerRS.getString(2);
                name = firstName != null ? firstName + " " + lastName : lastName;
            } else {
                name = "";
            }
            dashboards.add(new DashboardDescriptor(dashboardRS.getString(2), dashboardRS.getLong(1),  dashboardRS.getString(4), dashboardRS.getLong(3), Roles.SUBSCRIBER, name,
                    dashboardRS.getBoolean(7), dashboardRS.getInt(8)));
        }
        ownerStmt.close();
        dashboardGroupStmt.close();
        return dashboards;
    }

    public void saveDashboard(Dashboard dashboard, EIConnection conn) throws SQLException {
        if (dashboard.getId() == 0) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD (DASHBOARD_NAME, URL_KEY, " +
                    "ACCOUNT_VISIBLE, DATA_SOURCE_ID, CREATION_DATE, UPDATE_DATE, DESCRIPTION, EXCHANGE_VISIBLE, AUTHOR_NAME, TEMPORARY_DASHBOARD," +
                    "PUBLIC_VISIBLE, border_color, border_thickness, background_color, padding," +
                    "recommended_exchange, ytd_date, ytd_override, marmotscript, folder, absolute_sizing) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            insertStmt.setString(1, dashboard.getName());
            insertStmt.setString(2, dashboard.getUrlKey());
            insertStmt.setBoolean(3, dashboard.isAccountVisible());
            insertStmt.setLong(4, dashboard.getDataSourceID());
            insertStmt.setTimestamp(5, new Timestamp(dashboard.getCreationDate().getTime()));
            insertStmt.setTimestamp(6, new Timestamp(dashboard.getUpdateDate().getTime()));
            insertStmt.setString(7, dashboard.getDescription());
            insertStmt.setBoolean(8, dashboard.isExchangeVisible());
            insertStmt.setString(9, dashboard.getAuthorName());
            insertStmt.setBoolean(10, dashboard.isTemporary());
            insertStmt.setBoolean(11, dashboard.isPublicVisible());
            insertStmt.setInt(12, dashboard.getBorderColor());
            insertStmt.setInt(13, dashboard.getBorderThickness());
            insertStmt.setInt(14, dashboard.getBackgroundColor());
            insertStmt.setInt(15, dashboard.getPadding());
            insertStmt.setBoolean(16, dashboard.isRecommendedExchange());
            insertStmt.setString(17, dashboard.getYtdMonth() == null ? "December" : dashboard.getYtdMonth());
            insertStmt.setBoolean(18, dashboard.isOverrideYTD());
            insertStmt.setString(19, dashboard.getMarmotScript());
            insertStmt.setInt(20, dashboard.getFolder());
            insertStmt.setBoolean(21, dashboard.isAbsoluteSizing());
            insertStmt.execute();
            dashboard.setId(Database.instance().getAutoGenKey(insertStmt));
            insertStmt.close();
        } else {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET DASHBOARD_NAME = ?," +
                    "URL_KEY = ?, ACCOUNT_VISIBLE = ?, UPDATE_DATE = ?, DESCRIPTION = ?, EXCHANGE_VISIBLE = ?, AUTHOR_NAME = ?, TEMPORARY_DASHBOARD = ?," +
                    "PUBLIC_VISIBLE = ?, border_color = ?, border_thickness = ?, background_color = ?, padding = ?," +
                    "recommended_exchange = ?, ytd_date = ?, ytd_override = ?, marmotscript = ?, folder = ?, absolute_sizing = ? WHERE DASHBOARD_ID = ?");
            updateStmt.setString(1, dashboard.getName());
            updateStmt.setString(2, dashboard.getUrlKey());
            updateStmt.setBoolean(3, dashboard.isAccountVisible());
            updateStmt.setTimestamp(4, new Timestamp(dashboard.getUpdateDate().getTime()));
            updateStmt.setString(5, dashboard.getDescription());
            updateStmt.setBoolean(6, dashboard.isExchangeVisible());
            updateStmt.setString(7, dashboard.getAuthorName());
            updateStmt.setBoolean(8, dashboard.isTemporary());
            updateStmt.setBoolean(9, dashboard.isPublicVisible());
            updateStmt.setInt(10, dashboard.getBorderColor());
            updateStmt.setInt(11, dashboard.getBorderThickness());
            updateStmt.setInt(12, dashboard.getBackgroundColor());
            updateStmt.setInt(13, dashboard.getPadding());
            updateStmt.setBoolean(14, dashboard.isRecommendedExchange());
            updateStmt.setString(15, dashboard.getYtdMonth() == null ? "December" : dashboard.getYtdMonth());
            updateStmt.setBoolean(16, dashboard.isOverrideYTD());
            updateStmt.setString(17, dashboard.getMarmotScript());
            updateStmt.setInt(18, dashboard.getFolder());
            updateStmt.setBoolean(19, dashboard.isAbsoluteSizing());
            updateStmt.setLong(20, dashboard.getId());
            updateStmt.executeUpdate();
            updateStmt.close();
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM DASHBOARD_TO_DASHBOARD_ELEMENT WHERE DASHBOARD_ID = ?");
            clearStmt.setLong(1, dashboard.getId());
            clearStmt.executeUpdate();
            clearStmt.close();
            PreparedStatement clearUserStmt = conn.prepareStatement("DELETE FROM USER_TO_DASHBOARD WHERE DASHBOARD_ID = ?");
            clearUserStmt.setLong(1, dashboard.getId());
            clearUserStmt.executeUpdate();
            clearUserStmt.close();
            PreparedStatement clearDSStmt = conn.prepareStatement("DELETE FROM DASHBOARD_TO_FILTER WHERE DASHBOARD_ID = ?");
            clearDSStmt.setLong(1, dashboard.getId());
            clearDSStmt.executeUpdate();
            clearDSStmt.close();
        }

        long id = dashboard.getRootElement().save(conn);

        PreparedStatement saveRootStmt = conn.prepareStatement("INSERT INTO dashboard_to_dashboard_element (dashboard_id, dashboard_element_id) values (?, ?)");
        saveRootStmt.setLong(1, dashboard.getId());
        saveRootStmt.setLong(2, id);
        saveRootStmt.execute();
        saveRootStmt.close();

        Session session = Database.instance().createSession(conn);
        try {
            for (FilterDefinition filterDefinition : dashboard.getFilters()) {
                filterDefinition.beforeSave(session);
                session.saveOrUpdate(filterDefinition);
            }
            session.flush();
        } finally {
            session.close();
        }

        PreparedStatement filterStmt = conn.prepareStatement("INSERT INTO DASHBOARD_TO_FILTER (DASHBOARD_ID, FILTER_ID) VALUES (?, ?)");
        for (FilterDefinition filterDefinition : dashboard.getFilters()) {
            filterStmt.setLong(1, dashboard.getId());
            filterStmt.setLong(2, filterDefinition.getFilterID());
            filterStmt.execute();
        }
        filterStmt.close();

        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO USER_TO_DASHBOARD (USER_ID, DASHBOARD_ID) VALUES (?, ?)");
        for (FeedConsumer feedConsumer : dashboard.getAdministrators()) {
            UserStub userStub = (UserStub) feedConsumer;
            saveStmt.setLong(1, userStub.getUserID());
            saveStmt.setLong(2, dashboard.getId());
            saveStmt.execute();
        }
        saveStmt.close();
    }

    public Dashboard getDashboard(long dashboardID, EIConnection conn) throws Exception {
        Dashboard dashboard;
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD_NAME, URL_KEY, ACCOUNT_VISIBLE, DATA_SOURCE_ID, CREATION_DATE," +
                    "UPDATE_DATE, DESCRIPTION, EXCHANGE_VISIBLE, AUTHOR_NAME, temporary_dashboard, public_visible, border_color, border_thickness," +
                "background_color, padding, recommended_exchange, ytd_date, ytd_override, marmotscript, folder, absolute_sizing FROM DASHBOARD WHERE DASHBOARD_ID = ?");
        queryStmt.setLong(1, dashboardID);
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            dashboard = new Dashboard();
            dashboard.setId(dashboardID);
            dashboard.setName(rs.getString(1));
            dashboard.setUrlKey(rs.getString(2));
            dashboard.setAccountVisible(rs.getBoolean(3));
            dashboard.setDataSourceID(rs.getLong(4));
            dashboard.setCreationDate(new Date(rs.getTimestamp(5).getTime()));
            dashboard.setUpdateDate(new Date(rs.getTimestamp(6).getTime()));
            dashboard.setDescription(rs.getString(7));
            dashboard.setExchangeVisible(rs.getBoolean(8));
            dashboard.setAuthorName(rs.getString(9));
            dashboard.setTemporary(rs.getBoolean(10));
            dashboard.setPublicVisible(rs.getBoolean(11));
            dashboard.setBorderColor(rs.getInt(12));
            dashboard.setBorderThickness(rs.getInt(13));
            dashboard.setBackgroundColor(rs.getInt(14));
            dashboard.setPadding(rs.getInt(15));
            dashboard.setRecommendedExchange(rs.getBoolean(16));
            dashboard.setYtdMonth(rs.getString(17));
            dashboard.setOverrideYTD(rs.getBoolean(18));
            dashboard.setMarmotScript(rs.getString(19));
            dashboard.setFolder(rs.getInt(20));
            dashboard.setAbsoluteSizing(rs.getBoolean(21));
            PreparedStatement findElementsStmt = conn.prepareStatement("SELECT DASHBOARD_ELEMENT.DASHBOARD_ELEMENT_ID, ELEMENT_TYPE FROM " +
                    "DASHBOARD_ELEMENT, DASHBOARD_TO_DASHBOARD_ELEMENT WHERE DASHBOARD_ID = ? AND DASHBOARD_ELEMENT.DASHBOARD_ELEMENT_ID = DASHBOARD_TO_DASHBOARD_ELEMENT.DASHBOARD_ELEMENT_ID");
            findElementsStmt.setLong(1, dashboardID);
            ResultSet elementRS = findElementsStmt.executeQuery();
            if (elementRS.next()) {
                long elementID = elementRS.getLong(1);
                int elementType = elementRS.getInt(2);
                DashboardElement dashboardElement = getElement(conn, elementID, elementType);
                dashboard.setRootElement(dashboardElement);
            }
            findElementsStmt.close();
        } else {
            throw new RuntimeException("Couldn't find dashboard " + dashboardID);
        }
        queryStmt.close();

        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM dashboard_to_filter where dashboard_id = ?");
            filterStmt.setLong(1, dashboardID);
            ResultSet filterRS = filterStmt.executeQuery();
            while (filterRS.next()) {
                FilterDefinition filter = (FilterDefinition) session.createQuery("from FilterDefinition where filterID = ?").setLong(0, filterRS.getLong(1)).list().get(0);
                filter.afterLoad();
                filters.add(filter);
            }
            filterStmt.close();
        } finally {
            session.close();
        }
        dashboard.setFilters(filters);
        PreparedStatement getUserStmt = conn.prepareStatement("SELECT USER_ID FROM USER_TO_DASHBOARD WHERE DASHBOARD_ID = ?");
        getUserStmt.setLong(1, dashboardID);
        ResultSet userRS = getUserStmt.executeQuery();
        List<FeedConsumer> admins = new ArrayList<FeedConsumer>();
        while (userRS.next()) {
            long userID = userRS.getLong(1);
            // TODO: cleanup
            admins.add(new UserStub(userID, null, null, null, 0, null));
        }
        getUserStmt.close();
        dashboard.setAdministrators(admins);
        Feed feed = FeedRegistry.instance().getFeed(dashboard.getDataSourceID(), conn);
        dashboard.setDataSourceInfo(feed.createSourceInfo(conn));
        return dashboard;
    }

    public Dashboard getDashboard(long dashboardID) throws Exception {
        Dashboard dashboard = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            dashboard = getDashboard(dashboardID, conn);
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return dashboard;
    }

    public static DashboardElement getElement(EIConnection conn, long elementID, int elementType) throws SQLException {
        DashboardElement element;
        if (elementType == DashboardElement.GRID) {
            element = DashboardGrid.loadGrid(elementID, conn);
        } else if (elementType == DashboardElement.REPORT) {
            element = DashboardReport.loadReport(elementID, conn);
        } else if (elementType == DashboardElement.STACK) {
            element = DashboardStack.loadGrid(elementID, conn);
        } else if (elementType == DashboardElement.IMAGE) {
            element = DashboardImage.loadImage(elementID, conn);
        } else if (elementType == DashboardElement.SCORECARD) {
            element = DashboardScorecard.loadReport(elementID, conn);
        } else if (elementType == DashboardElement.TEXT) {
            element = DashboardText.loadImage(elementID, conn);
        } else {
            throw new RuntimeException();
        }
        return element;
    }

    public void deleteDashboard(long dashboardID) throws Exception {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DASHBOARD WHERE DASHBOARD_ID = ?");
            deleteStmt.setLong(1, dashboardID);
            deleteStmt.executeUpdate();
            deleteStmt.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }
}
