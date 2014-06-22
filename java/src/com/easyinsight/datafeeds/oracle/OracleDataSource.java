package com.easyinsight.datafeeds.oracle;

import com.easyinsight.PasswordStorage;
import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.datafeeds.oracle.client.*;
import com.easyinsight.datafeeds.oracle.history.ExternalReportWSSService;
import com.easyinsight.datafeeds.oracle.history.ExternalReportWSSService_Service;
import com.easyinsight.datafeeds.oracle.history.ReportRequest;
import com.easyinsight.datafeeds.oracle.history.ReportResponse;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import javax.xml.ws.BindingProvider;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.*;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

/**
 * User: jamesboe
 * Date: 6/9/14
 * Time: 8:04 AM
 */
public class OracleDataSource extends CompositeServerDataSource {

    private String oracleUserName;
    private String oraclePassword;
    private String oracleAccount;
    private transient List<TransientAppointment> appointment;
    private transient List<Revenue> childRevenue;
    private transient List<SplitRevenue> splitRevenue;
    private transient List<CategorySummaryRevenue> categorySummaryRevenue;
    private transient List<OpportunityLead> opportunityLead;
    private transient List<OpportunitySource> opportunitySource1;
    private transient List<OpportunityContact> opportunityContact;
    private transient List<RecurringRevenue> recurringRevenue;
    private transient List<OpportunityResponse> opportunityResponse;
    private transient List<RevenuePartner> revenuePartnerPrimary;
    private transient List<Note> note;
    private transient List<OpportunityResource> opportunityResource;
    private transient List<OpportunityCompetitor> opportunityCompetitor3;
    private transient List<OpportunityReference> opportunityReference;
    private transient List<RevenueLineSet> revenueLineSet;
    private transient List<RevenueTerritory> revenueTerritory;
    private transient List<OpportunityCompetitor> opportunityCompetitor2;

    @Override
    protected void refreshDone() {
        super.refreshDone();
        appointment = null;
        childRevenue = null;
        splitRevenue = null;
        categorySummaryRevenue = null;
        opportunityLead = null;
        opportunitySource1 = null;
        opportunityContact = null;
        recurringRevenue = null;
        opportunityResponse = null;
        revenuePartnerPrimary = null;
        note = null;
        opportunityReference = null;
        opportunityCompetitor3 = null;
        revenueLineSet = null;
        revenueTerritory = null;
        opportunityCompetitor2 = null;
    }

    @Override
    protected Collection<ChildConnection> getLiveChildConnections() {
        return Arrays.asList(
                new ChildConnection(FeedType.ORACLE_OPPORTUNITYSOURCE, FeedType.ORACLE_OPPORTUNITYLEAD, OracleOpportunitySource.OPTYID, OracleOpportunityLeadSource.OPTYID),
                new ChildConnection(FeedType.ORACLE_OPPORTUNITYSOURCE, FeedType.ORACLE_OPPORTUNITYCONTACT, OracleOpportunitySource.OPTYID, OracleOpportunityContactSource.OPTYID),
                new ChildConnection(FeedType.ORACLE_OPPORTUNITYSOURCE, FeedType.ORACLE_NOTE, OracleOpportunitySource.OPTYID, OracleNoteSource.SOURCEOBJECTID),
                new ChildConnection(FeedType.ORACLE_OPPORTUNITYSOURCE, FeedType.ORACLE_OPPORTUNITY_HISTORY, OracleOpportunitySource.OPTYID, OracleOpportunityHistorySource.OPPORTUNITY_ID)
        );
    }

    public OracleDataSource() {
        setFeedName("Oracle Sales Cloud");
    }

    public String getOracleUserName() {
        return oracleUserName;
    }

    public void setOracleUserName(String oracleUserName) {
        this.oracleUserName = oracleUserName;
    }

    public String getOraclePassword() {
        return oraclePassword;
    }

    public void setOraclePassword(String oraclePassword) {
        this.oraclePassword = oraclePassword;
    }

    public String getOracleAccount() {
        return oracleAccount;
    }

    public void setOracleAccount(String oracleAccount) {
        this.oracleAccount = oracleAccount;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM ORACLE_SALESCLOUD WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO ORACLE_SALESCLOUD (DATA_SOURCE_ID, ORACLE_USERNAME, ORACLE_PASSWORD, ORACLE_URL) VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, getOracleUserName());
        if (oraclePassword != null) {
            insertStmt.setString(3, PasswordStorage.encryptString(oraclePassword));
        } else {
            insertStmt.setNull(3, Types.VARCHAR);
        }
        insertStmt.setString(4, oracleAccount);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT ORACLE_USERNAME, ORACLE_PASSWORD, ORACLE_URL FROM ORACLE_SALESCLOUD WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            String userName = rs.getString(1);
            String password = rs.getString(2);
            if (password != null) {
                password = PasswordStorage.decryptString(password);
            }
            String url = rs.getString(3);
            setOracleUserName(userName);
            setOraclePassword(password);
            setOracleAccount(url);
        }
        queryStmt.close();
    }

    public String getUrl() {
        if (oracleAccount == null || "".equals(oracleAccount)) {
            return oracleAccount;
        }
        String basecampUrl = ((oracleAccount.startsWith("http://") || oracleAccount.startsWith("https://")) ? "" : "https://") + oracleAccount;
        basecampUrl = basecampUrl.replaceFirst("^http://", "https://");
        if(basecampUrl.endsWith("/")) {
            basecampUrl = basecampUrl.substring(0, basecampUrl.length() - 1);
        }
        /*if (!basecampUrl.contains(".")) {
            basecampUrl = basecampUrl + ".infusionsoft.com";
        }*/
        return basecampUrl;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<>();
        types.add(FeedType.ORACLE_REVENUE);
        types.add(FeedType.ORACLE_SPLITREVENUE);
        types.add(FeedType.ORACLE_CATEGORYSUMMARYREVENUE);
        types.add(FeedType.ORACLE_OPPORTUNITYLEAD);
        types.add(FeedType.ORACLE_OPPORTUNITYSOURCE);
        types.add(FeedType.ORACLE_OPPORTUNITYCONTACT);
        types.add(FeedType.ORACLE_RECURRINGREVENUE);
        types.add(FeedType.ORACLE_OPPORTUNITYRESPONSE);
        types.add(FeedType.ORACLE_OPPORTUNITY_HISTORY);
        types.add(FeedType.ORACLE_REVENUEPARTNER);
        types.add(FeedType.ORACLE_NOTE);
        types.add(FeedType.ORACLE_OPPORTUNITYRESOURCE);
        types.add(FeedType.ORACLE_OPPORTUNITYCOMPETITOR);
        types.add(FeedType.ORACLE_OPPORTUNITYREFERENCE);
        types.add(FeedType.ORACLE_REVENUELINESET);
        types.add(FeedType.ORACLE_REVENUETERRITORY);
        types.add(FeedType.ORACLE_OPPORTUNITYCOMPETITOR);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return getLiveChildConnections();
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.ORACLE_OPPORTUNITYSOURCE.getType()) {
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
    public FeedType getFeedType() {
        return FeedType.ORACLE_COMPOSITE;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    public List<TransientAppointment> getAppointment() {
        return appointment;
    }

    public List<Revenue> getChildRevenue() {
        return childRevenue;
    }

    public List<SplitRevenue> getSplitRevenue() {
        return splitRevenue;
    }

    public List<CategorySummaryRevenue> getCategorySummaryRevenue() {
        return categorySummaryRevenue;
    }

    public void setAppointment(List<TransientAppointment> appointment) {
        this.appointment = appointment;
    }

    public void setChildRevenue(List<Revenue> childRevenue) {
        this.childRevenue = childRevenue;
    }

    public void setSplitRevenue(List<SplitRevenue> splitRevenue) {
        this.splitRevenue = splitRevenue;
    }

    public void setCategorySummaryRevenue(List<CategorySummaryRevenue> categorySummaryRevenue) {
        this.categorySummaryRevenue = categorySummaryRevenue;
    }

    public void setOpportunityLead(List<OpportunityLead> opportunityLead) {
        this.opportunityLead = opportunityLead;
    }

    public List<OpportunityLead> getOpportunityLead() {
        return opportunityLead;
    }

    public void setOpportunitySource1(List<OpportunitySource> opportunitySource1) {
        this.opportunitySource1 = opportunitySource1;
    }

    public List<OpportunitySource> getOpportunitySource1() {
        return opportunitySource1;
    }

    public void setOpportunityContact(List<OpportunityContact> opportunityContact) {
        this.opportunityContact = opportunityContact;
    }

    public List<OpportunityContact> getOpportunityContact() {
        return opportunityContact;
    }

    public void setRecurringRevenue(List<RecurringRevenue> recurringRevenue) {
        this.recurringRevenue = recurringRevenue;
    }

    public List<RecurringRevenue> getRecurringRevenue() {
        return recurringRevenue;
    }

    public void setOpportunityResponse(List<OpportunityResponse> opportunityResponse) {
        this.opportunityResponse = opportunityResponse;
    }

    public List<OpportunityResponse> getOpportunityResponse() {
        return opportunityResponse;
    }

    public void setRevenuePartnerPrimary(List<RevenuePartner> revenuePartnerPrimary) {
        this.revenuePartnerPrimary = revenuePartnerPrimary;
    }

    public List<RevenuePartner> getRevenuePartnerPrimary() {
        return revenuePartnerPrimary;
    }

    public void setNote(List<Note> note) {
        this.note = note;
    }

    public List<Note> getNote() {
        return note;
    }

    public void setOpportunityResource(List<OpportunityResource> opportunityResource) {
        this.opportunityResource = opportunityResource;
    }

    public List<OpportunityResource> getOpportunityResource() {
        return opportunityResource;
    }

    public void setOpportunityCompetitor3(List<OpportunityCompetitor> opportunityCompetitor3) {
        this.opportunityCompetitor3 = opportunityCompetitor3;
    }

    public List<OpportunityCompetitor> getOpportunityCompetitor3() {
        return opportunityCompetitor3;
    }

    public void setOpportunityReference(List<OpportunityReference> opportunityReference) {
        this.opportunityReference = opportunityReference;
    }

    public List<OpportunityReference> getOpportunityReference() {
        return opportunityReference;
    }

    public void setRevenueLineSet(List<RevenueLineSet> revenueLineSet) {
        this.revenueLineSet = revenueLineSet;
    }

    public List<RevenueLineSet> getRevenueLineSet() {
        return revenueLineSet;
    }

    public void setRevenueTerritory(List<RevenueTerritory> revenueTerritory) {
        this.revenueTerritory = revenueTerritory;
    }

    public List<RevenueTerritory> getRevenueTerritory() {
        return revenueTerritory;
    }

    public void setOpportunityCompetitor2(List<OpportunityCompetitor> opportunityCompetitor2) {
        this.opportunityCompetitor2 = opportunityCompetitor2;
    }

    public List<OpportunityCompetitor> getOpportunityCompetitor2() {
        return opportunityCompetitor2;
    }

    public static void main(String[] args) throws Exception {
        ExternalReportWSSService opportunityService = new ExternalReportWSSService_Service(new URL("https://bcy.bi.ap1.oraclecloud.com//xmlpserver/services/ExternalReportWSSService?wsdl")).getExternalReportWSSService();
        BindingProvider prov = (BindingProvider) opportunityService;
        prov.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "bala.gupta");
        prov.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "Welcome1");
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setReportAbsolutePath("/Custom/Customer Relationship Management/OppHistory.xdo");
        reportRequest.setSizeOfDataChunkDownload(-1);
        ReportResponse response = opportunityService.runReport(reportRequest, "");
        byte[] bytes = response.getReportBytes();
        //byte[] decodedBytes = Base64.getDecoder().decode(bytes);
        Document document = new Builder().build(new ByteArrayInputStream(bytes));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        Nodes nodes = document.query("/DATA_DS/G_1");
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            String optyID = node.query("OPTY_ID/text()").get(0).getValue();
            String stageName = node.query("NAME_1/text()").get(0).getValue();
            String stageID = node.query("STG_ID/text()").get(0).getValue();
            String stageEnterDate = node.query("STG_ENTER_DATE/text()").get(0).getValue();
            Date date = sdf.parse(stageEnterDate);
            System.out.println(date);
        }
        //System.out.println(string);
    }
}
