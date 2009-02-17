package com.easyinsight.solutions;

import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedDescriptor;
import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.userupload.UserUploadService;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.TableDefinitionMetadata;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.api.APIService;
import com.easyinsight.api.dynamic.DynamicServiceDefinition;
import com.easyinsight.api.dynamic.ConfiguredMethod;
import com.easyinsight.AnalysisItem;
import com.easyinsight.goals.GoalTreeDescriptor;
import com.easyinsight.goals.GoalService;
import com.easyinsight.goals.GoalTree;

import java.util.*;
import java.sql.*;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:32:59 AM
 */
public class SolutionService {

    private FeedStorage feedStorage = new FeedStorage();
    private AnalysisStorage analysisStorage = new AnalysisStorage();
    private UserUploadService userUploadService = new UserUploadService();

    public void addSolutionArchive(byte[] archive, long solutionID, String solutionArchiveName) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateArchiveStmt = conn.prepareStatement("UPDATE SOLUTION SET ARCHIVE = ?, SOLUTION_ARCHIVE_NAME = ? WHERE SOLUTION_ID = ?");
            updateArchiveStmt.setBytes(1, archive);
            updateArchiveStmt.setString(2, solutionArchiveName);
            updateArchiveStmt.setLong(3, solutionID);
            updateArchiveStmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public void addSolution(Solution solution, List<Integer> feedIDs) {
        long userID = SecurityUtil.getUserID();
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement insertSolutionStmt = conn.prepareStatement("INSERT INTO SOLUTION (NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, goal_tree_id, SOLUTION_TIER) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertSolutionStmt.setString(1, solution.getName());
            insertSolutionStmt.setString(2, solution.getDescription());
            insertSolutionStmt.setString(3, solution.getIndustry());
            insertSolutionStmt.setString(4, solution.getAuthor());
            insertSolutionStmt.setBoolean(5, solution.isCopyData());
            if (solution.getGoalTreeID() == 0) {
                insertSolutionStmt.setNull(6, Types.BIGINT);
            } else {
                insertSolutionStmt.setLong(6, solution.getGoalTreeID());
            }
            insertSolutionStmt.setInt(7, solution.getSolutionTier());
            insertSolutionStmt.execute();
            long solutionID = Database.instance().getAutoGenKey(insertSolutionStmt);
            PreparedStatement addRoleStmt = conn.prepareStatement("INSERT INTO USER_TO_SOLUTION (USER_ID, SOLUTION_ID, USER_ROLE) VALUES (?, ?, ?)");
            addRoleStmt.setLong(1, userID);
            addRoleStmt.setLong(2, solutionID);
            addRoleStmt.setInt(3, SolutionRoles.OWNER);
            addRoleStmt.execute();
            PreparedStatement addFeedStmt = conn.prepareStatement("INSERT INTO SOLUTION_TO_FEED (FEED_ID, SOLUTION_ID) VALUES (?, ?)");
            for (Integer feedID : feedIDs) {
                addFeedStmt.setLong(1, feedID);
                addFeedStmt.setLong(2, solutionID);
                addFeedStmt.execute();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public void updateSolution(Solution solution, List<Integer> feedIDs) {
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateSolutionStmt = conn.prepareStatement("UPDATE SOLUTION SET NAME = ?, DESCRIPTION = ?, INDUSTRY = ?, AUTHOR = ?, COPY_DATA = ?, GOAL_TREE_ID = ?, SOLUTION_TIER = ? WHERE SOLUTION_ID = ?",
                    Statement.RETURN_GENERATED_KEYS);
            updateSolutionStmt.setString(1, solution.getName());
            updateSolutionStmt.setString(2, solution.getDescription());
            updateSolutionStmt.setString(3, solution.getIndustry());
            updateSolutionStmt.setString(4, solution.getAuthor());
            updateSolutionStmt.setBoolean(5, solution.isCopyData());
            if (solution.getGoalTreeID() == 0) {
                updateSolutionStmt.setNull(6, Types.BIGINT);
            } else {
                updateSolutionStmt.setLong(6, solution.getGoalTreeID());
            }
            updateSolutionStmt.setInt(7, solution.getSolutionTier());
            updateSolutionStmt.setLong(8, solution.getSolutionID());
            updateSolutionStmt.executeUpdate();
            PreparedStatement deleteFeedsStmt = conn.prepareStatement("DELETE FROM SOLUTION_TO_FEED WHERE SOLUTION_ID = ?");
            deleteFeedsStmt.setLong(1, solution.getSolutionID());
            deleteFeedsStmt.executeUpdate();
            PreparedStatement addFeedStmt = conn.prepareStatement("INSERT INTO SOLUTION_TO_FEED (FEED_ID, SOLUTION_ID) VALUES (?, ?)");
            for (Integer feedID : feedIDs) {
                addFeedStmt.setLong(1, feedID);
                addFeedStmt.setLong(2, solution.getSolutionID());
                addFeedStmt.execute();
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public List<Object> installSolution(long solutionID) {
        // establish the connection from the account/user to the solution
        // retrieve the feeds for this solution
        // retrieve the insights matching that feed
        // clone the feed/insights
        long userID = SecurityUtil.getUserID();
        Solution solution = getSolution(solutionID);
        Connection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<Object> objects = installSolution(userID, solution, conn);
            conn.commit();
            return objects;
        } catch (Exception e) {
            LogClass.error(e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.instance().closeConnection(conn);
        }
    }

    public List<Object> installSolution(long userID, Solution solution, Connection conn) throws SQLException, CloneNotSupportedException {
        List<Object> objects = new ArrayList<Object>(generateFeedsForSolution(solution.getSolutionID(), userID, conn, solution.isCopyData()));
        PreparedStatement addRoleStmt = conn.prepareStatement("INSERT INTO USER_TO_SOLUTION (USER_ID, SOLUTION_ID, USER_ROLE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        addRoleStmt.setLong(1, userID);
        addRoleStmt.setLong(2, solution.getSolutionID());
        addRoleStmt.setInt(3, SolutionRoles.INSTALLER);
        addRoleStmt.execute();
        long installLinkID = Database.instance().getAutoGenKey(addRoleStmt);
        for (Object obj : objects) {
            if (obj instanceof FeedDescriptor) {
                FeedDescriptor feedDescriptor = (FeedDescriptor) obj;

            }
        }
        return objects;
    }

    public List<FeedDescriptor> getAvailableFeeds() {
        List<FeedDescriptor> feedDescriptors = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME FROM DATA_FEED");
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long feedID = rs.getLong(1);
                String name = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setDataFeedID(feedID);
                feedDescriptor.setName(name);
                feedDescriptors.add(feedDescriptor);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return feedDescriptors;
    }

    public SolutionContents getSolutionContents(long solutionID) {
        List<FeedDescriptor> feedDescriptors = new ArrayList<FeedDescriptor>();
        List<InsightDescriptor> insightDescriptors = new ArrayList<InsightDescriptor>();
        List<GoalTreeDescriptor> goalTreeDescriptors = new ArrayList<GoalTreeDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME FROM SOLUTION_TO_FEED, DATA_FEED " +
                    "WHERE SOLUTION_ID = ? AND DATA_FEED.DATA_FEED_ID = SOLUTION_TO_FEED.FEED_ID");
            queryStmt.setLong(1, solutionID);
            PreparedStatement queryInsightsStmt = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE, DATA_FEED_ID FROM ANALYSIS WHERE DATA_FEED_ID = ?");
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long feedID = rs.getLong(1);
                String name = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setDataFeedID(feedID);
                feedDescriptor.setName(name);
                feedDescriptors.add(feedDescriptor);
                queryInsightsStmt.setLong(1, feedID);
                ResultSet insightRS = queryInsightsStmt.executeQuery();
                while (insightRS.next()) {
                    long insightID = insightRS.getLong(1);
                    String insightName = insightRS.getString(2);
                    insightDescriptors.add(new InsightDescriptor(insightID, insightName, rs.getLong(3)));
                }
            }
            PreparedStatement getGoalsStmt = conn.prepareStatement("SELECT goal_tree.goal_tree_id, name FROM solution_to_goal_tree, goal_tree " +
                    "WHERE SOLUTION_ID = ? AND goal_tree.goal_tree_id = solution_to_goal_tree.goal_tree_id");
            getGoalsStmt.setLong(1, solutionID);
            ResultSet goalRS = getGoalsStmt.executeQuery();
            while (goalRS.next()) {
                long goalID = goalRS.getLong(1);
                String goalName = goalRS.getString(2);
                GoalTreeDescriptor goalTreeDescriptor = new GoalTreeDescriptor(goalID, goalName, 0);
                goalTreeDescriptors.add(goalTreeDescriptor);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        SolutionContents solutionContents = new SolutionContents();
        solutionContents.setFeedDescriptors(feedDescriptors);
        solutionContents.setGoalTreeDescriptors(goalTreeDescriptors);
        solutionContents.setInsightDescriptors(insightDescriptors);
        return solutionContents;
    }

    public List<FeedDescriptor> getDescriptorsForSolution(long solutionID) {
        List<FeedDescriptor> feedDescriptors = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME FROM SOLUTION_TO_FEED, DATA_FEED " +
                    "WHERE SOLUTION_ID = ? AND DATA_FEED.DATA_FEED_ID = SOLUTION_TO_FEED.FEED_ID");
            queryStmt.setLong(1, solutionID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long feedID = rs.getLong(1);
                String name = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setDataFeedID(feedID);
                feedDescriptor.setName(name);
                feedDescriptors.add(feedDescriptor);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return feedDescriptors;
    }

    private List<SolutionInstallInfo> generateFeedsForSolution(long solutionID, long userID, Connection conn, boolean copyData) throws SQLException, CloneNotSupportedException {
        List<SolutionInstallInfo> descriptors = new ArrayList<SolutionInstallInfo>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT FEED_ID FROM SOLUTION_TO_FEED WHERE SOLUTION_ID = ?");
        queryStmt.setLong(1, solutionID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long feedID = rs.getLong(1);
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
            FeedDescriptor feedDescriptor = installFeed(userID, conn, copyData, feedID, feedDefinition);
            descriptors.add(new SolutionInstallInfo(feedDefinition.getDataFeedID(), feedDescriptor.getDataFeedID()));
        }
        return descriptors;
    }

    private FeedDescriptor installFeed(long userID, Connection conn, boolean copyData, long feedID, FeedDefinition feedDefinition) throws CloneNotSupportedException, SQLException {
        FeedDefinition clonedFeedDefinition = cloneFeed(userID, conn, feedDefinition);
        feedStorage.updateDataFeedConfiguration(clonedFeedDefinition, conn);
        buildClonedDataStores(copyData, feedDefinition, clonedFeedDefinition, conn);
        FeedDescriptor feedDescriptor = new FeedDescriptor();
        feedDescriptor.setDataFeedID(clonedFeedDefinition.getDataFeedID());
        feedDescriptor.setName(clonedFeedDefinition.getFeedName());
        feedDescriptor.setFeedType(clonedFeedDefinition.getFeedType().getType());
        userUploadService.createUserFeedLink(userID, clonedFeedDefinition.getDataFeedID(), Roles.OWNER, conn);
        List<InsightDescriptor> clonedDefs = new ArrayList<InsightDescriptor>();
        List<AnalysisDefinition> insights = getInsightsFromFeed(feedID, conn);
        for (AnalysisDefinition insight : insights) {
            if (insight.isRootDefinition()) {
                continue;
            }
            AnalysisDefinition clonedInsight = insight.clone();
            clonedInsight.setAnalysisPolicy(AnalysisPolicy.PRIVATE);
            clonedInsight.setDataFeedID(clonedFeedDefinition.getDataFeedID());
            clonedInsight.setUserBindings(Arrays.asList(new UserToAnalysisBinding(userID, UserPermission.OWNER)));
            analysisStorage.saveAnalysis(clonedInsight, conn);
            clonedDefs.add(new InsightDescriptor(clonedInsight.getAnalysisID(), clonedInsight.getTitle(), clonedInsight.getDataFeedID()));
            List<FeedDefinition> insightFeeds = getFeedsFromInsight(clonedInsight.getAnalysisID(), conn);
            for (FeedDefinition insightFeed : insightFeeds) {
                installFeed(userID, conn, copyData, insightFeed.getDataFeedID(), insightFeed);
            }
        }
        feedDescriptor.setChildren(clonedDefs);
        System.out.println("*** created cloned data source " + clonedFeedDefinition.getDataFeedID());
        return feedDescriptor;
    }

    private void buildClonedDataStores(boolean copyData, FeedDefinition feedDefinition, FeedDefinition clonedFeedDefinition, Connection conn) throws SQLException {
        if (copyData) {
            TableDefinitionMetadata sourceTable = TableDefinitionMetadata.readConnection(feedDefinition, conn);
            DataSet dataSet;
            try {
                dataSet = sourceTable.retrieveData(null, null);
            } finally {
                sourceTable.closeConnection();
            }
            TableDefinitionMetadata clonedTable = TableDefinitionMetadata.readConnection(clonedFeedDefinition, conn);
            try {
                clonedTable.createTable();
                clonedTable.insertData(dataSet);
                clonedTable.commit();
            } catch (SQLException e) {
                LogClass.error(e);
                clonedTable.rollback();
                throw new RuntimeException(e);
            } finally {
                clonedTable.closeConnection();
            }
        } else {
            TableDefinitionMetadata clonedTable = TableDefinitionMetadata.readConnection(clonedFeedDefinition, conn);
            try {
                clonedTable.createTable();
                clonedTable.commit();
            } catch (SQLException e) {
                LogClass.error(e);
                clonedTable.rollback();
                throw new RuntimeException(e);
            } finally {
                clonedTable.closeConnection();
            }
        }
    }

    private FeedDefinition cloneFeed(long userID, Connection conn, FeedDefinition feedDefinition) throws CloneNotSupportedException, SQLException {
        FeedDefinition clonedFeedDefinition = feedDefinition.clone();
        clonedFeedDefinition.setUploadPolicy(new UploadPolicy(userID));
        feedStorage.addFeedDefinitionData(clonedFeedDefinition, conn);
        AnalysisDefinition rootInsight = analysisStorage.getAnalysisDefinition(feedDefinition.getAnalysisDefinitionID(), conn);
        AnalysisDefinition clonedRootInsight = rootInsight.clone();
        clonedRootInsight.setUserBindings(Arrays.asList(new UserToAnalysisBinding(userID, UserPermission.OWNER)));
        analysisStorage.saveAnalysis(clonedRootInsight, conn);
        clonedFeedDefinition.setAnalysisDefinitionID(clonedRootInsight.getAnalysisID());
        if (clonedFeedDefinition.getDynamicServiceDefinitionID() > 0) {
            cloneAPIs(conn, feedDefinition, clonedFeedDefinition);
        }
        return clonedFeedDefinition;
    }

    private void cloneAPIs(Connection conn, FeedDefinition feedDefinition, FeedDefinition clonedFeedDefinition) {
        Session session = Database.instance().createSession(conn);
        try {
            APIService apiService = new APIService();
            DynamicServiceDefinition dynamicServiceDefinition = apiService.getDynamicServiceDefinition(feedDefinition.getDataFeedID(), conn, session);
            List<ConfiguredMethod> clonedConfiguredMethods = new ArrayList<ConfiguredMethod>();
            for (ConfiguredMethod configuredMethod : dynamicServiceDefinition.getConfiguredMethods()) {
                List<AnalysisItem> clonedMethodItems = new ArrayList<AnalysisItem>();
                for (AnalysisItem keyItem : configuredMethod.getKeys()) {
                    // find that item in the cloned definition...
                    AnalysisItem matchedItem = null;
                    for (AnalysisItem clonedItem : clonedFeedDefinition.getFields()) {
                        if (clonedItem.equals(keyItem)) {
                            matchedItem = clonedItem;
                        }
                    }
                    clonedMethodItems.add(matchedItem);
                }
                ConfiguredMethod clonedMethod = configuredMethod.clone();
                clonedMethod.setKeys(clonedMethodItems);
                clonedConfiguredMethods.add(clonedMethod);
            }
            DynamicServiceDefinition clonedDefinition = dynamicServiceDefinition.clone();
            clonedDefinition.setConfiguredMethods(clonedConfiguredMethods);
            apiService.addDynamicServiceDefinition(clonedDefinition, conn);
            clonedFeedDefinition.setDynamicServiceDefinitionID(clonedDefinition.getServiceID());
        } finally {
            session.close();
        }
    }

    private List<AnalysisDefinition> getInsightsFromFeed(long feedID, Connection conn) throws SQLException {
        List<AnalysisDefinition> analyses = new ArrayList<AnalysisDefinition>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS WHERE DATA_FEED_ID = ?");
        queryStmt.setLong(1, feedID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            analyses.add(AnalysisDefinitionFactory.fromWSDefinition(analysisStorage.getAnalysisDefinition(rs.getLong(1), conn).createBlazeDefinition()));
        }
        return analyses;
    }

    private List<FeedDefinition> getFeedsFromInsight(long insightID, Connection conn) throws SQLException {
        List<FeedDefinition> feeds = new ArrayList<FeedDefinition>();
        PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED_ID FROM ANALYSIS_BASED_FEED WHERE analysis_id = ?");
        queryStmt.setLong(1, insightID);
        ResultSet rs = queryStmt.executeQuery();
        while (rs.next()) {
            long feedID = rs.getLong(1);
            feeds.add(feedStorage.getFeedDefinitionData(feedID, conn));
        }
        return feeds;
    }

    private Solution getSolution(long solutionID) {
        Connection conn = Database.instance().getConnection();
        try {
            return getSolution(solutionID, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public Solution getSolution(long solutionID, Connection conn) throws SQLException {
        PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION_ID, NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, SOLUTION_ARCHIVE_NAME, goal_tree_id" +
                " FROM SOLUTION WHERE SOLUTION_ID = ?");
        getSolutionsStmt.setLong(1, solutionID);
        ResultSet rs = getSolutionsStmt.executeQuery();
        if (rs.next()) {
            String name = rs.getString(2);
            String description = rs.getString(3);
            String industry = rs.getString(4);
            String author = rs.getString(5);
            boolean copyData = rs.getBoolean(6);
            String solutionArchiveName = rs.getString(7);
            long goalTreeID = rs.getLong(8);
            Solution solution = new Solution();
            solution.setName(name);
            solution.setDescription(description);
            solution.setSolutionID(solutionID);
            solution.setIndustry(industry);
            solution.setAuthor(author);
            solution.setCopyData(copyData);
            solution.setSolutionArchiveName(solutionArchiveName);
            solution.setGoalTreeID(goalTreeID);
            return solution;
        } else {
            throw new RuntimeException();
        }
    }

    public List<SolutionGoalTreeDescriptor> getTreesFromSolutions() {
        int solutionTier = SecurityUtil.getAccountTier();
        List<SolutionGoalTreeDescriptor> descriptors = new ArrayList<SolutionGoalTreeDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement treeStmt = conn.prepareStatement("SELECT SOLUTION_ID, SOLUTION.NAME, GOAL_TREE.GOAL_TREE_ID, GOAL_TREE.NAME FROM SOLUTION, GOAL_TREE WHERE " +
                    "SOLUTION.GOAL_TREE_ID = GOAL_TREE.GOAL_TREE_ID AND SOLUTION.SOLUTION_TIER >= ?");
            treeStmt.setInt(1, solutionTier);
            ResultSet rs = treeStmt.executeQuery();
            while (rs.next()) {
                long solutionID = rs.getLong(1);
                String solutionName = rs.getString(2);
                long goalTreeID = rs.getLong(3);
                String goalTreeName = rs.getString(4);
                descriptors.add(new SolutionGoalTreeDescriptor(goalTreeID, goalTreeName, 0, solutionID, solutionName));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return descriptors;
    }

    public List<Solution> getSolutions() {
        int accountType = SecurityUtil.getAccountTier();
        List<Solution> solutions = new ArrayList<Solution>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION_ID, NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, SOLUTION_ARCHIVE_NAME, GOAL_TREE_ID FROM SOLUTION WHERE SOLUTION_TIER <= ?");
            getSolutionsStmt.setInt(1, accountType);
            PreparedStatement dataSourceCountStmt = conn.prepareStatement("SELECT COUNT(*) FROM solution_to_feed WHERE solution_id = ?");
            PreparedStatement goalTreeCountStmt = conn.prepareStatement("SELECT COUNT(*) FROM solution_to_goal_tree WHERE solution_id = ?");
            ResultSet rs = getSolutionsStmt.executeQuery();
            while (rs.next()) {
                long solutionID = rs.getLong(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String industry = rs.getString(4);
                String author = rs.getString(5);
                boolean copyData = rs.getBoolean(6);
                String solutionArchiveName = rs.getString(7);
                long goalTreeID = rs.getLong(8);
                Solution solution = new Solution();
                solution.setName(name);
                solution.setDescription(description);
                solution.setSolutionID(solutionID);
                solution.setIndustry(industry);
                solution.setAuthor(author);
                solution.setCopyData(copyData);
                solution.setSolutionArchiveName(solutionArchiveName);
                solution.setGoalTreeID(goalTreeID);
                dataSourceCountStmt.setLong(1, solutionID);
                ResultSet dataSourceRS = dataSourceCountStmt.executeQuery();
                dataSourceRS.next();
                goalTreeCountStmt.setLong(1, solutionID);
                ResultSet goalTreeRS = goalTreeCountStmt.executeQuery();
                goalTreeRS.next();
                solution.setInstallable(dataSourceRS.getInt(1) > 0 || goalTreeRS.getInt(1) > 0);
                solutions.add(solution);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return solutions;
    }

    public void uninstallSolution(long solutionID, boolean deleteFeeds) {
        long userID = SecurityUtil.getUserID();
        Connection conn = Database.instance().getConnection();
        try {
            if (deleteFeeds) {

            }
            PreparedStatement uninstallStmt = conn.prepareStatement("DELETE FROM USER_TO_SOLUTION WHERE SOLUTION_ID = ? AND USER_ID = ?");
            uninstallStmt.setLong(1, solutionID);
            uninstallStmt.setLong(2, userID);
            uninstallStmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public List<Solution> getInstalledSolutions() {
        long userID = SecurityUtil.getUserID();
        List<Solution> solutions = new ArrayList<Solution>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION.SOLUTION_ID, NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, SOLUTION_ARCHIVE_NAME FROM " +
                    "SOLUTION, USER_TO_SOLUTION WHERE SOLUTION.SOLUTION_ID = USER_TO_SOLUTION.SOLUTION_ID AND USER_TO_SOLUTION.USER_ID = ? AND " +
                    "USER_TO_SOLUTION.USER_ROLE = ?");
            getSolutionsStmt.setLong(1, userID);
            getSolutionsStmt.setInt(2, SolutionRoles.INSTALLER);
            ResultSet rs = getSolutionsStmt.executeQuery();
            while (rs.next()) {
                long solutionID = rs.getLong(1);
                String name = rs.getString(2);
                String description = rs.getString(3);
                String industry = rs.getString(4);
                String author = rs.getString(5);
                boolean copyData = rs.getBoolean(6);
                String solutionArchiveName = rs.getString(7);
                Solution solution = new Solution();
                solution.setName(name);
                solution.setDescription(description);
                solution.setSolutionID(solutionID);
                solution.setIndustry(industry);
                solution.setAuthor(author);
                solution.setCopyData(copyData);
                solution.setSolutionArchiveName(solutionArchiveName);
                solutions.add(solution);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return solutions;
    }
    
    public byte[] getArchive(long solutionID) {
        Connection conn = Database.instance().getConnection();
        byte[] bytes;
        try {
            Statement getArchiveStmt = conn.createStatement();
            ResultSet rs = getArchiveStmt.executeQuery("SELECT ARCHIVE FROM SOLUTION WHERE SOLUTION_ID = " + solutionID);
            if (rs.next()) {
                bytes = rs.getBytes(1);
            } else {
                throw new RuntimeException("Couldn't find archive for solution " + solutionID);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return bytes;
    }

    public List<Solution> getSolutionsWithTags(List<Tag> tags) {
        List<Solution> solutions = new ArrayList<Solution>();
        Connection conn = Database.instance().getConnection();
        try {
            StringBuilder queryBuilder = new StringBuilder("SELECT SOLUTION.SOLUTION_ID FROM SOLUTION, SOLUTION_TAG WHERE SOLUTION_TAG.TAG_NAME IN (");
            Iterator<Tag> tagIter = tags.iterator();
            while (tagIter.hasNext()) {
                tagIter.next();
                queryBuilder.append("?");
                if (tagIter.hasNext()) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(") AND SOLUTION_TAG.SOLUTION_ID = SOLUTION.SOLUTION_ID");
            PreparedStatement querySolutionsStmt = conn.prepareStatement(queryBuilder.toString());
            int i = 1;
            for (Tag tag : tags) {
                querySolutionsStmt.setString(i++, tag.getTagName());
            }
            ResultSet solutionIDRS = querySolutionsStmt.executeQuery();
            while (solutionIDRS.next()) {
                solutions.add(getSolution(solutionIDRS.getLong(1)));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return solutions;
    }

    public void installSubTree(long solutionID) {
        Connection conn = Database.instance().getConnection();
        try {
            GoalService goalService = new GoalService();
            SolutionService solutionService = new SolutionService();
            Solution solution = solutionService.getSolution(solutionID, conn);
            GoalTree goalTree = goalService.getGoalTree(solution.getGoalTreeID());
            
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
