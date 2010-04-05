package com.easyinsight.kpi;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.FilterDefinition;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.email.UserStub;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.groups.GroupDescriptor;
import com.easyinsight.reportpackage.ReportPackageDescriptor;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: Jan 21, 2010
 * Time: 11:35:41 AM
 */
public class KPIStorage {
    public void saveKPI(KPI kpi, EIConnection conn) throws Exception {
        if (kpi.getKpiID() == 0) {
            PreparedStatement insertKPIStmt = conn.prepareStatement("INSERT INTO KPI (ANALYSIS_MEASURE_ID, DATA_FEED_ID," +
                    "DESCRIPTION, ICON_IMAGE, KPI_NAME, HIGH_IS_GOOD, GOAL_VALUE, GOAL_DEFINED, TEMPORARY, DAY_WINDOW, THRESHOLD, DATE_DIMENSION_ID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            Session session = Database.instance().createSession(conn);
            kpi.getAnalysisMeasure().beforeSave();
            session.saveOrUpdate(kpi.getAnalysisMeasure());
            session.flush();
            session.close();
            insertKPIStmt.setLong(1, kpi.getAnalysisMeasure().getAnalysisItemID());
            insertKPIStmt.setLong(2, kpi.getCoreFeedID());
            insertKPIStmt.setString(3, kpi.getDescription());
            insertKPIStmt.setString(4, kpi.getIconImage());
            insertKPIStmt.setString(5, kpi.getName());
            insertKPIStmt.setInt(6, kpi.getHighIsGood());
            insertKPIStmt.setDouble(7, kpi.getGoalValue());
            insertKPIStmt.setBoolean(8, kpi.isGoalDefined());
            insertKPIStmt.setBoolean(9, kpi.isTemporary());
            insertKPIStmt.setInt(10, kpi.getDayWindow());
            insertKPIStmt.setDouble(11, kpi.getThreshold());
            if (kpi.getDateDimension() == null) {
                insertKPIStmt.setNull(12, Types.BIGINT);
            } else {
                insertKPIStmt.setLong(12, kpi.getDateDimension().getAnalysisItemID());
            }
            insertKPIStmt.execute();
            kpi.setKpiID(Database.instance().getAutoGenKey(insertKPIStmt));
            insertKPIStmt.close();
        } else {
            PreparedStatement updateKPIStmt = conn.prepareStatement("UPDATE KPI SET ANALYSIS_MEASURE_ID = ?, DATA_FEED_ID = ?," +
                    "DESCRIPTION = ?, ICON_IMAGE = ?, KPI_NAME = ?, HIGH_IS_GOOD = ?, GOAL_VALUE = ?, GOAL_DEFINED = ?, TEMPORARY = ?," +
                    "DAY_WINDOW = ?, THRESHOLD = ?, DATE_DIMENSION_ID = ? WHERE KPI_ID = ?");
            kpi.getAnalysisMeasure().beforeSave();
            Session session = Database.instance().createSession(conn);
            session.saveOrUpdate(kpi.getAnalysisMeasure());
            session.flush();
            session.close();
            updateKPIStmt.setLong(1, kpi.getAnalysisMeasure().getAnalysisItemID());
            updateKPIStmt.setLong(2, kpi.getCoreFeedID());
            updateKPIStmt.setString(3, kpi.getDescription());
            updateKPIStmt.setString(4, kpi.getIconImage());
            updateKPIStmt.setString(5, kpi.getName());
            updateKPIStmt.setInt(6, kpi.getHighIsGood());
            updateKPIStmt.setDouble(7, kpi.getGoalValue());
            updateKPIStmt.setBoolean(8, kpi.isGoalDefined());
            updateKPIStmt.setBoolean(9, kpi.isTemporary());
            updateKPIStmt.setInt(10, kpi.getDayWindow());
            updateKPIStmt.setDouble(11, kpi.getThreshold());
            if (kpi.getDateDimension() == null) {
                updateKPIStmt.setNull(12, Types.BIGINT);
            } else {
                updateKPIStmt.setLong(12, kpi.getDateDimension().getAnalysisItemID());
            }
            updateKPIStmt.setLong(13, kpi.getKpiID());
            updateKPIStmt.executeUpdate();
            updateKPIStmt.close();
        }
        saveFilters(kpi, conn);
        saveProblemFilters(kpi, conn);
        saveUsers(kpi, conn);
    }

    

    public List<KPI> getKPIsForUser(long userID) throws Exception {
        List<KPI> kpis = new ArrayList<KPI>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getKPIStmt = conn.prepareStatement("SELECT KPI.KPI_ID, ANALYSIS_MEASURE_ID, KPI.DATA_FEED_ID, KPI.DESCRIPTION," +
                "ICON_IMAGE, KPI_NAME, DATA_FEED.feed_name, KPI.goal_defined, KPI.goal_value, KPI.high_is_good, KPI.temporary, KPI.DAY_WINDOW, KPI.THRESHOLD," +
                    "KPI.DATE_DIMENSION_ID FROM KPI, DATA_FEED, UPLOAD_POLICY_USERS WHERE " +
                "data_feed.data_feed_id = kpi.data_feed_id AND UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND " +
                    "KPI.TEMPORARY = ?");
            getKPIStmt.setLong(1, userID);
            getKPIStmt.setBoolean(2, false);
            ResultSet kpiRS = getKPIStmt.executeQuery();
            while (kpiRS.next()) {
                kpis.add(getKPIFromResultSet(conn, kpiRS));
            }
            getKPIStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
        return kpis;
    }

    public KPI getKPI(long kpiID, EIConnection conn) throws Exception {
        PreparedStatement getKPIStmt = conn.prepareStatement("SELECT KPI.KPI_ID, ANALYSIS_MEASURE_ID, KPI.DATA_FEED_ID, KPI.DESCRIPTION," +
                "ICON_IMAGE, KPI_NAME, DATA_FEED.feed_name, KPI.goal_defined, KPI.goal_value, KPI.high_is_good, KPI.temporary, KPI.DAY_WINDOW, KPI.THRESHOLD," +
                "KPI.DATE_DIMENSION_ID FROM KPI, DATA_FEED WHERE kpi.kpi_id = ? AND data_feed.data_feed_id = kpi.data_feed_id");
        getKPIStmt.setLong(1, kpiID);
        KPI kpi = null;
        ResultSet kpiRS = getKPIStmt.executeQuery();
        if (kpiRS.next()) {
            kpi = getKPIFromResultSet(conn, kpiRS);
        }
        getKPIStmt.close();
        return kpi;
    }

    private KPI getKPIFromResultSet(EIConnection conn, ResultSet kpiRS) throws Exception {
        KPI kpi;
        long kpiID = kpiRS.getLong(1);
        long measureID = kpiRS.getLong(2);
        long dataFeedID = kpiRS.getLong(3);
        String description = kpiRS.getString(4);
        String iconImage = kpiRS.getString(5);
        String kpiName = kpiRS.getString(6);
        String dataSourceName = kpiRS.getString(7);
        boolean goalDefined = kpiRS.getBoolean(8);
        double goalValue = kpiRS.getDouble(9);
        int highIsGood = kpiRS.getInt(10);
        boolean temporary = kpiRS.getBoolean(11);
        int dayWindow = kpiRS.getInt(12);
        double threshold = kpiRS.getDouble(13);
        long dateDimensionID = kpiRS.getLong(14);
        boolean dateDefined = !kpiRS.wasNull();
        Session session = Database.instance().createSession(conn);
        List measures = session.createQuery("from AnalysisMeasure where analysisItemID = ?").setLong(0, measureID).list();
        AnalysisMeasure measure = (AnalysisMeasure) measures.get(0);
        AnalysisDateDimension date = null;
        if (dateDefined) {
            date = (AnalysisDateDimension) session.createQuery("from AnalysisDateDimension where analysisItemID = ?").setLong(0, dateDimensionID).list().get(0);
        }
        measure.afterLoad();
        session.flush();
        session.close();
        kpi = new KPI();
        kpi.setKpiID(kpiID);
        kpi.setAnalysisMeasure(measure);
        kpi.setCoreFeedID(dataFeedID);
        kpi.setDescription(description);
        kpi.setIconImage(iconImage);
        kpi.setName(kpiName);
        kpi.setCoreFeedName(dataSourceName);
        kpi.setGoalDefined(goalDefined);
        kpi.setHighIsGood(highIsGood);
        kpi.setGoalValue(goalValue);
        kpi.setKpiOutcome(getLatestGoalValue(kpi, conn));
        kpi.setFilters(getFilters(kpiID, conn));
        kpi.setProblemConditions(getProblemFilters(kpiID, conn));
        kpi.setTemporary(temporary);
        kpi.setReports(getReports(dataFeedID, conn));
        kpi.setPackages(getPackages(dataFeedID, conn));
        kpi.setKpiTrees(getKPITrees(dataFeedID, conn));
        kpi.setDayWindow(dayWindow);
        kpi.setThreshold(threshold);
        kpi.setDateDimension(date);
        kpi.setKpiUsers(getKPIUsers(kpiID, conn));
        PreparedStatement queryStmt = conn.prepareStatement("SELECT SOLUTION_ID FROM SOLUTION_INSTALL WHERE INSTALLED_DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, dataFeedID);
        ResultSet solutionRS = queryStmt.executeQuery();
        if (solutionRS.next()) {
            kpi.setConnectionID(solutionRS.getLong(1));
        }
        queryStmt.close();
        return kpi;
    }

    private List<InsightDescriptor> getReports(long dataFeedID, EIConnection conn) throws SQLException {
        List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE, REPORT_TYPE FROM ANALYSIS WHERE DATA_FEED_ID = ? AND " +
                "ANALYSIS.temporary_report = ?");
        queryStmt.setLong(1, dataFeedID);
        queryStmt.setBoolean(2, false);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long reportID = rs.getLong(1);
            String reportName = rs.getString(2);
            int reportType = rs.getInt(3);
            reports.add(new InsightDescriptor(reportID, reportName, dataFeedID, reportType));
        }
        queryStmt.close();
        return reports;
    }

    private List<ReportPackageDescriptor> getPackages(long dataFeedID, EIConnection conn) throws SQLException {
        List<ReportPackageDescriptor> packages = new ArrayList<ReportPackageDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT REPORT_PACKAGE.report_package_id, report_package.package_name FROM REPORT_PACKAGE, REPORT_PACKAGE_TO_REPORT, ANALYSIS WHERE " +
                "REPORT_PACKAGE.report_package_id = report_package_to_report.report_id AND report_package_to_report.report_id = analysis.analysis_id AND " +
                "ANALYSIS.data_feed_id = ? AND ANALYSIS.temporary_report = ?");
        queryStmt.setLong(1, dataFeedID);
        queryStmt.setBoolean(2, false);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long packageID = rs.getLong(1);
            String packageName = rs.getString(2);
            packages.add(new ReportPackageDescriptor(packageName, packageID));
        }
        queryStmt.close();
        return packages;
    }

    private List<GoalTreeDescriptor> getKPITrees(long dataSourceID, EIConnection conn) throws SQLException {
        List<GoalTreeDescriptor> reports = new ArrayList<GoalTreeDescriptor>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT GOAL_TREE.goal_tree_id, GOAL_TREE.name FROM GOAL_TREE, GOAL_TREE_NODE, KPI WHERE GOAL_TREE.goal_tree_id = GOAL_TREE_NODE.goal_tree_id AND " +
                "GOAL_TREE_NODE.kpi_id = KPI.KPI_ID AND KPI.data_feed_id = ?");
        queryStmt.setLong(1, dataSourceID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long kpiTreeID = rs.getLong(1);
            String kpiTreeName = rs.getString(2);
            reports.add(new GoalTreeDescriptor(kpiTreeID, kpiTreeName, 0, null));
        }
        queryStmt.close();
        return reports;
    }

    public void saveKPIOutcome(long kpiID, Double newValue, Double oldValue, Date evaluationDate, int outcomeValue,
                                   int direction, boolean problemEvaluated, Double percentChange, boolean directional, Connection conn) throws SQLException {
        if (newValue != null && (Double.isNaN(newValue) || Double.isInfinite(newValue))) {
            newValue = null;
        }
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO KPI_VALUE (kpi_id, start_value, end_value," +
                    "evaluation_date, outcome_value, direction, problem_evaluated, percent_change, directional) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setLong(1, kpiID);
        if (oldValue == null) {
            insertStmt.setNull(2, Types.DOUBLE);
        } else {
            insertStmt.setDouble(2, oldValue);
        }
        if (newValue == null) {
            insertStmt.setNull(3, Types.DOUBLE);
        } else {
            insertStmt.setDouble(3, newValue);
        }
        insertStmt.setTimestamp(4, new java.sql.Timestamp(evaluationDate.getTime()));
        insertStmt.setInt(5, outcomeValue);
        insertStmt.setInt(6, direction);
        insertStmt.setBoolean(7, problemEvaluated);
        if (percentChange == null) {
            insertStmt.setNull(8, Types.DOUBLE);
        } else {
            if (Double.isInfinite(percentChange) || Double.isNaN(percentChange)) {
                percentChange = 0.;
            }
            insertStmt.setDouble(8, percentChange);
        }
        insertStmt.setBoolean(9, directional);
        insertStmt.execute();
        insertStmt.close();
    }

    public Double findLastGoalValue(Connection conn, long kpiID) throws SQLException {
        Double oldValue = null;

            PreparedStatement findMaxStmt = conn.prepareStatement("SELECT MAX(EVALUATION_DATE) FROM KPI_VALUE WHERE KPI_ID = ? AND EVALUATION_DATE < ?");
            findMaxStmt.setLong(1, kpiID);
            findMaxStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
            ResultSet dateRS = findMaxStmt.executeQuery();
            if (dateRS.next()) {
                Timestamp timestampDate = dateRS.getTimestamp(1);
                if (!dateRS.wasNull()) {
                    PreparedStatement lastValStmt = conn.prepareStatement("SELECT END_VALUE, MAX(EVALUATION_DATE) FROM KPI_VALUE WHERE KPI_ID = ? AND " +
                        "EVALUATION_DATE < ?");
                    lastValStmt.setLong(1, kpiID);
                    lastValStmt.setTimestamp(2, timestampDate);
                    ResultSet rs = lastValStmt.executeQuery();
                    if (rs.next()) {
                        if (!rs.wasNull()) {
                            oldValue = rs.getDouble(1);
                        }
                    }
                    lastValStmt.close();
                }
            }
            findMaxStmt.close();

        return oldValue;
    }

    public KPIOutcome getLatestGoalValue(KPI kpi, EIConnection conn) throws SQLException {
        KPIOutcome kpiOutcome = null;
        PreparedStatement findLastDateStmt = conn.prepareStatement("SELECT MAX(EVALUATION_DATE) FROM KPI_VALUE WHERE KPI_ID = ?");
        findLastDateStmt.setLong(1, kpi.getKpiID());
        ResultSet dateRS = findLastDateStmt.executeQuery();
        if (dateRS.next()) {
            Timestamp maxDate = dateRS.getTimestamp(1);
            if (!dateRS.wasNull()) {
                PreparedStatement queryStmt = conn.prepareStatement("SELECT DIRECTION, END_VALUE, EVALUATION_DATE," +
                    "OUTCOME_VALUE, PROBLEM_EVALUATED, START_VALUE, PERCENT_CHANGE, DIRECTIONAL FROM KPI_VALUE WHERE KPI_ID = ? AND EVALUATION_DATE = ?");
                queryStmt.setLong(1, kpi.getKpiID());
                queryStmt.setTimestamp(2, maxDate);
                ResultSet rs = queryStmt.executeQuery();
                if (rs.next()) {
                    int direction = rs.getInt(1);
                    Double endValue = rs.getDouble(6);
                    if (rs.wasNull()) {
                        endValue = null;
                    }
                    Timestamp timestamp = rs.getTimestamp(3);
                    Date evaluationDate = null;
                    if (!rs.wasNull()) {
                        evaluationDate = new Date(timestamp.getTime());
                    }
                    int outcome = rs.getInt(4);
                    Double value = rs.getDouble(2);
                    if (rs.wasNull()) {
                        value = null;
                    }
                    boolean problem = rs.getBoolean(5);
                    Double percentChange = rs.getDouble(7);
                    if (rs.wasNull()) {
                        percentChange = null;
                    }
                    boolean directional = rs.getBoolean(8);
                    kpiOutcome = new KPIOutcome(outcome, direction, endValue, problem, value, evaluationDate, kpi.getKpiID(), percentChange, directional);
                }
                queryStmt.close();
            }
        }
        findLastDateStmt.close();
        return kpiOutcome;
    }

    public KPIOutcome getLatestGoalValue(KPI kpi) throws SQLException {        
        EIConnection conn = Database.instance().getConnection();
        try {
            return getLatestGoalValue(kpi, conn);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void saveKPIValue(KPI kpi, KPIValue kpiValue, EIConnection conn) throws Exception {
        PreparedStatement insertKPIValueStmt = conn.prepareStatement("INSERT INTO KPI_VALUE (end_value, evaluation_date, kpi_id) VALUES (?, ?, ?)");
        insertKPIValueStmt.setDouble(1, kpiValue.getValue());
        insertKPIValueStmt.setTimestamp(2, new java.sql.Timestamp(kpiValue.getDate().getTime()));
        insertKPIValueStmt.setLong(3, kpi.getKpiID());
        insertKPIValueStmt.execute();
        insertKPIValueStmt.close();
    }

    public KPIValue getLastKPIValue(long kpiID, EIConnection conn) throws Exception {
        PreparedStatement findMaxStmt = conn.prepareStatement("SELECT MAX(EVALUATION_DATE) FROM KPI_VALUE WHERE KPI_ID = ?");
        findMaxStmt.setLong(1, kpiID);
        //findMaxStmt.setDate(2, new java.sql.Date(System.currentTimeMillis()));
        ResultSet dateRS = findMaxStmt.executeQuery();
        if (dateRS.next()) {
            Timestamp timestampDate = dateRS.getTimestamp(1);
            if (!dateRS.wasNull()) {
                PreparedStatement lastValStmt = conn.prepareStatement("SELECT END_VALUE FROM KPI_VALUE WHERE KPI_ID = ? AND " +
                    "EVALUATION_DATE = ?");
                lastValStmt.setLong(1, kpiID);
                lastValStmt.setTimestamp(2, timestampDate);
                ResultSet rs = lastValStmt.executeQuery();
                if (rs.next()) {
                    if (!rs.wasNull()) {
                        double value = rs.getDouble(1);
                        java.util.Date date = new java.util.Date(timestampDate.getTime());
                        KPIValue kpiValue = new KPIValue();
                        kpiValue.setValue(value);
                        kpiValue.setDate(date);
                        return kpiValue;
                    }
                }
                lastValStmt.close();
            }
        }
        findMaxStmt.close();
        return null;
    }

    private void saveUsers(KPI kpi, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM KPI_ROLE WHERE KPI_ID = ?");
        clearExistingStmt.setLong(1, kpi.getKpiID());
        clearExistingStmt.executeUpdate();
        clearExistingStmt.close();

        PreparedStatement saveUsersStmt = conn.prepareStatement("INSERT INTO KPI_ROLE (KPI_ID, USER_ID, GROUP_ID, OWNER, RESPONSIBLE) VALUES (?, ?, ?, ?, ?)");
        for (KPIUser kpiUser : kpi.getKpiUsers()) {
            saveUsersStmt.setLong(1, kpi.getKpiID());
            if (kpiUser.getFeedConsumer().type() == FeedConsumer.USER) {
                UserStub userStub = (UserStub) kpiUser.getFeedConsumer();
                saveUsersStmt.setLong(2, userStub.getUserID());
            } else {
                saveUsersStmt.setNull(2, Types.BIGINT);
            }
            if (kpiUser.getFeedConsumer().type() == FeedConsumer.GROUP) {
                GroupDescriptor groupStub = (GroupDescriptor) kpiUser.getFeedConsumer();
                saveUsersStmt.setLong(3, groupStub.getGroupID());
            } else {
                saveUsersStmt.setNull(3, Types.BIGINT);
            }
            saveUsersStmt.setBoolean(4, kpiUser.isOwner());
            saveUsersStmt.setBoolean(5, kpiUser.isResponsible());
            saveUsersStmt.execute();
        }
        saveUsersStmt.close();
    }

    private void saveFilters(KPI kpi, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM KPI_TO_FILTER WHERE KPI_ID = ?");
        clearExistingStmt.setLong(1, kpi.getKpiID());
        clearExistingStmt.executeUpdate();
        clearExistingStmt.close();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement saveFiltersStmt = conn.prepareStatement("INSERT INTO KPI_TO_FILTER (KPI_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filterDefinition : kpi.getFilters()) {
                filterDefinition.getField().reportSave(session);
                filterDefinition.beforeSave();
                session.saveOrUpdate(filterDefinition);
                session.flush();
                saveFiltersStmt.setLong(1, kpi.getKpiID());
                saveFiltersStmt.setLong(2, filterDefinition.getFilterID());
                saveFiltersStmt.execute();
            }
            saveFiltersStmt.close();
        } finally {
            session.close();
        }
    }

    private List<FilterDefinition> getFilters(long kpiID, Connection conn) throws SQLException {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT FILTER_ID FROM KPI_TO_FILTER WHERE kpi_id = ?");
        feedQueryStmt.setLong(1, kpiID);
        ResultSet rs = feedQueryStmt.executeQuery();
        Session session = Database.instance().createSession(conn);
        try {
            while (rs.next()) {
                List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, rs.getLong(1)).list();
                if (results.size() > 0) {
                    FilterDefinition filter = (FilterDefinition) results.get(0);
                    filter.getField().afterLoad();
                    filter.afterLoad();
                    filters.add(filter);
                }
            }
        } finally {
            session.close();
        }
        feedQueryStmt.close();
        return filters;
    }

    private List<KPIUser> getKPIUsers(long kpiID, EIConnection conn) throws SQLException {
        PreparedStatement queryStmt = conn.prepareStatement("SELECT KPI_ROLE.USER_ID, GROUP_ID, USER.USERNAME, USER.EMAIL, USER.NAME, COMMUNITY_GROUP.name, OWNER, RESPONSIBLE, USER.ACCOUNT_ID FROM " +
                "KPI_ROLE LEFT JOIN USER ON KPI_ROLE.USER_ID = USER.USER_ID LEFT JOIN COMMUNITY_GROUP ON KPI_ROLE.GROUP_ID = COMMUNITY_GROUP.COMMUNITY_GROUP_ID " +
                "WHERE KPI_ID = ? ");
        queryStmt.setLong(1, kpiID);
        ResultSet rs = queryStmt.executeQuery();
        List<KPIUser> users = new ArrayList<KPIUser>();
        while (rs.next()) {
            long userID = rs.getLong(1);
            FeedConsumer feedConsumer;
            if (!rs.wasNull()) {
                feedConsumer = new UserStub(userID, rs.getString(3), rs.getString(4), rs.getString(5), rs.getLong(9));
            } else {
                feedConsumer = new GroupDescriptor(rs.getString(6), rs.getLong(3), 0, "");
            }
            KPIUser kpiUser = new KPIUser();
            kpiUser.setFeedConsumer(feedConsumer);
            kpiUser.setOwner(rs.getBoolean(7));
            kpiUser.setResponsible(rs.getBoolean(8));
            users.add(kpiUser);
        }
        queryStmt.close();
        return users;
    }

    private List<FilterDefinition> getProblemFilters(long kpiID, Connection conn) throws SQLException {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        PreparedStatement feedQueryStmt = conn.prepareStatement("SELECT FILTER_ID FROM KPI_TO_PROBLEM_FILTER WHERE kpi_id = ?");
        feedQueryStmt.setLong(1, kpiID);
        ResultSet rs = feedQueryStmt.executeQuery();
        Session session = Database.instance().createSession(conn);
        try {
            while (rs.next()) {
                List results = session.createQuery("from FilterDefinition where filterID = ?").setLong(0, rs.getLong(1)).list();
                if (results.size() > 0) {
                    FilterDefinition filter = (FilterDefinition) results.get(0);
                    filter.getField().afterLoad();
                    filter.afterLoad();
                    filters.add(filter);
                }
            }
        } finally {
            session.close();
        }
        feedQueryStmt.close();
        return filters;
    }

    private void saveProblemFilters(KPI kpi, Connection conn) throws SQLException {
        PreparedStatement clearExistingStmt = conn.prepareStatement("DELETE FROM KPI_TO_PROBLEM_FILTER WHERE KPI_ID = ?");
        clearExistingStmt.setLong(1, kpi.getKpiID());
        clearExistingStmt.executeUpdate();
        clearExistingStmt.close();
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement saveFiltersStmt = conn.prepareStatement("INSERT INTO KPI_TO_PROBLEM_FILTER (KPI_ID, FILTER_ID) VALUES (?, ?)");
            for (FilterDefinition filterDefinition : kpi.getProblemConditions()) {
                filterDefinition.beforeSave();
                session.saveOrUpdate(filterDefinition);
                session.flush();
                saveFiltersStmt.setLong(1, kpi.getKpiID());
                saveFiltersStmt.setLong(2, filterDefinition.getFilterID());
                saveFiltersStmt.execute();
            }
            saveFiltersStmt.close();
        } finally {
            session.close();
        }
    }

    public void deleteKPI(long kpiID) throws SQLException {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement deleteKPIStmt = conn.prepareStatement("DELETE FROM KPI WHERE KPI_ID = ?");
            deleteKPIStmt.setLong(1, kpiID);
            deleteKPIStmt.executeUpdate();
            deleteKPIStmt.close();
        } finally {
            Database.closeConnection(conn);
        }
    }
}
