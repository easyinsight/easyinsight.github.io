package com.easyinsight.datafeeds;

import com.easyinsight.kpi.KPI;
import com.easyinsight.userupload.UploadPolicy;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.users.Credentials;
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
import java.sql.ResultSet;
import java.io.Serializable;

import org.hibernate.Session;

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
    private int viewCount;
    private int ratingCount;
    private double ratingAverage;
    private String ratingSource;
    private boolean dataPersisted;    
    private Collection<Tag> tags = new HashSet<Tag>();
    private String ownerName;
    private String attribution;
    private String description;
    private long dynamicServiceDefinitionID;
    private String apiKey;
    private boolean uncheckedAPIEnabled;
    private boolean uncheckedAPIUsingBasicAuth;
    private boolean validatedAPIEnabled;
    private boolean validatedAPIUsingBasicAuth;
    private boolean inheritAccountAPISettings;
    private long refreshDataInterval;
    private List<FeedFolder> folders = new ArrayList<FeedFolder>();
    private boolean visible = true;
    private long parentSourceID;

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

    private Credentials cachedCredentials;

    public FeedDefinition() {
    }

    public long getRefreshDataInterval() {
        return refreshDataInterval;
    }

    public void setRefreshDataInterval(long refreshDataInterval) {
        this.refreshDataInterval = refreshDataInterval;
    }

    public Credentials getCachedCredentials() {
        return cachedCredentials;
    }

    public void setCachedCredentials(Credentials cachedCredentials) {
        this.cachedCredentials = cachedCredentials;
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

    public boolean isValidatedAPIUsingBasicAuth() {
        return validatedAPIUsingBasicAuth;
    }

    public void setValidatedAPIUsingBasicAuth(boolean validatedAPIUsingBasicAuth) {
        this.validatedAPIUsingBasicAuth = validatedAPIUsingBasicAuth;
    }

    public boolean isUncheckedAPIEnabled() {
        return uncheckedAPIEnabled;
    }

    public void setUncheckedAPIEnabled(boolean uncheckedAPIEnabled) {
        this.uncheckedAPIEnabled = uncheckedAPIEnabled;
    }

    public boolean isValidatedAPIEnabled() {
        return validatedAPIEnabled;
    }

    public void setValidatedAPIEnabled(boolean validatedAPIEnabled) {
        this.validatedAPIEnabled = validatedAPIEnabled;
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

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public String getRatingSource() {
        return ratingSource;
    }

    public void setRatingSource(String ratingSource) {
        this.ratingSource = ratingSource;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public double getRatingAverage() {
        return ratingAverage;
    }

    public void setRatingAverage(double ratingAverage) {
        this.ratingAverage = ratingAverage;
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
        feed.setFeedID(getDataFeedID());
        feed.setAttribution(getAttribution());
        feed.setProperties(createProperties());
        feed.setFilterExampleMessage(getFilterExampleMessage());
        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
        List<AnalysisItem> clones = new ArrayList<AnalysisItem>();
        for (AnalysisItem field : getFields()) {
            try {
                AnalysisItem clone = field.clone();
                clones.add(clone);
                replacementMap.put(field.getAnalysisItemID(), clone);
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
        for (AnalysisItem clone : clones) {
            clone.updateIDs(replacementMap);
        }
        List<FeedNode> feedNodes = new ArrayList<FeedNode>();
        for (FeedFolder feedFolder : getFolders()) {
            try {
                FeedFolder clonedFolder = feedFolder.clone();
                clonedFolder.updateIDs(replacementMap);
                if (clonedFolder.getChildFolders().size() > 0 || clonedFolder.getChildItems().size() > 0) {
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
        feed.setName(getFeedName());
        feed.setVisible(isVisible());
        feed.setType(getDataSourceType());
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement solutionQueryStmt = conn.prepareStatement("SELECT SOLUTION_ID FROM SOLUTION_INSTALL WHERE INSTALLED_DATA_SOURCE_ID = ?");
            solutionQueryStmt.setLong(1, getDataFeedID());
            ResultSet solutionRS = solutionQueryStmt.executeQuery();
            if (solutionRS.next()) {
                long solutionID = solutionRS.getLong(1);
                feed.setOriginSolution(solutionID);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            Database.closeConnection(conn);
        }
        return feed;
    }

    public void customStorage(Connection conn) throws SQLException {
        
    }

    public void customLoad(Connection conn) throws SQLException {
        
    }

    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        return (FeedDefinition) super.clone();
    }

    public DataSourceCloneResult cloneDataSource(Connection conn) throws CloneNotSupportedException, SQLException {
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
        for (AnalysisItem analysisItem : replacementMap.values()) {
            analysisItem.updateIDs(replacementMap);
        }
        feedDefinition.setFields(clonedFields);
        List<Tag> clonedTags = new ArrayList<Tag>();
        for (Tag tag : tags) {
            clonedTags.add(tag);
        }
        feedDefinition.setTags(clonedTags);
        feedDefinition.setVisible(visible);
        feedDefinition.setDataFeedID(0);
        feedDefinition.setApiKey(RandomTextGenerator.generateText(12));
        feedDefinition.setCachedCredentials(null);
        feedDefinition.setRefreshDataInterval(0);
        feedDefinition.setAccountVisible(false);
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

    public InitialAnalysis initialAnalysisDefinition() {
        return new InitialAnalysis();
    }

    public int getCredentialsDefinition() {
        return CredentialsDefinition.NO_CREDENTIALS;
    }

    public List<KPI> createKPIs() {
        return new ArrayList<KPI>();
    }

    protected AnalysisItem findAnalysisItem(String key) {
        AnalysisItem item = null;
        for (AnalysisItem field : getFields()) {
            if (field.getKey().toKeyString().equals(key)) {
                item = field;
            }
        }
        return item;
    }

    protected AnalysisItem findAnalysisItemByDisplayName(String key) {
        AnalysisItem item = null;
        for (AnalysisItem field : getFields()) {
            if (field.toDisplay().equals(key)) {
                item = field;
            }
        }
        return item;
    }
    
     public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
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

    public String validateCredentials(Credentials credentials) {
        throw new UnsupportedOperationException();
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        throw new UnsupportedOperationException();
    }

    public Map<String, Key> newDataSourceFields(Credentials credentials) {
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
        if (key == null) {
            System.out.println("*** could not find " + sourceKey);
        }
        return key;
    }

    public void postClone(Connection conn) throws SQLException {
        
    }

    public void delete(Connection conn) throws SQLException {
        onDelete(conn);
        
        // cascade into base classes - see http://bugs.mysql.com/bug.php?id=13102 as to why this code sucks.
        PreparedStatement deleteTodosStmt = conn.prepareStatement("delete from todo_base where todo_id in (select todo_id from configure_data_feed_todo where data_source_id = ?)");
        PreparedStatement deleteAsyncTasksStmt = conn.prepareStatement("DELETE FROM scheduled_task WHERE scheduled_task_id in (select scheduled_task_id from server_refresh_task where data_source_id = ?)");
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
        deleteTodosStmt.setLong(1, dataFeedID);
        deleteAsyncTasksStmt.setLong(1, dataFeedID);
        deleteStmt.setLong(1, dataFeedID);

        deleteTodosStmt.executeUpdate();
        deleteAsyncTasksStmt.executeUpdate();
        deleteStmt.executeUpdate();
    }

    protected void onDelete(Connection conn) throws SQLException {
        DataStorage.delete(dataFeedID, conn);
    }

    protected FeedFolder defineFolder(String name) {
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

    public boolean needsCredentials(List<CredentialFulfillment> existingCredentials) {
        return false;
    }

    public void beforeSave(EIConnection conn) {

    }

    public void decorateLinks(List<AnalysisItem> analysisItems) {
        
    }
}
