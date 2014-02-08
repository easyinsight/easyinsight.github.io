package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.IJoin;
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
 * Date: 4/24/13
 * Time: 11:18 AM
 */
public class InfusionsoftCompositeSource extends CompositeServerDataSource {

    private String infusionApiKey;
    private String url;

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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO INFUSIONSOFT (DATA_SOURCE_ID, URL, API_KEY) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, url);
        insertStmt.setString(3, infusionApiKey);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement queryStmt = conn.prepareStatement("SELECT URL, API_KEY FROM INFUSIONSOFT WHERE DATA_SOURCE_ID = ?");
        queryStmt.setLong(1, getDataFeedID());
        ResultSet rs = queryStmt.executeQuery();
        if (rs.next()) {
            setUrl(rs.getString(1));
            setInfusionApiKey(rs.getString(2));
        }
        queryStmt.close();
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        List<ChildConnection> connections = new ArrayList<ChildConnection>();

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_AFFILIATES, FeedType.INFUSIONSOFT_CONTACTS, InfusionsoftAffiliateSource.CONTACT_ID, InfusionsoftContactSource.ID));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_AFFILIATES, FeedType.INFUSIONSOFT_REFERRAL, InfusionsoftAffiliateSource.AFFILIATE_ID, InfusionsoftReferralSource.AFFILIATE_ID));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_REFERRAL, InfusionsoftContactSource.ID, InfusionsoftReferralSource.CONTACT_ID));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_COMPANIES, InfusionsoftContactSource.COMPANY_ID, InfusionsoftCompanySource.ID));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_EXPENSES, FeedType.INFUSIONSOFT_CONTACTS, InfusionsoftExpenseSource.CONTACT_ID, InfusionsoftContactSource.ID));

        // ecommerce

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_JOBS, InfusionsoftContactSource.ID, InfusionsoftJobSource.CONTACT_ID, IJoin.ONE, IJoin.MANY));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_JOBS, FeedType.INFUSIONSOFT_ORDER_ITEM, InfusionsoftJobSource.JOB_ID, InfusionsoftOrderItemSource.ORDER_ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICES, FeedType.INFUSIONSOFT_INVOICE_ITEM, InfusionsoftInvoiceSource.ID, InfusionsoftInvoiceItemSource.INVOICE_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICE_ITEM, FeedType.INFUSIONSOFT_ORDER_ITEM, InfusionsoftInvoiceItemSource.ORDER_ITEM_ID, InfusionsoftOrderItemSource.ORDER_ITEM_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICES, FeedType.INFUSIONSOFT_JOBS, InfusionsoftInvoiceSource.JOB_ID, InfusionsoftJobSource.JOB_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_INVOICES, FeedType.INFUSIONSOFT_INVOICE_PAYMENT, InfusionsoftInvoiceSource.ID, InfusionsoftInvoicePaymentSource.INVOICE_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_PAYMENT, FeedType.INFUSIONSOFT_INVOICE_PAYMENT, InfusionsoftPaymentSource.PAYMENT_ID, InfusionsoftInvoicePaymentSource.PAYMENT_ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_AFFILIATES, InfusionsoftLeadSource.AFFILIATE_ID, InfusionsoftAffiliateSource.AFFILIATE_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_CONTACTS, InfusionsoftLeadSource.CONTACT_ID, InfusionsoftContactSource.ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_LEAD_SOURCE, InfusionsoftContactSource.LEAD_SOURCE_ID, InfusionsoftLeadSourceSource.ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_LEAD_SOURCE, InfusionsoftLeadSource.LEAD_SOURCE, InfusionsoftLeadSourceSource.ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_LEAD, FeedType.INFUSIONSOFT_STAGE, InfusionsoftLeadSource.STAGE_ID, InfusionsoftStageSource.STAGE_ID, IJoin.ONE, IJoin.ONE));

        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACTS, FeedType.INFUSIONSOFT_CONTACT_TO_TAG, InfusionsoftContactSource.ID, InfusionsoftContactToTag.CONTACT_ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_CONTACT_TO_TAG, FeedType.INFUSIONSOFT_TAG, InfusionsoftContactToTag.GROUP_ID, InfusionsoftTagSource.ID, IJoin.ONE, IJoin.ONE));
        connections.add(new ChildConnection(FeedType.INFUSIONSOFT_TAG, FeedType.INFUSIONSOFT_TAG_GROUP, InfusionsoftTagSource.TAG_CATEGORY_ID, InfusionsoftContactGroupCategorySource.ID, IJoin.ONE, IJoin.ONE));

        return connections;
    }
}
