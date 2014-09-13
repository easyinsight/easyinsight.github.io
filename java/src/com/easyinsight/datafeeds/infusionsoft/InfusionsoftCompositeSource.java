package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.HTMLConnectionFactory;
import com.easyinsight.datafeeds.IJoin;
import com.easyinsight.datafeeds.IServerDataSourceDefinition;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.users.Account;
import org.apache.xmlrpc.XmlRpcException;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 4/24/13
 * Time: 11:18 AM
 */
public class InfusionsoftCompositeSource extends CompositeServerDataSource {

    private String infusionApiKey;
    private String url;
    private String userID;

    public void configureFactory(HTMLConnectionFactory factory) {
        factory.addField("Infusionsoft URL", "url");
        factory.addField("Infusionsoft API Key", "infusionApiKey");
        factory.type(HTMLConnectionFactory.TYPE_BASIC_AUTH);
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    private Map<String, String> userCache;
    private Map<String, String> leadStageCache;
    private Map<String, String> leadSourceCache;
    private Map<String, String> leadStatusCache;
    private Map<String, String> contactCache;
    private Map<String, String> leadSourceCategoryCache;
    private Map<String, String> affiliateCache;
    private Map<String, String> productCache;

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.INFUSIONSOFT_STAGE.getType() ||
                    s.getFeedType().getType() == FeedType.INFUSIONSOFT_USERS.getType() ||
                    s.getFeedType().getType() == FeedType.INFUSIONSOFT_LEAD_SOURCE.getType()) {
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

    public Map<String, String> getUserCache() {
        return userCache;
    }

    public void setUserCache(Map<String, String> userCache) {
        this.userCache = userCache;
    }

    public Map<String, String> getLeadStageCache() {
        return leadStageCache;
    }

    public Map<String, String> getLeadSourceCache() {
        return leadSourceCache;
    }

    public void setLeadSourceCache(Map<String, String> leadSourceCache) {
        this.leadSourceCache = leadSourceCache;
    }

    public void setLeadStageCache(Map<String, String> leadStageCache) {
        this.leadStageCache = leadStageCache;
    }

    public Map<String, String> getLeadStatusCache() {
        return leadStatusCache;
    }

    public void setLeadStatusCache(Map<String, String> leadStatusCache) {
        this.leadStatusCache = leadStatusCache;
    }

    public Map<String, String> getContactCache() {
        return contactCache;
    }

    public void setContactCache(Map<String, String> contactCache) {
        this.contactCache = contactCache;
    }

    public Map<String, String> getLeadSourceCategoryCache() {
        return leadSourceCategoryCache;
    }

    public void setLeadSourceCategoryCache(Map<String, String> leadSourceCategoryCache) {
        this.leadSourceCategoryCache = leadSourceCategoryCache;
    }

    public Map<String, String> getAffiliateCache() {
        return affiliateCache;
    }

    public void setAffiliateCache(Map<String, String> affiliateCache) {
        this.affiliateCache = affiliateCache;
    }

    public Map<String, String> getProductCache() {
        return productCache;
    }

    public void setProductCache(Map<String, String> productCache) {
        this.productCache = productCache;
    }

    public InfusionsoftCompositeSource() {
        setFeedName("Infusionsoft");
    }

    public String getInfusionApiKey() {
        return infusionApiKey;
    }

    public void setInfusionApiKey(String infusionApiKey) {
        this.infusionApiKey = infusionApiKey;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
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
            basecampUrl = basecampUrl + ".infusionsoft.com";
        }
        return basecampUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.INFUSIONSOFT_LEAD);
        types.add(FeedType.INFUSIONSOFT_STAGE);
        types.add(FeedType.INFUSIONSOFT_STAGE_HISTORY);
        types.add(FeedType.INFUSIONSOFT_AFFILIATES);

        types.add(FeedType.INFUSIONSOFT_CAMPAIGNEE);
        types.add(FeedType.INFUSIONSOFT_CAMPAIGN_STEP);
        types.add(FeedType.INFUSIONSOFT_REFERRAL);
        types.add(FeedType.INFUSIONSOFT_TAG_GROUP);

        types.add(FeedType.INFUSIONSOFT_COMPANIES);
        types.add(FeedType.INFUSIONSOFT_CONTACTS);
        types.add(FeedType.INFUSIONSOFT_JOBS);
        types.add(FeedType.INFUSIONSOFT_SUBSCRIPTIONS);
        types.add(FeedType.INFUSIONSOFT_PRODUCTS);
        types.add(FeedType.INFUSIONSOFT_PRODUCT_INTEREST);
        types.add(FeedType.INFUSIONSOFT_CONTACT_ACTION);
        types.add(FeedType.INFUSIONSOFT_RECURRING_ORDERS);
        types.add(FeedType.INFUSIONSOFT_CAMPAIGNS);
        types.add(FeedType.INFUSIONSOFT_ORDER_ITEM);
        types.add(FeedType.INFUSIONSOFT_PAYMENT);
        types.add(FeedType.INFUSIONSOFT_INVOICES);
        types.add(FeedType.INFUSIONSOFT_INVOICE_ITEM);
        types.add(FeedType.INFUSIONSOFT_INVOICE_PAYMENT);
        types.add(FeedType.INFUSIONSOFT_LEAD_SOURCE);
        types.add(FeedType.INFUSIONSOFT_JOB_RECURRING_INSTANCE);
        types.add(FeedType.INFUSIONSOFT_PAY_PLAN);
        types.add(FeedType.INFUSIONSOFT_EXPENSES);
        types.add(FeedType.INFUSIONSOFT_TAG);
        types.add(FeedType.INFUSIONSOFT_CONTACT_TO_TAG);
        types.add(FeedType.INFUSIONSOFT_USERS);
        return types;
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        cache = null;
        leadStageCache = null;
        leadStatusCache = null;
        leadSourceCache = null;
    }

    private InfusionsoftCustomCache cache;

    public InfusionsoftCustomCache getCache() {
        if (cache == null) {
            cache = new InfusionsoftCustomCache();
            cache.query(this);
        }
        return cache;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM INFUSIONSOFT WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO INFUSIONSOFT (DATA_SOURCE_ID, URL, API_KEY, USER_ID) VALUES (?, ?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, url);
        insertStmt.setString(3, infusionApiKey);
        insertStmt.setString(4, userID);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, API_KEY, USER_ID FROM INFUSIONSOFT WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setUrl(rs.getString(1));
            setInfusionApiKey(rs.getString(2));
            setUserID(rs.getString(3));
        }
        queryStmt.close();
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        List<ChildConnection> connections = new ArrayList<ChildConnection>();

        // CRM (opportunities, contacts, companies)

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_STAGE_HISTORY, InfusionsoftLeadSource.LEAD_ID, InfusionsoftStageMoveSource.OPPORTUNITY_ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_CONTACTS, InfusionsoftLeadSource.CONTACT_ID, InfusionsoftContactSource.ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_COMPANIES, InfusionsoftContactSource.COMPANY_ID, InfusionsoftCompanySource.ID));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_PRODUCT_INTEREST, InfusionsoftLeadSource.LEAD_ID, InfusionsoftProductInterestSource.OBJECT_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_PRODUCT_INTEREST, FeedType.INFUSIONSOFT_PRODUCTS, InfusionsoftProductInterestSource.PRODUCT_ID, InfusionsoftProductSource.PRODUCT_ID));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_PRODUCTS, FeedType.INFUSIONSOFT_SUBSCRIPTIONS, InfusionsoftProductSource.PRODUCT_ID, InfusionsoftSubscriptionSource.PRODUCT_ID));


        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_STAGE, InfusionsoftLeadSource.STAGE_ID, InfusionsoftStageSource.STAGE_ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_AFFILIATES, InfusionsoftLeadSource.AFFILIATE_ID, InfusionsoftAffiliateSource.AFFILIATE_ID, IJoin.ONE, IJoin.ONE));


        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_AFFILIATES, FeedType.INFUSIONSOFT_CONTACTS, InfusionsoftAffiliateSource.CONTACT_ID, InfusionsoftContactSource.ID));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_AFFILIATES, FeedType.INFUSIONSOFT_REFERRAL, InfusionsoftAffiliateSource.AFFILIATE_ID, InfusionsoftReferralSource.AFFILIATE_ID));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_REFERRAL, InfusionsoftContactSource.ID, InfusionsoftReferralSource.CONTACT_ID));

        // ecommerce

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_JOBS, InfusionsoftContactSource.ID, InfusionsoftJobSource.CONTACT_ID, IJoin.ONE, IJoin.MANY));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_JOBS, FeedType.INFUSIONSOFT_ORDER_ITEM, InfusionsoftJobSource.JOB_ID, InfusionsoftOrderItemSource.ORDER_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_ORDER_ITEM, FeedType.INFUSIONSOFT_PRODUCTS, InfusionsoftOrderItemSource.PRODUCT_ID, InfusionsoftProductSource.PRODUCT_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_JOBS, FeedType.INFUSIONSOFT_INVOICES, InfusionsoftJobSource.JOB_ID, InfusionsoftInvoiceSource.JOB_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICES, FeedType.INFUSIONSOFT_INVOICE_ITEM, InfusionsoftInvoiceSource.ID, InfusionsoftInvoiceItemSource.INVOICE_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_EXPENSES, FeedType.INFUSIONSOFT_CONTACTS, InfusionsoftExpenseSource.CONTACT_ID, InfusionsoftContactSource.ID));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICES, FeedType.INFUSIONSOFT_INVOICE_PAYMENT, InfusionsoftInvoiceSource.ID, InfusionsoftInvoicePaymentSource.INVOICE_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICE_PAYMENT, FeedType.INFUSIONSOFT_PAYMENT, InfusionsoftInvoicePaymentSource.PAYMENT_ID, InfusionsoftPaymentSource.PAYMENT_ID,  IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_RECURRING_ORDERS, FeedType.INFUSIONSOFT_PRODUCTS, InfusionsoftRecurringOrderSource.PRODUCT_ID, InfusionsoftProductSource.PRODUCT_ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_LEAD_SOURCE, InfusionsoftContactSource.LEAD_SOURCE_ID, InfusionsoftLeadSourceSource.ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_CONTACT_TO_TAG, InfusionsoftContactSource.ID, InfusionsoftContactToTag.CONTACT_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACT_TO_TAG, FeedType.INFUSIONSOFT_TAG, InfusionsoftContactToTag.GROUP_ID, InfusionsoftTagSource.ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_TAG, FeedType.INFUSIONSOFT_TAG_GROUP, InfusionsoftTagSource.TAG_CATEGORY_ID, InfusionsoftContactGroupCategorySource.ID, IJoin.ONE, IJoin.ONE));


        return connections;
    }

    public List<InfusionsoftReport> getAvailableReports() throws MalformedURLException, XmlRpcException {
        return new InfusionsoftSavedFilterSource().getReports(this);
    }

    public List<InfusionsoftUser> getUsers() throws MalformedURLException, XmlRpcException {
        return new InfusionsoftUserSource().getUsers(this);
    }
}
