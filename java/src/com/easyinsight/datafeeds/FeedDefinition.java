package com.easyinsight.datafeeds;

import com.easyinsight.core.*;
import com.easyinsight.datafeeds.composite.CustomFieldTag;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.solutions.SolutionInstallInfo;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.tag.Tag;
import com.easyinsight.users.Account;
import com.easyinsight.users.SuggestedUser;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.sql.ResultSet;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.Serializable;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * User: James Boe
 * Date: Jan 29, 2008
 * Time: 10:57:24 PM
 */
public class FeedDefinition implements Cloneable, Serializable {
    private String feedName;
    private List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
    private UploadPolicy uploadPolicy = new UploadPolicy();
    private boolean publiclyVisible;
    private boolean marketplaceVisible;
    private boolean accountVisible = true;
    private long dataFeedID;
    private long size;
    private Date dateCreated;
    private Date dateUpdated;
    private boolean dataPersisted;
    private String ownerName;
    private String attribution;
    private String description;
    private long dynamicServiceDefinitionID;
    private int dataSourceBehavior;
    private String apiKey;
    private boolean uncheckedAPIEnabled;
    private boolean uncheckedAPIUsingBasicAuth;
    private boolean inheritAccountAPISettings;
    private List<FeedFolder> folders = new ArrayList<FeedFolder>();
    private boolean visible = true;
    private long parentSourceID;
    private Date lastRefreshStart;
    private String marmotScript;
    private String refreshMarmotScript;
    private boolean concreteFieldsEditable;
    private DataSourceInfo dataSourceInfo;
    private boolean kpiSource;
    private List<AddonReport> addonReports;
    private boolean fieldCleanupEnabled = true;
    private boolean fieldLookupEnabled;
    private long defaultFieldTag;
    private boolean manualReportRun;
    private boolean showTags;
    private boolean visibleWithinParentConfiguration;
    private boolean defaultToFullJoins;

    public void configureFactory(HTMLConnectionFactory factory) {

    }

    public boolean isDefaultToFullJoins() {
        return defaultToFullJoins;
    }

    public void setDefaultToFullJoins(boolean defaultToFullJoins) {
        this.defaultToFullJoins = defaultToFullJoins;
    }

    public boolean isVisibleWithinParentConfiguration() {
        return visibleWithinParentConfiguration;
    }

    public void setVisibleWithinParentConfiguration(boolean visibleWithinParentConfiguration) {
        this.visibleWithinParentConfiguration = visibleWithinParentConfiguration;
    }

    public List<CustomFieldTag> customFieldTags() {
        return null;
    }

    public boolean isShowTags() {
        return showTags;
    }

    public void setShowTags(boolean showTags) {
        this.showTags = showTags;
    }

    public boolean isManualReportRun() {
        return manualReportRun;
    }

    public void setManualReportRun(boolean manualReportRun) {
        this.manualReportRun = manualReportRun;
    }

    public boolean isMigrateRequired() {
        return true;
    }

    public long getDefaultFieldTag() {
        return defaultFieldTag;
    }

    public void setDefaultFieldTag(long defaultFieldTag) {
        this.defaultFieldTag = defaultFieldTag;
    }

    public boolean isFieldLookupEnabled() {
        return fieldLookupEnabled;
    }

    public void setFieldLookupEnabled(boolean fieldLookupEnabled) {
        this.fieldLookupEnabled = fieldLookupEnabled;
    }

    public boolean isFieldCleanupEnabled() {
        return fieldCleanupEnabled;
    }

    public void setFieldCleanupEnabled(boolean fieldCleanupEnabled) {
        this.fieldCleanupEnabled = fieldCleanupEnabled;
    }

    public List<AddonReport> getAddonReports() {
        return addonReports;
    }

    public void setAddonReports(List<AddonReport> addonReports) {
        this.addonReports = addonReports;
    }

    public boolean isKpiSource() {
        return kpiSource;
    }

    public void setKpiSource(boolean kpiSource) {
        this.kpiSource = kpiSource;
    }

    public String toXML() {
        Element dataSource = new Element("dataSource");
        dataSource.addAttribute(new Attribute("urlKey", apiKey));
        dataSource.addAttribute(new Attribute("accountVisible", String.valueOf(accountVisible)));
        Element fields = new Element("fields");
        dataSource.appendChild(fields);
        for (AnalysisItem analysisItem : getFields()) {
            Element field = new Element("field");
            fields.appendChild(field);
            field.appendChild(analysisItem.toXML(null));
        }
        return dataSource.toXML();
    }

    public int getDataSourceBehavior() {
        if (dataSourceBehavior == 0) {
            dataSourceBehavior = getDataSourceType();
        }
        return dataSourceBehavior;
    }

    public void setDataSourceBehavior(int dataSourceBehavior) {
        this.dataSourceBehavior = dataSourceBehavior;
    }

    public String getRefreshMarmotScript() {
        return refreshMarmotScript;
    }

    public void setRefreshMarmotScript(String refreshMarmotScript) {
        this.refreshMarmotScript = refreshMarmotScript;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public boolean isConcreteFieldsEditable() {
        return concreteFieldsEditable;
    }

    public void setConcreteFieldsEditable(boolean concreteFieldsEditable) {
        this.concreteFieldsEditable = concreteFieldsEditable;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public boolean customJoinsAllowed(EIConnection conn) throws SQLException {
        return false;
    }

    public boolean gmtTime() {
        return false;
    }

    public Date getLastRefreshStart() {
        return lastRefreshStart;
    }

    public void setLastRefreshStart(Date lastRefreshStart) {
        this.lastRefreshStart = lastRefreshStart;
    }

    public boolean requiresConfiguration() {
        return true;
    }

    public int getDataSourceType() {
        return DataSourceInfo.STORED_PUSH;
    }

    public int getVersion() {
        return 1;
    }

    public List<DataSourceMigration> getMigrations() {
        return new ArrayList<DataSourceMigration>();
    }

    public List<FeedFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<FeedFolder> folders) {
        this.folders = folders;
    }

    public long getParentSourceID() {
        return parentSourceID;
    }

    public void setParentSourceID(long parentSourceID) {
        this.parentSourceID = parentSourceID;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getFilterExampleMessage() {
        return "On the left, you'll see the list of fields available to you. Drag a field from that list into the area to the right to create a filter.";
    }

    public FeedDefinition() {
    }

    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public boolean isInheritAccountAPISettings() {
        return inheritAccountAPISettings;
    }

    public void setInheritAccountAPISettings(boolean inheritAccountAPISettings) {
        this.inheritAccountAPISettings = inheritAccountAPISettings;
    }

    public boolean isUncheckedAPIUsingBasicAuth() {
        return uncheckedAPIUsingBasicAuth;
    }

    public void setUncheckedAPIUsingBasicAuth(boolean uncheckedAPIUsingBasicAuth) {
        this.uncheckedAPIUsingBasicAuth = uncheckedAPIUsingBasicAuth;
    }

    public boolean isUncheckedAPIEnabled() {
        return uncheckedAPIEnabled;
    }

    public void setUncheckedAPIEnabled(boolean uncheckedAPIEnabled) {
        this.uncheckedAPIEnabled = uncheckedAPIEnabled;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public long getDynamicServiceDefinitionID() {
        return dynamicServiceDefinitionID;
    }

    public void setDynamicServiceDefinitionID(long dynamicServiceDefinitionID) {
        this.dynamicServiceDefinitionID = dynamicServiceDefinitionID;
    }    

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDataPersisted() {
        return dataPersisted;
    }

    public void setDataPersisted(boolean dataPersisted) {
        this.dataPersisted = dataPersisted;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public List<AnalysisItem> getFields() {
        return fields;
    }

    public void setFields(List<AnalysisItem> fields) {
        this.fields = fields;
    }

    public UploadPolicy getUploadPolicy() {
        return uploadPolicy;
    }

    public void setUploadPolicy(UploadPolicy uploadPolicy) {
        this.uploadPolicy = uploadPolicy;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public String getFeedName() {
        return feedName;
    }

    public FeedType getFeedType() {
        return FeedType.DEFAULT;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public Feed createFeedObject(FeedDefinition parent) {
        return new StaticFeed();
    }

    protected Map<String, String> createProperties() {
        return new HashMap<String, String>();
    }

    public Feed createFeed(EIConnection conn) {
        FeedDefinition parentSource = null;
        if (getParentSourceID() > 0) {
            try {
                parentSource = new FeedStorage().getFeedDefinitionData(getParentSourceID(), conn);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return createFeed(conn, parentSource);
    }

    public Feed createFeed(EIConnection conn, FeedDefinition parentSource) {

        Feed feed = createFeedObject(parentSource);

        // load from report
        Map<String, List<AnalysisItem>> kpis = new HashMap<String, List<AnalysisItem>>();
        if (!isKpiSource()) {
            loadKPIs(kpis, conn);
        }
        feed.setDataSource(this);
        feed.setFeedID(getDataFeedID());
        feed.setAttribution(getAttribution());
        feed.setProperties(createProperties());
        feed.setFilterExampleMessage(getFilterExampleMessage());
        populateFeedFields(feed, kpis);
        try {
            Map<String, AnalysisItem> map = new HashMap<String, AnalysisItem>();
            for (AnalysisItem item : feed.getFields()) {
                map.put(item.toDisplay(), item);
            }
            long defaultTag = getDefaultFieldTag();
            PreparedStatement tagStmt = conn.prepareStatement("SELECT account_tag.account_tag_id, account_tag.tag_name, field_to_tag.display_name FROM " +
                    "field_to_tag, account_tag WHERE field_to_tag.account_tag_id = account_tag.account_tag_id AND " +
                    "field_to_tag.data_source_id = ?");
            tagStmt.setLong(1, getDataFeedID());
            ResultSet tagRS = tagStmt.executeQuery();
            Set<Tag> tags = new HashSet<Tag>();
            while (tagRS.next()) {
                Tag tag = new Tag(tagRS.getLong(1), tagRS.getString(2), false, false, false);
                if (!tags.contains(tag)) {
                    tags.add(tag);
                    if (tag.getId() == defaultTag) {
                        feed.setDefaultTag(tag);
                    }
                }
                String name = tagRS.getString(3);
                AnalysisItem item = map.get(name);
                if (item != null) {
                    if (item.getTags() == null) {
                        item.setTags(new ArrayList<Tag>());
                    }
                    item.getTags().add(tag);
                }
            }
            feed.setTags(new ArrayList<Tag>(tags));
            tagStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        }
        feed.setName(getFeedName());
        feed.setVisible(isVisible());
        feed.setType(getDataSourceType());
        feed.setFeedType(getFeedType());
        feed.setExchangeSave(new DataSourceTypeRegistry().isExchangeType(getFeedType().getType()));
        try {
            decorateFields(feed.getFields(), conn);
        } catch (SQLException e) {
            LogClass.error(e);
        }
        return feed;
    }

    public List<AnalysisItem> allFields(EIConnection conn) {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>(getFields());
        Map<String, List<AnalysisItem>> kpis = new HashMap<String, List<AnalysisItem>>();
        loadKPIs(kpis, conn);
        for (List<AnalysisItem> kpiList : kpis.values()) {
            fields.addAll(kpiList);
        }
        return fields;
    }

    protected void loadKPIs(Map<String, List<AnalysisItem>> kpis, EIConnection conn) {
        Session session = Database.instance().createSession(conn);
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT ANALYSIS_ID, TITLE FROM ANALYSIS WHERE DATA_FEED_ID = ? AND data_source_field_report = ?");
            PreparedStatement fieldStmt = conn.prepareStatement("SELECT analysis_item.ANALYSIS_ITEM_ID FROM report_structure, analysis_item WHERE analysis_id = ? AND " +
                    "report_structure.analysis_item_id = analysis_item.analysis_item_id and analysis_item.kpi = ?");
            queryStmt.setLong(1, getDataFeedID());
            queryStmt.setBoolean(2, true);
            ResultSet rs = queryStmt.executeQuery();
            while (rs.next()) {
                long reportID = rs.getLong(1);
                String reportName = rs.getString(2);
                List<AnalysisItem> kpiList = new ArrayList<AnalysisItem>();
                fieldStmt.setLong(1, reportID);
                fieldStmt.setBoolean(2, true);
                ResultSet fieldRS = fieldStmt.executeQuery();
                while (fieldRS.next()) {
                    long fieldID = fieldRS.getLong(1);
                    AnalysisItem analysisItem = (AnalysisItem) session.createQuery("from AnalysisItem where analysisItemID = ?").setLong(0, fieldID).list().get(0);
                    analysisItem.afterLoad();
                    FieldDataSourceOrigin origin = new FieldDataSourceOrigin();
                    origin.setReport(reportID);
                    analysisItem.setOrigin(origin);
                    kpiList.add(analysisItem);
                }
                if (kpiList.size() > 0) {
                    kpis.put(reportName, kpiList);
                }
            }
            queryStmt.close();
            fieldStmt.close();
        } catch (SQLException e) {
            LogClass.error(e);
        } finally {
            session.close();
        }
    }

    protected List<AnalysisItem> fieldsForFeed() {
        return getFields();
    }

    private void addAddonReports(List<AnalysisItem> clones, List<FeedNode> feedNodes, EIConnection conn) {
        for (AddonReport addonReport : addonReports) {
            long addonReportID = addonReport.getReportID();
            Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
            List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
            WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(addonReportID, conn);
            Map<String, AnalysisItem> structure = report.createStructure();
            for (AnalysisItem item : structure.values()) {
                AnalysisItem clone;
                if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                    AnalysisDateDimension baseDate = (AnalysisDateDimension) item;
                    AnalysisDateDimension date = new AnalysisDateDimension();
                    date.setDateLevel(baseDate.getDateLevel());
                    date.setOutputDateFormat(baseDate.getOutputDateFormat());
                    date.setDateOnlyField(baseDate.isDateOnlyField() || baseDate.hasType(AnalysisItemTypes.DERIVED_DATE));
                    clone = date;
                } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                    AnalysisMeasure baseMeasure = (AnalysisMeasure) item;
                    AnalysisMeasure measure = new AnalysisMeasure();
                    measure.setFormattingType(item.getFormattingType());
                    if (report.isPersistedCache()) {
                        measure.setAggregation(AggregationTypes.SUM);
                    } else {
                        measure.setAggregation(baseMeasure.getAggregation());
                    }
                    measure.setPrecision(baseMeasure.getPrecision());
                    measure.setMinPrecision(baseMeasure.getMinPrecision());
                    clone = measure;
                } else {
                    clone = new AnalysisDimension();
                }
                clone.setOriginalDisplayName(item.toDisplay());
                clone.setDisplayName(report.getName() + " - " + item.toDisplay());
                clone.setUnqualifiedDisplayName(item.getUnqualifiedDisplayName());
                ReportKey reportKey = new ReportKey();
                reportKey.setParentKey(item.getKey());
                reportKey.setReportID(addonReportID);
                clone.setKey(reportKey);
                replacementMap.put(item.getAnalysisItemID(), clone);
                fields.add(clone);
            }
            ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
            for (AnalysisItem clone : fields) {
                clone.updateIDs(replacements);
                clones.add(clone);
            }
            FolderNode folderNode = new FolderNode();
            folderNode.setAddonReportDescriptor(new InsightDescriptor(report.getAnalysisID(), report.getName(), report.getDataFeedID(), report.getReportType(), report.getUrlKey(), 0, false));
            folderNode.setAddonReportID(addonReportID);
            FeedFolder feedFolder = new FeedFolder();
            feedFolder.setName(report.getName());
            folderNode.setFolder(feedFolder);
            for (AnalysisItem analysisItem : fields) {
                folderNode.getChildren().add(analysisItem.toFeedNode());
            }
            feedNodes.add(folderNode);
        }
    }

    public void decorateFields(List<AnalysisItem> fields, EIConnection conn) throws SQLException {

    }

    private void populateFeedFields(Feed feed, Map<String, List<AnalysisItem>> kpis) {
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        List<AnalysisItem> clones = new ArrayList<AnalysisItem>();
        List<AnalysisItem> allFields = new ArrayList<AnalysisItem>();
        allFields.addAll(fieldsForFeed());
        for (List<AnalysisItem> kpiList : kpis.values()) {
            allFields.addAll(kpiList);
        }

        for (AnalysisItem field : allFields) {
            try {
                AnalysisItem clone = field.clone();
                //clone.setParentItemID(field.getAnalysisItemID());
                clones.add(clone);
                clone.setConcrete(true);
                replacementMap.put(field.getAnalysisItemID(), clone);
                if (!isKpiSource()) {
                    if (field.isKpi()) {
                        clone.setKpi(false);
                        FieldDataSourceOrigin origin = new FieldDataSourceOrigin();
                        origin.setReport(field.getOrigin().getReport());
                        clone.setOrigin(origin);
                        clone.setFieldType(AnalysisItem.ORDER_KPI);
                        Key key = null;
                        AnalysisItem dataSourceItem = findAnalysisItemByDisplayName(clone.toDisplay());
                        if (dataSourceItem != null) {
                            key = dataSourceItem.getKey();
                        } else {
                            if (clone.getOriginalDisplayName() != null) {
                                dataSourceItem = findAnalysisItemByDisplayName(clone.getOriginalDisplayName());
                            }
                            if (dataSourceItem != null) {
                                key = dataSourceItem.getKey();
                            } else {
                                dataSourceItem = findAnalysisItem(clone.getKey().toKeyString());
                                if (dataSourceItem != null) {
                                    key = dataSourceItem.getKey();
                                }
                            }
                        }
                        if (key != null) {
                            clone.setKey(key);
                        } else {
                            Key clonedKey = clone.getKey().clone();
                            clone.setKey(clonedKey);
                        }
                    }
                }
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
        for (AnalysisItem clone : clones) {
            clone.updateIDs(replacements);
        }

        List<FeedNode> feedNodes = new ArrayList<FeedNode>();
        for (FeedFolder feedFolder : getFolders()) {
            try {
                FeedFolder clonedFolder = feedFolder.clone();
                clonedFolder.updateIDs(replacementMap);

                /*if (compositeSourceFolder(clonedFolder)) {
                    addKPIsToFolder(clonedFolder, kpis, replacementMap);
                }*/

                boolean hasVisibleChildren = clonedFolder.getChildFolders().size() > 0;
                if (!hasVisibleChildren) {
                    for (AnalysisItem analysisItem : clonedFolder.getChildItems()) {
                        hasVisibleChildren = true;
                        break;
                    }
                }
                if (hasVisibleChildren) {
                    feedNodes.add(clonedFolder.toFeedNode());
                }
            } catch (CloneNotSupportedException e) {
                LogClass.error(e);
            }
        }
        addKPIs(kpis, replacementMap, feedNodes);
        for (AnalysisItem analysisItem : replacementMap.values()) {
            feedNodes.add(analysisItem.toFeedNode());
        }
        if (addonReports != null) {
            EIConnection conn = Database.instance().getConnection();
            try {
                addAddonReports(clones, feedNodes, conn);
            } finally {
                Database.closeConnection(conn);
            }
        }
        Collections.sort(feedNodes, new Comparator<FeedNode>() {

                public int compare(FeedNode o1, FeedNode o2) {
                    if (o1 instanceof FolderNode && !(o2 instanceof FolderNode)) {
                        return -1;
                    } else if (!(o1 instanceof FolderNode) && o2 instanceof FolderNode) {
                        return 1;
                    }
                    if ("KPIs".equals(o1.toDisplay())) {
                        return -1;
                    } else if ("KPIs".equals(o2.toDisplay())) {
                        return 1;
                    }
                    return o1.toDisplay().compareTo(o2.toDisplay());
                }
            });
        feed.setFieldHierarchy(feedNodes);
        feed.setUrlKey(getApiKey());
        decorateLinks(clones);
        feed.setFields(clones);
    }

    protected void addKPIs(Map<String, List<AnalysisItem>> kpis, Map<Long, AnalysisItem> replacementMap, List<FeedNode> feedNodes) {
        for (Map.Entry<String, List<AnalysisItem>> kpiEntry : kpis.entrySet()) {
            FeedFolder kpiFolder = new FeedFolder();
            kpiFolder.setName(kpiEntry.getKey());
            for (AnalysisItem kpi : kpiEntry.getValue()) {
                AnalysisItem targetKPI = replacementMap.remove(kpi.getAnalysisItemID());
                kpiFolder.addAnalysisItem(targetKPI);
            }
            feedNodes.add(kpiFolder.toFeedNode());
        }
    }

    protected void addKPIsToFolder(FeedFolder clonedFolder, List<AnalysisItem> kpis, Map<Long, AnalysisItem> replacementMap) {

    }

    protected boolean compositeSourceFolder(FeedFolder clonedFolder) {
        return false;
    }

    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
    }

    public void customStorage(Connection conn) throws SQLException {
        
    }

    public void customLoad(Connection conn) throws SQLException {
        
    }

    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        return (FeedDefinition) super.clone();
    }

    public Map<Long, SolutionInstallInfo> cloneDataSource(Connection conn) throws Exception {
        FeedDefinition feedDefinition = clone(conn);
        List<AnalysisItem> clonedFields = new ArrayList<AnalysisItem>();
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        Map<Key, Key> keyReplacementMap = new HashMap<Key, Key>();

        // is this different for normal data sources vs. composite data sources?
        // if it's a normal data source, we're just going to clone the key here, then it's in the map for lookup
        // if it's a composite data source, cloning the key requires that we update the target as well
        // 

        for (AnalysisItem analysisItem : fields) {
            AnalysisItem clonedItem = analysisItem.clone();
            Key clonedKey = clonedItem.getKey().clone();
            clonedItem.setKey(clonedKey);
            keyReplacementMap.put(analysisItem.getKey(), clonedKey);
            clonedFields.add(clonedItem);
            replacementMap.put(analysisItem.getAnalysisItemID(), clonedItem);
        }
        ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
        for (AnalysisItem analysisItem : replacementMap.values()) {
            analysisItem.updateIDs(replacements);
        }
        feedDefinition.setFields(clonedFields);
        feedDefinition.setVisible(visible);
        feedDefinition.setDataFeedID(0);
        feedDefinition.setApiKey(RandomTextGenerator.generateText(12));
        feedDefinition.setAccountVisible(false);
        feedDefinition.setLastRefreshStart(new Date(1));
        List<FeedFolder> clonedFolders = new ArrayList<FeedFolder>();
        for (FeedFolder feedFolder : getFolders()) {
            try {
                FeedFolder clonedFolder = feedFolder.clone();
                clonedFolder.updateIDs(replacementMap);
                clonedFolders.add(clonedFolder);
            } catch (CloneNotSupportedException e) {
                LogClass.error(e);
            }
        }
        feedDefinition.setFolders(clonedFolders);
        Map<Long, SolutionInstallInfo> map = new HashMap<Long, SolutionInstallInfo>();
        map.put(getDataFeedID(), new SolutionInstallInfo(getDataFeedID(), new DataSourceDescriptor(), getFeedName(), false, keyReplacementMap, feedDefinition));
        return map;
    }

    public AnalysisItem findAnalysisItemByKey(String key) {
        AnalysisItem item = null;
        for (AnalysisItem field : getFields()) {
            if (field.getKey().toKeyString().equals(key)) {
                item = field;
            }
        }
        return item;
    }

    public AnalysisItem findAnalysisItem(String key) {
        AnalysisItem item = null;
        for (AnalysisItem field : getFields()) {
            if (field.getKey().toKeyString().equals(key)) {
                item = field;
            }
        }
        return item;
    }

    public AnalysisItem findAnalysisItemByDisplayName(String key) {
        AnalysisItem item = null;
        for (AnalysisItem field : getFields()) {
            if (field.toDisplay().equals(key)) {
                item = field;
            }
        }
        return item;
    }
    
     public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        throw new UnsupportedOperationException();
    }

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }

    public boolean isMarketplaceVisible() {
        return marketplaceVisible;
    }

    public void setMarketplaceVisible(boolean marketplaceVisible) {
        this.marketplaceVisible = marketplaceVisible;
    }

    public String validateCredentials() {
        return null;
    }

    @Nullable
    public ReportFault validateDataConnectivity() {
        return null;
    }

    public List<AnalysisItem> createAnalysisItemsNew(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        return createAnalysisItemsNew(keys, conn, parentDefinition);
    }

    public Map<String, Key> newDataSourceFields(FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
    }


    // A little nonsensical, but returns false if not a ServerDataSource to prevent events from firing.
    public boolean isConfigured() {
        return false;
    }

    public Key getField(String sourceKey) {
        Key key = null;
        for (AnalysisItem field : getFields()) {
            if (sourceKey.equals(field.getKey().toKeyString())) {
                key = field.getKey();
            }
        }
        return key;
    }

    public void postClone(Connection conn) throws Exception {
        
    }

    public void delete(Connection conn) throws SQLException {
        LogClass.info("USER " + SecurityUtil.getUserID() + " DELETING DATA SOURCE " + dataFeedID);
        onDelete(conn);
        
        // cascade into base classes - see http://bugs.mysql.com/bug.php?id=13102 as to why this code sucks.
        Session session = Database.instance().createSession(conn);

        /*List reportObjects = session.createQuery("from AnalysisDefinition where dataFeedID = ?").setLong(0, getDataFeedID()).list();*/

        /*for (Object reportObject : reportObjects) {
            AnalysisDefinition analysisDefinition = (AnalysisDefinition) reportObject;
            for (AnalysisItem analysisItem : analysisDefinition.getReportStructure().values()) {
                session.delete(analysisItem.getKey());
            }
            session.delete(analysisDefinition);
        }*/

        session.flush();

        //PreparedStatement deleteKeyStmt = conn.prepareStatement("delete from item_key where item_key_id = ?");
        if (getFields() != null) {
            for (AnalysisItem field : getFields()) {

                session.delete(field.getKey());
            }
        }

        session.flush();
        session.close();
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM DATA_FEED WHERE DATA_FEED_ID = ?");

        deleteStmt.setLong(1, dataFeedID);
        deleteStmt.executeUpdate();
        deleteStmt.close();
    }

    protected void onDelete(Connection conn) throws SQLException {
        DataStorage.delete(dataFeedID, conn);
    }

    public List<String> getNestedFolders() {
        return null;
    }

    public FeedFolder defineFolder(String name) {
        for (FeedFolder feedFolder : getFolders()) {
            if (name.equals(feedFolder.getName())) {
                return feedFolder;
            }
        }
        FeedFolder feedFolder = new FeedFolder();
        feedFolder.setName(name);
        getFolders().add(feedFolder);
        return feedFolder;
    }

    protected FeedFolder defineFolder(String name, FeedFolder parent) {
        for (FeedFolder feedFolder : parent.getChildFolders()) {
            if (name.equals(feedFolder.getName())) {
                return feedFolder;
            }
        }
        FeedFolder feedFolder = new FeedFolder();
        feedFolder.setName(name);
        parent.getChildFolders().add(feedFolder);
        return feedFolder;
    }

    public boolean isLongRefresh() {
        return false;
    }

    public void beforeSave(EIConnection conn) throws Exception {

    }

    public void decorateLinks(List<AnalysisItem> analysisItems) {
        
    }

    public boolean checkDateTime(String name, Key key) {
        return true;
    }

    public List<SuggestedUser> retrieveUsers(EIConnection conn) throws Exception {
        return new ArrayList<SuggestedUser>();
    }

    public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report, DataSourceInfo dataSourceInfo) {
        return new ArrayList<IntentionSuggestion>();
    }

    public List<Intention> createIntentions(WSAnalysisDefinition report, List<AnalysisItem> fields, int type) throws SQLException {
        return new ArrayList<Intention>();
    }

    public void updateLinks(AnalysisItem analysisItem) {
        decorateLinks(Arrays.asList(analysisItem));
    }

    public void removeURLLinkIfExists(String pattern, AnalysisItem analysisItem) {
        Iterator<Link> iter = analysisItem.getLinks().iterator();
        while (iter.hasNext()) {
            Link link = iter.next();
            if (link instanceof URLLink) {
                URLLink urlLink = (URLLink) link;
                if (urlLink.getUrl() != null && urlLink.getUrl().contains(pattern)) {
                    iter.remove();
                }
            }
        }
    }

    public boolean fullNightlyRefresh() {
        return false;
    }

    public boolean preserveDataOnMigrate() {
        return true;
    }

    public boolean requiresPostOAuthSetup() {
        return false;
    }

    public boolean waitsOnServiceUtil() {
        return false;
    }

    public boolean rebuildFieldWindow() {
        return false;
    }

    public Collection<DataSourceDescriptor> getDataSources(EIConnection conn) throws SQLException {
        Set<DataSourceDescriptor> dataSources = new HashSet<DataSourceDescriptor>();
        dataSources.add(new DataSourceDescriptor(getFeedName(), getDataFeedID(), getDataSourceType(), isAccountVisible(), getDataSourceBehavior()));
        return dataSources;
    }

    protected static String getJSONValue(Map n, String key) {
        Object obj = n.get(key);
        if(obj != null)
            return obj.toString();
        else
            return null;
    }

    protected static String getXMLValue(Node n, String xpath) {
        Nodes results = n.query(xpath);
        if (results.size() > 0)
            return results.get(0).getValue();
        else
            return null;
    }

    public String generateURL(String url, String end) {
        if (url == null || "".equals(url)) {
            return url;
        }
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "https://") + url;
        basecampUrl = basecampUrl.replaceFirst("^http://", "https://");
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if (!basecampUrl.contains(".")) {
            basecampUrl = basecampUrl + "." + end;
        }
        return basecampUrl;
    }

    public String postOAuthSetup(HttpServletRequest request) {
        return null;
    }
}
