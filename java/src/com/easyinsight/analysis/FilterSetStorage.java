package com.easyinsight.analysis;

import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: 1/20/14
 * Time: 1:28 PM
 */
public class FilterSetStorage {
    public void saveFilterSet(FilterSet filterSet) {
        EIConnection conn = Database.instance().getConnection();
        try {
            if (filterSet.getId() == 0) {
                filterSet.setUrlKey(RandomTextGenerator.generateText(20));
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO FILTER_SET (USER_ID, DATA_SOURCE_ID, FILTER_SET_NAME, FILTER_SET_DESCRIPTION, URL_KEY) " +
                        "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setLong(1, SecurityUtil.getAccountID());
                stmt.setLong(2, filterSet.getDataSourceID());
                stmt.setString(3, filterSet.getName());
                stmt.setString(4, filterSet.getDescription());
                stmt.setString(5, filterSet.getUrlKey());
                stmt.execute();
                filterSet.setId(Database.instance().getAutoGenKey(stmt));
                stmt.close();
            } else {
                PreparedStatement stmt = conn.prepareStatement("UPDATE FILTER_SET SET FILTER_SET_NAME = ?, FILTER_SET_DESCRIPTION = ?, URL_KEY = ? WHERE " +
                        "FILTER_SET_ID = ?");
                stmt.setString(1, filterSet.getName());
                stmt.setString(2, filterSet.getDescription());
                stmt.setString(3, filterSet.getUrlKey());
                stmt.setLong(4, filterSet.getId());
                stmt.executeUpdate();
                stmt.close();
            }
            Session session = Database.instance().createSession(conn);
            try {
                for (FilterDefinition filterDefinition : filterSet.getFilters()) {
                    filterDefinition.beforeSave(session);
                    session.saveOrUpdate(filterDefinition);
                }
                session.flush();
            } finally {
                session.close();
            }

            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM filter_set_to_filter WHERE filter_set_id = ?");
            clearStmt.setLong(1, filterSet.getId());
            clearStmt.executeUpdate();
            clearStmt.close();

            PreparedStatement filterStmt = conn.prepareStatement("INSERT INTO filter_set_to_filter (filter_set_id, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filterDefinition : filterSet.getFilters()) {
                filterStmt.setLong(1, filterSet.getId());
                filterStmt.setLong(2, filterDefinition.getFilterID());
                filterStmt.execute();
            }
            filterStmt.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }
    }

    /*public void deleteFilterSet(long filterSetID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM FILTER_SET WHERE FILTER_SET_ID = ?");
            stmt.setLong(1, semanticEntity.getId());
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }
    }*/

    public FilterSet getFilterSet(long id, EIConnection conn) {
        FilterSet filterSet = null;

        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT FILTER_SET_NAME, FILTER_SET_DESCRIPTION, URL_KEY, DATA_SOURCE_ID FROM " +
                    "FILTER_SET, USER WHERE USER.ACCOUNT_ID = ? AND FILTER_SET_ID = ? AND FILTER_SET.USER_ID = USER.USER_ID");
            stmt.setLong(1, SecurityUtil.getAccountID());
            stmt.setLong(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                filterSet = new FilterSet();
                filterSet.setName(rs.getString(1));
                filterSet.setDescription(rs.getString(2));
                filterSet.setUrlKey(rs.getString(3));
                filterSet.setDataSourceID(rs.getLong(4));
                filterSet.setId(id);
                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                Session session = Database.instance().createSession(conn);
                try {
                    PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM filter_set_to_filter WHERE filter_set_id = ?");
                    filterStmt.setLong(1, id);
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
                filterSet.setFilters(filters);
            }
            stmt.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        }

        return filterSet;
    }

    public FilterSet getFilterSet(long id) {
        FilterSet filterSet = null;
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT FILTER_SET_NAME, FILTER_SET_DESCRIPTION, URL_KEY, DATA_SOURCE_ID FROM " +
                    "FILTER_SET WHERE FILTER_SET.USER_ID = ? AND FILTER_SET_ID = ? AND FILTER_SET.USER_ID = USER.USER_ID");
            stmt.setLong(1, SecurityUtil.getAccountID());
            stmt.setLong(2, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                filterSet = new FilterSet();
                filterSet.setName(rs.getString(1));
                filterSet.setDescription(rs.getString(2));
                filterSet.setUrlKey(rs.getString(3));
                filterSet.setDataSourceID(rs.getLong(4));
                filterSet.setId(id);
                List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
                Session session = Database.instance().createSession(conn);
                try {
                    PreparedStatement filterStmt = conn.prepareStatement("SELECT FILTER_ID FROM filter_set_to_filter WHERE filter_set_id = ?");
                    filterStmt.setLong(1, id);
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
                filterSet.setFilters(filters);
            }
            stmt.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }

        return filterSet;
    }

    public List<FilterSetDescriptor> getFilterSetsForDataSource(long dataSourceID) {
        List<FilterSetDescriptor> filterSets = new ArrayList<FilterSetDescriptor>();

        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT FILTER_SET_ID, FILTER_SET_NAME, FILTER_SET_DESCRIPTION, URL_KEY, DATA_SOURCE_ID FROM " +
                    "FILTER_SET WHERE FILTER_SET.USER_ID = ? AND FILTER_SET.DATA_SOURCE_ID = ?");
            stmt.setLong(1, SecurityUtil.getAccountID());
            stmt.setLong(2, dataSourceID);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                FilterSetDescriptor filterSetDescriptor = new FilterSetDescriptor();
                filterSetDescriptor.setId(rs.getLong(1));
                filterSetDescriptor.setName(rs.getString(2));
                filterSetDescriptor.setDescription(rs.getString(3));
                filterSetDescriptor.setUrlKey(rs.getString(4));
                filterSetDescriptor.setDataSourceID(rs.getLong(5));
                filterSets.add(filterSetDescriptor);
            }
            stmt.close();
        } catch (SQLException se) {
            throw new RuntimeException(se);
        } finally {
            Database.closeConnection(conn);
        }

        return filterSets;
    }
}
