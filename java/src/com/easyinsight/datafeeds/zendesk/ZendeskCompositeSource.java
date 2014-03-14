package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.composite.CustomFieldTag;
import com.easyinsight.users.Account;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 3/21/11
 * Time: 11:35 PM
 */
public class ZendeskCompositeSource extends CompositeServerDataSource {

    private String url;
    private String zdUserName;
    private String zdPassword;
    private boolean loadComments;
    private boolean hackMethod = false;
    private String zdApiKey;

    public ZendeskCompositeSource() {
        setFeedName("Zendesk");
    }

    public boolean isHackMethod() {
        return hackMethod;
    }

    public void setHackMethod(boolean hackMethod) {
        this.hackMethod = hackMethod;
    }

    public List<CustomFieldTag> customFieldTags() {
        List<CustomFieldTag> customTags = new ArrayList<CustomFieldTag>();
        customTags.add(new CustomFieldTag(ZendeskTicketSource.CUSTOM_FIELD_TICKET, "Custom Ticket Field"));
        return customTags;
    }

    @Override
    public FeedDefinition clone(Connection conn) throws CloneNotSupportedException, SQLException {
        ZendeskCompositeSource zendeskCompositeSource = (ZendeskCompositeSource) super.clone(conn);
        zendeskCompositeSource.setUrl(null);
        zendeskCompositeSource.setZdUserName(null);
        zendeskCompositeSource.setZdPassword(null);
        return zendeskCompositeSource;
    }

    public String validateCredentials() {
        try {
            HttpClient client = new HttpClient();
            client.getParams().setAuthenticationPreemptive(true);
            String username = zdUserName + ((zdApiKey == null) ? "" : "/token");
            String password = (zdApiKey == null) ? zdPassword : zdApiKey;
            Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
            client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            HttpMethod restMethod = new GetMethod(getUrl() + "/organizations.xml");
            client.executeMethod(restMethod);
            String response = restMethod.getResponseBodyAsString();
            if (response.contains("Couldn't authenticate you")) {
                return "Zendesk rejected the specified credentials.";
            }
            return null;
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.ZENDESK_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.ZENDESK_GROUP);
        types.add(FeedType.ZENDESK_ORGANIZATION);
        types.add(FeedType.ZENDESK_USER);
        types.add(FeedType.ZENDESK_TICKET);
        types.add(FeedType.ZENDESK_COMMENTS);
        types.add(FeedType.ZENDESK_GROUP_TO_USER);
        return types;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        Collections.sort(children, new Comparator<IServerDataSourceDefinition>() {

            public int compare(IServerDataSourceDefinition feedDefinition, IServerDataSourceDefinition feedDefinition1) {
                if (feedDefinition.getFeedType().getType() == FeedType.ZENDESK_TICKET.getType()) {
                    return -1;
                }
                return 0;
            }
        });
        return null;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.ZENDESK_USER, FeedType.ZENDESK_ORGANIZATION, ZendeskUserSource.ORGANIZATION_ID, ZendeskOrganizationSource.ID),
                new ChildConnection(FeedType.ZENDESK_TICKET, FeedType.ZENDESK_GROUP, ZendeskTicketSource.GROUP_ID, ZendeskGroupSource.ID),
                new ChildConnection(FeedType.ZENDESK_TICKET, FeedType.ZENDESK_ORGANIZATION, ZendeskTicketSource.ORGANIZATION_ID, ZendeskOrganizationSource.ID),
                new ChildConnection(FeedType.ZENDESK_GROUP, FeedType.ZENDESK_GROUP_TO_USER, ZendeskGroupSource.ID, ZendeskGroupToUserJoinSource.GROUP_ID),
                new ChildConnection(FeedType.ZENDESK_GROUP_TO_USER, FeedType.ZENDESK_USER, ZendeskGroupToUserJoinSource.USER_ID, ZendeskUserSource.ID),
                new ChildConnection(FeedType.ZENDESK_TICKET, FeedType.ZENDESK_COMMENTS, ZendeskTicketSource.TICKET_ID, ZendeskCommentSource.COMMENT_TICKET_ID));
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM ZENDESK WHERE data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ZENDESK (URL, ZENDESK_USERNAME, ZENDESK_PASSWORD, ZENDESK_API_KEY, DATA_SOURCE_ID, LOAD_COMMENTS, HACK_METHOD) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
        insertStmt.setString(1, url);
        insertStmt.setString(2, zdUserName);
        if (zdPassword != null && (zdApiKey == null || zdApiKey.isEmpty())) {
            insertStmt.setString(3, PasswordStorage.encryptString(zdPassword));
        } else {
            insertStmt.setString(3, null);
        }
        if(zdApiKey == null || !zdApiKey.isEmpty())
            insertStmt.setString(4, zdApiKey);
        else
            insertStmt.setString(4, null);
        insertStmt.setLong(5, getDataFeedID());
        insertStmt.setBoolean(6, isLoadComments());
        insertStmt.setBoolean(7, hackMethod);
        insertStmt.execute();
        insertStmt.close();
    }

    /*@Override
    protected void beforeRefresh(Date lastRefreshTime) {
        super.beforeRefresh(lastRefreshTime);
        ZendeskUserCache cache = new ZendeskUserCache();
        try {
            cache.populate(ZendeskUserCache.getHttpClient(this), null, this);
            for (ZendeskUser zendeskUser : cache.getUsers().values()) {
                if (zdUserName.equals(zendeskUser.getEmail())) {
                    System.out.println("...");
                    if (!"admin".equals(zendeskUser.getRole())) {
                        throw new ReportException(new DataSourceConnectivityReportFault("You must be an administrator for the Zendesk connection to function.", this));
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, ZENDESK_USERNAME, ZENDESK_PASSWORD, ZENDESK_API_KEY, LOAD_COMMENTS, HACK_METHOD FROM ZENDESK WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            zdUserName = rs.getString(2);
            zdPassword = rs.getString(3);
            zdApiKey = rs.getString(4);
            loadComments = rs.getBoolean(5);
            if (zdPassword != null) {
                zdPassword = PasswordStorage.decryptString(zdPassword);
            }
            hackMethod = rs.getBoolean(6);
        }
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    private transient List<Comment> comments;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    private transient ZendeskUserCache zendeskUserCache;

    public ZendeskUserCache getOrCreateUserCache(HttpClient httpClient) throws InterruptedException {
        if (zendeskUserCache == null) {
            zendeskUserCache = new ZendeskUserCache();
            zendeskUserCache.populate(httpClient, getUrl(), this);
        }
        return zendeskUserCache;
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
            basecampUrl = basecampUrl + ".zendesk.com";
        }
        return basecampUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getZdUserName() {
        return zdUserName;
    }

    public void setZdUserName(String zdUserName) {
        this.zdUserName = zdUserName;
    }

    public String getZdPassword() {
        return zdPassword;
    }

    public void setZdPassword(String zdPassword) {
        this.zdPassword = zdPassword;
    }

    public boolean isLoadComments() {
        return loadComments;
    }

    public void setLoadComments(boolean loadComments) {
        this.loadComments = loadComments;
    }

    public String getZdApiKey() {
        return zdApiKey;
    }
    public void setZdApiKey(String value) {
        zdApiKey = value;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        comments = null;
    }

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.addField("Zendesk URL", "url", "Your Highrise URL is the browser URL you normally use to connect to Highrise. For example, if you access Highrise as yourcompanyname.highrisehq.com, put yourcompanyname in as the Highrise URL.");
        factory.addField("Zendesk User Name", "zdUserName", "You can find the token on your Highrise page under My Info - API Token.");
        factory.addField("Zendesk API Authentication Token:", "zdApiKey", "You can find the token on your Highrise page under My Info - API Token.");
        factory.type(HTMLConnectionFactory.TYPE_BASIC_AUTH);
    }
}
