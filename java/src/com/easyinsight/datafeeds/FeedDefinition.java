package com.easyinsight.datafeeds;

import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.SuggestedUser;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.analysis.*;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.util.RandomTextGenerator;
import com.easyinsight.logging.LogClass;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;

import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.io.Serializable;

import nu.xom.Attribute;
import nu.xom.Element;
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
    private boolean accountVisible;
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
        throw new UnsupportedOperationException();
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

    public Feed createFeed() {
        FeedDefinition parentSource = null; 
        if (getParentSourceID() > 0) {
            try {
                parentSource = new FeedStorage().getFeedDefinitionData(getParentSourceID());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        Feed feed = createFeedObject(parentSource);
        feed.setDataSource(this);
        feed.setFeedID(getDataFeedID());
        feed.setAttribution(getAttribution());
        feed.setProperties(createProperties());
        feed.setFilterExampleMessage(getFilterExampleMessage());
        populateFeedFields(feed);
        feed.setName(getFeedName());
        feed.setVisible(isVisible());
        feed.setType(getDataSourceType());
        feed.setFeedType(getFeedType());
        feed.setExchangeSave(new DataSourceTypeRegistry().isExchangeType(getFeedType().getType()));
        return feed;
    }

    private void populateFeedFields(Feed feed) {
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        List<AnalysisItem> clones = new ArrayList<AnalysisItem>();
        for (AnalysisItem field : getFields()) {
            try {
                AnalysisItem clone = field.clone();
                clones.add(clone);
                clone.setConcrete(true);
                replacementMap.put(field.getAnalysisItemID(), clone);
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
                boolean hasVisibleChildren = clonedFolder.getChildFolders().size() > 0;
                if (!hasVisibleChildren) {
                    for (AnalysisItem analysisItem : clonedFolder.getChildItems()) {
                        if (!analysisItem.isHidden()) {
                            hasVisibleChildren = true;
                            break;
                        }
                    }
                }
                if (hasVisibleChildren) {
                    feedNodes.add(clonedFolder.toFeedNode());
                }
            } catch (CloneNotSupportedException e) {
                LogClass.error(e);
            }
        }
        for (AnalysisItem analysisItem : replacementMap.values()) {
            if (!analysisItem.isHidden()) {
                feedNodes.add(analysisItem.toFeedNode());
            }
        }
        Collections.sort(feedNodes, new Comparator<FeedNode>() {

                public int compare(FeedNode o1, FeedNode o2) {
                    if (o1 instanceof FolderNode && !(o2 instanceof FolderNode)) {
                        return -1;
                    } else if (!(o1 instanceof FolderNode) && o2 instanceof FolderNode) {
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

    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
    }

    public void customStorage(Connection conn) throws SQLException {
        
    }

    public void customLoad(Connection conn) throws SQLException {
        
    }

    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        return (FeedDefinition) super.clone();
    }

    public DataSourceCloneResult cloneDataSource(Connection conn) throws Exception {
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
        feedDefinition.setLastRefreshStart(null);
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
        return new DataSourceCloneResult(feedDefinition, keyReplacementMap);
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
        throw new UnsupportedOperationException();
    }

    @Nullable
    public ReportFault validateDataConnectivity() {
        return null;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        throw new UnsupportedOperationException();
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
}
