package com.easyinsight.solutions;

import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.AuthorizationManager;
import com.easyinsight.security.AuthorizationRequirement;
import com.easyinsight.goals.*;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.users.Account;
import com.easyinsight.exchange.SolutionReportExchangeItem;
import com.easyinsight.exchange.ReportExchangeItem;

import java.util.*;
import java.util.Date;
import java.sql.*;
import java.io.ByteArrayInputStream;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Aug 28, 2008
 * Time: 12:32:59 AM
 */
public class SolutionService {

    public Solution retrieveSolution(long solutionID) {
        try {
            return getSolution(solutionID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void deleteSolution(long solutionID) {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM SOLUTION WHERE SOLUTION_ID = ?");
            stmt.setLong(1, solutionID);
            stmt.executeUpdate();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void addSolutionArchive(byte[] archive, long solutionID, String solutionArchiveName) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateArchiveStmt = conn.prepareStatement("UPDATE SOLUTION SET ARCHIVE = ?, SOLUTION_ARCHIVE_NAME = ? WHERE SOLUTION_ID = ?");
            ByteArrayInputStream bais = new ByteArrayInputStream(archive);

            updateArchiveStmt.setBinaryStream(1, bais, archive.length);
            //updateArchiveStmt.setBytes(1, archive);
            updateArchiveStmt.setString(2, solutionArchiveName);
            updateArchiveStmt.setLong(3, solutionID);
            updateArchiveStmt.executeUpdate();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }

    public long addSolution(Solution solution, List<Integer> feedIDs) {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement insertSolutionStmt = conn.prepareStatement("INSERT INTO SOLUTION (NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, goal_tree_id, " +
                    "SOLUTION_TIER, CATEGORY, screencast_directory, screencast_mp4_name, footer_text, logo_link) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
            insertSolutionStmt.setInt(8, solution.getCategory());
            insertSolutionStmt.setString(9, solution.getScreencastDirectory());
            insertSolutionStmt.setString(10, solution.getScreencastName());
            insertSolutionStmt.setString(11, solution.getFooterText());
            insertSolutionStmt.setString(12, solution.getLogoLink());
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
            return solutionID;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.instance().closeConnection(conn);
        }
    }

    public void updateSolution(Solution solution, List<Integer> feedIDs) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateSolutionStmt = conn.prepareStatement("UPDATE SOLUTION SET NAME = ?, DESCRIPTION = ?, INDUSTRY = ?, AUTHOR = ?, " +
                    "COPY_DATA = ?, GOAL_TREE_ID = ?, SOLUTION_TIER = ?, CATEGORY = ?, screencast_directory = ?, screencast_mp4_name = ?, footer_text = ?," +
                    "logo_link = ? WHERE SOLUTION_ID = ?",
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
            updateSolutionStmt.setInt(8, solution.getCategory());
            updateSolutionStmt.setString(9, solution.getScreencastDirectory());
            updateSolutionStmt.setString(10, solution.getScreencastName());
            updateSolutionStmt.setString(11, solution.getFooterText());
            updateSolutionStmt.setString(12, solution.getLogoLink());
            updateSolutionStmt.setLong(13, solution.getSolutionID());
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
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.instance().closeConnection(conn);
        }
    }

    public AuthorizationRequirement determineAuthorizationRequirements(long solutionID) {
        List<AuthorizationRequirement> authRequirements = new ArrayList<AuthorizationRequirement>();
        try {
            Solution solution = getSolution(solutionID);
            Set<FeedType> types = new SolutionVisitor().getFeedTypes(solution);
            for (FeedType type : types) {
                AuthorizationRequirement authorizationRequirement = new AuthorizationManager().authorize(type);
                if (authorizationRequirement != null) {
                    authRequirements.add(authorizationRequirement);
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        if (authRequirements.size() > 0) {
            return authRequirements.get(0);
        }
        return null;
    }

    public void findInstalledSourcesForSolution(long solutionID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DISTINCT DATA_FEED.DATA_FEED_ID " +
                    " FROM UPLOAD_POLICY_USERS, DATA_FEED, SOLUTION_INSTALL WHERE " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND " +
                    "DATA_FEED.DATA_FEED_ID = SOLUTION_INSTALL.INSTALLED_DATA_SOURCE_ID AND SOLUTION_INSTALL.SOLUTION_ID = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setLong(2, solutionID);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long dataSourceID = rs.getLong(1);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<DataSourceDescriptor> determineDataSource(long dataSourceID) {
        List<DataSourceDescriptor> descriptors = new ArrayList<DataSourceDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement dsQueryStmt = conn.prepareStatement("SELECT SOLUTION_INSTALL.ORIGINAL_DATA_SOURCE_ID FROM " +
                    "SOLUTION_INSTALL WHERE SOLUTION_INSTALL.installed_data_source_id = ?");
            dsQueryStmt.setLong(1, dataSourceID);
            ResultSet dsRS = dsQueryStmt.executeQuery();
            if (dsRS.next()) {
                long originalDataSourceID = dsRS.getLong(1);
                PreparedStatement queryStmt = conn.prepareStatement("SELECT SOLUTION_INSTALL.installed_data_source_id, DATA_FEED.FEED_NAME FROM " +
                    "SOLUTION_INSTALL, DATA_FEED, UPLOAD_POLICY_USERS WHERE " +
                    "SOLUTION_INSTALL.original_data_source_id = ? AND " +
                    "SOLUTION_INSTALL.INSTALLED_DATA_SOURCE_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID");
                queryStmt.setLong(1, originalDataSourceID);
                queryStmt.setLong(2, SecurityUtil.getUserID());
                ResultSet rs = queryStmt.executeQuery();
                while (rs.next()) {
                    long id = rs.getLong(1);
                    String name = rs.getString(2);
                    descriptors.add(new DataSourceDescriptor(name, id));
                }
            }



        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {            
            Database.closeConnection(conn);
        }
        return descriptors;
    }

    public InsightDescriptor installReport(long reportID, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedStorage feedStorage = new FeedStorage();
            FeedDefinition targetDataSource = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            Session session = Database.instance().createSession(conn);
            AnalysisDefinition report = new AnalysisStorage().getPersistableReport(reportID, session);
            FeedDefinition sourceDataSource = feedStorage.getFeedDefinitionData(report.getDataFeedID(), conn);
            Map<Key, Key> keyReplacementMap = createKeyReplacementMap(targetDataSource, sourceDataSource);
            InsightDescriptor id = installReportToDataSource(targetDataSource, report, conn, keyReplacementMap);
            conn.commit();
            session.close();
            return id;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private Map<Key, Key> createKeyReplacementMap(FeedDefinition localDefinition, FeedDefinition sourceDefinition) {
        Map<Key, Key> keys = new HashMap<Key, Key>();
        for (AnalysisItem sourceField : sourceDefinition.getFields()) {
            for (AnalysisItem targetField : localDefinition.getFields()) {
                if (sourceField.getKey().toKeyString().equals(targetField.getKey().toKeyString())) {
                    keys.put(sourceField.getKey(), targetField.getKey());
                    break;
                }
            }
        }
        return keys;
    }

    private InsightDescriptor installReportToDataSource(FeedDefinition localDefinition, AnalysisDefinition report, Connection conn,
                                                        Map<Key, Key> keyReplacementMap) throws CloneNotSupportedException {
        AnalysisDefinition clonedReport = report.clone(keyReplacementMap, localDefinition.getFields());
        clonedReport.setSolutionVisible(false);
        clonedReport.setAnalysisPolicy(AnalysisPolicy.PRIVATE);
        clonedReport.setDataFeedID(localDefinition.getDataFeedID());

        // what to do here...

        clonedReport.setUserBindings(Arrays.asList(new UserToAnalysisBinding(SecurityUtil.getUserID(), UserPermission.OWNER)));
        new AnalysisStorage().saveAnalysis(clonedReport, conn);
        return new InsightDescriptor(clonedReport.getAnalysisID(), clonedReport.getTitle(),
                clonedReport.getDataFeedID(), clonedReport.getReportType());
    }

    public List<SolutionReportExchangeItem> getSolutionReports() {
        List<SolutionReportExchangeItem> reports = new ArrayList<SolutionReportExchangeItem>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT TAG FROM ANALYSIS_TO_TAG, ANALYSIS_TAGS WHERE " +
                    "ANALYSIS_TO_TAG.analysis_tags_id = ANALYSIS_TAGS.analysis_tags_id AND analysis_to_tag.analysis_id = ?");
            PreparedStatement analysisQueryStmt = conn.prepareStatement("SELECT DISTINCT ANALYSIS.ANALYSIS_ID, ANALYSIS.TITLE, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE, " +
                    "avg(USER_REPORT_RATING.rating), analysis.create_date, ANALYSIS.DESCRIPTION, DATA_FEED.FEED_NAME, ANALYSIS.AUTHOR_NAME," +
                    "DATA_FEED.PUBLICLY_VISIBLE, SOLUTION.NAME, SOLUTION.SOLUTION_ID FROM DATA_FEED, SOLUTION_INSTALL, SOLUTION, ANALYSIS " +
                    " LEFT JOIN USER_REPORT_RATING ON USER_REPORT_RATING.report_id = ANALYSIS.ANALYSIS_ID WHERE ANALYSIS.DATA_FEED_ID = DATA_FEED.DATA_FEED_ID AND " +
                    "ANALYSIS.DATA_FEED_ID = SOLUTION_INSTALL.installed_data_source_id AND ANALYSIS.SOLUTION_VISIBLE = ? GROUP BY ANALYSIS.ANALYSIS_ID");
            analysisQueryStmt.setBoolean(1, true);
            ResultSet analysisRS = analysisQueryStmt.executeQuery();
            while (analysisRS.next()) {
                long analysisID = analysisRS.getLong(1);
                if (analysisID == 0) {
                    continue;
                }
                String title = analysisRS.getString(2);
                long dataSourceID = analysisRS.getLong(3);
                int reportType = analysisRS.getInt(4);
                double ratingAverage = analysisRS.getDouble(5);

                Date created = null;
                java.sql.Date date = analysisRS.getDate(6);
                if (date != null) {
                    created = new Date(date.getTime());
                }

                String description = analysisRS.getString(7);
                String dataSourceName = analysisRS.getString(8);
                String authorName = analysisRS.getString(9);
                boolean accessible = analysisRS.getBoolean(10);
                String solutionName = analysisRS.getString(11);
                long solutionID = analysisRS.getLong(12);
                getTagsStmt.setLong(1, analysisID);
                SolutionReportExchangeItem item = new SolutionReportExchangeItem(title, analysisID, reportType, dataSourceID,
                        ratingAverage, 0, created, description, authorName, dataSourceName, accessible, solutionID, solutionName);
                reports.add(item);
                ResultSet tagRS = getTagsStmt.executeQuery();
                List<String> tags = new ArrayList<String>();
                while (tagRS.next()) {
                    tags.add(tagRS.getString(1));
                }
                item.setTags(tags);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return reports;
    }

    public void getReportsForSolution(long solutionID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID FROM ANALYSIS, solution_install WHERE " +
                    "ANALYSIS.DATA_FEED_ID = SOLUTION_INSTALL.installed_data_source_id AND SOLUTION_INSTALL.solution_id = ? AND " +
                    "ANALYSIS.solution_visible = ?");
            queryStmt.setLong(1, solutionID);
            queryStmt.setBoolean(2, true);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long reportID = rs.getLong(1);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<SolutionInstallInfo> installSolution(long solutionID) {
        // establish the connection from the account/user to the solution
        // retrieve the feeds for this solution
        // retrieve the insights matching that feed
        // clone the feed/insights
        Solution solution = getSolution(solutionID);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            InstallationSystem installationSystem = new InstallationSystem(conn);
            installationSystem.installSolution(solution);
            conn.commit();
            return installationSystem.getAllSolutions();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public List<FeedDescriptor> getAvailableFeeds() {
        SecurityUtil.authorizeAccountTier(Account.ADMINISTRATOR);
        List<FeedDescriptor> feedDescriptors = new ArrayList<FeedDescriptor>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_FEED.DATA_FEED_ID, DATA_FEED.FEED_NAME FROM DATA_FEED, UPLOAD_POLICY_USERS WHERE " +
                    "UPLOAD_POLICY_USERS.USER_ID = ? AND DATA_FEED.DATA_FEED_ID = UPLOAD_POLICY_USERS.FEED_ID AND DATA_FEED.VISIBLE = ?");
            queryStmt.setLong(1, SecurityUtil.getUserID());
            queryStmt.setBoolean(2, true);
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
            PreparedStatement queryInsightsStmt = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE, DATA_FEED_ID, REPORT_TYPE FROM ANALYSIS WHERE DATA_FEED_ID = ? AND ROOT_DEFINITION = ?");
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long feedID = rs.getLong(1);
                String name = rs.getString(2);
                FeedDescriptor feedDescriptor = new FeedDescriptor();
                feedDescriptor.setDataFeedID(feedID);
                feedDescriptor.setName(name);
                feedDescriptors.add(feedDescriptor);
                queryInsightsStmt.setLong(1, feedID);
                queryInsightsStmt.setBoolean(2, false);
                ResultSet insightRS = queryInsightsStmt.executeQuery();
                while (insightRS.next()) {
                    long insightID = insightRS.getLong(1);
                    String insightName = insightRS.getString(2);
                    insightDescriptors.add(new InsightDescriptor(insightID, insightName, insightRS.getLong(3), insightRS.getInt(4)));
                }
            }
            PreparedStatement getGoalsStmt = conn.prepareStatement("SELECT goal_tree.goal_tree_id, goal_tree.name, goal_tree.goal_tree_icon FROM solution, goal_tree " +
                    "WHERE SOLUTION_ID = ? AND goal_tree.goal_tree_id = solution.goal_tree_id");
            getGoalsStmt.setLong(1, solutionID);
            ResultSet goalRS = getGoalsStmt.executeQuery();
            while (goalRS.next()) {
                long goalID = goalRS.getLong(1);
                String goalName = goalRS.getString(2);
                GoalTreeDescriptor goalTreeDescriptor = new GoalTreeDescriptor(goalID, goalName, 0, goalRS.getString(3));
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
        int accountType = 0;
        if (SecurityUtil.getUserID(false) > 0) {
            accountType = SecurityUtil.getAccountTier();
        }
        PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION_ID, NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, SOLUTION_ARCHIVE_NAME, goal_tree_id," +
                "solution_image, screencast_directory, screencast_mp4_name, solution_tier, footer_text, logo_link FROM SOLUTION WHERE SOLUTION_ID = ?");
        getSolutionsStmt.setLong(1, solutionID);
        PreparedStatement dataSourceCountStmt = conn.prepareStatement("SELECT COUNT(*) FROM solution_to_feed WHERE solution_id = ?");
        PreparedStatement goalTreeCountStmt = conn.prepareStatement("SELECT COUNT(*) FROM solution_to_goal_tree WHERE solution_id = ?");
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
            solution.setImage(rs.getBytes(9));
            solution.setScreencastDirectory(rs.getString(10));
            solution.setScreencastName(rs.getString(11));
            solution.setSolutionTier(rs.getInt(12));
            solution.setFooterText(rs.getString(13));
            solution.setLogoLink(rs.getString(14));
            solution.setAccessible(solution.getSolutionTier() <= accountType);
            dataSourceCountStmt.setLong(1, solutionID);
            ResultSet dataSourceRS = dataSourceCountStmt.executeQuery();
            dataSourceRS.next();
            goalTreeCountStmt.setLong(1, solutionID);
            ResultSet goalTreeRS = goalTreeCountStmt.executeQuery();
            goalTreeRS.next();
            solution.setInstallable(dataSourceRS.getInt(1) > 0 || goalTreeRS.getInt(1) > 0);
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
            PreparedStatement treeStmt = conn.prepareStatement("SELECT SOLUTION_ID, SOLUTION.NAME, GOAL_TREE.GOAL_TREE_ID, GOAL_TREE.NAME, GOAL_TREE.goal_tree_icon FROM SOLUTION, GOAL_TREE WHERE " +
                    "SOLUTION.GOAL_TREE_ID = GOAL_TREE.GOAL_TREE_ID AND SOLUTION.SOLUTION_TIER <= ?");
            treeStmt.setInt(1, solutionTier);
            ResultSet rs = treeStmt.executeQuery();
            while (rs.next()) {
                long solutionID = rs.getLong(1);
                String solutionName = rs.getString(2);
                long goalTreeID = rs.getLong(3);
                String goalTreeName = rs.getString(4);
                descriptors.add(new SolutionGoalTreeDescriptor(goalTreeID, goalTreeName, 0, solutionID, solutionName, rs.getString(5)));
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
        int accountType = 0;
        if (SecurityUtil.getUserID(false) > 0) {
            accountType = SecurityUtil.getAccountTier();
        }
        List<Solution> solutions = new ArrayList<Solution>();
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement getSolutionsStmt = conn.prepareStatement("SELECT SOLUTION_ID, NAME, DESCRIPTION, INDUSTRY, AUTHOR, COPY_DATA, SOLUTION_ARCHIVE_NAME, GOAL_TREE_ID, SOLUTION_TIER," +
                    "solution_image, CATEGORY, screencast_directory, screencast_mp4_name, footer_text, logo_link FROM SOLUTION WHERE SOLUTION_TIER <= ?");
            getSolutionsStmt.setInt(1, Account.ADMINISTRATOR);
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
                int solutionTier = rs.getInt(9);
                byte[] solutionImage = rs.getBytes(10);
                Solution solution = new Solution();
                solution.setName(name);
                solution.setAccessible(solutionTier <= accountType);
                solution.setDescription(description);
                solution.setSolutionID(solutionID);
                solution.setIndustry(industry);
                solution.setAuthor(author);
                solution.setCopyData(copyData);
                solution.setSolutionArchiveName(solutionArchiveName);
                solution.setGoalTreeID(goalTreeID);
                solution.setSolutionTier(solutionTier);
                solution.setImage(solutionImage);
                solution.setCategory(rs.getInt(11));
                solution.setScreencastDirectory(rs.getString(12));
                solution.setScreencastName(rs.getString(13));
                solution.setFooterText(rs.getString(14));
                solution.setLogoLink(rs.getString(15));
                dataSourceCountStmt.setLong(1, solutionID);
                ResultSet dataSourceRS = dataSourceCountStmt.executeQuery();
                dataSourceRS.next();
                goalTreeCountStmt.setLong(1, solutionID);
                ResultSet goalTreeRS = goalTreeCountStmt.executeQuery();
                goalTreeRS.next();
                solution.setInstallable(dataSourceRS.getInt(1) > 0 || goalTreeRS.getInt(1) > 0);
                solutions.add(solution);
            }
            Collections.sort(solutions, new Comparator<Solution>() {

                public int compare(Solution o1, Solution o2) {
                    if (o1.isAccessible() && !o2.isAccessible()) {
                        return -1;
                    } else if (!o1.isAccessible() && o2.isAccessible()) {
                        return 1;
                    } else {
                        return o1.getName().compareTo(o2.getName());
                    }
                }
            });
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

    public void addSolutionImage(byte[] bytes, long solutionID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateArchiveStmt = conn.prepareStatement("UPDATE SOLUTION SET SOLUTION_IMAGE = ? WHERE SOLUTION_ID = ?");
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            updateArchiveStmt.setBinaryStream(1, bais, bytes.length);
            updateArchiveStmt.setLong(2, solutionID);
            updateArchiveStmt.executeUpdate();
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.instance().closeConnection(conn);
        }
    }
}
