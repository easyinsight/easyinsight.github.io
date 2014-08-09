package com.easyinsight.datafeeds.freshdesk;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
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
 * Date: 1/10/14
 * Time: 11:45 AM
 */
public class FreshdeskCompositeSource extends CompositeServerDataSource {
    private String url;
    private String freshdeskApiKey;

    private transient List<String> ticketIDs;
    private transient Map<String, List<Map>> notes;
    private transient Map<String, List<Map>> statusUpdates;
    private transient Map<String, List<Map>> assignmentUpdates;

    public FreshdeskCompositeSource() {
        setFeedName("Freshdesk");
    }

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.addField("Freshdesk URL", "url", "Your Freshdesk URL is the browser URL you normally use to connect to Freshdesk. For example, if you access Freshdesk as yourcompanyname.freshdesk.com, put yourcompanyname in as the Freshdesk URL.");
        factory.addField("Freshdesk API Authentication Token:", "token", "You can find the token on your Freshdesk page under My Info - API Token.");
        factory.type(HTMLConnectionFactory.TYPE_BASIC_AUTH);
    }

    public Map<String, List<Map>> getNotes() {
        return notes;
    }

    public void setNotes(Map<String, List<Map>> notes) {
        this.notes = notes;
    }

    public Map<String, List<Map>> getStatusUpdates() {
        return statusUpdates;
    }

    public void setStatusUpdates(Map<String, List<Map>> statusUpdates) {
        this.statusUpdates = statusUpdates;
    }

    public Map<String, List<Map>> getAssignmentUpdates() {
        return assignmentUpdates;
    }

    public void setAssignmentUpdates(Map<String, List<Map>> assignmentUpdates) {
        this.assignmentUpdates = assignmentUpdates;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM FRESHDESK WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement saveStmt = conn.prepareStatement("INSERT INTO FRESHDESK (DATA_SOURCE_ID, URL, API_TOKEN) VALUES (?, ?, ?)");
        saveStmt.setLong(1, getDataFeedID());
        saveStmt.setString(2, url);
        saveStmt.setString(3, freshdeskApiKey);
        saveStmt.execute();
        saveStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, API_TOKEN FROM FRESHDESK WHERE data_source_id = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            freshdeskApiKey = rs.getString(2);
        }
        queryStmt.close();
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFreshdeskApiKey() {
        return freshdeskApiKey;
    }

    public void setFreshdeskApiKey(String freshdeskApiKey) {
        this.freshdeskApiKey = freshdeskApiKey;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.FRESHDESK_TICKET);
        //types.add(FeedType.FRESHDESK_TIME);
        types.add(FeedType.FRESHDESK_ACTIVITY);
        types.add(FeedType.FRESHDESK_NOTE);
        types.add(FeedType.FRESHDESK_ASSIGNMENT);
        types.add(FeedType.FRESHDESK_STATUS);
        return types;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.FRESHDESK_TICKET.getType()) {
                set.add(s.getFeedType().getType());
                end.add(s);
            }
        }
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.FRESHDESK_ACTIVITY.getType()) {
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

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return getLiveChildConnections();
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(
                new ChildConnection(FeedType.FRESHDESK_TICKET, FeedType.FRESHDESK_ACTIVITY, FreshdeskTicketSource.DISPLAY_ID, FreshdeskActivitySource.ACTIVITY_TICKET_ID),
                new ChildConnection(FeedType.FRESHDESK_TICKET, FeedType.FRESHDESK_ASSIGNMENT, FreshdeskTicketSource.DISPLAY_ID, FreshdeskAssignmentSource.ASSIGNMENT_TICKET_ID),
                new ChildConnection(FeedType.FRESHDESK_TICKET, FeedType.FRESHDESK_STATUS, FreshdeskTicketSource.DISPLAY_ID, FreshdeskStatusSource.STATUS_TICKET_ID),
                new ChildConnection(FeedType.FRESHDESK_TICKET, FeedType.FRESHDESK_NOTE, FreshdeskTicketSource.DISPLAY_ID, FreshdeskNoteSource.NOTE_TICKET_ID)
        );
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHDESK_COMPOSITE;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "https://") + url;
        basecampUrl = basecampUrl.replaceFirst("^http://", "https://");
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if (!basecampUrl.contains(".")) {
            basecampUrl = basecampUrl + ".freshdesk.com";
        }
        return basecampUrl;
    }

    @Override
    protected void refreshDone() {
        ticketIDs = null;
        notes = null;
        statusUpdates = null;
        assignmentUpdates = null;
    }

    public void setTicketIDs(List<String> ticketIDs) {
        this.ticketIDs = ticketIDs;
    }

    public List<String> getTicketIDs() {
        return ticketIDs;
    }
}
