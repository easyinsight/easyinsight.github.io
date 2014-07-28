package com.easyinsight.datafeeds.insightly;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/16/12
 * Time: 4:22 PM
 */
public class InsightlyCompositeSource extends CompositeServerDataSource {
    private String insightlyApiKey;

    public InsightlyCompositeSource() {
        setFeedName("Insightly");
    }

    public String getInsightlyApiKey() {
        return insightlyApiKey;
    }

    public void setInsightlyApiKey(String insightlyApiKey) {
        this.insightlyApiKey = insightlyApiKey;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    private Map<String, List<InsightlyLink>> linkedOrgMap;
    private Map<String, List<InsightlyLink>> linkedContactMap;

    @Override
    protected void refreshDone() {
        super.refreshDone();
        linkedContactMap = null;
        linkedOrgMap = null;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.INSIGHTLY_OPPORTUNITIES.getType()) {
                set.add(s.getFeedType().getType());
                end.add(s);
            }
        }
        for (IServerDataSourceDefinition s : children) {
            if (!set.contains(s.getFeedType().getType())) {
                end.add(s);
            }
        }
        return end;
    }

    public Map<String, List<InsightlyLink>> getLinkedOrgMap() {
        return linkedOrgMap;
    }

    public void setLinkedOrgMap(Map<String, List<InsightlyLink>> linkedOrgMap) {
        this.linkedOrgMap = linkedOrgMap;
    }

    public Map<String, List<InsightlyLink>> getLinkedContactMap() {
        return linkedContactMap;
    }

    public void setLinkedContactMap(Map<String, List<InsightlyLink>> linkedContactMap) {
        this.linkedContactMap = linkedContactMap;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM INSIGHTLY_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO INSIGHTLY_SOURCE (API_KEY, DATA_SOURCE_ID) VALUES (?, ?)");
        insertStmt.setString(1, insightlyApiKey);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT API_KEY FROM INSIGHTLY_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            insightlyApiKey = rs.getString(1);
        }
        getStmt.close();
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INSIGHTLY_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.INSIGHTLY_CONTACTS);
        types.add(FeedType.INSIGHTLY_NOTES);
        types.add(FeedType.INSIGHTLY_NOTE_LINKS);
        types.add(FeedType.INSIGHTLY_ORGANIZATIONS);
        types.add(FeedType.INSIGHTLY_OPPORTUNITIES);
        types.add(FeedType.INSIGHTLY_PROJECTS);
        types.add(FeedType.INSIGHTLY_TASKS);
        types.add(FeedType.INSIGHTLY_OPPORTUNITY_TO_ORGANIZATION);
        types.add(FeedType.INSIGHTLY_OPPORTUNITY_TO_CONTACT);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return getChildConnections();
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.INSIGHTLY_OPPORTUNITIES, FeedType.INSIGHTLY_PROJECTS, InsightlyOpportunitySource.OPPORTUNITY_ID, InsightlyProjectSource.OPPORTUNITY_ID),
            new ChildConnection(FeedType.INSIGHTLY_OPPORTUNITIES, FeedType.INSIGHTLY_ORGANIZATIONS, InsightlyOpportunitySource.LINKED_ORGANIZATION, InsightlyOrganisationSource.ORGANIZATION_ID),
            new ChildConnection(FeedType.INSIGHTLY_PROJECTS, FeedType.INSIGHTLY_TASKS, InsightlyProjectSource.PROJECT_ID, InsightlyTaskSource.PROJECT_ID),
            new ChildConnection(FeedType.INSIGHTLY_OPPORTUNITIES, FeedType.INSIGHTLY_TASKS, InsightlyOpportunitySource.OPPORTUNITY_ID, InsightlyTaskSource.OPPORTUNITY_ID),
            new ChildConnection(FeedType.INSIGHTLY_ORGANIZATIONS, FeedType.INSIGHTLY_CONTACTS, InsightlyOrganisationSource.ORGANIZATION_ID, InsightlyContactSource.ORGANIZATION_ID),
            new ChildConnection(FeedType.INSIGHTLY_ORGANIZATIONS, FeedType.INSIGHTLY_NOTE_LINKS, InsightlyOrganisationSource.ORGANIZATION_ID, InsightlyNoteLinkSource.ORGANIZATION_ID),
            new ChildConnection(FeedType.INSIGHTLY_CONTACTS, FeedType.INSIGHTLY_NOTE_LINKS, InsightlyContactSource.CONTACT_ID, InsightlyNoteLinkSource.CONTACT_ID),
            new ChildConnection(FeedType.INSIGHTLY_OPPORTUNITIES, FeedType.INSIGHTLY_NOTE_LINKS, InsightlyOpportunitySource.OPPORTUNITY_ID, InsightlyNoteLinkSource.OPPORTUNITY_ID),
            new ChildConnection(FeedType.INSIGHTLY_PROJECTS, FeedType.INSIGHTLY_NOTE_LINKS, InsightlyProjectSource.PROJECT_ID, InsightlyNoteLinkSource.PROJECT_ID),
            new ChildConnection(FeedType.INSIGHTLY_NOTE_LINKS, FeedType.INSIGHTLY_NOTES, InsightlyNoteLinkSource.NOTE_ID, InsightlyNoteSource.NOTE_ID),
            new ChildConnection(FeedType.INSIGHTLY_TASKS, FeedType.INSIGHTLY_CONTACTS, InsightlyTaskSource.CONTACT_ID, InsightlyContactSource.CONTACT_ID),
            new ChildConnection(FeedType.INSIGHTLY_TASKS, FeedType.INSIGHTLY_ORGANIZATIONS, InsightlyTaskSource.ORGANIZATION_ID, InsightlyOrganisationSource.ORGANIZATION_ID));
    }
}
