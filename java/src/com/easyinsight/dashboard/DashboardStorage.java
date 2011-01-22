package com.easyinsight.dashboard;

import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.core.RolePrioritySet;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedConsumer;
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

    public RolePrioritySet<DashboardDescriptor> getDashboards(long userID, long accountID, EIConnection conn) throws SQLException {
        RolePrioritySet<DashboardDescriptor> dashboards = new RolePrioritySet<DashboardDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id from " +
                "dashboard, user_to_dashboard, user where user.account_id = ? and dashboard.dashboard_id = user_to_dashboard.dashboard_id and " +
                "dashboard.temporary_dashboard = ? and dashboard.account_visible = ? and user_to_dashboard.user_id = user.user_id");
        queryStmt.setLong(1, accountID);
        queryStmt.setBoolean(2, false);
        queryStmt.setBoolean(3, true);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            dashboards.add(new DashboardDescriptor(rs.getString(2), rs.getLong(1), rs.getString(3), rs.getLong(4), Roles.OWNER));
        }
        queryStmt.close();
        PreparedStatement ueryAccountStmt = conn.prepareStatement("SELECT DASHBOARD.dashboard_id, dashboard.dashboard_name, dashboard.url_key, dashboard.data_source_id from " +
                "dashboard, user_to_dashboard where user_id = ? and dashboard.dashboard_id = user_to_dashboard.dashboard_id and " +
                "dashboard.temporary_dashboard = ?");
        ueryAccountStmt.setLong(1, userID);
        ueryAccountStmt.setBoolean(2, false);
        ResultSet accountRS = ueryAccountStmt.executeQuery();
        while (accountRS.next()) {
            dashboards.add(new DashboardDescriptor(accountRS.getString(2), accountRS.getLong(1), accountRS.getString(3), accountRS.getLong(4), Roles.SHARER));
        }
        ueryAccountStmt.close();
        return dashboards;
    }

    public void saveDashboard(Dashboard dashboard, EIConnection conn) throws SQLException {
        if (dashboard.getId() == 0) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO DASHBOARD (DASHBOARD_NAME, URL_KEY, " +
                    "ACCOUNT_VISIBLE, DATA_SOURCE_ID, CREATION_DATE, UPDATE_DATE, DESCRIPTION, EXCHANGE_VISIBLE, AUTHOR_NAME, TEMPORARY_DASHBOARD) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
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
            insertStmt.execute();
            dashboard.setId(Database.instance().getAutoGenKey(insertStmt));
            insertStmt.close();
        } else {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET DASHBOARD_NAME = ?," +
                    "URL_KEY = ?, ACCOUNT_VISIBLE = ?, UPDATE_DATE = ?, DESCRIPTION = ?, EXCHANGE_VISIBLE = ?, AUTHOR_NAME = ?, TEMPORARY_DASHBOARD = ? WHERE DASHBOARD_ID = ?");
            updateStmt.setString(1, dashboard.getName());
            updateStmt.setString(2, dashboard.getUrlKey());
            updateStmt.setBoolean(3, dashboard.isAccountVisible());
            updateStmt.setTimestamp(4, new Timestamp(dashboard.getUpdateDate().getTime()));
            updateStmt.setString(5, dashboard.getDescription());
            updateStmt.setBoolean(6, dashboard.isExchangeVisible());
            updateStmt.setString(7, dashboard.getAuthorName());
            updateStmt.setBoolean(8, dashboard.isTemporary());
            updateStmt.setLong(9, dashboard.getId());
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
                    "UPDATE_DATE, DESCRIPTION, EXCHANGE_VISIBLE, AUTHOR_NAME, temporary_dashboard FROM DASHBOARD WHERE DASHBOARD_ID = ?");
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
