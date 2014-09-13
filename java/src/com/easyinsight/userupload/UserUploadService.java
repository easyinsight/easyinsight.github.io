package com.easyinsight.userupload;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.easyinsight.benchmark.BenchmarkManager;
import com.easyinsight.config.ConfigLoader;
import com.easyinsight.core.DataSourceDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.dashboard.DashboardService;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.datafeeds.basecampnext.BasecampNextAccount;
import com.easyinsight.datafeeds.basecampnext.BasecampNextCompositeSource;
import com.easyinsight.datafeeds.database.ServerDatabaseConnection;
import com.easyinsight.datafeeds.file.FileBasedFeedDefinition;
import com.easyinsight.datafeeds.infusionsoft.*;
import com.easyinsight.datafeeds.json.JSONDataSource;
import com.easyinsight.datafeeds.json.JSONSetup;
import com.easyinsight.datafeeds.smartsheet.SmartsheetTableSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.etl.LookupTableDescriptor;
import com.easyinsight.goals.InstallationSystem;
import com.easyinsight.scorecard.DataSourceRefreshEvent;
import com.easyinsight.scorecard.ScorecardDescriptor;
import com.easyinsight.scorecard.ScorecardInternalService;
import com.easyinsight.scorecard.ScorecardService;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.StorageLimitException;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.security.Roles;
import com.easyinsight.tag.Tag;
import com.easyinsight.users.*;
import com.easyinsight.analysis.*;
import com.easyinsight.PasswordStorage;
import com.easyinsight.scheduler.*;
import com.easyinsight.solutions.SolutionInstallInfo;

import java.io.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.util.ServiceUtil;
import com.xerox.amazonws.sqs2.Message;
import com.xerox.amazonws.sqs2.MessageQueue;
import com.xerox.amazonws.sqs2.SQSUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Jan 26, 2008
 * Time: 9:17:37 PM
 */
public class UserUploadService {


    private static FeedStorage feedStorage = new FeedStorage();
    private static Map<Long, RawUploadData> rawDataMap = new WeakHashMap<Long, RawUploadData>();

    public UserUploadService() {
    }

    public boolean validateUpload(String uploadKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT UPLOAD_SUCCESSFUL FROM UPLOAD_BYTES WHERE UPLOAD_KEY = ? AND USER_ID = ?");

            ps.setString(1, uploadKey);
            ps.setLong(2, SecurityUtil.getUserID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        return false;
    }

    public void hackyUpload(String uploadKey, byte[] bytes) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT UPLOAD_BYTES_ID FROM UPLOAD_BYTES WHERE UPLOAD_KEY = ? AND USER_ID = ?");

            ps.setString(1, uploadKey);
            ps.setLong(2, SecurityUtil.getUserID());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ByteArrayOutputStream dest = new ByteArrayOutputStream();

                ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(dest));
                zos.putNextEntry(new ZipEntry("data.csv"));
                zos.write(bytes);
                zos.closeEntry();
                zos.close();
                bytes = dest.toByteArray();
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
                ObjectMetadata objectMetadata = new ObjectMetadata();
                objectMetadata.setContentLength(bytes.length);
                s3.putObject(new PutObjectRequest("archival1", uploadKey + ".zip", stream, objectMetadata));
                stream.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<Tag> getDataSourceTags() {

        EIConnection conn = Database.instance().getConnection();

        try {
            return getDataSourceTags(conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<Tag> getDataSourceTags(EIConnection conn) throws SQLException {
        List<Tag> tags = new ArrayList<Tag>();
        PreparedStatement getTagsStmt = conn.prepareStatement("SELECT TAG_NAME, ACCOUNT_TAG_ID,DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ? AND DATA_SOURCE_TAG = ? ORDER BY TAG_INDEX");
        getTagsStmt.setLong(1, SecurityUtil.getAccountID());
        getTagsStmt.setBoolean(2, true);
        ResultSet rs = getTagsStmt.executeQuery();

        while (rs.next()) {
            String tagName = rs.getString(1);
            long tagID = rs.getLong(2);
            boolean dataSourceTag = rs.getBoolean(3);
            boolean reportTag = rs.getBoolean(4);
            boolean fieldTag = rs.getBoolean(5);
            tags.add(new Tag(tagID, tagName, dataSourceTag, reportTag, fieldTag));
        }
        return tags;
    }

    public List<Tag> getReportTags() {
        List<Tag> tags = new ArrayList<Tag>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT TAG_NAME, ACCOUNT_TAG_ID,DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ? AND REPORT_TAG = ? ORDER BY TAG_INDEX");
            getTagsStmt.setLong(1, SecurityUtil.getAccountID());
            getTagsStmt.setBoolean(2, true);
            ResultSet rs = getTagsStmt.executeQuery();
            while (rs.next()) {
                String tagName = rs.getString(1);
                long tagID = rs.getLong(2);
                boolean dataSourceTag = rs.getBoolean(3);
                boolean reportTag = rs.getBoolean(4);
                boolean fieldTag = rs.getBoolean(5);
                tags.add(new Tag(tagID, tagName, dataSourceTag, reportTag, fieldTag));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return tags;
    }


    public List<Tag> saveTags(List<Tag> tags) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT TAG_NAME, ACCOUNT_TAG_ID, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ?");
            getTagsStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet rs = getTagsStmt.executeQuery();
            List<Tag> existingTags = new ArrayList<Tag>();
            while (rs.next()) {
                String tagName = rs.getString(1);
                long tagID = rs.getLong(2);
                boolean dataSourceTag = rs.getBoolean(3);
                boolean reportTag = rs.getBoolean(4);
                boolean fieldTag = rs.getBoolean(5);
                existingTags.add(new Tag(tagID, tagName, dataSourceTag, reportTag, fieldTag));
            }
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM ACCOUNT_TAG WHERE ACCOUNT_TAG_ID = ?");


            for (Tag existingTag : existingTags) {
                if (!tags.contains(existingTag)) {
                    deleteStmt.setLong(1, existingTag.getId());
                    deleteStmt.executeUpdate();
                }
            }

            int i = 0;
            for (Tag tag : tags) {
                if (tag.getId() == 0) {
                    createTag(conn, i, tag);
                } else {
                    saveTag(conn, i, tag);
                }
                i++;
            }
            deleteStmt.close();

            return tags;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void saveTag(EIConnection conn, int i, Tag tag) throws SQLException {
        PreparedStatement updateStmt = conn.prepareStatement("UPDATE ACCOUNT_TAG SET TAG_NAME = ?, data_source_tag = ?, report_tag = ?, field_tag = ?" + (i == -1 ? "" : ", tag_index = ?") + " WHERE ACCOUNT_TAG_ID = ? AND ACCOUNT_TAG.ACCOUNT_ID = ?");
        updateStmt.setString(1, tag.getName());
        updateStmt.setBoolean(2, tag.isDataSource());
        updateStmt.setBoolean(3, tag.isReport());
        updateStmt.setBoolean(4, tag.isField());

        if (i != -1) {
            updateStmt.setInt(5, i);
            updateStmt.setLong(6, tag.getId());
            updateStmt.setLong(7, SecurityUtil.getAccountID());
        } else {
            updateStmt.setLong(5, tag.getId());
            updateStmt.setLong(6, SecurityUtil.getAccountID());
        }

        updateStmt.executeUpdate();
        updateStmt.close();
    }

    public Tag createTag(EIConnection conn, int i, Tag tag) throws SQLException {
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TAG (TAG_NAME, ACCOUNT_ID, tag_index, data_source_tag, report_tag, field_tag) " +
                "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, tag.getName());
        insertStmt.setLong(2, SecurityUtil.getAccountID());
        insertStmt.setInt(3, i);
        insertStmt.setBoolean(4, tag.isDataSource());
        insertStmt.setBoolean(5, tag.isReport());
        insertStmt.setBoolean(6, tag.isField());
        insertStmt.execute();
        long id = Database.instance().getAutoGenKey(insertStmt);
        tag.setId(id);
        insertStmt.close();
        return tag;
    }

    public void tagReportsAndDashboards(List<EIDescriptor> descriptors, Tag tag) {
        EIConnection conn = Database.instance().getConnection();
        try {
            tagReportsAndDashboards(descriptors, tag, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void tagReportsAndDashboards(List<EIDescriptor> descriptors, Tag tag, EIConnection conn) throws SQLException {
        PreparedStatement existingReportStmt = conn.prepareStatement("SELECT tag_id FROM report_to_tag WHERE report_id = ?");
        PreparedStatement existingDashboardStmt = conn.prepareStatement("SELECT tag_id FROM dashboard_to_tag WHERE dashboard_id = ?");
        PreparedStatement saveReportStmt = conn.prepareStatement("INSERT INTO report_to_tag (tag_id, report_id) VALUES (?, ?)");
        PreparedStatement saveDashboardStmt = conn.prepareStatement("INSERT INTO dashboard_to_tag (tag_id, dashboard_id) VALUES (?, ?)");
        for (EIDescriptor dsd : descriptors) {
            if (dsd.getType() == EIDescriptor.REPORT) {
                SecurityUtil.authorizeReport(dsd.getId(), Roles.EDITOR);
                Set<Long> existingIDs = new HashSet<Long>();
                existingReportStmt.setLong(1, dsd.getId());
                ResultSet rs = existingReportStmt.executeQuery();
                while (rs.next()) {
                    existingIDs.add(rs.getLong(1));
                }
                if (!existingIDs.contains(tag.getId())) {
                    saveReportStmt.setLong(1, tag.getId());
                    saveReportStmt.setLong(2, dsd.getId());
                    saveReportStmt.execute();
                }
            } else if (dsd.getType() == EIDescriptor.DASHBOARD) {
                SecurityUtil.authorizeDashboard(dsd.getId(), Roles.EDITOR);
                Set<Long> existingIDs = new HashSet<Long>();
                existingDashboardStmt.setLong(1, dsd.getId());
                ResultSet rs = existingDashboardStmt.executeQuery();
                while (rs.next()) {
                    existingIDs.add(rs.getLong(1));
                }
                if (!existingIDs.contains(tag.getId())) {
                    saveDashboardStmt.setLong(1, tag.getId());
                    saveDashboardStmt.setLong(2, dsd.getId());
                    saveDashboardStmt.execute();
                }
            }
        }
        existingReportStmt.close();
        existingDashboardStmt.close();
        saveReportStmt.close();
        saveDashboardStmt.close();
    }

    public void untagReportsAndDashboards(List<EIDescriptor> descriptors, Tag tag) {
        EIConnection conn = Database.instance().getConnection();
        try {
            untagReportsAndDashboards(descriptors, tag, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void untagReportsAndDashboards(List<EIDescriptor> descriptors, Tag tag, EIConnection conn) throws SQLException {
        PreparedStatement deleteReportStmt = conn.prepareStatement("DELETE FROM report_to_tag WHERE tag_id = ? AND report_id = ?");
        PreparedStatement deleteDashboardStmt = conn.prepareStatement("DELETE FROM dashboard_to_tag WHERE tag_id = ? AND dashboard_id = ?");
        for (EIDescriptor dsd : descriptors) {
            if (dsd.getType() == EIDescriptor.REPORT) {
                SecurityUtil.authorizeReport(dsd.getId(), Roles.EDITOR);
                deleteReportStmt.setLong(1, tag.getId());
                deleteReportStmt.setLong(2, dsd.getId());
                deleteReportStmt.executeUpdate();
            } else if (dsd.getType() == EIDescriptor.DASHBOARD) {
                SecurityUtil.authorizeDashboard(dsd.getId(), Roles.EDITOR);
                deleteDashboardStmt.setLong(1, tag.getId());
                deleteDashboardStmt.setLong(2, dsd.getId());
                deleteDashboardStmt.executeUpdate();
            }
        }
        deleteReportStmt.close();
        deleteDashboardStmt.close();
    }

    public void tagDataSources(List<DataSourceDescriptor> dataSources, Tag tag) {
        EIConnection conn = Database.instance().getConnection();
        try {
            tagDataSources(dataSources, tag, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void tagDataSources(List<DataSourceDescriptor> dataSources, Tag tag, EIConnection conn) throws SQLException {
        PreparedStatement existingStmt = conn.prepareStatement("SELECT account_tag_id FROM data_source_to_tag WHERE data_source_id = ?");
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO data_source_to_tag (account_tag_id, data_source_id) VALUES (?, ?)");
        for (DataSourceDescriptor dsd : dataSources) {
            Set<Long> existingIDs = new HashSet<Long>();
            existingStmt.setLong(1, dsd.getId());
            ResultSet rs = existingStmt.executeQuery();
            while (rs.next()) {
                existingIDs.add(rs.getLong(1));
            }
            if (!existingIDs.contains(tag.getId())) {
                saveStmt.setLong(1, tag.getId());
                saveStmt.setLong(2, dsd.getId());
                saveStmt.execute();
            }
        }
        existingStmt.close();
        saveStmt.close();
    }

    public void untagDataSource(List<DataSourceDescriptor> dataSources, Tag tag) {
        EIConnection conn = Database.instance().getConnection();
        try {
            untagDataSource(dataSources, tag, conn);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void untagDataSource(List<DataSourceDescriptor> dataSources, Tag tag, EIConnection conn) throws SQLException {
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM data_source_to_tag WHERE account_tag_id = ? AND data_source_id = ?");
        for (DataSourceDescriptor dsd : dataSources) {
            deleteStmt.setLong(1, tag.getId());
            deleteStmt.setLong(2, dsd.getId());
            deleteStmt.executeUpdate();
        }
        deleteStmt.close();
    }

    public void deleteReports(List<EIDescriptor> descriptors) {

        try {
            for (EIDescriptor descriptor : descriptors) {
                if (descriptor.getType() == EIDescriptor.REPORT) {
                    new AnalysisService().deleteAnalysisDefinition(descriptor.getId());
                } else if (descriptor.getType() == EIDescriptor.SCORECARD) {
                    new ScorecardService().deleteScorecard(descriptor.getId());
                } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                    new DashboardService().deleteDashboard(descriptor.getId());
                } else if (descriptor.getType() == EIDescriptor.LOOKUP_TABLE) {
                    new FeedService().deleteLookupTable(descriptor.getId());
                }
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public List<String> validationReportDeletion(List<EIDescriptor> descriptors) {
        try {
            for (EIDescriptor descriptor : descriptors) {
                if (descriptor.getType() == EIDescriptor.REPORT) {
                    new AnalysisService().deleteAnalysisDefinition(descriptor.getId());
                } else if (descriptor.getType() == EIDescriptor.SCORECARD) {
                    new ScorecardService().deleteScorecard(descriptor.getId());
                } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                    new DashboardService().deleteDashboard(descriptor.getId());
                } else if (descriptor.getType() == EIDescriptor.LOOKUP_TABLE) {
                    new FeedService().deleteLookupTable(descriptor.getId());
                }
            }
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long newFolder(String name, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement saveFolderStmt = conn.prepareStatement("INSERT INTO REPORT_FOLDER (ACCOUNT_ID, FOLDER_NAME, FOLDER_SEQUENCE, DATA_SOURCE_ID) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            saveFolderStmt.setLong(1, SecurityUtil.getAccountID());
            saveFolderStmt.setString(2, name);
            saveFolderStmt.setInt(3, 1);
            saveFolderStmt.setLong(4, dataSourceID);
            saveFolderStmt.execute();
            long id = Database.instance().getAutoGenKey(saveFolderStmt);
            saveFolderStmt.close();
            return id;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void move(EIDescriptor descriptor, int targetFolder) {
        EIConnection conn = Database.instance().getConnection();
        try {
            if (descriptor.getType() == EIDescriptor.REPORT) {
                SecurityUtil.authorizeReport(descriptor.getId(), Roles.EDITOR);
                InsightDescriptor insightDescriptor = (InsightDescriptor) descriptor;
                new AnalysisStorage().clearCache(descriptor.getId(), insightDescriptor.getDataFeedID());
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET FOLDER = ? WHERE ANALYSIS_ID = ?");
                updateStmt.setInt(1, targetFolder);
                updateStmt.setLong(2, descriptor.getId());
                updateStmt.executeUpdate();
                updateStmt.close();
            } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                SecurityUtil.authorizeDashboard(descriptor.getId(), Roles.EDITOR);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE DASHBOARD SET FOLDER = ? WHERE DASHBOARD_ID = ?");
                updateStmt.setInt(1, targetFolder);
                updateStmt.setLong(2, descriptor.getId());
                updateStmt.executeUpdate();
                updateStmt.close();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<DataSourceDescriptor> scopeCopy(List<DataSourceDescriptor> dataSources) {
        EIConnection conn = Database.instance().getConnection();
        try {
            Set<DataSourceDescriptor> allSources = new HashSet<DataSourceDescriptor>();
            for (DataSourceDescriptor dsd : dataSources) {
                SecurityUtil.authorizeFeed(dsd.getId(), Roles.OWNER);
                FeedDefinition dataSource = feedStorage.getFeedDefinitionData(dsd.getId());
                allSources.addAll(dataSource.getDataSources(conn));
            }
            return new ArrayList<DataSourceDescriptor>(allSources);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    /*public List<SolutionInstallInfo> copyDataSource(List<DataSourceDescriptor> dataSources, String newTag, boolean copyData) {

        EIConnection conn = Database.instance().getConnection();
        try {

            Set<Long> existing = new HashSet<Long>();
            List<SolutionInstallInfo> infos = new ArrayList<SolutionInstallInfo>();
            Map<Long, SolutionInstallInfo> allInfos = new HashMap<Long, SolutionInstallInfo>();
            for (DataSourceDescriptor dataSource : dataSources) {
                if (!existing.contains(dataSource.getId())) {
                    FeedDefinition existingDef = feedStorage.getFeedDefinitionData(dataSource.getId(), conn);
                    Map<Long, SolutionInstallInfo> results = DataSourceCopyUtils.installFeed(SecurityUtil.getUserID(), conn, copyData, existingDef, existingDef.getFeedName(), 0,
                            SecurityUtil.getAccountID(), SecurityUtil.getUserName(), allInfos);
                    if (results != null) {
                        allInfos.putAll(results);
                    }
                    for (SolutionInstallInfo info : results.values()) {
                        existing.add(info.getPreviousID());
                        infos.add(info);
                    }
                }
            }

            if (newTag != null) {
                PreparedStatement getTagStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID FROM ACCOUNT_TAG WHERE TAG_NAME = ?");
                getTagStmt.setString(1, newTag);
                ResultSet rs = getTagStmt.executeQuery();
                long tagID;
                if (rs.next()) {
                    tagID = rs.getLong(1);
                } else {
                    PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TAG (TAG_NAME, ACCOUNT_ID) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                    insertStmt.setString(1, newTag);
                    insertStmt.setLong(2, SecurityUtil.getAccountID());
                    insertStmt.execute();
                    tagID = Database.instance().getAutoGenKey(insertStmt);
                    insertStmt.close();
                }
                getTagStmt.close();
                PreparedStatement ps = conn.prepareStatement("INSERT INTO DATA_SOURCE_TO_TAG (DATA_SOURCE_ID, ACCOUNT_TAG_ID) VALUES (?, ?)");
                for (SolutionInstallInfo info : infos) {
                    ps.setLong(1, info.getNewDataSource().getDataFeedID());
                    ps.setLong(2, tagID);
                    ps.execute();
                }
                ps.close();
            }

            conn.commit();
            return infos;
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            if (!conn.getAutoCommit()) {
                conn.setAutoCommit(true);
            }
            Database.closeConnection(conn);
        }
    }*/

    private boolean keep(EIDescriptor descriptor, boolean onlyMyData) {
        return !onlyMyData || descriptor.getRole() == Roles.OWNER;
    }

    private long getDataSourceID(EIDescriptor descriptor) {
        if (descriptor.getType() == EIDescriptor.DASHBOARD) {
            return ((DashboardDescriptor) descriptor).getDataSourceID();
        } else if (descriptor.getType() == EIDescriptor.REPORT) {
            return ((InsightDescriptor) descriptor).getDataFeedID();
        } else if (descriptor.getType() == EIDescriptor.SCORECARD) {
            return ((ScorecardDescriptor) descriptor).getDataSourceID();
        } else {
            throw new RuntimeException();
        }
    }


    public MyDataTree getFeedAnalysisTree(boolean onlyMyData) {
        return getFeedAnalysisTree(onlyMyData, 0);
    }

    public List<EIDescriptor> getFeedAnalysisTreeForDataSource(DataSourceDescriptor dataSourceDescriptor) {
        SecurityUtil.authorizeFeedAccess(dataSourceDescriptor.getId());
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            List<EIDescriptor> objects = new ArrayList<EIDescriptor>();
            List<EIDescriptor> results = new ArrayList<EIDescriptor>();

            boolean testAccountVisible = FeedService.testAccountVisible(conn);

            AnalysisStorage analysisStorage = new AnalysisStorage();
            List<DashboardDescriptor> dashboards = new DashboardStorage().getDashboardsForDataSource(userID, accountID, conn, dataSourceDescriptor.getId(), testAccountVisible).values();
            List<InsightDescriptor> reports = analysisStorage.getInsightDescriptorsForDataSource(userID, accountID, dataSourceDescriptor.getId(), conn, testAccountVisible);
            objects.addAll(dashboards);
            objects.addAll(reports);
            objects.addAll(new ScorecardInternalService().getScorecards(userID, accountID, conn, testAccountVisible).values());

            Map<Long, Tag> tags = getAllTags(conn);

            addTagsToDashboards(dashboards, conn, tags);
            addTagsToReports(reports, conn, tags);

            Iterator<EIDescriptor> iter = objects.iterator();
            while (iter.hasNext()) {
                EIDescriptor descriptor = iter.next();
                if (!keep(descriptor, false)) {
                    iter.remove();
                }
            }

            iter = objects.iterator();
            while (iter.hasNext()) {
                EIDescriptor descriptor = iter.next();
                long dataSourceID = getDataSourceID(descriptor);
                if (dataSourceID == dataSourceDescriptor.getId()) {
                    dataSourceDescriptor.getChildren().add(descriptor);
                }
                iter.remove();
            }

            for (LookupTableDescriptor lookupTableDescriptor : feedStorage.getLookupTableDescriptors(conn)) {
                if (lookupTableDescriptor.getDataSourceID() == dataSourceDescriptor.getId()) {
                    lookupTableDescriptor.setRole(dataSourceDescriptor.getRole());
                    dataSourceDescriptor.getChildren().add(lookupTableDescriptor);
                }
            }

            for (EIDescriptor descriptor : results) {
                if (descriptor.getType() == EIDescriptor.DATA_SOURCE) {
                    Collections.sort(dataSourceDescriptor.getChildren(), new Comparator<EIDescriptor>() {

                        public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                            String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName() : "";
                            String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName() : "";
                            return name1.compareTo(name2);
                        }
                    });
                }
            }

            Collections.sort(results, new Comparator<EIDescriptor>() {

                public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                    String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName() : "";
                    String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName() : "";
                    return name1.compareTo(name2);
                }
            });
            conn.commit();
            return dataSourceDescriptor.getChildren();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.closeConnection(conn);
        }
    }

    public Map<Long, Tag> getAllTags(EIConnection conn) throws SQLException {
        Map<Long, Tag> tags = new LinkedHashMap<Long, Tag>();

        PreparedStatement getTagsStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, TAG_NAME, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ? ORDER BY TAG_INDEX");

        getTagsStmt.setLong(1, SecurityUtil.getAccountID());
        ResultSet tagRS = getTagsStmt.executeQuery();

        while (tagRS.next()) {
            tags.put(tagRS.getLong(1), new Tag(tagRS.getLong(1), tagRS.getString(2), tagRS.getBoolean(3), tagRS.getBoolean(4), tagRS.getBoolean(5)));
        }
        return tags;
    }

    private void addTagsToDashboards(List<DashboardDescriptor> dashboards, EIConnection conn, Map<Long, Tag> tags) throws SQLException {
        PreparedStatement getTagsToDashboardStmt = conn.prepareStatement("SELECT DASHBOARD_TO_TAG.TAG_ID, DASHBOARD_ID FROM dashboard_to_tag, account_tag WHERE " +
                "account_tag.account_tag_id = dashboard_to_tag.tag_id AND account_tag.account_id = ?");
        getTagsToDashboardStmt.setLong(1, SecurityUtil.getAccountID());
        ResultSet dashboardTagRS = getTagsToDashboardStmt.executeQuery();
        Map<Long, List<Tag>> dashboardToTagMap = new HashMap<Long, List<Tag>>();
        while (dashboardTagRS.next()) {
            long dataSourceID = dashboardTagRS.getLong(2);
            long tagID = dashboardTagRS.getLong(1);
            Tag tag = tags.get(tagID);
            List<Tag> t = dashboardToTagMap.get(dataSourceID);
            if (t == null) {
                t = new ArrayList<Tag>();
                dashboardToTagMap.put(dataSourceID, t);
            }
            t.add(tag);
        }
        getTagsToDashboardStmt.close();

        for (DashboardDescriptor d : dashboards) {
            d.setTags(dashboardToTagMap.get(d.getId()));
        }

    }

    private void addTagsToReports(List<InsightDescriptor> reports, EIConnection conn, Map<Long, Tag> tags) throws SQLException {
        PreparedStatement getTagsToReportsStmt = conn.prepareStatement("SELECT REPORT_TO_TAG.TAG_ID, REPORT_ID FROM report_to_tag, account_tag WHERE " +
                "account_tag.account_tag_id = report_to_tag.tag_id AND account_tag.account_id = ?");
        getTagsToReportsStmt.setLong(1, SecurityUtil.getAccountID());
        ResultSet reportTagRS = getTagsToReportsStmt.executeQuery();
        Map<Long, List<Tag>> reportToTagMap = new HashMap<Long, List<Tag>>();
        while (reportTagRS.next()) {
            long dataSourceID = reportTagRS.getLong(2);
            long tagID = reportTagRS.getLong(1);
            Tag tag = tags.get(tagID);
            List<Tag> t = reportToTagMap.get(dataSourceID);
            if (t == null) {
                t = new ArrayList<Tag>();
                reportToTagMap.put(dataSourceID, t);
            }
            t.add(tag);
        }
        getTagsToReportsStmt.close();

        for (InsightDescriptor d : reports) {
            d.setTags(reportToTagMap.get(d.getId()));
        }


    }


    public void renameFolder(long folderID, String name) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE REPORT_FOLDER SET FOLDER_NAME = ? WHERE REPORT_FOLDER_ID = ? AND ACCOUNT_ID = ?");
            updateStmt.setString(1, name);
            updateStmt.setLong(2, folderID);
            updateStmt.setLong(3, SecurityUtil.getAccountID());
            updateStmt.executeUpdate();
            updateStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void deleteFolder(long folderID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET FOLDER = ? WHERE FOLDER = ?");
            updateStmt.setLong(1, 1);
            updateStmt.setLong(2, folderID);
            updateStmt.executeUpdate();
            updateStmt.close();
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM REPORT_FOLDER WHERE REPORT_FOLDER_ID = ? AND ACCOUNT_ID = ?");
            deleteStmt.setLong(1, folderID);
            deleteStmt.setLong(2, SecurityUtil.getAccountID());
            deleteStmt.executeUpdate();
            deleteStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<CustomFolder> getCustomFolders(long dataSourceID) {
        List<CustomFolder> folders = new ArrayList<CustomFolder>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement getFoldersStmt = conn.prepareStatement("SELECT REPORT_FOLDER_ID, FOLDER_NAME, DATA_SOURCE_ID FROM REPORT_FOLDER WHERE ACCOUNT_ID = ? AND " +
                    "DATA_SOURCE_ID = ?");
            getFoldersStmt.setLong(1, SecurityUtil.getAccountID());
            getFoldersStmt.setLong(2, dataSourceID);
            ResultSet folderRS = getFoldersStmt.executeQuery();
            while (folderRS.next()) {
                long id = folderRS.getLong(1);
                String name = folderRS.getString(2);
                CustomFolder customFolder = new CustomFolder();
                customFolder.setName(name);
                customFolder.setId(id);
                folders.add(customFolder);
            }
            getFoldersStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return folders;
    }

    public MyDataTree getFeedAnalysisTree(boolean onlyMyData, long groupID) {
        onlyMyData = false;
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long startTime = System.currentTimeMillis();
            boolean testAccountVisible = FeedService.testAccountVisible(conn);
            List<EIDescriptor> objects = new ArrayList<EIDescriptor>();
            List<EIDescriptor> results = new ArrayList<EIDescriptor>();
            List<DataSourceDescriptor> dataSources;
            if (groupID == 0) {
                dataSources = feedStorage.getDataSources(userID, accountID, conn, testAccountVisible);
            } else {
                dataSources = feedStorage.getDataSourcesForGroup(userID, groupID, conn);
            }

            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, TAG_NAME, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ? ORDER BY TAG_INDEX");
            PreparedStatement getTagsToDataSourcesStmt = conn.prepareStatement("SELECT DATA_SOURCE_TO_TAG.ACCOUNT_TAG_ID, DATA_SOURCE_ID FROM data_source_to_tag, account_tag WHERE " +
                    "account_tag.account_tag_id = data_source_to_tag.account_tag_id AND account_tag.account_id = ?");
            PreparedStatement getTagsToReportsStmt = conn.prepareStatement("SELECT REPORT_TO_TAG.TAG_ID, REPORT_ID FROM report_to_tag, account_tag WHERE " +
                    "account_tag.account_tag_id = report_to_tag.tag_id AND account_tag.account_id = ?");
            PreparedStatement getTagsToDashboardStmt = conn.prepareStatement("SELECT DASHBOARD_TO_TAG.TAG_ID, DASHBOARD_ID FROM dashboard_to_tag, account_tag WHERE " +
                    "account_tag.account_tag_id = dashboard_to_tag.tag_id AND account_tag.account_id = ?");
            getTagsStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet tagRS = getTagsStmt.executeQuery();
            Map<Long, Tag> tags = new LinkedHashMap<Long, Tag>();
            while (tagRS.next()) {
                tags.put(tagRS.getLong(1), new Tag(tagRS.getLong(1), tagRS.getString(2), tagRS.getBoolean(3), tagRS.getBoolean(4), tagRS.getBoolean(5)));
            }

            getTagsToDataSourcesStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet dsTagRS = getTagsToDataSourcesStmt.executeQuery();
            Map<Long, List<Tag>> dsToTagMap = new HashMap<Long, List<Tag>>();
            while (dsTagRS.next()) {
                long dataSourceID = dsTagRS.getLong(2);
                long tagID = dsTagRS.getLong(1);
                Tag tag = tags.get(tagID);
                List<Tag> t = dsToTagMap.get(dataSourceID);
                if (t == null) {
                    t = new ArrayList<Tag>();
                    dsToTagMap.put(dataSourceID, t);
                }
                t.add(tag);
            }
            getTagsStmt.close();
            getTagsToDataSourcesStmt.close();

            getTagsToReportsStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet reportTagRS = getTagsToReportsStmt.executeQuery();
            Map<Long, List<Tag>> reportToTagMap = new HashMap<Long, List<Tag>>();
            while (reportTagRS.next()) {
                long dataSourceID = reportTagRS.getLong(2);
                long tagID = reportTagRS.getLong(1);
                Tag tag = tags.get(tagID);
                List<Tag> t = reportToTagMap.get(dataSourceID);
                if (t == null) {
                    t = new ArrayList<Tag>();
                    reportToTagMap.put(dataSourceID, t);
                }
                t.add(tag);
            }
            getTagsToReportsStmt.close();

            getTagsToDashboardStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet dashboardTagRS = getTagsToDashboardStmt.executeQuery();
            Map<Long, List<Tag>> dashboardToTagMap = new HashMap<Long, List<Tag>>();
            while (dashboardTagRS.next()) {
                long dataSourceID = dashboardTagRS.getLong(2);
                long tagID = dashboardTagRS.getLong(1);
                Tag tag = tags.get(tagID);
                List<Tag> t = dashboardToTagMap.get(dataSourceID);
                if (t == null) {
                    t = new ArrayList<Tag>();
                    dashboardToTagMap.put(dataSourceID, t);
                }
                t.add(tag);
            }
            getTagsToDashboardStmt.close();


            Iterator<DataSourceDescriptor> dataSourceIter = dataSources.iterator();
            while (dataSourceIter.hasNext()) {
                DataSourceDescriptor dataSource = dataSourceIter.next();
                if (!keep(dataSource, onlyMyData)) {
                    dataSourceIter.remove();
                } else {
                    dataSource.setTags(dsToTagMap.get(dataSource.getId()));
                }
            }


            AnalysisStorage analysisStorage = new AnalysisStorage();

            if (groupID == 0) {
                List<DashboardDescriptor> dashboards = new DashboardStorage().getDashboards(userID, accountID, conn, testAccountVisible).values();
                for (DashboardDescriptor descriptor : dashboards) {
                    descriptor.setTags(dashboardToTagMap.get(descriptor.getId()));
                }
                objects.addAll(dashboards);
                List<InsightDescriptor> reports = analysisStorage.getReports(userID, accountID, conn, testAccountVisible).values();
                for (InsightDescriptor report : reports) {
                    report.setTags(reportToTagMap.get(report.getId()));
                }
                objects.addAll(reports);
                objects.addAll(new ScorecardInternalService().getScorecards(userID, accountID, conn, testAccountVisible).values());
            } else {
                objects.addAll(analysisStorage.getReportsForGroup(groupID, conn).values());
                objects.addAll(new DashboardStorage().getDashboardForGroup(groupID, conn).values());
                objects.addAll(new ScorecardInternalService().getScorecardsForGroup(groupID, conn).values());
            }

            Iterator<EIDescriptor> iter = objects.iterator();
            while (iter.hasNext()) {
                EIDescriptor descriptor = iter.next();
                if (!keep(descriptor, onlyMyData)) {
                    iter.remove();
                }
            }

            Map<Long, DataSourceDescriptor> descriptorMap = new HashMap<Long, DataSourceDescriptor>();
            for (DataSourceDescriptor dataSource : dataSources) {
                descriptorMap.put(dataSource.getId(), dataSource);
            }

            PreparedStatement getFoldersStmt = conn.prepareStatement("SELECT REPORT_FOLDER_ID, FOLDER_NAME, DATA_SOURCE_ID FROM REPORT_FOLDER WHERE ACCOUNT_ID = ?");
            getFoldersStmt.setLong(1, SecurityUtil.getAccountID());

            ResultSet folderRS = getFoldersStmt.executeQuery();
            while (folderRS.next()) {
                long id = folderRS.getLong(1);
                String name = folderRS.getString(2);
                long dataSourceID = folderRS.getLong(3);
                CustomFolder customFolder = new CustomFolder();
                customFolder.setName(name);
                customFolder.setId(id);
                DataSourceDescriptor dataSourceDescriptor = descriptorMap.get(dataSourceID);
                if (dataSourceDescriptor != null) {
                    dataSourceDescriptor.getCustomFolders().add(customFolder);
                }
            }
            getFoldersStmt.close();

            long folderTime = System.currentTimeMillis();

            if (groupID != 0) {
                int role = SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
                for (EIDescriptor descriptor : objects) {
                    descriptor.setRole(role);
                }
            }

            PreparedStatement stmt = conn.prepareStatement("SELECT DATA_FEED.FEED_NAME, DATA_FEED.FEED_TYPE, FEED_PERSISTENCE_METADATA.LAST_DATA_TIME, refresh_behavior FROM DATA_FEED LEFT JOIN FEED_PERSISTENCE_METADATA ON DATA_FEED.DATA_FEED_ID = FEED_PERSISTENCE_METADATA.FEED_ID WHERE " +
                    "DATA_FEED_ID = ?");
            PreparedStatement findOwnerStmt = conn.prepareStatement("SELECT FIRST_NAME, NAME FROM USER, UPLOAD_POLICY_USERS WHERE UPLOAD_POLICY_USERS.USER_ID = USER.USER_ID AND " +
                    "UPLOAD_POLICY_USERS.FEED_ID = ?");

            DataSourceDescriptor placeholder = new DataSourceDescriptor("Not Tied to a Data Source", 0, 0, false, 0);
            descriptorMap.put(0L, placeholder);
            iter = objects.iterator();
            while (iter.hasNext()) {
                EIDescriptor descriptor = iter.next();
                long dataSourceID = getDataSourceID(descriptor);
                DataSourceDescriptor dataSource = descriptorMap.get(dataSourceID);
                if (dataSource != null) {
                    dataSource.getChildren().add(descriptor);
                } else {
                    if (dataSourceID == 0) {
                        placeholder.getChildren().add(dataSource);
                    } else {
                        stmt.setLong(1, dataSourceID);
                        ResultSet dsRS = stmt.executeQuery();
                        if (dsRS.next()) {
                            findOwnerStmt.setLong(1, dataSourceID);
                            ResultSet ownerRS = findOwnerStmt.executeQuery();
                            String name;
                            if (ownerRS.next()) {
                                String firstName = ownerRS.getString(1);
                                String lastName = ownerRS.getString(2);
                                name = firstName != null ? firstName + " " + lastName : lastName;
                            } else {
                                name = "";
                            }
                            Timestamp lastTime = dsRS.getTimestamp(3);
                            Date lastDataTime = null;
                            if (lastTime != null) {
                                lastDataTime = new Date(lastTime.getTime());
                            }
                            DataSourceDescriptor stub = new DataSourceDescriptor(dsRS.getString(1), dataSourceID, dsRS.getInt(2), false, dsRS.getInt(4));
                            stub.setAuthor(name);
                            stub.setLastDataTime(lastDataTime);
                            dataSources.add(stub);
                            descriptorMap.put(dataSourceID, stub);
                            stub.getChildren().add(descriptor);
                        }
                    }
                }
                iter.remove();
            }

            long dsOwnerTime = System.currentTimeMillis();

            if (!placeholder.getChildren().isEmpty()) {
                dataSources.add(placeholder);
            }

            //results.addAll(objects);
            PreparedStatement analystStmt = conn.prepareStatement("SELECT ANALYST FROM USER WHERE USER_ID = ?");
            analystStmt.setLong(1, SecurityUtil.getUserID());
            ResultSet analystRS = analystStmt.executeQuery();
            analystRS.next();
            boolean analyst = analystRS.getBoolean(1);
            analystStmt.close();
            if (groupID == 0 && analyst) {
                List<LookupTableDescriptor> lookupTables = feedStorage.getLookupTableDescriptors(conn);
                for (LookupTableDescriptor lookupTableDescriptor : lookupTables) {
                    DataSourceDescriptor feedDescriptor = descriptorMap.get(lookupTableDescriptor.getDataSourceID());
                    if (feedDescriptor != null) {
                        lookupTableDescriptor.setRole(feedDescriptor.getRole());
                        feedDescriptor.getChildren().add(lookupTableDescriptor);
                    }
                }
                PreparedStatement filterSetStmt = conn.prepareStatement("SELECT FILTER_SET_ID, FILTER_SET_NAME, FILTER_SET_DESCRIPTION, " +
                        "URL_KEY, DATA_SOURCE_ID FROM FILTER_SET WHERE FILTER_SET.USER_ID = ?");
                filterSetStmt.setLong(1, SecurityUtil.getAccountID());
                ResultSet filterSetRS = filterSetStmt.executeQuery();
                while (filterSetRS.next()) {
                    FilterSetDescriptor filterSetDescriptor = new FilterSetDescriptor();
                    filterSetDescriptor.setId(filterSetRS.getLong(1));
                    filterSetDescriptor.setName(filterSetRS.getString(2));
                    filterSetDescriptor.setDescription(filterSetRS.getString(3));
                    filterSetDescriptor.setUrlKey(filterSetRS.getString(4));
                    filterSetDescriptor.setDataSourceID(filterSetRS.getLong(5));
                    DataSourceDescriptor feedDescriptor = descriptorMap.get(filterSetDescriptor.getDataSourceID());
                    if (feedDescriptor != null) {
                        filterSetDescriptor.setRole(feedDescriptor.getRole());
                        feedDescriptor.getChildren().add(filterSetDescriptor);
                    }
                }
            }
            results.addAll(dataSources);

            long lookupTime = System.currentTimeMillis();

            if (groupID != 0) {
                int role = SecurityUtil.authorizeGroup(groupID, Roles.SUBSCRIBER);
                for (EIDescriptor descriptor : results) {
                    descriptor.setRole(role);
                }
            }

            for (EIDescriptor descriptor : results) {
                if (descriptor.getType() == EIDescriptor.DATA_SOURCE) {
                    DataSourceDescriptor dataSourceDescriptor = (DataSourceDescriptor) descriptor;
                    Collections.sort(dataSourceDescriptor.getChildren(), new Comparator<EIDescriptor>() {

                        public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                            String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName() : "";
                            String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName() : "";
                            return name1.compareToIgnoreCase(name2);
                        }
                    });
                }
            }

            Collections.sort(results, new Comparator<EIDescriptor>() {

                public int compare(EIDescriptor eiDescriptor, EIDescriptor eiDescriptor1) {
                    String name1 = eiDescriptor.getName() != null ? eiDescriptor.getName() : "";
                    String name2 = eiDescriptor1.getName() != null ? eiDescriptor1.getName() : "";
                    return name1.compareToIgnoreCase(name2);
                }
            });

            int dataSourceCount = 0;
            int reportCount = 0;
            int dashboardCount = 0;
            MyDataTree myDataTree = new MyDataTree(results, onlyMyData);
            myDataTree.setDashboardCount(dashboardCount);
            myDataTree.setTags(new ArrayList<Tag>(tags.values()));
            myDataTree.setDataSourceCount(dataSourceCount);
            myDataTree.setReportCount(reportCount);
            conn.commit();
            BenchmarkManager.recordBenchmark("Home Tree", (System.currentTimeMillis() - startTime), SecurityUtil.getUserID());
            return myDataTree;
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(false);
            Database.closeConnection(conn);
        }
    }

    public FeedDefinition getDataFeedConfiguration(long dataFeedID) {
        SecurityUtil.authorizeFeed(dataFeedID, Roles.SUBSCRIBER);
        try {
            return getFeedDefinition(dataFeedID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public static FeedDefinition getFeedDefinition(long dataFeedID) {
        try {
            return feedStorage.getFeedDefinitionData(dataFeedID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateFeedDefinition(FeedDefinition feedDefinition) {
        try {
            SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.OWNER);
            feedStorage.updateDataFeedConfiguration(feedDefinition);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public long addRawUploadData(long userID, String fileName, byte[] rawData) {

        // get size of data on that user
        Connection conn = Database.instance().getConnection();
        try {
            long uploadID;
            PreparedStatement anythingExistingStmt = conn.prepareStatement("SELECT USER_UPLOAD_ID FROM USER_UPLOAD WHERE " +
                    "ACCOUNT_ID = ? AND DATA_NAME = ?");
            anythingExistingStmt.setLong(1, userID);
            anythingExistingStmt.setString(2, fileName);
            ResultSet existingRS = anythingExistingStmt.executeQuery();
            if (existingRS.next()) {
                uploadID = existingRS.getLong(1);
                PreparedStatement updateStmt = conn.prepareStatement("UPDATE USER_UPLOAD SET USER_DATA = ? WHERE " +
                        "USER_UPLOAD_ID = ?");
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                updateStmt.setBinaryStream(1, bais, rawData.length);
                updateStmt.setLong(2, uploadID);
                updateStmt.executeUpdate();
            } else {
                PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO USER_UPLOAD (ACCOUNT_ID, DATA_NAME, " +
                        "USER_DATA) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                insertStmt.setLong(1, userID);
                insertStmt.setString(2, fileName);
                ByteArrayInputStream bais = new ByteArrayInputStream(rawData);
                insertStmt.setBinaryStream(3, bais, rawData.length);
                insertStmt.execute();
                uploadID = Database.instance().getAutoGenKey(insertStmt);
            }
            rawDataMap.put(uploadID, new RawUploadData(userID, fileName, rawData));
            return uploadID;
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public long createNewDefaultFeed(String name) {
        Connection conn = Database.instance().getConnection();
        DataStorage tableDef = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = new FeedDefinition();
            feedDefinition.setFeedName(name);
            feedDefinition.setOwnerName(retrieveUser(conn).getUserName());
            UploadPolicy uploadPolicy = new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID());
            feedDefinition.setUploadPolicy(uploadPolicy);
            feedDefinition.setFields(new ArrayList<AnalysisItem>());
            FeedCreationResult result = new FeedCreation().createFeed(feedDefinition, conn, new DataSet(), uploadPolicy);
            tableDef = result.getTableDefinitionMetadata();
            tableDef.commit();
            conn.commit();
            return result.getFeedID();
        } catch (Throwable e) {
            LogClass.error(e);
            if (tableDef != null) {
                tableDef.rollback();
            }
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LogClass.error(e1);
            }
            throw new RuntimeException(e);
        } finally {
            if (tableDef != null) {
                tableDef.closeConnection();
            }
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                LogClass.error(e);
            }
            Database.closeConnection(conn);
        }
    }

    // three contexts here
    // excel/csv upload
    // google documents
    // unchecked API

    public UploadResponse analyzeUpload(UploadContext uploadContext) {
        UploadResponse uploadResponse;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);

            String validation = uploadContext.validateUpload(conn);

            if (validation != null) {
                uploadResponse = new UploadResponse(validation);
            } else {
                byte[] bytes = null;
                if (uploadContext instanceof FlatFileUploadContext) {
                    AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
                    S3Object object = s3.getObject(new GetObjectRequest("archival1", ((FlatFileUploadContext) uploadContext).getUploadKey() + ".zip"));
                    byte retrieveBuf[];
                    retrieveBuf = new byte[1];
                    InputStream bfis = object.getObjectContent();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    while (bfis.read(retrieveBuf) != -1) {
                        baos.write(retrieveBuf);
                    }
                    byte[] resultBytes = baos.toByteArray();
                    ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes);
                    ZipInputStream zin = new ZipInputStream(bais);
                    zin.getNextEntry();

                    byte[] buffer = new byte[8192];
                    ByteArrayOutputStream fout = new ByteArrayOutputStream();
                    BufferedOutputStream bufOS = new BufferedOutputStream(fout, 8192);
                    int nBytes;
                    while ((nBytes = zin.read(buffer)) != -1) {
                        bufOS.write(buffer, 0, nBytes);
                    }
                    /*for (int c = zin.read(); c != -1; c = zin.read()) {
                        bufOS.write(c);
                    }*/
                    bufOS.close();
                    fout.close();

                    bytes = fout.toByteArray();

                    baos = null;
                    bufOS = null;
                    fout = null;
                }
                List<AnalysisItem> fields = uploadContext.guessFields(conn, bytes);
                List<FieldUploadInfo> fieldInfos = new ArrayList<FieldUploadInfo>();
                for (AnalysisItem field : fields) {
                    FieldUploadInfo fieldUploadInfo = new FieldUploadInfo();
                    fieldUploadInfo.setGuessedItem(field);
                    fieldUploadInfo.setSampleValues(uploadContext.getSampleValues(field.getKey()));
                    fieldInfos.add(fieldUploadInfo);
                }
                uploadResponse = new UploadResponse();
                uploadResponse.setSuccessful(true);
                uploadResponse.setInfos(fieldInfos);
            }
            conn.commit();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return uploadResponse;
    }

    /*public UploadResponse createStubSource(String name, UploadContext uploadContext, List<AnalysisItem> analysisItems, boolean accountVisible) {
        UploadResponse uploadResponse;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            long dataSourceID = uploadContext.createDataSource(name, analysisItems, conn, accountVisible, null);
            PreparedStatement ps1 = conn.prepareStatement("SELECT URL_KEY FROM DATA_FEED WHERE DATA_FEED_ID = ?");
            ps1.setLong(1, dataSourceID);
            ResultSet urlRS = ps1.executeQuery();
            urlRS.next();
            String urlKey = urlRS.getString(1);
            ps1.close();
            conn.commit();
            uploadResponse = new UploadResponse(dataSourceID, urlKey);
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            uploadResponse = new UploadResponse("Something caused an internal error in the processing of the uploaded file.");
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return uploadResponse;
    }*/

    public UploadResponse createDataSource(String name, UploadContext uploadContext, List<AnalysisItem> analysisItems, boolean accountVisible) {
        UploadResponse uploadResponse;
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            byte[] bytes = null;
            if (uploadContext instanceof FlatFileUploadContext) {
                FlatFileUploadContext flatFileUploadContext = (FlatFileUploadContext) uploadContext;
                AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
                S3Object object = s3.getObject(new GetObjectRequest("archival1", flatFileUploadContext.getUploadKey() + ".zip"));

                byte retrieveBuf[];
                retrieveBuf = new byte[1];
                InputStream bfis = object.getObjectContent();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while (bfis.read(retrieveBuf) != -1) {
                    baos.write(retrieveBuf);
                }
                byte[] resultBytes = baos.toByteArray();
                ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes);
                ZipInputStream zin = new ZipInputStream(bais);
                zin.getNextEntry();

                byte[] buffer = new byte[8192];
                ByteArrayOutputStream fout = new ByteArrayOutputStream();
                BufferedOutputStream bufOS = new BufferedOutputStream(fout, 8192);
                int nBytes;
                while ((nBytes = zin.read(buffer)) != -1) {
                    bufOS.write(buffer, 0, nBytes);
                }
                /*for (int c = zin.read(); c != -1; c = zin.read()) {
                    bufOS.write(c);
                }*/
                bufOS.close();
                fout.close();

                bytes = fout.toByteArray();

                baos = null;
                bufOS = null;
                fout = null;
            }
            long dataSourceID = uploadContext.createDataSource(name, analysisItems, conn, accountVisible, bytes);
            uploadResponse = new UploadResponse(dataSourceID);
            conn.commit();
            return uploadResponse;
        } catch (ReportException re) {
            if (re.getReportFault() instanceof StorageLimitFault) {
                conn.rollback();
                uploadResponse = new UploadResponse("You have reached your account storage limit. You need to reduce the size of the data, clean up other data sources on the account, or upgrade to a higher account tier.");
            } else {
                conn.rollback();
                uploadResponse = new UploadResponse(re.getMessage());
            }
        } catch (StorageLimitException se) {
            conn.rollback();
            uploadResponse = new UploadResponse("You have reached your account storage limit.");
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            uploadResponse = new UploadResponse("Something caused an internal error in the processing of the uploaded file.");
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return uploadResponse;
    }

    public void defineAccountReports(List<EIDescriptor> descriptors) {
        SecurityUtil.authorizeAccountAdmin();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM ACCOUNT_TO_REPORT WHERE ACCOUNT_ID = ?");
            clearStmt.setLong(1, SecurityUtil.getAccountID());
            clearStmt.executeUpdate();
            clearStmt.close();
            PreparedStatement clearDashboardStmt = conn.prepareStatement("DELETE FROM ACCOUNT_TO_DASHBOARD WHERE ACCOUNT_ID = ?");
            clearDashboardStmt.setLong(1, SecurityUtil.getAccountID());
            clearDashboardStmt.executeUpdate();
            clearDashboardStmt.close();
            PreparedStatement saveReportStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TO_REPORT (ACCOUNT_ID, REPORT_ID) VALUES (?, ?)");
            PreparedStatement saveDashboardStmt = conn.prepareStatement("INSERT INTO ACCOUNT_TO_DASHBOARD (ACCOUNT_ID, DASHBOARD_ID) VALUES (?, ?)");
            for (EIDescriptor descriptor : descriptors) {
                if (descriptor.getType() == EIDescriptor.REPORT) {
                    saveReportStmt.setLong(1, SecurityUtil.getAccountID());
                    saveReportStmt.setLong(2, descriptor.getId());
                    saveReportStmt.execute();
                } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                    saveDashboardStmt.setLong(1, SecurityUtil.getAccountID());
                    saveDashboardStmt.setLong(2, descriptor.getId());
                    saveDashboardStmt.execute();
                }
            }
            saveReportStmt.close();
            saveDashboardStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void addAccountReport(EIDescriptor descriptor, EIConnection conn) throws SQLException {
        SecurityUtil.authorizeAccountAdmin();
        PreparedStatement ps = null;
        try {
            if (descriptor.getType() == EIDescriptor.REPORT) {
                ps = conn.prepareStatement("INSERT INTO ACCOUNT_TO_REPORT (ACCOUNT_ID, REPORT_ID) VALUES (?, ?)");

            } else if (descriptor.getType() == EIDescriptor.DASHBOARD) {
                ps = conn.prepareStatement("INSERT INTO ACCOUNT_TO_DASHBOARD (ACCOUNT_ID, DASHBOARD_ID) VALUES (?, ?)");
            }
            if (ps == null) return;

            ps.setLong(1, SecurityUtil.getAccountID());
            ps.setLong(2, descriptor.getId());
            ps.execute();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public void deleteAccountReports(List<EIDescriptor> descriptors, EIConnection conn) throws SQLException {
        PreparedStatement deleteDashboard = conn.prepareStatement("DELETE FROM ACCOUNT_TO_DASHBOARD WHERE ACCOUNT_ID = ? AND DASHBOARD_ID = ?");
        PreparedStatement deleteReport = conn.prepareStatement("DELETE FROM ACCOUNT_TO_REPORT WHERE ACCOUNT_ID = ? AND REPORT_ID = ?");
        try {
            for(EIDescriptor descriptor: descriptors) {
                PreparedStatement deleteStmt = null;
                if(descriptor.getType() == EIDescriptor.DASHBOARD) {
                    deleteStmt = deleteDashboard;
                } else if(descriptor.getType() == EIDescriptor.REPORT) {
                    deleteStmt = deleteReport;
                }
                if(deleteStmt == null) return;
                deleteStmt.setLong(1, SecurityUtil.getAccountID());
                deleteStmt.setLong(2, descriptor.getId());
                deleteStmt.execute();
            }
        } finally {
            if(deleteDashboard != null)
                deleteDashboard.close();
            if(deleteReport != null)
                deleteReport.close();
        }
    }


    public List<EIDescriptor> getAccountReports() {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<EIDescriptor> reports = new ArrayList<EIDescriptor>();
            PreparedStatement getReportStmt = conn.prepareStatement("SELECT ANALYSIS.TITLE, ANALYSIS.ANALYSIS_ID, ANALYSIS.DATA_FEED_ID, ANALYSIS.REPORT_TYPE," +
                    "ANALYSIS.URL_KEY, ANALYSIS.DESCRIPTION FROM " +
                    "ANALYSIS, ACCOUNT_TO_REPORT WHERE ACCOUNT_TO_REPORT.ACCOUNT_ID = ? AND ACCOUNT_TO_REPORT.REPORT_ID = ANALYSIS.ANALYSIS_ID");
            getReportStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet reportRS = getReportStmt.executeQuery();
            while (reportRS.next()) {
                String title = reportRS.getString(1);
                long reportID = reportRS.getLong(2);
                long dataSourceID = reportRS.getLong(3);
                int reportType = reportRS.getInt(4);
                String urlKey = reportRS.getString(5);
                try {
                    InsightDescriptor id = new InsightDescriptor(reportID, title, dataSourceID, reportType, urlKey, Roles.OWNER, true);
                    id.setDescription(reportRS.getString(6));
                    reports.add(id);
                } catch (com.easyinsight.security.SecurityException e) {
                    // ignore
                }
            }
            getReportStmt.close();
            PreparedStatement getDashboardStmt = conn.prepareStatement("SELECT DASHBOARD.DASHBOARD_NAME, DASHBOARD.DASHBOARD_ID, DASHBOARD.DATA_SOURCE_ID, " +
                    "DASHBOARD.URL_KEY, DASHBOARD.DESCRIPTION FROM " +
                    "DASHBOARD, ACCOUNT_TO_DASHBOARD WHERE ACCOUNT_TO_DASHBOARD.ACCOUNT_ID = ? AND ACCOUNT_TO_DASHBOARD.DASHBOARD_ID = DASHBOARD.DASHBOARD_ID");
            getDashboardStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet dashboardRS = getDashboardStmt.executeQuery();
            while (dashboardRS.next()) {
                String title = dashboardRS.getString(1);
                long reportID = dashboardRS.getLong(2);
                long dataSourceID = dashboardRS.getLong(3);
                String urlKey = dashboardRS.getString(4);
                try {
                    SecurityUtil.authorizeDashboard(reportID);
                    DashboardDescriptor dd = new DashboardDescriptor(title, reportID, urlKey, dataSourceID, Roles.OWNER, "", true);
                    dd.setDescription(dashboardRS.getString(5));
                    reports.add(dd);
                } catch (com.easyinsight.security.SecurityException e) {
                    // ignore
                }
            }
            getDashboardStmt.close();
            return reports;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static RawUploadData retrieveRawData(long uploadID) {
        Connection conn = Database.instance().getConnection();
        RawUploadData result = null;
        try {
            conn.setAutoCommit(false);
            result = retrieveRawData(uploadID, conn);
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return result;
    }

    public static RawUploadData retrieveRawData(long uploadID, Connection conn) throws SQLException {
        RawUploadData rawUploadData = rawDataMap.get(uploadID);
        if (rawUploadData == null) {
            PreparedStatement rawDataStmt = conn.prepareStatement("SELECT ACCOUNT_ID, DATA_NAME, USER_DATA FROM " +
                    "USER_UPLOAD WHERE USER_UPLOAD_ID = ?");
            rawDataStmt.setLong(1, uploadID);
            ResultSet dataRS = rawDataStmt.executeQuery();
            if (dataRS.next()) {
                long accountID = dataRS.getLong(1);
                String dataName = dataRS.getString(2);
                byte[] userData = dataRS.getBytes(3);
                rawUploadData = new RawUploadData(accountID, dataName, userData);
            } else {
                throw new RuntimeException("Couldn't find upload info");
            }

        }
        return rawUploadData;
    }

    public void checkTag(Tag t) {
        Database.useConnection(false, (conn) -> {
            checkTag(t, conn);
        });
    }

    public void checkTag(Tag t, EIConnection conn) throws SQLException {
        PreparedStatement checkStatement = conn.prepareStatement("SELECT COUNT(*) FROM data_source_to_tag WHERE account_tag_id = ?");
        checkStatement.setLong(1, t.getId());
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            long count = rs.getLong(1);
            if (count == 0) {
                PreparedStatement typeStatement = conn.prepareStatement("DELETE FROM ACCOUNT_TAG WHERE account_tag_id = ? AND REPORT_TAG = ? AND FIELD_TAG = ?");
                typeStatement.setLong(1, t.getId());
                typeStatement.setBoolean(2, false);
                typeStatement.setBoolean(3, false);
                typeStatement.execute();
                PreparedStatement removeStatement = conn.prepareStatement("UPDATE ACCOUNT_TAG SET DATA_SOURCE_TAG = ? WHERE ACCOUNT_TAG_ID = ?");
                removeStatement.setBoolean(1, false);
                removeStatement.setLong(2, t.getId());
                removeStatement.execute();
            }
        }
    }

    public void checkReportsTag(Tag t) {
        Database.useConnection(false, (conn) -> {
            checkReportsTag(t, conn);
        });
    }

    public void checkReportsTag(Tag t, EIConnection conn) throws SQLException {
        PreparedStatement checkStatement = conn.prepareStatement("SELECT COUNT(*) FROM report_to_tag WHERE tag_id = ?");
        checkStatement.setLong(1, t.getId());
        ResultSet rs = checkStatement.executeQuery();
        if (rs.next()) {
            long count = rs.getLong(1);
            if (count == 0) {
                PreparedStatement typeStatement = conn.prepareStatement("DELETE FROM ACCOUNT_TAG WHERE account_tag_id = ? AND DATA_SOURCE_TAG = ? AND FIELD_TAG = ?");
                typeStatement.setLong(1, t.getId());
                typeStatement.setBoolean(2, false);
                typeStatement.setBoolean(3, false);
                typeStatement.execute();
                PreparedStatement removeStatement = conn.prepareStatement("UPDATE ACCOUNT_TAG SET report_tag = ? WHERE ACCOUNT_TAG_ID = ?");
                removeStatement.setBoolean(1, false);
                removeStatement.setLong(2, t.getId());
                removeStatement.execute();
            }
        }
    }

    public static class RawUploadData {

        private RawUploadData(long accountID, String dataName, byte[] userData) {
            this.accountID = accountID;
            this.dataName = dataName;
            this.userData = userData;
        }

        public byte[] getUserData() {
            return userData;
        }

        public void setUserData(byte[] userData) {
            this.userData = userData;
        }

        public String getDataName() {
            return dataName;
        }

        public void setDataName(String dataName) {
            this.dataName = dataName;
        }

        public long getAccountID() {
            return accountID;
        }

        public void setAccountID(long accountID) {
            this.accountID = accountID;
        }

        long accountID;
        String dataName;
        byte[] userData;
    }

    public void deleteUserUploads(List<DataSourceDescriptor> dataSources) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            for (DataSourceDescriptor dataSource : dataSources) {
                deleteUserUpload(dataSource.getId(), conn);
            }
            conn.commit();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void deleteUserUpload(long dataFeedID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            deleteUserUpload(dataFeedID, conn);
            conn.commit();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private void deleteUserUpload(long dataFeedID, EIConnection conn) throws SQLException {
        int role = SecurityUtil.getUserRoleToFeed(dataFeedID);
        if (role == Roles.OWNER) {
            LogClass.info("USER " + SecurityUtil.getUserID() + " DELETING DATA SOURCE " + dataFeedID);
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(dataFeedID, conn);
            feedDefinition.setVisible(false);
            try {
                feedDefinition.delete(conn);
            } catch (HibernateException e) {
                LogClass.error(e);
                PreparedStatement manualDeleteStmt = conn.prepareStatement("DELETE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                manualDeleteStmt.setLong(1, dataFeedID);
                manualDeleteStmt.executeUpdate();
            }
        } else if (role == Roles.SUBSCRIBER) {
        } else {
            throw new SecurityException();
        }
    }

    public long createAnalysisBasedFeed(AnalysisBasedFeedDefinition definition) {
        long userID = SecurityUtil.getUserID();
        SecurityUtil.authorizeReport(definition.getReportID(), Roles.OWNER);
        try {
            long feedID = feedStorage.addFeedDefinitionData(definition);
            new UserUploadInternalService().createUserFeedLink(userID, feedID, Roles.OWNER);
            return feedID;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void subscribe(long dataFeedID) {
        SecurityUtil.authorizeFeedAccess(dataFeedID);
        long userID = SecurityUtil.getUserID();
        try {
            new UserUploadInternalService().createUserFeedLink(userID, dataFeedID, Roles.SUBSCRIBER);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public int getRole(long userID, long feedID) {
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement existingLinkQuery = conn.prepareStatement("SELECT USER_ROLE FROM USER_TO_FEED WHERE " +
                    "USER_ID = ? AND DATA_FEED_ID = ?");
            existingLinkQuery.setLong(1, userID);
            existingLinkQuery.setLong(2, feedID);
            ResultSet rs = existingLinkQuery.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new RuntimeException();
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void deleteUserFeedLink(long accountID, long feedID, Connection conn) throws SQLException {
        PreparedStatement existingLinkQuery = conn.prepareStatement("DELETE FROM UPLOAD_POLICY_USERS WHERE " +
                "USER_ID = ? AND FEED_ID = ?");
        existingLinkQuery.setLong(1, accountID);
        existingLinkQuery.setLong(2, feedID);
        existingLinkQuery.executeUpdate();
    }

    public CredentialsResponse refreshData(long feedID) {
        return refreshData(feedID, null);
    }

    public CredentialsResponse refreshData(long feedID, final Map<String, Object> refreshProperties) {
        SecurityUtil.authorizeFeed(feedID, Roles.SUBSCRIBER);
        try {
            CredentialsResponse credentialsResponse;
            //final IServerDataSourceDefinition dataSource = (IServerDataSourceDefinition)
            /*if (SecurityUtil.getAccountTier() < dataSource.getRequiredAccountTier()) {
                return new CredentialsResponse(false, "Your account level is no longer valid for this data source connection.", feedID);
            }*/
            final FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID);
            EIConnection timeConn = Database.instance().getConnection();
            int avgTime;
            try {
                PreparedStatement avgElapsed = timeConn.prepareStatement("SELECT AVG(ELAPSED) FROM DATA_SOURCE_REFRESH_AUDIT WHERE DATA_SOURCE_ID = ? AND REFRESH_DATE >= ?");
                avgElapsed.setLong(1, feedID);
                long oneMonth = 1000L * 60 * 60 * 24 * 30;
                long time = System.currentTimeMillis() - oneMonth;
                avgElapsed.setTimestamp(2, new Timestamp(time));
                ResultSet rs = avgElapsed.executeQuery();
                rs.next();
                avgTime = rs.getInt(1);
                avgElapsed.close();
            } finally {
                Database.closeConnection(timeConn);
            }
            if ((feedDefinition.getDataSourceType() != DataSourceInfo.LIVE)) {
                if (DataSourceMutex.mutex().lock(feedDefinition.getDataFeedID())) {
                    final String callID = ServiceUtil.instance().longRunningCall(feedDefinition.getDataFeedID());
                    credentialsResponse = new CredentialsResponse(true, feedDefinition.getDataFeedID());
                    credentialsResponse.setCallDataID(callID);
                    credentialsResponse.setEstimatedDuration(avgTime);
                    final String userName = SecurityUtil.getUserName();
                    final long userID = SecurityUtil.getUserID();
                    final long accountID = SecurityUtil.getAccountID();
                    final int accountType = SecurityUtil.getAccountTier();
                    final boolean accountAdmin = SecurityUtil.isAccountAdmin();
                    final int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
                    final String personaName = SecurityUtil.getPersonaName();
                    DataSourceThreadPool.instance().addActivity(new Runnable() {

                        public void run() {
                            SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin, firstDayOfWeek, personaName);
                            EIConnection conn = Database.instance().getConnection();
                            try {
                                List<ReportFault> warnings = new ArrayList<ReportFault>();
                                conn.setAutoCommit(false);
                                Date now = new Date();
                                List<FeedDefinition> sourcesToRefresh = new ArrayList<FeedDefinition>();
                                if (feedDefinition.getFeedType().getType() == FeedType.COMPOSITE.getType()) {
                                    CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) feedDefinition;
                                    for (CompositeFeedNode node : compositeFeedDefinition.getCompositeFeedNodes()) {
                                        FeedDefinition child = feedStorage.getFeedDefinitionData(node.getDataFeedID(), conn);
                                        sourcesToRefresh.add(child);
                                    }
                                } else {
                                    sourcesToRefresh.add(feedDefinition);
                                }

                                boolean changed = false;
                                for (FeedDefinition sourceToRefresh : sourcesToRefresh) {
                                    if (sourceToRefresh instanceof IServerDataSourceDefinition && (sourceToRefresh.getDataSourceType() == DataSourceInfo.STORED_PULL ||
                                            sourceToRefresh.getDataSourceType() == DataSourceInfo.COMPOSITE_PULL)) {
                                        IServerDataSourceDefinition refreshable = (IServerDataSourceDefinition) sourceToRefresh;
                                        DataSourceRefreshEvent info = new DataSourceRefreshEvent();
                                        info.setDataSourceName("Synchronizing with " + refreshable.getFeedName());
                                        ServiceUtil.instance().updateStatus(callID, ServiceUtil.RUNNING, info);
                                        changed = changed || new DataSourceFactory().createSource(conn, warnings, now, sourceToRefresh, refreshable, callID, refreshProperties).invoke();
                                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO DATA_SOURCE_REFRESH_LOG (REFRESH_TIME, DATA_SOURCE_ID) VALUES (?, ?)");
                                        stmt.setTimestamp(1, new Timestamp(now.getTime()));
                                        stmt.setLong(2, sourceToRefresh.getDataFeedID());
                                        stmt.execute();
                                        stmt.close();
                                    }
                                }

                                ReportFault warning = null;
                                if (!warnings.isEmpty()) {
                                    warning = warnings.get(warnings.size() - 1);
                                }
                                DataSourceRefreshResult result = new DataSourceRefreshResult();
                                result.setDate(now);
                                result.setWarning(warning);
                                result.setNewFields(sourcesToRefresh.size() == 1 && sourcesToRefresh.get(0).rebuildFieldWindow() && changed);
                                ServiceUtil.instance().updateStatus(callID, ServiceUtil.DONE, result);

                                conn.commit();
                            } catch (ReportException re) {
                                if (!conn.getAutoCommit()) {
                                    conn.rollback();
                                }
                                ServiceUtil.instance().updateStatus(callID, ServiceUtil.FAILED, re.getReportFault());
                            } catch (Exception e) {
                                LogClass.error(e);
                                if (!conn.getAutoCommit()) {
                                    conn.rollback();
                                }
                                ServiceUtil.instance().updateStatus(callID, ServiceUtil.FAILED, e.getMessage());
                            } finally {
                                conn.setAutoCommit(true);
                                Database.closeConnection(conn);
                                DataSourceMutex.mutex().unlock(feedDefinition.getDataFeedID());
                                SecurityUtil.clearThreadLocal();
                            }
                        }
                    });
                } else {
                    credentialsResponse = new CredentialsResponse(true, feedDefinition.getDataFeedID());
                    credentialsResponse.setEstimatedDuration(avgTime);
                }
            } else {
                if (feedDefinition instanceof ServerDataSourceDefinition) {
                    ServerDataSourceDefinition serverDataSourceDefinition = (ServerDataSourceDefinition) feedDefinition;
                    EIConnection conn = Database.instance().getConnection();
                    try {
                        conn.setAutoCommit(false);
                        serverDataSourceDefinition.migrations(conn, null);
                        feedDefinition.setVisible(true);
                        feedStorage.updateDataFeedConfiguration(feedDefinition, conn);
                        conn.commit();
                    } catch (Exception e) {
                        LogClass.error(e);
                        conn.rollback();
                        throw new RuntimeException(e);
                    } finally {
                        conn.setAutoCommit(true);
                        Database.closeConnection(conn);
                    }
                }

                credentialsResponse = new CredentialsResponse(true, feedID);
                credentialsResponse.setEstimatedDuration(avgTime);
            }
            return credentialsResponse;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public AnalyzeUploadResponse analyzeUpdate(long feedID, String uploadKey) {
        SecurityUtil.authorizeFeed(feedID, Roles.SUBSCRIBER);
        EIConnection conn = Database.instance().getConnection();
        AnalyzeUploadResponse analyzeUploadResponse = new AnalyzeUploadResponse();
        try {
            conn.setAutoCommit(false);
            FileBasedFeedDefinition dataSource = (FileBasedFeedDefinition) feedStorage.getFeedDefinitionData(feedID, conn);
            AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
            S3Object object = s3.getObject(new GetObjectRequest("archival1", uploadKey + ".zip"));

            byte retrieveBuf[];
            retrieveBuf = new byte[1];
            InputStream bfis = object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (bfis.read(retrieveBuf) != -1) {
                baos.write(retrieveBuf);
            }
            byte[] resultBytes = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes);
            ZipInputStream zin = new ZipInputStream(bais);
            zin.getNextEntry();

            byte[] buffer = new byte[8192];
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            BufferedOutputStream bufOS = new BufferedOutputStream(fout, 8192);
            int nBytes;
            while ((nBytes = zin.read(buffer)) != -1) {
                bufOS.write(buffer, 0, nBytes);
            }
            /*for (int c = zin.read(); c != -1; c = zin.read()) {
                bufOS.write(c);
            }*/
            bufOS.close();
            fout.close();

            byte[] bytes = fout.toByteArray();

            baos = null;
            bufOS = null;
            fout = null;

            System.out.println(SecurityUtil.getUserID() + " uploaded " + bytes.length + " bytes with key " + uploadKey + " for update of data source " + feedID + ".");

            UserUploadAnalysis analysis = dataSource.getUploadFormat().analyze(bytes);
            Map<String, AnalysisItem> fieldMap = new HashMap<String, AnalysisItem>();
            for (AnalysisItem field : dataSource.getFields()) {
                fieldMap.put(field.getKey().toBaseKey().toKeyString(), field);
            }
            Collection<FieldUploadInfo> fieldInfos = new ArrayList<FieldUploadInfo>();
            for (AnalysisItem field : analysis.getFields()) {
                if (!fieldMap.containsKey(field.getKey().toBaseKey().toKeyString())) {
                    FieldUploadInfo fieldUploadInfo = new FieldUploadInfo();
                    fieldUploadInfo.setGuessedItem(field);
                    fieldUploadInfo.setSampleValues(new ArrayList<String>(analysis.getSampleMap().get(field.getKey())));
                    fieldInfos.add(fieldUploadInfo);
                }
            }
            conn.commit();
            analyzeUploadResponse.setFieldUploadInfos(fieldInfos);
            return analyzeUploadResponse;
        } catch (UploadException e) {
            conn.rollback();
            analyzeUploadResponse.setError(e.getMessage());
            return analyzeUploadResponse;
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            analyzeUploadResponse.setError(e.getMessage());
            return analyzeUploadResponse;
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public String startUpload() {
        EIConnection conn = Database.instance().getConnection();
        try {
            String key = RandomTextGenerator.generateText(80);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO UPLOAD_BYTES (UPLOAD_TIME, UPLOAD_KEY, USER_ID) VALUES (?, ?, ?)");
            stmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            stmt.setString(2, key);
            stmt.setLong(3, SecurityUtil.getUserID());
            stmt.execute();
            stmt.close();
            return key;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateData(long feedID, String uploadKey, boolean update, List<AnalysisItem> newFields) {
        SecurityUtil.authorizeFeed(feedID, Roles.SUBSCRIBER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            System.out.println(Runtime.getRuntime().freeMemory() + " - " + Runtime.getRuntime().totalMemory());
            FileBasedFeedDefinition dataSource = (FileBasedFeedDefinition) feedStorage.getFeedDefinitionData(feedID, conn);

            AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials("0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI"));
            S3Object object = s3.getObject(new GetObjectRequest("archival1", uploadKey + ".zip"));

            byte retrieveBuf[];
            retrieveBuf = new byte[1];
            InputStream bfis = object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (bfis.read(retrieveBuf) != -1) {
                baos.write(retrieveBuf);
            }
            byte[] resultBytes = baos.toByteArray();
            baos = null;
            ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes);
            ZipInputStream zin = new ZipInputStream(bais);
            zin.getNextEntry();

            byte[] buffer = new byte[8192];
            ByteArrayOutputStream fout = new ByteArrayOutputStream();
            BufferedOutputStream bufOS = new BufferedOutputStream(fout, 8192);
            int nBytes;
            while ((nBytes = zin.read(buffer)) != -1) {
                bufOS.write(buffer, 0, nBytes);
            }
            /*for (int c = zin.read(); c != -1; c = zin.read()) {
                bufOS.write(c);
            }*/
            bufOS.close();
            fout.close();

            byte[] bytes = fout.toByteArray();

            bufOS = null;
            fout = null;

            System.out.println(SecurityUtil.getUserID() + " uploaded " + bytes.length + " bytes with key " + uploadKey + " for update of data source " + feedID + ".");
            if (dataSource.getUploadFormat() instanceof CsvFileUploadFormat) {
                FileProcessOptimizedUpdateScheduledTask task = new FileProcessOptimizedUpdateScheduledTask();
                task.setFeedID(feedID);
                task.setNewFields(newFields);
                task.setUpdate(update);
                task.setUserID(SecurityUtil.getUserID());
                task.setAccountID(SecurityUtil.getAccountID());
                task.updateData(feedID, update, conn, bytes);
            } else {
                FileProcessUpdateScheduledTask task = new FileProcessUpdateScheduledTask();
                task.setFeedID(feedID);
                task.setNewFields(newFields);
                task.setUpdate(update);
                task.setUserID(SecurityUtil.getUserID());
                task.setAccountID(SecurityUtil.getAccountID());
                task.updateData(feedID, update, conn, bytes);
            }
            conn.commit();
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void updateData(long feedID, String uploadKey, boolean update) {
        updateData(feedID, uploadKey, update, null);
    }

    public long uploadPNG(byte[] bytes) {
        try {
            Long userID = SecurityUtil.getUserID(false);
            if (userID == 0) {
                userID = null;
            }
            return new PNGExportOperation().write(bytes, userID);
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CredentialsResponse validateCredentials(FeedDefinition feedDefinition) {
        try {
            String failureMessage = feedDefinition.validateCredentials();
            CredentialsResponse credentialsResponse;
            if (failureMessage == null) {
                credentialsResponse = new CredentialsResponse(true, feedDefinition.getDataFeedID());
            } else {
                credentialsResponse = new CredentialsResponse(false, failureMessage, feedDefinition.getDataFeedID());
            }
            return credentialsResponse;
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault());
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }


    public long newExternalDataSource(FeedDefinition feedDefinition) {
        if (SecurityUtil.getAccountTier() < feedDefinition.getRequiredAccountTier()) {
            throw new RuntimeException("You are not allowed to create data sources of this type with your account.");
        }
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            IServerDataSourceDefinition serverDataSourceDefinition = (IServerDataSourceDefinition) feedDefinition;
            long id = serverDataSourceDefinition.create(conn, null, null);
            conn.commit();
            return id;
        } catch (Throwable e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public static User retrieveUser(Connection conn) {
        long userID = SecurityUtil.getUserID();
        return retrieveUser(conn, userID);
    }

    public static User retrieveUser(Connection conn, long userID) {
        try {
            User user = null;
            Session session = Database.instance().createSession(conn);
            List results;
            try {
                session.beginTransaction();
                results = session.createQuery("from User where userID = ?").setLong(0, userID).list();
                session.getTransaction().commit();
            } catch (Throwable e) {
                session.getTransaction().rollback();
                throw new RuntimeException(e);
            } finally {
                session.close();
            }
            if (results.size() > 0) {
                user = (User) results.get(0);
            }
            return user;
        } catch (Throwable e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Credentials encryptCredentials(Credentials creds) {
        Credentials c = new Credentials();
        c.setUserName(PasswordStorage.encryptString(creds.getUserName() + ":" + SecurityUtil.getUserName()));
        c.setPassword(PasswordStorage.encryptString(creds.getPassword() + ":" + SecurityUtil.getUserName()));
        c.setEncrypted(true);
        return c;
    }

    private Credentials decryptCredentials(Credentials creds) throws MalformedCredentialsException {
        Credentials c = new Credentials();
        String s = PasswordStorage.decryptString(creds.getUserName());
        int i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if (i == -1) {
            throw new MalformedCredentialsException();
        }
        c.setUserName(s.substring(0, i));
        s = PasswordStorage.decryptString(creds.getPassword());
        i = s.lastIndexOf(":" + SecurityUtil.getUserName());
        if (i == -1)
            throw new MalformedCredentialsException();
        c.setPassword(s.substring(0, i));
        return c;
    }

    public Collection<BasecampNextAccount> getBasecampAccounts(BasecampNextCompositeSource dataSource) {
        try {
            return dataSource.getBasecampAccounts();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public Collection<BasecampNextAccount> getSmartsheetTables(SmartsheetTableSource dataSource) {
        try {
            return dataSource.getTables();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CredentialsResponse completeInstallation(final FeedDefinition dataSource) {
        return completeInstallation(dataSource, null);
    }

    public CredentialsResponse completeInstallation(final FeedDefinition dataSource, final FeedDefinition withParent) {
        SecurityUtil.authorizeFeed(dataSource.getDataFeedID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            final IServerDataSourceDefinition serverDataSourceDefinition = (IServerDataSourceDefinition) dataSource;
            serverDataSourceDefinition.create(conn, null, withParent);
            CredentialsResponse credentialsResponse = null;
            if (SecurityUtil.getAccountTier() < dataSource.getRequiredAccountTier()) {
                return new CredentialsResponse(false, "Your account level is no longer valid for this data source connection.", dataSource.getDataFeedID());
            }
            if ((dataSource.getDataSourceType() != DataSourceInfo.LIVE)) {
                if (DataSourceMutex.mutex().lock(dataSource.getDataFeedID())) {

                } else {
                    credentialsResponse = new CredentialsResponse(true, dataSource.getDataFeedID());
                }
            } else {
                dataSource.setVisible(true);
                feedStorage.updateDataFeedConfiguration(dataSource, conn);
                credentialsResponse = new CredentialsResponse(true, dataSource.getDataFeedID());
            }
            conn.commit();
            if (credentialsResponse == null) {
                final String callID = ServiceUtil.instance().longRunningCall(dataSource.getDataFeedID());
                credentialsResponse = new CredentialsResponse(true, dataSource.getDataFeedID());
                credentialsResponse.setCallDataID(callID);
                final String userName = SecurityUtil.getUserName();
                final long userID = SecurityUtil.getUserID();
                final long accountID = SecurityUtil.getAccountID();
                final int accountType = SecurityUtil.getAccountTier();
                final boolean accountAdmin = SecurityUtil.isAccountAdmin();
                final int firstDayOfWeek = SecurityUtil.getFirstDayOfWeek();
                final String personaName = SecurityUtil.getPersonaName();
                DataSourceThreadPool.instance().addActivity(new Runnable() {

                    public void run() {
                        SecurityUtil.populateThreadLocal(userName, userID, accountID, accountType, accountAdmin, firstDayOfWeek, personaName);
                        EIConnection conn = Database.instance().getConnection();
                        try {
                            conn.setAutoCommit(false);
                            Date now = new Date();
                            boolean changed = new DataSourceFactory().createSource(conn, new ArrayList<>(), now, dataSource, serverDataSourceDefinition, callID, null).invoke();
                            conn.commit();
                            DataSourceRefreshResult result = new DataSourceRefreshResult();
                            result.setDate(now);
                            result.setNewFields(changed && dataSource.rebuildFieldWindow());
                            ServiceUtil.instance().updateStatus(callID, ServiceUtil.DONE, result);
                        } catch (ReportException re) {
                            if (!conn.getAutoCommit()) {
                                conn.rollback();
                            }
                            ServiceUtil.instance().updateStatus(callID, ServiceUtil.FAILED, re.getReportFault());
                        } catch (Exception e) {
                            LogClass.error(e);
                            if (!conn.getAutoCommit()) {
                                conn.rollback();
                            }
                            ServiceUtil.instance().updateStatus(callID, ServiceUtil.FAILED, e.getMessage());
                        } finally {
                            conn.setAutoCommit(true);
                            Database.closeConnection(conn);
                            DataSourceMutex.mutex().unlock(dataSource.getDataFeedID());
                            SecurityUtil.clearThreadLocal();
                        }
                    }
                });
            }
            return credentialsResponse;
        } catch (ReportException re) {
            return new CredentialsResponse(false, re.getReportFault().toString(), dataSource.getDataFeedID());
        } catch (Exception e) {
            LogClass.error(e);
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public boolean flatFileUpload(long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ENDPOINT FROM FILE_BASED_DATA_SOURCE WHERE DATA_SOURCE_ID = ?");
            queryStmt.setLong(1, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                String endpoint = rs.getString(1);
                if (endpoint == null || "".equals(endpoint.trim())) {
                    return true;
                }
            } else {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public static class DataSourceFactory {
        public IUploadDataSource createSource(EIConnection conn, List<ReportFault> warnings, Date now, FeedDefinition sourceToRefresh, IServerDataSourceDefinition refreshable, String callID,
                                              Map<String, Object> properties) {
            if (sourceToRefresh.getFeedType().getType() == FeedType.SERVER_MYSQL.getType() ||
                    sourceToRefresh.getFeedType().getType() == FeedType.SERVER_SQL_SERVER.getType() ||
                    sourceToRefresh.getFeedType().getType() == FeedType.ORACLE.getType() ||
                    sourceToRefresh.getFeedType().getType() == FeedType.SERVER_POSTGRES.getType()) {
                return new SQSUploadDataSource(sourceToRefresh.getDataFeedID(), (ServerDatabaseConnection) sourceToRefresh);
            } else {
                return new UploadDataSource(conn, warnings, now, sourceToRefresh, refreshable, callID, properties);
            }
        }
    }

    public static interface IUploadDataSource {
        public boolean invoke() throws Exception;
    }

    public static class SQSUploadDataSource implements IUploadDataSource {

        private long dataSourceID;
        private ServerDatabaseConnection dataSource;

        private SQSUploadDataSource(long dataSourceID, ServerDatabaseConnection dataSource) {
            this.dataSourceID = dataSourceID;
            this.dataSource = dataSource;
        }

        public boolean invoke() throws Exception {
            MessageQueue msgQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseRequestQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            MessageQueue responseQueue = SQSUtils.connectToQueue(ConfigLoader.instance().getDatabaseResponseQueue(), "0AWCBQ78TJR8QCY8ABG2", "bTUPJqHHeC15+g59BQP8ackadCZj/TsSucNwPwuI");
            msgQueue.sendMessage(dataSourceID + "|" + System.currentTimeMillis());
            boolean responded = false;
            boolean changed = false;
            int i = 0;
            boolean success = false;
            while (!responded && i < (dataSource.getTimeout() * 60)) {
                Message message = responseQueue.receiveMessage();
                if (message == null) {
                    i++;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        // ignore
                    }
                } else {
                    String body = message.getMessageBody();
                    String[] parts = body.split("\\|");
                    long sourceID = Long.parseLong(parts[0]);
                    //long time = Long.parseLong(parts[3]);
                    System.out.println("got response with id = " + sourceID);
                    if (sourceID == dataSourceID) {
                        success = true;
                        responseQueue.deleteMessage(message);
                        boolean successful = Boolean.parseBoolean(parts[1]);
                        if (successful) {
                            changed = Boolean.parseBoolean(parts[2]);
                            System.out.println("matched!");
                            responded = true;
                        } else {
                            String error = parts[2];
                            throw new ReportException(new DataSourceConnectivityReportFault(error, dataSource));
                        }
                    } /*else if (time < (System.currentTimeMillis() - 1000 * 60 * 60)) {
                        System.out.println("Dropping old message");
                        responseQueue.deleteMessage(message);
                    }*/
                }
            }
            if (!success) {
                throw new ReportException(new DataSourceConnectivityReportFault("The connection timed out.", dataSource));
            }
            return changed;
        }
    }

    public static class UploadDataSource implements IUploadDataSource {
        private EIConnection conn;
        private List<ReportFault> warnings;
        private Date now;
        private FeedDefinition sourceToRefresh;
        private IServerDataSourceDefinition refreshable;
        private String callID;
        private Map<String, Object> refreshProperties;

        public UploadDataSource(EIConnection conn, List<ReportFault> warnings, Date now, FeedDefinition sourceToRefresh, IServerDataSourceDefinition refreshable, String callID,
                                Map<String, Object> refreshProperties) {
            this.conn = conn;
            this.warnings = warnings;
            this.now = now;
            this.sourceToRefresh = sourceToRefresh;
            this.refreshable = refreshable;
            this.callID = callID;
            this.refreshProperties = refreshProperties;
        }

        public boolean invoke() throws Exception {
            boolean changed = refreshable.refreshData(SecurityUtil.getAccountID(), new Date(), conn, null, callID, sourceToRefresh.getLastRefreshStart(), false, warnings, refreshProperties);
            sourceToRefresh.setVisible(true);
            sourceToRefresh.setLastRefreshStart(now);
            if (changed) {
                new DataSourceInternalService().updateFeedDefinition(sourceToRefresh, conn, true, true);
            } else {
                feedStorage.updateDataFeedConfiguration(sourceToRefresh, conn);
            }
            return changed;
        }
    }

    public JSONSetup describeJSON(String userName, String password, int httpMethod, String url, String jsonPath, String jsonResultPath, String nextPageLink, int page) {
        SecurityUtil.getUserID();
        try {
            return JSONDataSource.jsonString(userName, password, httpMethod, url, jsonPath, jsonResultPath, nextPageLink, page);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public InfusionsoftReportInfo getInfusionsoftReports(InfusionsoftCompositeSource infusionsoftCompositeSource) {
        SecurityUtil.authorizeFeedAccess(infusionsoftCompositeSource.getDataFeedID());
        try {
            List<InfusionsoftReport> availableReports = infusionsoftCompositeSource.getAvailableReports();
            List<InfusionsoftUser> users = infusionsoftCompositeSource.getUsers();
            return new InfusionsoftReportInfo(availableReports, users);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public void addInfusionsoftReportSource(InfusionsoftCompositeSource infusionsoftCompositeSource, String reportID, String name, String userID) {
        SecurityUtil.authorizeFeedAccess(infusionsoftCompositeSource.getDataFeedID());
        try {
            EIConnection conn = Database.instance().getConnection();
            InfusionsoftReportSource reportSource;
            try {
                reportSource = (InfusionsoftReportSource) new InstallationSystem(conn).installConnection(FeedType.INFUSIONSOFT_REPORT.getType());
            } finally {
                Database.closeConnection(conn);
            }
            reportSource.setParentSourceID(infusionsoftCompositeSource.getDataFeedID());
            reportSource.setReportID(reportID);
            reportSource.setFeedName(name);
            reportSource.setUserID(userID);
            long id = completeInstallation(reportSource, infusionsoftCompositeSource).getDataSourceID();
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(id);
            infusionsoftCompositeSource.getCompositeFeedNodes().add(node);
            new FeedService().updateFeedDefinition(infusionsoftCompositeSource);
        } catch (Exception e) {
            LogClass.error(e);
        }
    }
}
