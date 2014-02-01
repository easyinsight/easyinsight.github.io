package com.easyinsight.datafeeds;

import com.easyinsight.analysis.*;
import com.easyinsight.core.*;
import com.easyinsight.dashboard.DashboardStorage;
import com.easyinsight.datafeeds.basecamp.BaseCampTodoSource;
import com.easyinsight.datafeeds.composite.FederatedDataSource;
import com.easyinsight.datafeeds.composite.FederationSource;
import com.easyinsight.datafeeds.constantcontact.CCContactSource;
import com.easyinsight.datafeeds.database.SchemaResponse;
import com.easyinsight.datafeeds.database.ServerDatabaseConnection;
import com.easyinsight.datafeeds.freshbooks.FreshbooksClientSource;
import com.easyinsight.datafeeds.harvest.HarvestProjectSource;
import com.easyinsight.datafeeds.highrise.HighRiseCompanySource;
import com.easyinsight.datafeeds.highrise.HighRiseContactSource;
import com.easyinsight.datafeeds.highrise.HighRiseDealSource;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.PersistableDataSetForm;
import com.easyinsight.etl.LookupPair;
import com.easyinsight.etl.LookupTable;
import com.easyinsight.etl.LookupTableUtil;
import com.easyinsight.scorecard.ScorecardInternalService;
import com.easyinsight.storage.DatabaseShardException;
import com.easyinsight.tag.Tag;
import com.easyinsight.userupload.AnalysisItemFormatMapper;
import com.easyinsight.userupload.ExcelUploadFormat;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.*;
import com.easyinsight.security.SecurityException;

import java.sql.*;
import java.util.*;

import com.easyinsight.util.RandomTextGenerator;
import org.hibernate.Session;

/**
 * User: jboe
 * Date: Jan 3, 2008
 * Time: 3:30:22 PM
 */
public class FeedService {

    private FeedStorage feedStorage = new FeedStorage();
    private AnalysisStorage analysisStorage = new AnalysisStorage();

    public FeedService() {
        // this goes into a different data provider        
    }

    public List<InsightDescriptor> getDataSourceReports(long dataSourceID) {
        List<InsightDescriptor> reports = new ArrayList<InsightDescriptor>();
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement ps = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE, REPORT_TYPE, URL_KEY, data_source_field_report " +
                    "FROM ANALYSIS WHERE DATA_FEED_ID = ? AND TEMPORARY_REPORT = ?");
            ps.setLong(1, dataSourceID);
            ps.setBoolean(2, false);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long reportID = rs.getLong(1);
                String title = rs.getString(2);
                int reportType = rs.getInt(3);
                String urlKey = rs.getString(4);
                boolean dataSourceReport = rs.getBoolean(5);
                InsightDescriptor insightDescriptor = new InsightDescriptor(reportID, title, dataSourceID, reportType, urlKey, 0, false);
                insightDescriptor.setDataSourceReport(dataSourceReport);
                reports.add(insightDescriptor);
            }
            ps.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return reports;
    }

    public void updateDataSourceReports(long dataSourceID, List<InsightDescriptor> reports, List<InsightDescriptor> removed) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE ANALYSIS SET data_source_field_report = ? WHERE analysis_id = ?");
            for (InsightDescriptor r : reports) {
                if (r.isDataSourceReport()) {
                    updateStmt.setBoolean(1, true);
                    updateStmt.setLong(2, r.getId());
                    updateStmt.executeUpdate();
                }
            }
            for (InsightDescriptor r : removed) {
                updateStmt.setBoolean(1, false);
                updateStmt.setLong(2, r.getId());
                updateStmt.executeUpdate();
            }
            updateStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public FeedDefinition convertDataSource(FeedDefinition dataSource, int toType) {
        SecurityUtil.authorizeFeed(dataSource.getDataFeedID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            feedStorage.updateDataFeedConfiguration(dataSource, conn, toType);
            return feedStorage.getFeedDefinitionData(dataSource.getDataFeedID(), conn, false);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<Tag> getFieldTags() {
        EIConnection conn = Database.instance().getConnection();
        try {
            List<Tag> tags = new ArrayList<Tag>();
            PreparedStatement ps = conn.prepareStatement("SELECT account_tag.account_tag_id, account_tag.tag_name FROM account_tag WHERE " +
                    "account_tag.account_id = ? and account_tag.field_tag = ?");
            ps.setLong(1, SecurityUtil.getAccountID());
            ps.setBoolean(2, true);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long tagID = rs.getLong(1);
                String tagName = rs.getString(2);
                tags.add(new Tag(tagID, tagName, false, false, false));
            }
            ps.close();
            return tags;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<AnalysisItemConfiguration> getAnalysisItemConfigurations(long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            FeedDefinition dataSource = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            List<AnalysisItemConfiguration> configurations = new ArrayList<AnalysisItemConfiguration>();

            // what should these be linked by...

            PreparedStatement ps = conn.prepareStatement("SELECT field_to_tag.account_tag_id, account_tag.tag_name, field_to_tag.display_name FROM " +
                    "field_to_tag, account_tag WHERE data_source_id = ? AND " +
                    "field_to_tag.account_tag_id = account_tag.account_tag_id");
            PreparedStatement loadFormatting = conn.prepareStatement("SELECT report_field_extension_id, display_name FROM analysis_item_to_report_field_extension WHERE data_source_id = ?");

            Map<String, List<Tag>> tagMap = new HashMap<String, List<Tag>>();
            ps.setLong(1, dataSourceID);
            ResultSet tagRS = ps.executeQuery();
            while (tagRS.next()) {
                long tagID = tagRS.getLong(1);
                String tagName = tagRS.getString(2);
                String fieldName = tagRS.getString(3);
                List<Tag> tags = tagMap.get(fieldName);
                if (tags == null) {
                    tags = new ArrayList<Tag>();
                    tagMap.put(fieldName, tags);
                }
                tags.add(new Tag(tagID, tagName, false, false, false));
            }
            Map<String, List<Long>> extensionMap = new HashMap<String, List<Long>>();
            loadFormatting.setLong(1, dataSourceID);
            ResultSet formatRS = loadFormatting.executeQuery();
            while (formatRS.next()) {
                long extensionID = formatRS.getLong(1);
                String name = formatRS.getString(2);
                List<Long> extensions = extensionMap.get(name);
                if (extensions == null) {
                    extensions = new ArrayList<Long>();
                    extensionMap.put(name, extensions);
                }
                extensions.add(extensionID);
            }
            for (AnalysisItem field : dataSource.getFields()) {
                AnalysisItemConfiguration configuration = new AnalysisItemConfiguration();
                configuration.setAnalysisItem(field);
                List<Tag> tags = tagMap.get(field.toDisplay());
                if (tags == null) {
                    tags = new ArrayList<Tag>();
                }
                configuration.setTags(tags);
                List<Long> extensions = extensionMap.get(field.toDisplay());
                if (extensions != null) {
                    for (Long formattingID : extensions) {
                        Session session = Database.instance().createSession(conn);
                        try {
                            ReportFieldExtension extension = (ReportFieldExtension) session.createQuery("from ReportFieldExtension where reportFieldExtensionID = ?").setLong(0, formattingID).list().get(0);
                            extension.afterLoad();
                            if (extension instanceof TextReportFieldExtension) {
                                configuration.setTextExtension((TextReportFieldExtension) extension);
                            } else if (extension instanceof YTDReportFieldExtension) {
                                configuration.setYtdExtension((YTDReportFieldExtension) extension);
                            } else if (extension instanceof VerticalListReportExtension) {
                                configuration.setVerticalListExtension((VerticalListReportExtension) extension);
                            }
                        } finally {
                            session.close();
                        }
                    }
                }
                configurations.add(configuration);
            }
            ps.close();
            loadFormatting.close();
            return configurations;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void updateAnalysisItemConfigurations(List<AnalysisItemConfiguration> configurations, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM field_to_tag WHERE data_source_id = ?");
            PreparedStatement clearExtStmt = conn.prepareStatement("DELETE FROM analysis_item_to_report_field_extension WHERE data_source_id = ?");
            PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO field_to_tag (account_tag_id, display_name, data_source_id) values (?, ?, ?)");
            PreparedStatement saveExtStmt = conn.prepareStatement("INSERT INTO analysis_item_to_report_field_extension (report_field_extension_id, " +
                    "display_name, extension_type, data_source_id) values (?, ?, ?, ?)");
            clearStmt.setLong(1, dataSourceID);
            clearStmt.executeUpdate();
            clearExtStmt.setLong(1, dataSourceID);
            clearExtStmt.executeUpdate();
            for (AnalysisItemConfiguration configuration : configurations) {
                for (Tag tag : configuration.getTags()) {
                    saveStmt.setLong(1, tag.getId());
                    saveStmt.setString(2, configuration.getAnalysisItem().toDisplay());
                    saveStmt.setLong(3, dataSourceID);
                    saveStmt.execute();
                }
                saveExtension(conn, saveExtStmt, configuration, configuration.getTextExtension(), ReportFieldExtension.TEXT, dataSourceID);
                saveExtension(conn, saveExtStmt, configuration, configuration.getYtdExtension(), ReportFieldExtension.YTD, dataSourceID);
            }
            clearStmt.close();
            clearExtStmt.close();
            saveStmt.close();
            saveExtStmt.close();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    private void saveExtension(EIConnection conn, PreparedStatement saveExtStmt, AnalysisItemConfiguration configuration, ReportFieldExtension extension,
                               int type, long dataSourceID) throws SQLException {
        Session session = Database.instance().createSession(conn);
        try {
            if (extension != null) {
                extension.reportSave(session);
                session.saveOrUpdate(extension);
                saveExtStmt.setLong(1, extension.getReportFieldExtensionID());
                saveExtStmt.setString(2, configuration.getAnalysisItem().toDisplay());
                saveExtStmt.setInt(3, type);
                saveExtStmt.setLong(4, dataSourceID);
                saveExtStmt.execute();
            }
            session.flush();
        } finally {
            session.close();
        }
    }

    public void generateTemplate(long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        try {
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID);
            List<AnalysisItem> fields = new ArrayList<AnalysisItem>(dataSource.getFields());
            Collections.sort(fields, new Comparator<AnalysisItem>() {

                public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                    return analysisItem.toDisplay().compareTo(analysisItem1.toDisplay());
                }
            });
            StringBuilder sb = new StringBuilder();
            for (AnalysisItem field : fields) {
                if (field.isConcrete()) {
                    sb.append(field.toDisplay()).append(",");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            System.out.println(sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void importData(byte[] bytes, long dataSourceID) {
        SecurityUtil.authorizeFeedAccess(dataSourceID);
        EIConnection conn = Database.instance().getConnection();
        DataStorage dataStorage = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = feedStorage.getFeedDefinitionData(dataSourceID, conn);
            PersistableDataSetForm data = new ExcelUploadFormat().createDataSet(bytes, dataSource.getFields(), new AnalysisItemFormatMapper(dataSource.getFields()));
            DataSet dataSet = data.toDataSet(null);
            dataStorage = DataStorage.writeConnection(dataSource, conn);
            dataStorage.insertData(dataSet);
            dataStorage.commit();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            if (dataStorage != null) {
                dataStorage.rollback();
            }
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            if (dataStorage != null) {
                dataStorage.closeConnection();
            }
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    private List<FeedDefinition> resolveToDataSource(Key key, EIConnection conn) throws SQLException {
        List<FeedDefinition> dataSources = new ArrayList<FeedDefinition>();
        if (key instanceof NamedKey) {
            //dataSources.add(dataSource);
        } else {
            DerivedKey derivedKey = (DerivedKey) key;
            long parentID = derivedKey.getFeedID();
            FeedDefinition parent = new FeedStorage().getFeedDefinitionData(parentID, conn);
            dataSources.add(parent);
            dataSources.addAll(resolveToDataSource(derivedKey.getParentKey(), conn));
        }
        return dataSources;
    }

    public WSAnalysisDefinition fieldToDataSourceLevel(WSAnalysisDefinition report, AnalysisItem analysisItem, long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            if (report.getAddedItems().contains(analysisItem)) {
                report.getAddedItems().remove(analysisItem);
            }
            AnalysisItem clone = analysisItem.clone();
            dataSource.getFields().add(clone);
            new DataSourceInternalService().updateFeedDefinition(dataSource, conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        if (report.getAnalysisID() > 0) {
            try {
                return new AnalysisService().saveAnalysisDefinition(report);
            } catch (Exception e) {
                LogClass.error(e);
                throw new RuntimeException(e);
            }
        } else {
            return report;
        }
    }

    public void convertToCalculation(AnalysisMeasure analysisItem, long dataSourceID, String calculation, boolean rowLevel, boolean cache) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            AnalysisMeasure analysisCalculation = new AnalysisMeasure();
            analysisCalculation.setKey(analysisItem.getKey().toBaseKey());
            analysisCalculation.setDisplayName(analysisItem.getDisplayName());
            analysisCalculation.setAggregation(AggregationTypes.SUM);
            analysisCalculation.setFilters(analysisItem.getFilters());
            analysisCalculation.setLinks(analysisItem.getLinks());
            analysisCalculation.setFormattingType(analysisItem.getFormattingType());
            analysisCalculation.setUnderline(analysisItem.isUnderline());
            analysisCalculation.setMinPrecision(analysisItem.getMinPrecision());
            analysisCalculation.setPrecision(analysisItem.getPrecision());
            // find all reports with this calculation and replace
            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(dataSourceID, conn);
            List<FeedDefinition> dataSources = new ArrayList<FeedDefinition>();
            dataSources.add(dataSource);
            dataSources.addAll(resolveToDataSource(analysisItem.getKey(), conn));
            FeedDefinition useSource = dataSources.get(dataSources.size() - 1);
            for (FeedDefinition source : dataSources) {
                Iterator<AnalysisItem> iter = source.getFields().iterator();
                while (iter.hasNext()) {
                    AnalysisItem analysisItem1 = iter.next();
                    if (analysisItem.getType() == analysisItem1.getType() && analysisItem.getKey().toBaseKey().toKeyString().equals(analysisItem1.getKey().toBaseKey().toKeyString())) {
                        iter.remove();
                    }
                }
            }
            useSource.getFields().add(analysisCalculation);
            new DataSourceInternalService().updateFeedDefinition(useSource, conn);
            for (FeedDefinition source : dataSources) {
                AnalysisItem replacement = null;
                for (AnalysisItem field : source.getFields()) {
                    if (field.getKey().toBaseKey().toKeyString().equals(analysisItem.getKey().toBaseKey().toKeyString())) {
                        replacement = field;
                        break;
                    }
                }
                List<InsightDescriptor> reports = new AnalysisStorage().getInsightDescriptorsForDataSource(SecurityUtil.getUserID(),
                        SecurityUtil.getAccountID(), source.getDataFeedID(), conn, true);
                Session session = Database.instance().createSession(conn);
                for (InsightDescriptor report : reports) {
                    AnalysisDefinition analysisDefinition = new AnalysisStorage().getPersistableReport(report.getId(), session);
                    Map<String, AnalysisItem> copy = analysisDefinition.getReportStructure();
                    for (Map.Entry<String, AnalysisItem> entry : copy.entrySet()) {
                        if (entry.getValue().equals(analysisItem)) {
                            analysisDefinition.getReportStructure().put(entry.getKey(), replacement);
                        }
                    }
                    analysisStorage.saveAnalysis(analysisDefinition, session);
                }
                session.close();
            }
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

    public DataSourceDescriptor autoCreateCompositeSource(DataSourceDescriptor source, DataSourceDescriptor target) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);

            FeedDefinition sourceObj = feedStorage.getFeedDefinitionData(source.getId(), conn);
            FeedDefinition targetObj = feedStorage.getFeedDefinitionData(target.getId(), conn);

            CompositeFeedNode sourceNode = new CompositeFeedNode(source.getId(), 0, 0, source.getName(), source.getDataSourceType(), source.getDataSourceBehavior());

            if (target.getDataSourceType() == FeedType.COMPOSITE.getType()) {
                CompositeFeedDefinition compositeFeedDefinition = (CompositeFeedDefinition) targetObj;
                compositeFeedDefinition.getCompositeFeedNodes().add(sourceNode);
                for (CompositeFeedNode childNode : compositeFeedDefinition.getCompositeFeedNodes()) {
                    FeedDefinition childObj = feedStorage.getFeedDefinitionData(childNode.getDataFeedID(), conn);
                    Key sourceKey = null;
                    Key targetKey = null;
                    if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && childObj.getFeedType().getType() == FeedType.CONSTANT_CONTACT.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(HighRiseContactSource.CONTACT_WORK_EMAIL).getKey();
                        targetKey = childObj.findAnalysisItem(CCContactSource.CONTACT_EMAIL).getKey();
                    } else if (source.getDataSourceType() == FeedType.CONSTANT_CONTACT.getType() && childObj.getFeedType().getType() == FeedType.HIGHRISE_COMPOSITE.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(CCContactSource.CONTACT_EMAIL).getKey();
                        targetKey = childObj.findAnalysisItem(HighRiseContactSource.CONTACT_WORK_EMAIL).getKey();
                    } else if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && childObj.getDataSourceType() == FeedType.BASECAMP_MASTER.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(HighRiseDealSource.DEAL_NAME).getKey();
                        targetKey = childObj.findAnalysisItem(BaseCampTodoSource.PROJECTNAME).getKey();
                    } else if (source.getDataSourceType() == FeedType.BASECAMP_MASTER.getType() && childObj.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(BaseCampTodoSource.PROJECTNAME).getKey();
                        targetKey = childObj.findAnalysisItem(HighRiseDealSource.DEAL_NAME).getKey();
                    } else if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && childObj.getDataSourceType() == FeedType.FRESHBOOKS_COMPOSITE.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(HighRiseCompanySource.COMPANY_NAME).getKey();
                        targetKey = childObj.findAnalysisItem(FreshbooksClientSource.ORGANIZATION).getKey();
                    } else if (source.getDataSourceType() == FeedType.FRESHBOOKS_COMPOSITE.getType() && childObj.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(FreshbooksClientSource.ORGANIZATION).getKey();
                        targetKey = childObj.findAnalysisItem(HighRiseCompanySource.COMPANY_NAME).getKey();
                    } else if (source.getDataSourceType() == FeedType.HARVEST_COMPOSITE.getType() && childObj.getFeedType().getType() == FeedType.HIGHRISE_COMPOSITE.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(HarvestProjectSource.HIGHRISE_ID).getKey();
                        targetKey = childObj.findAnalysisItem(HighRiseDealSource.DEAL_ID).getKey();
                    } else if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && childObj.getFeedType().getType() == FeedType.HARVEST_COMPOSITE.getType()) {
                        targetKey = childObj.findAnalysisItem(HarvestProjectSource.HIGHRISE_ID).getKey();
                        sourceKey = sourceObj.findAnalysisItem(HighRiseDealSource.DEAL_ID).getKey();
                    } else if (source.getDataSourceType() == FeedType.HARVEST_COMPOSITE.getType() && childObj.getFeedType().getType() == FeedType.BASECAMP_MASTER.getType()) {
                        sourceKey = sourceObj.findAnalysisItem(HarvestProjectSource.BASECAMP_ID).getKey();
                        targetKey = childObj.findAnalysisItem(BaseCampTodoSource.PROJECTID).getKey();
                    } else if (source.getDataSourceType() == FeedType.BASECAMP_MASTER.getType() && childObj.getFeedType().getType() == FeedType.HARVEST_COMPOSITE.getType()) {
                        targetKey = childObj.findAnalysisItem(HarvestProjectSource.BASECAMP_ID).getKey();
                        sourceKey = sourceObj.findAnalysisItem(BaseCampTodoSource.PROJECTID).getKey();
                    }
                    if (sourceKey != null && targetKey != null) {
                        CompositeFeedConnection compositeFeedConnection = new CompositeFeedConnection(source.getId(), childObj.getDataFeedID(), sourceKey, targetKey, sourceObj.getFeedName(),
                            childObj.getFeedName(), false, false, false, false);
                        compositeFeedDefinition.getConnections().add(compositeFeedConnection);
                    }
                }
                new DataSourceInternalService().updateFeedDefinition(compositeFeedDefinition, conn);
                return new DataSourceDescriptor(compositeFeedDefinition.getFeedName(), compositeFeedDefinition.getDataFeedID(),
                        compositeFeedDefinition.getFeedType().getType(), compositeFeedDefinition.isAccountVisible(), compositeFeedDefinition.getDataSourceBehavior());
            } else {
                CompositeFeedNode targetNode = new CompositeFeedNode(target.getId(), 0, 0, target.getName(), target.getDataSourceType(), target.getDataSourceBehavior());
                String dataSourceName;

                Key sourceKey;
                Key targetKey;
                if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && target.getDataSourceType() == FeedType.CONSTANT_CONTACT.getType()) {
                    sourceKey = sourceObj.findAnalysisItem(HighRiseContactSource.CONTACT_WORK_EMAIL).getKey();
                    targetKey = targetObj.findAnalysisItem(CCContactSource.CONTACT_EMAIL).getKey();
                    dataSourceName = "Combined Data Sources";
                } else if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && target.getDataSourceType() == FeedType.BASECAMP_MASTER.getType()) {
                    sourceKey = sourceObj.findAnalysisItem(HighRiseDealSource.DEAL_NAME).getKey();
                    targetKey = targetObj.findAnalysisItem(BaseCampTodoSource.PROJECTNAME).getKey();
                    dataSourceName = "Combined Data Sources";
                } else if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && target.getDataSourceType() == FeedType.FRESHBOOKS_COMPOSITE.getType()) {
                    sourceKey = sourceObj.findAnalysisItem(HighRiseCompanySource.COMPANY_NAME).getKey();
                    targetKey = targetObj.findAnalysisItem(FreshbooksClientSource.ORGANIZATION).getKey();
                    dataSourceName = "Combined Data Sources";
                } else if (source.getDataSourceType() == FeedType.HIGHRISE_COMPOSITE.getType() && target.getDataSourceType() == FeedType.HARVEST_COMPOSITE.getType()) {
                    sourceKey = sourceObj.findAnalysisItem(HighRiseDealSource.DEAL_ID).getKey();
                    targetKey = targetObj.findAnalysisItem(HarvestProjectSource.HIGHRISE_ID).getKey();
                    dataSourceName = "Combined Data Sources";
                } else if (source.getDataSourceType() == FeedType.BASECAMP_MASTER.getType() && target.getDataSourceType() == FeedType.HARVEST_COMPOSITE.getType()) {
                    sourceKey = sourceObj.findAnalysisItem(BaseCampTodoSource.PROJECTID).getKey();
                    targetKey = targetObj.findAnalysisItem(HarvestProjectSource.BASECAMP_ID).getKey();
                    dataSourceName = "Combined Data Sources";
                } else {
                    throw new RuntimeException();
                }
                CompositeFeedConnection compositeFeedConnection = new CompositeFeedConnection(source.getId(), target.getId(), sourceKey, targetKey, sourceObj.getFeedName(),
                        targetObj.getFeedName(), false, false, false, false);
                CompositeFeedDefinition dataSource = createCompositeFeed(Arrays.asList(sourceNode, targetNode), Arrays.asList(compositeFeedConnection), dataSourceName, conn);
                conn.commit();
                return new DataSourceDescriptor(dataSource.getFeedName(), dataSource.getDataFeedID(), dataSource.getFeedType().getType(), false, dataSource.getDataSourceBehavior());
            }
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public static boolean testAccountVisible(EIConnection conn) throws SQLException {
        PreparedStatement accountStmt = conn.prepareStatement("SELECT TEST_ACCOUNT_VISIBLE FROM USER WHERE USER_ID = ?");
        accountStmt.setLong(1, SecurityUtil.getUserID());
        ResultSet rs = accountStmt.executeQuery();
        boolean testAccountVisible = true;
        if (rs.next()) {
            testAccountVisible = rs.getBoolean(1);
        }
        accountStmt.close();
        return testAccountVisible;
    }

    public List<JoinSuggestion> suggestJoins(List<DataSourceDescriptor> scope) {
        List<JoinSuggestion> suggestions = new ArrayList<JoinSuggestion>();
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            boolean testAccountVisible = testAccountVisible(conn);
            List<DataSourceDescriptor> dataSources = feedStorage.getDataSources(userID, accountID, conn, testAccountVisible);
            JoinSuggestion suggestion0 = analyze(FeedType.HIGHRISE_COMPOSITE, FeedType.COMPOSITE, "Highrise", "Existing Cube", dataSources, conn);
            if (suggestion0 != null) {
                suggestions.add(suggestion0);
            }
            JoinSuggestion suggestionA = analyze(FeedType.BASECAMP, FeedType.COMPOSITE, "Highrise", "Existing Cube", dataSources, conn);
            if (suggestionA != null) {
                suggestions.add(suggestionA);
            }
            JoinSuggestion suggestionB = analyze(FeedType.CONSTANT_CONTACT, FeedType.COMPOSITE, "Constant Contact", "Existing Cube", dataSources, conn);
            if (suggestionB != null) {
                suggestions.add(suggestionB);
            }
            JoinSuggestion suggestionC = analyze(FeedType.FRESHBOOKS_COMPOSITE, FeedType.COMPOSITE, "FreshBooks", "Existing Cube", dataSources, conn);
            if (suggestionC != null) {
                suggestions.add(suggestionC);
            }
            JoinSuggestion suggestion1 = analyze(FeedType.HIGHRISE_COMPOSITE, FeedType.CONSTANT_CONTACT, "Highrise", "Constant Contact", dataSources, conn);
            if (suggestion1 != null) {
                suggestions.add(suggestion1);
            }
            JoinSuggestion suggestion2 = analyze(FeedType.HIGHRISE_COMPOSITE, FeedType.BASECAMP_MASTER, "Highrise", "Basecamp", dataSources, conn);
            if (suggestion2 != null) {
                suggestions.add(suggestion2);
            }
            JoinSuggestion suggestion3 = analyze(FeedType.HIGHRISE_COMPOSITE, FeedType.FRESHBOOKS_COMPOSITE, "Highrise", "Freshbooks", dataSources, conn);
            if (suggestion3 != null) {
                suggestions.add(suggestion3);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return suggestions;
    }

    private JoinSuggestion analyze(FeedType type1, FeedType type2, String type1Name, String type2Name, List<DataSourceDescriptor> dataSources, EIConnection conn) throws SQLException {
        List<DataSourceDescriptor> type1DataSources = getDataSources(dataSources, type1);
        List<DataSourceDescriptor> type2DataSources = getDataSources(dataSources, type2);
        if (!type1DataSources.isEmpty() && !type2DataSources.isEmpty()) {
            Map<Long, DataSourceDescriptor> sourceMap = new HashMap<Long, DataSourceDescriptor>();
            Map<Long, DataSourceDescriptor> targetMap = new HashMap<Long, DataSourceDescriptor>();
            PreparedStatement nodeStmt = conn.prepareStatement("SELECT COMPOSITE_NODE.COMPOSITE_FEED_ID FROM COMPOSITE_NODE WHERE " +
                    "DATA_FEED_ID = ?");
            for (DataSourceDescriptor dataSource : type1DataSources) {
                nodeStmt.setLong(1, dataSource.getId());
                ResultSet rs = nodeStmt.executeQuery();
                while (rs.next()) {
                    long id = rs.getLong(1);
                    sourceMap.put(id, dataSource);
                }
            }
            for (DataSourceDescriptor dataSource : type2DataSources) {
                nodeStmt.setLong(1, dataSource.getId());
                ResultSet rs = nodeStmt.executeQuery();
                while (rs.next()) {
                    long id = rs.getLong(1);
                    targetMap.put(id, dataSource);
                }
            }
            for (Map.Entry<Long, DataSourceDescriptor> sourceEntry : sourceMap.entrySet()) {
                DataSourceDescriptor target = targetMap.get(sourceEntry.getKey());
                if (target != null) {
                    type1DataSources.remove(sourceEntry.getValue());
                    type2DataSources.remove(target);
                }
            }
            nodeStmt.close();
        }
        if (!type1DataSources.isEmpty() && !type2DataSources.isEmpty()) {
            JoinSuggestion joinSuggestion = new JoinSuggestion();
            joinSuggestion.setSourceType(type1Name);
            joinSuggestion.setTargetType(type2Name);
            joinSuggestion.setPossibleSources(type1DataSources);
            joinSuggestion.setPossibleTargets(type2DataSources);
            return joinSuggestion;
        }
        return null;
    }

    private List<DataSourceDescriptor> getDataSources(List<DataSourceDescriptor> sources, FeedType feedType) {
        List<DataSourceDescriptor> dataSources = new ArrayList<DataSourceDescriptor>();
        for (DataSourceDescriptor source : sources) {
            if (source.getDataSourceType() == feedType.getType()) {
                dataSources.add(source);
            }
        }
        return dataSources;
    }

    public HomeState determineHomeState() {

        EIConnection conn = Database.instance().getConnection();
        try {
            boolean testAccountVisible = testAccountVisible(conn);
            List<DataSourceDescriptor> dataSources = feedStorage.getDataSources(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, testAccountVisible);
            List<InsightDescriptor> reports = analysisStorage.getReports(SecurityUtil.getUserID(), SecurityUtil.getAccountID(), conn, testAccountVisible).values();
            return new HomeState(dataSources, reports);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }

    }
    
    public ReportFault getCredentials(List<Integer> dataSourceIDs) {
        EIConnection conn = Database.instance().getConnection();
        try {
            for (Integer dataSourceID : dataSourceIDs) {
                Feed feed = FeedRegistry.instance().getFeed(dataSourceID, conn);
                ReportFault reportFault = feed.getDataSource().validateDataConnectivity();
                if (reportFault != null) {
                    return reportFault;
                }
            }
            return null;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<EIDescriptor> getDescriptors() {
        List<EIDescriptor> descriptorList = new ArrayList<EIDescriptor>();
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            boolean testAccountVisible = testAccountVisible(conn);
            descriptorList.addAll(feedStorage.getDataSources(userID, accountID, conn, testAccountVisible));
            descriptorList.addAll(analysisStorage.getReports(userID, accountID, conn, testAccountVisible).values());
            descriptorList.addAll(new DashboardStorage().getDashboards(userID, accountID, conn, testAccountVisible).values());
            descriptorList.addAll(new ScorecardInternalService().getScorecards(userID, accountID, conn, testAccountVisible).values());
            Map<String, Integer> countMap = new HashMap<String, Integer>();
            Set<String> dupeNames = new HashSet<String>();
            Set<String> allNames = new HashSet<String>();
            for (EIDescriptor descriptor : descriptorList) {
                if (!allNames.add(descriptor.getName())) {
                    dupeNames.add(descriptor.getName());
                }
            }
            for (EIDescriptor descriptor : descriptorList) {
                if (dupeNames.contains(descriptor.getName())) {
                    Integer count = countMap.get(descriptor.getName());
                    if (count == null) {
                        count = 1;
                    } else {
                        count = count + 1;
                    }
                    countMap.put(descriptor.getName(), count);
                    descriptor.setName(descriptor.getName() + " (" + count + ")");
                }    
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public List<EIDescriptor> getDescriptorsForDataSource(long dataSourceID) {

        List<EIDescriptor> descriptorList = new ArrayList<EIDescriptor>();
        if (dataSourceID == 0) {
            return descriptorList;
        }
        long userID = SecurityUtil.getUserID();
        long accountID = SecurityUtil.getAccountID();
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            descriptorList.addAll(analysisStorage.getInsightDescriptorsForDataSource(userID, accountID, dataSourceID, conn, true));
            descriptorList.addAll(new DashboardStorage().getDashboardsForDataSource(userID, accountID, conn, dataSourceID, true).values());
            Map<String, Integer> countMap = new HashMap<String, Integer>();
            Set<String> dupeNames = new HashSet<String>();
            Set<String> allNames = new HashSet<String>();
            for (EIDescriptor descriptor : descriptorList) {
                if (!allNames.add(descriptor.getName())) {
                    dupeNames.add(descriptor.getName());
                }
            }
            for (EIDescriptor descriptor : descriptorList) {
                if (dupeNames.contains(descriptor.getName())) {
                    Integer count = countMap.get(descriptor.getName());
                    if (count == null) {
                        count = 1;
                    } else {
                        count = count + 1;
                    }
                    countMap.put(descriptor.getName(), count);
                    descriptor.setName(descriptor.getName() + " (" + count + ")");
                }
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return descriptorList;
    }

    public FeedResponse openFeedIfPossible(String urlKey) {
        FeedResponse feedResponse;
        try {
            try {
                long feedID = SecurityUtil.authorizeFeedAccess(urlKey);
                //long userID = SecurityUtil.getUserID();
                DataSourceDescriptor feedDescriptor = feedStorage.getFeedDescriptor(feedID);
                feedResponse = new FeedResponse(FeedResponse.SUCCESS, feedDescriptor);
            } catch (SecurityException e) {
                if (e.getReason() == SecurityException.LOGIN_REQUIRED)
                    feedResponse = new FeedResponse(FeedResponse.NEED_LOGIN, null);
                else
                    feedResponse = new FeedResponse(FeedResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedResponse;
    }

    public FeedResponse openFeedByAPIKey(String apiKey) {
        FeedResponse feedResponse;
        try {
            try {
                long feedID = feedStorage.getFeedForAPIKey(SecurityUtil.getUserID(), apiKey);
                DataSourceDescriptor feedDescriptor = feedStorage.getFeedDescriptor(feedID);
                feedResponse = new FeedResponse(FeedResponse.SUCCESS, feedDescriptor);
            } catch (SecurityException e) {
                if (e.getReason() == SecurityException.LOGIN_REQUIRED)
                    feedResponse = new FeedResponse(FeedResponse.NEED_LOGIN, null);
                else
                    feedResponse = new FeedResponse(FeedResponse.REJECTED, null);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return feedResponse;
    }

    public void wipeData(long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        Connection conn = Database.instance().getConnection();
        DataStorage metadata = null;
        try {
            conn.setAutoCommit(false);
            FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(feedID, conn);
            metadata = DataStorage.writeConnection(feedDefinition, conn);
            metadata.truncate();
            metadata.commit();
            conn.commit();
        } catch (DatabaseShardException dse) {
            // all fine
        } catch (Exception e) {
            LogClass.error(e);
            if (metadata != null) {
                metadata.rollback();
            }
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
            if (metadata != null) {
                metadata.closeConnection();
            }
            Database.closeConnection(conn);
        }
    }

    public JoinAnalysis testJoin(CompositeFeedConnection connection) {
        try {
            return new JoinTester(connection).generateReport();
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public CompositeResponse getMultipleFeeds(EIDescriptor firstID, EIDescriptor secondID) {
        CompositeResponse compositeResponse = new CompositeResponse();
        try {
            if (firstID.getType() == EIDescriptor.DATA_SOURCE) {
                FeedDefinition first = getFeedDefinition(firstID.getId());
                compositeResponse.setFirstFields(first.getFields());
                compositeResponse.setFirstID(firstID.getId());
                compositeResponse.setFirstName(first.getFeedName());
            } else {
                List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
                AddonReport addonReport = new AddonReport();
                addonReport.setReportID(firstID.getId());
                List<FeedNode> children = new DataService().addonFields(addonReport).getChildren();
                for (FeedNode child : children) {
                    AnalysisItemNode analysisItemNode = (AnalysisItemNode) child;
                    fields.add(analysisItemNode.getAnalysisItem());
                }
                compositeResponse.setFirstFields(fields);
                compositeResponse.setFirstID(firstID.getId());
                compositeResponse.setFirstName(firstID.getName());
            }

            if (secondID.getType() == EIDescriptor.DATA_SOURCE) {
                FeedDefinition second = getFeedDefinition(secondID.getId());
                compositeResponse.setSecondFields(second.getFields());
                compositeResponse.setSecondID(secondID.getId());
                compositeResponse.setSecondName(second.getFeedName());
            } else {
                List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
                AddonReport addonReport = new AddonReport();
                addonReport.setReportID(secondID.getId());
                List<FeedNode> children = new DataService().addonFields(addonReport).getChildren();
                for (FeedNode child : children) {
                    AnalysisItemNode analysisItemNode = (AnalysisItemNode) child;
                    fields.add(analysisItemNode.getAnalysisItem());
                }
                compositeResponse.setSecondFields(fields);
                compositeResponse.setSecondID(secondID.getId());
                compositeResponse.setSecondName(secondID.getName());
            }

        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return compositeResponse;
    }

    public List<DataSourceDescriptor> addonDataSources(long dataSourceID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT data_feed.data_feed_id, data_feed.feed_name FROM distinct_cached_addon_report_source, analysis, data_feed WHERE " +
                    "distinct_cached_addon_report_source.report_id = analysis.analysis_id AND analysis.data_feed_id = ? AND distinct_cached_addon_report_source.data_source_id = data_feed.data_feed_id");
            queryStmt.setLong(1, dataSourceID);
            ResultSet rs = queryStmt.executeQuery();
            List<DataSourceDescriptor> dataSources = new ArrayList<DataSourceDescriptor>();
            while (rs.next()) {
                dataSources.add(new DataSourceDescriptor(rs.getString(2), rs.getLong(1), 0, false, 0));
            }
            return dataSources;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<DataSourceDescriptor> searchForSubscribedFeeds() {
        long userID = SecurityUtil.getUserID();
        EIConnection conn = Database.instance().getConnection();
        try {
            List<DataSourceDescriptor> dataSources = feedStorage.getDataSources(userID, SecurityUtil.getAccountID(), conn, testAccountVisible(conn));
            Collections.sort(dataSources, new Comparator<DataSourceDescriptor>() {

                public int compare(DataSourceDescriptor dataSourceDescriptor, DataSourceDescriptor dataSourceDescriptor1) {
                    return dataSourceDescriptor.getName().compareTo(dataSourceDescriptor1.getName());
                }
            });

            PreparedStatement getTagsStmt = conn.prepareStatement("SELECT ACCOUNT_TAG_ID, TAG_NAME, DATA_SOURCE_TAG, REPORT_TAG, FIELD_TAG FROM ACCOUNT_TAG WHERE ACCOUNT_ID = ?");
            PreparedStatement getTagsToDataSourcesStmt = conn.prepareStatement("SELECT DATA_SOURCE_TO_TAG.ACCOUNT_TAG_ID, DATA_SOURCE_ID FROM data_source_to_tag, account_tag WHERE " +
                    "account_tag.account_tag_id = data_source_to_tag.account_tag_id and account_tag.account_id = ?");
            getTagsStmt.setLong(1, SecurityUtil.getAccountID());
            ResultSet tagRS = getTagsStmt.executeQuery();
            Map<Long, Tag> tags = new HashMap<Long, Tag>();
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

            for (DataSourceDescriptor dataSourceDescriptor : dataSources) {
                List<Tag> tagList = dsToTagMap.get(dataSourceDescriptor.getId());
                if (tagList == null) {
                    tagList = new ArrayList<Tag>();
                }
                dataSourceDescriptor.setTags(tagList);
            }

            return dataSources;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public List<DataSourceDescriptor> searchForHiddenChildren(long dataSourceID) {
        long userID = SecurityUtil.getUserID();
        try {
            return feedStorage.getExistingHiddenChildren(userID, dataSourceID);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    public FederatedDataSource createFederatedDataSource(List<FederationSource> sources, String name) {
        EIConnection conn = Database.instance().getConnection();
        try {
            FederatedDataSource federatedDataSource = new FederatedDataSource();
            federatedDataSource.setFeedName(name);
            federatedDataSource.setAccountVisible(true);
            federatedDataSource.setUploadPolicy(new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID()));
            federatedDataSource.setSources(sources);
            federatedDataSource.populateFields(conn);
            federatedDataSource.setApiKey(RandomTextGenerator.generateText(12));
            long feedID = feedStorage.addFeedDefinitionData(federatedDataSource, conn);
            DataStorage.liveDataSource(feedID, conn, federatedDataSource.getFeedType().getType());
            return federatedDataSource;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public FederatedDataSource createFederatedDataSource(List<FederationSource> sources, String name, EIConnection conn) throws Exception {

            FederatedDataSource federatedDataSource = new FederatedDataSource();
            federatedDataSource.setFeedName(name);
        federatedDataSource.setAccountVisible(true);
            federatedDataSource.setUploadPolicy(new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID()));
            federatedDataSource.setSources(sources);
            federatedDataSource.populateFields(conn);
            federatedDataSource.setApiKey(RandomTextGenerator.generateText(12));
            long feedID = feedStorage.addFeedDefinitionData(federatedDataSource, conn);
            DataStorage.liveDataSource(feedID, conn, federatedDataSource.getFeedType().getType());
            return federatedDataSource;

    }

    public AnalysisBasedFeedDefinition createReportDataSource(long reportID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            SecurityUtil.authorizeFeedAccess(reportID);
            AnalysisBasedFeedDefinition dataSource = new AnalysisBasedFeedDefinition();
            dataSource.setReportID(reportID);
            WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(reportID, conn);
            dataSource.setFeedName(report.getName());
            List<AnalysisItem> clones = new ArrayList<AnalysisItem>();
            Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
            for (AnalysisItem field : report.getAllAnalysisItems()) {
                AnalysisItem clone = field.clone();
                clones.add(clone);
                clone.setConcrete(true);
                replacementMap.put(field.getAnalysisItemID(), clone);
            }
            ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
            for (AnalysisItem clone : clones) {
                clone.updateIDs(replacements);
            }
            dataSource.setFields(clones);
            dataSource.setAccountVisible(true);
            dataSource.setUploadPolicy(new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID()));
            dataSource.setApiKey(RandomTextGenerator.generateText(20));
            long feedID = feedStorage.addFeedDefinitionData(dataSource, conn);
            DataStorage.liveDataSource(feedID, conn, dataSource.getFeedType().getType());
            return dataSource;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public CompositeFeedDefinition createCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges,
                                                       String feedName, EIConnection conn) throws Exception {
        CompositeFeedDefinition feedDef = new CompositeFeedDefinition();
        feedDef.setFeedName(feedName);
        feedDef.setAccountVisible(true);
        feedDef.setCompositeFeedNodes(compositeFeedNodes);
        feedDef.setConnections(edges);
        feedDef.setUploadPolicy(new UploadPolicy(SecurityUtil.getUserID(), SecurityUtil.getAccountID()));
        /*final ContainedInfo containedInfo = new ContainedInfo();
        new CompositeFeedNodeShallowVisitor() {

            protected void accept(CompositeFeedNode compositeFeedNode) throws SQLException {
                SecurityUtil.authorizeFeed(compositeFeedNode.getDataFeedID(), Roles.SUBSCRIBER);
                FeedDefinition feedDefinition = feedStorage.getFeedDefinitionData(compositeFeedNode.getDataFeedID(), conn);
                containedInfo.feedItems.addAll(feedDefinition.getFields());
            }
        }.visit(feedDef);*/
        feedDef.populateFields(conn);
        feedDef.setApiKey(RandomTextGenerator.generateText(12));
        long feedID = feedStorage.addFeedDefinitionData(feedDef, conn);
        DataStorage.liveDataSource(feedID, conn, feedDef.getFeedType().getType());
        return feedDef;
    }

    public CompositeFeedDefinition createCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges, String feedName) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            CompositeFeedDefinition feedDef = createCompositeFeed(compositeFeedNodes, edges, feedName, conn);
            conn.commit();
            return feedDef;
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public void updateCompositeFeed(List<CompositeFeedNode> compositeFeedNodes, List<CompositeFeedConnection> edges, long feedID) {
        SecurityUtil.authorizeFeed(feedID, Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            CompositeFeedDefinition compositeFeed = (CompositeFeedDefinition) getFeedDefinition(feedID);
            compositeFeed.setCompositeFeedNodes(compositeFeedNodes);
            compositeFeed.setConnections(edges);
            compositeFeed.populateFields(conn);
            feedStorage.updateDataFeedConfiguration(compositeFeed, conn);
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

    public String updateFeedDefinition(FeedDefinition feedDefinition) {
        SecurityUtil.authorizeFeed(feedDefinition.getDataFeedID(), Roles.SHARER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            new DataSourceInternalService().updateFeedDefinition(feedDefinition, conn);
            FeedRegistry.instance().flushCache(feedDefinition.getDataFeedID());
            conn.commit();
        } catch (UserMessageException ue) {
            LogClass.error(ue);
            return ue.getUserMessage();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {            
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return null;
    }

    public List<FeedDefinition> getDataSourcesForFederated(long federatedDataSourceID) {
        List<FeedDefinition> dataSources = new ArrayList<FeedDefinition>();
        SecurityUtil.authorizeFeed(federatedDataSourceID, Roles.SHARER);
        EIConnection conn = Database.instance().getConnection();
        try {
            FederatedDataSource federatedDataSource = (FederatedDataSource) feedStorage.getFeedDefinitionData(federatedDataSourceID, conn);
            for (FederationSource source : federatedDataSource.getSources()) {
                dataSources.add(feedStorage.getFeedDefinitionData(source.getDataSourceID(), conn));
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return dataSources;
    }

    public SchemaResponse exploreSchema(ServerDatabaseConnection serverDatabaseConnection) {
        SchemaResponse schemaResponse = new SchemaResponse();
        try {
            schemaResponse.setSchemaTables(serverDatabaseConnection.exploreSchema());
        } catch (SQLException e) {
            schemaResponse.setError(e.getMessage());
        } catch (Exception e) {
            LogClass.error(e);
            schemaResponse.setError("An internal server error occurred on trying to retrieve the schema.");
        }
        return schemaResponse;
    }

    public FeedDefinition getFeedDefinition(long dataFeedID) {
        SecurityUtil.authorizeFeed(dataFeedID, Roles.SHARER);
        EIConnection conn = Database.instance().getConnection();
        try {
            FeedDefinition dataSource = feedStorage.getFeedDefinitionData(dataFeedID, conn);
            //dataSource.setDataSourceInfo(dataSource.createFeed().createSourceInfo(conn));
            return dataSource;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }

    public void deleteLookupTable(long lookupTableID) {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
            queryStmt.setLong(1, lookupTableID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dataSourceID = rs.getLong(1);
                SecurityUtil.authorizeFeed(dataSourceID, Roles.OWNER);
                PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
                deleteStmt.setLong(1, lookupTableID);
                deleteStmt.executeUpdate();
                deleteStmt.close();
            }
            queryStmt.close();
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



    public LookupTable getLookupTable(long lookupTableID) {
        LookupTable lookupTable;
        EIConnection conn = Database.instance().getConnection();

        try {
            conn.setAutoCommit(false);
            lookupTable = getLookupTable(lookupTableID, conn);
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return lookupTable;
    }

    public LookupTable getLookupTable(long lookupTableID, EIConnection conn) throws SQLException {
        LookupTable lookupTable;
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT DATA_SOURCE_ID, LOOKUP_TABLE_NAME, SOURCE_ITEM_ID," +
                    "TARGET_ITEM_ID, URL_KEY FROM LOOKUP_TABLE WHERE LOOKUP_TABLE_ID = ?");
            queryStmt.setLong(1, lookupTableID);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long dataSourceID = rs.getLong(1);
                String name = rs.getString(2);
                long sourceItemID = rs.getLong(3);
                long targetItemID = rs.getLong(4);
                String urlKey = rs.getString(5);
                if (urlKey == null) {
                    urlKey = RandomTextGenerator.generateText(20);
                    PreparedStatement updateURLStmt = conn.prepareStatement("UPDATE LOOKUP_TABLE SET URL_KEY = ? WHERE " +
                            "LOOKUP_TABLE_ID = ?");
                    updateURLStmt.setString(1, urlKey);
                    updateURLStmt.setLong(2, lookupTableID);
                    updateURLStmt.execute();
                }
                AnalysisItem sourceItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, sourceItemID).list().get(0);
                sourceItem.afterLoad();
                AnalysisItem targetItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, targetItemID).list().get(0);
                targetItem.afterLoad();
                lookupTable = new LookupTable();
                lookupTable.setName(name);
                lookupTable.setDataSourceID(dataSourceID);
                lookupTable.setSourceField(sourceItem);
                lookupTable.setTargetField(targetItem);
                lookupTable.setUrlKey(urlKey);
                lookupTable.setLookupTableID(lookupTableID);
                PreparedStatement getPairsStmt = conn.prepareStatement("SELECT SOURCE_VALUE, TARGET_VALUE, target_date_value, target_measure, lookup_pair_id FROM " +
                        "LOOKUP_PAIR WHERE LOOKUP_TABLE_ID = ?");
                getPairsStmt.setLong(1, lookupTableID);
                ResultSet pairsRS = getPairsStmt.executeQuery();
                List<LookupPair> pairs = new ArrayList<LookupPair>();
                while (pairsRS.next()) {
                    String sourceValue = pairsRS.getString(1);
                    Value value;
                    if (targetItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        Timestamp targetDate = pairsRS.getTimestamp(3);
                        if (pairsRS.wasNull()) {
                            value = new EmptyValue();
                        } else {
                            value = new DateValue(new java.sql.Date(targetDate.getTime()));
                        }
                    } else if (targetItem.hasType(AnalysisItemTypes.MEASURE)) {
                        double number = pairsRS.getDouble(4);
                        if (pairsRS.wasNull()) {
                            value = new EmptyValue();
                        } else {
                            value = new NumericValue(number);
                        }
                    } else {
                        String targetValue = pairsRS.getString(2);
                        value = new StringValue(targetValue);
                    }
                    LookupPair lookupPair = new LookupPair();
                    lookupPair.setSourceValue(new StringValue(sourceValue));
                    lookupPair.setTargetValue(value);
                    lookupPair.setLookupPairID(pairsRS.getLong(5));
                    pairs.add(lookupPair);
                }
                getPairsStmt.close();
                lookupTable.setLookupPairs(pairs);
            } else {
                return null;
            }
            queryStmt.close();
        } finally {
            session.close();
        }
        return lookupTable;
    }

    public long openLookupTableIfPossible(String urlKey) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT LOOKUP_TABLE_ID, DATA_SOURCE_ID FROM LOOKUP_TABLE WHERE " +
                    "URL_KEY = ?");
            queryStmt.setString(1, urlKey);
            ResultSet rs = queryStmt.executeQuery();
            if (rs.next()) {
                long lookupTableID = rs.getLong(1);
                long dataSourceID = rs.getLong(2);
                SecurityUtil.authorizeFeedAccess(dataSourceID);
                return lookupTableID;
            }
        } catch (SecurityException se) {
            return 0;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
        return 0;
    }

    public long saveNewLookupTable(LookupTable lookupTable) {
        long id;
        SecurityUtil.authorizeFeed(lookupTable.getDataSourceID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);

            PreparedStatement insertTableStmt = conn.prepareStatement("INSERT INTO LOOKUP_TABLE (DATA_SOURCE_ID," +
                    "LOOKUP_TABLE_NAME, SOURCE_ITEM_ID, TARGET_ITEM_ID, URL_KEY) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertTableStmt.setLong(1, lookupTable.getDataSourceID());
            insertTableStmt.setString(2, lookupTable.getName());
            insertTableStmt.setLong(3, lookupTable.getSourceField().getAnalysisItemID());
            //insertTableStmt.setLong(4, lookupTable.getTargetField().getAnalysisItemID());
            insertTableStmt.setNull(4, Types.BIGINT);
            insertTableStmt.setString(5, RandomTextGenerator.generateText(20));
            insertTableStmt.execute();
            id = Database.instance().getAutoGenKey(insertTableStmt);

            FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(lookupTable.getDataSourceID(), conn, false);
            lookupTable.getTargetField().setLookupTableID(id);
            dataSource.getFields().add(lookupTable.getTargetField());

            lookupTable.getTargetField().setLookupTableID(id);
            new DataSourceInternalService().updateFeedDefinition(dataSource, conn);

            PreparedStatement updateLookupStmt = conn.prepareStatement("UPDATE LOOKUP_TABLE SET TARGET_ITEM_ID = ? WHERE LOOKUP_TABLE_ID = ?");
            updateLookupStmt.setLong(1, lookupTable.getTargetField().getAnalysisItemID());
            updateLookupStmt.setLong(2, id);
            updateLookupStmt.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
        return id;
    }

    private void savePairs(long id, AnalysisItem analysisItem, List<LookupPair> pairs, EIConnection conn) throws SQLException {
        /*PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM LOOKUP_PAIR WHERE LOOKUP_TABLE_ID = ?");
        clearStmt.setLong(1, id);
        clearStmt.executeUpdate();*/
        if (analysisItem.getType() == AnalysisItemTypes.DIMENSION) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID, " +
                    "SOURCE_VALUE, TARGET_VALUE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_PAIR SET SOURCE_VALUE = ?," +
                    "TARGET_VALUE = ? WHERE LOOKUP_PAIR_ID = ?");
            for (LookupPair lookupPair : pairs) {
                if (lookupPair.getLookupPairID() == 0) {
                    insertStmt.setLong(1, id);
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    insertStmt.setString(2, sourceValue);
                    insertStmt.setString(3, lookupPair.getTargetValue().toString());
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    updateStmt.setString(1, sourceValue);
                    updateStmt.setString(2, lookupPair.getTargetValue().toString());
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.executeUpdate();
                }
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID, " +
                    "SOURCE_VALUE, target_date_value) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_PAIR SET SOURCE_VALUE = ?," +
                    "TARGET_DATE_VALUE = ? WHERE LOOKUP_PAIR_ID = ?");
            for (LookupPair lookupPair : pairs) {
                if (lookupPair.getLookupPairID() == 0) {
                    insertStmt.setLong(1, id);
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    insertStmt.setString(2, sourceValue);
                    DateValue dateValue = (DateValue) lookupPair.getTargetValue();
                    if (dateValue.getDate() == null) {
                        insertStmt.setNull(3, Types.TIMESTAMP);
                    } else {
                        insertStmt.setTimestamp(3, new java.sql.Timestamp(dateValue.getDate().getTime()));
                    }
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    updateStmt.setString(1, sourceValue);
                    DateValue dateValue = (DateValue) lookupPair.getTargetValue();
                    if (dateValue.getDate() == null) {
                        updateStmt.setNull(2, Types.TIMESTAMP);
                    } else {
                        updateStmt.setTimestamp(2, new java.sql.Timestamp(dateValue.getDate().getTime()));
                    }
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.executeUpdate();
                }
            }
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO LOOKUP_PAIR (LOOKUP_TABLE_ID," +
                    "SOURCE_VALUE, TARGET_MEASURE) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_PAIR SET SOURCE_VALUE = ?," +
                    "TARGET_MEASURE = ? WHERE LOOKUP_PAIR_ID = ?");
            for (LookupPair lookupPair : pairs) {
                if (lookupPair.getLookupPairID() == 0) {
                    insertStmt.setLong(1, id);
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    insertStmt.setString(2, sourceValue);
                    NumericValue numericValue = (NumericValue) lookupPair.getTargetValue();
                    if (numericValue.getValue() == null) {
                        insertStmt.setNull(3, Types.DOUBLE);
                    } else {
                        insertStmt.setDouble(3, numericValue.getValue());
                    }
                    insertStmt.execute();
                } else {
                    String sourceValue = lookupPair.getSourceValue().toString();
                    /*if (sourceValue.length() > 200) {
                        sourceValue = sourceValue.substring(0, 200);
                    }*/
                    updateStmt.setString(1, sourceValue);
                    NumericValue numericValue = (NumericValue) lookupPair.getTargetValue();
                    if (numericValue.getValue() == null) {
                        updateStmt.setNull(2, Types.DOUBLE);
                    } else {
                        updateStmt.setDouble(2, numericValue.getValue());
                    }
                    updateStmt.setLong(3, lookupPair.getLookupPairID());
                    updateStmt.executeUpdate();
                }
            }
        }
    }

    public void updateLookupTable(LookupTable lookupTable) {
        SecurityUtil.authorizeFeed(lookupTable.getDataSourceID(), Roles.OWNER);
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE LOOKUP_TABLE SET LOOKUP_TABLE_NAME = ? WHERE " +
                    "LOOKUP_TABLE_ID = ?");
            updateStmt.setString(1, lookupTable.getName());
            updateStmt.setLong(2, lookupTable.getLookupTableID());
            updateStmt.executeUpdate();
            savePairs(lookupTable.getLookupTableID(), lookupTable.getTargetField(), lookupTable.getLookupPairs(), conn);
            FeedRegistry.instance().flushCache(lookupTable.getDataSourceID());
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

    public List<Value> getLookupTablePairs(long lookupTableID, List<FilterDefinition> filters) {
        try {
            LookupTable lookupTable = getLookupTable(lookupTableID);
            return LookupTableUtil.getValues(lookupTable, filters);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
