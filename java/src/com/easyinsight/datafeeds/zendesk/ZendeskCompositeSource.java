package com.easyinsight.datafeeds.zendesk;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.*;
import com.easyinsight.datafeeds.DataSourceCloneResult;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIUtil;
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

    public ZendeskCompositeSource() {
        setFeedName("Zendesk");
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
            Credentials defaultcreds = new UsernamePasswordCredentials(zdUserName, zdPassword);
            client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
            HttpMethod restMethod = new GetMethod(getUrl() + "/organizations.xml");
            client.executeMethod(restMethod);
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
        types.add(FeedType.ZENDESK_GROUP_TO_USER);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.ZENDESK_USER, FeedType.ZENDESK_ORGANIZATION, ZendeskUserSource.ORGANIZATION_ID, ZendeskOrganizationSource.ID),
                new ChildConnection(FeedType.ZENDESK_TICKET, FeedType.ZENDESK_GROUP, ZendeskTicketSource.GROUP_ID, ZendeskGroupSource.ID),
                new ChildConnection(FeedType.ZENDESK_TICKET, FeedType.ZENDESK_ORGANIZATION, ZendeskTicketSource.ORGANIZATION_ID, ZendeskOrganizationSource.ID),
                new ChildConnection(FeedType.ZENDESK_GROUP, FeedType.ZENDESK_GROUP_TO_USER, ZendeskGroupSource.ID, ZendeskGroupToUserJoinSource.GROUP_ID),
                new ChildConnection(FeedType.ZENDESK_GROUP_TO_USER, FeedType.ZENDESK_USER, ZendeskGroupToUserJoinSource.USER_ID, ZendeskUserSource.ID));
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM ZENDESK WHERE data_source_id = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ZENDESK (URL, ZENDESK_USERNAME, ZENDESK_PASSWORD, DATA_SOURCE_ID) " +
                "VALUES (?, ?, ?, ?)");
        insertStmt.setString(1, url);
        insertStmt.setString(2, zdUserName);
        if (zdPassword != null) {
            insertStmt.setString(3, PasswordStorage.encryptString(zdPassword));
        } else {
            insertStmt.setString(3, null);
        }
        insertStmt.setLong(4, getDataFeedID());
        insertStmt.execute();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, ZENDESK_USERNAME, ZENDESK_PASSWORD FROM ZENDESK WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            url = rs.getString(1);
            zdUserName = rs.getString(2);
            zdPassword = rs.getString(3);
            if (zdPassword != null) {
                zdPassword = PasswordStorage.decryptString(zdPassword);
            }
        }
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public List<KPI> createKPIs() {
        List<KPI> kpis = new ArrayList<KPI>();
        kpis.add(KPIUtil.createKPIWithFilters("Number of Urgent Open Tickets", "sign_warning.png", (AnalysisMeasure) findAnalysisItemByDisplayName(ZendeskTicketSource.COUNT),
                Arrays.asList((FilterDefinition) new FilterValueDefinition(findAnalysisItem(ZendeskTicketSource.PRIORITY), true, Arrays.asList((Object) "Urgent")),
                        new FilterValueDefinition(findAnalysisItem(ZendeskTicketSource.STATUS), true, Arrays.asList((Object) "Open"))), KPI.GOOD, 1));
        kpis.add(KPIUtil.createKPIWithFilters("Number of Open Tickets", "inbox.png", (AnalysisMeasure) findAnalysisItemByDisplayName(ZendeskTicketSource.COUNT),
                Arrays.asList((FilterDefinition) new FilterValueDefinition(findAnalysisItem(ZendeskTicketSource.STATUS), true, Arrays.asList((Object) "Open"))), KPI.GOOD, 1));
        kpis.add(KPIUtil.createKPIWithFilters("Number of Pending Tickets", "inbox.png", (AnalysisMeasure) findAnalysisItemByDisplayName(ZendeskTicketSource.COUNT),
                Arrays.asList((FilterDefinition) new FilterValueDefinition(findAnalysisItem(ZendeskTicketSource.STATUS), true, Arrays.asList((Object) "Pending"))), KPI.GOOD, 1));
        kpis.add(KPIUtil.createKPIForDateFilter("Tickets Created in the Last 30 Days", "document.png", (AnalysisMeasure) findAnalysisItemByDisplayName(ZendeskTicketSource.COUNT),
                (AnalysisDimension) findAnalysisItem(ZendeskTicketSource.CREATED_AT), MaterializedRollingFilterDefinition.MONTH, new ArrayList<FilterDefinition>(), KPI.GOOD, 1));
        kpis.add(KPIUtil.createKPIForDateFilter("Tickets Solved in the Last 30 Days", "check.png", (AnalysisMeasure) findAnalysisItemByDisplayName(ZendeskTicketSource.COUNT),
                (AnalysisDimension) findAnalysisItem(ZendeskTicketSource.SOLVED_AT), MaterializedRollingFilterDefinition.MONTH, new ArrayList<FilterDefinition>(), KPI.GOOD, 1));
        return kpis;
    }

    public String getUrl() {
        if (url == null || "".equals(url)) {
            return url;
        }
        String basecampUrl = ((url.startsWith("http://") || url.startsWith("https://")) ? "" : "http://") + url;
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        if(!(basecampUrl.endsWith(".zendesk.com")))
            basecampUrl = basecampUrl + ".zendesk.com";
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
}
