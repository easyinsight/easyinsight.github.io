package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 6:49:01 PM
 */
public class FreshbooksClientSource extends FreshbooksBaseSource {
    public static final String FIRST_NAME = "Client First Name";
    public static final String CLIENT_ID = "Client ID";
    public static final String EMAIL = "Client Email Address";
    public static final String LAST_NAME = "Client Last Name";
    public static final String NAME = "Client Name";
    public static final String USERNAME = "Client Username";
    public static final String PRIMARY_STREET1 = "Client Primary Street 1";
    public static final String PRIMARY_STREET2 = "Client Primary Street 2";
    public static final String CITY = "Client City";
    public static final String STATE = "Client State";
    public static final String POSTAL = "Client Postal Code";
    public static final String COUNTRY = "Client Country";
    public static final String ORGANIZATION = "Client Organization";
    public static final String WORK_PHONE = "Client Work Phone";
    public static final String NOTES = "Client Internal Notes";
    public static final String COUNT = "Client Count";

    public FreshbooksClientSource() {
        setFeedName("Clients");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(FIRST_NAME, LAST_NAME, NAME, USERNAME, PRIMARY_STREET1, PRIMARY_STREET2, CITY,
                STATE, POSTAL, COUNTRY, ORGANIZATION, WORK_PHONE, COUNT, CLIENT_ID, EMAIL, NOTES);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_CLIENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.FIRST_NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.NOTES), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.EMAIL), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.LAST_NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.USERNAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.PRIMARY_STREET1), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.PRIMARY_STREET2), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.CITY), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.STATE), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.COUNTRY), true));
        items.add(new AnalysisZipCode(keys.get(FreshbooksClientSource.POSTAL), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.ORGANIZATION), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.WORK_PHONE), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksClientSource.COUNT), AggregationTypes.SUM));
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parentDefinition;
        if (freshbooksCompositeSource.isLiveDataSource()) {
            return new DataSet();
        }
        try {
            DataSet dataSet = new DataSet();

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("client.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
                Nodes nodes = invoicesDoc.query("/response/clients");
                if (nodes.size() > 0) {
                    Node invoicesSummaryNode = nodes.get(0);
                    String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                    String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                    pages = Integer.parseInt(pageString);
                    currentPage = Integer.parseInt(currentPageString);
                    Nodes invoices = invoicesDoc.query("/response/clients/client");
                    for (int i = 0; i < invoices.size(); i++) {
                        Node invoice = invoices.get(i);
                        String firstName = queryField(invoice, "first_name/text()");
                        String lastName = queryField(invoice, "last_name/text()");
                        String name = firstName + " " + lastName;
                        String userName = queryField(invoice, "username/text()");
                        String email = queryField(invoice, "email/text()");
                        String clientID = queryField(invoice, "client_id/text()");
                        String workPhone = queryField(invoice, "work_phone/text()");
                        String address1 = queryField(invoice, "p_street1/text()");
                        String address2 = queryField(invoice, "p_street2/text()");
                        String city = queryField(invoice, "p_city/text()");
                        String state = queryField(invoice, "p_state/text()");
                        String zip = queryField(invoice, "p_code/text()");
                        String country = queryField(invoice, "p_country/text()");
                        String organization = queryField(invoice, "organization/text()");

                        IRow row = dataSet.createRow();
                        addValue(row, FreshbooksClientSource.FIRST_NAME, firstName, keys);
                        addValue(row, FreshbooksClientSource.CLIENT_ID, clientID, keys);
                        addValue(row, FreshbooksClientSource.LAST_NAME, lastName, keys);
                        addValue(row, FreshbooksClientSource.NAME, name, keys);
                        addValue(row, FreshbooksClientSource.USERNAME, userName, keys);
                        addValue(row, FreshbooksClientSource.EMAIL, email, keys);
                        addValue(row, FreshbooksClientSource.WORK_PHONE, workPhone, keys);
                        addValue(row, FreshbooksClientSource.PRIMARY_STREET1, address1, keys);
                        addValue(row, FreshbooksClientSource.PRIMARY_STREET2, address2, keys);
                        addValue(row, FreshbooksClientSource.CITY, city, keys);
                        addValue(row, FreshbooksClientSource.STATE, state, keys);
                        addValue(row, FreshbooksClientSource.POSTAL, zip, keys);
                        addValue(row, FreshbooksClientSource.COUNTRY, country, keys);
                        addValue(row, FreshbooksClientSource.ORGANIZATION, organization, keys);
                        addValue(row, FreshbooksClientSource.COUNT, 1, keys);
                    }
                    requestPage++;
                } else {
                    break;
                }
            } while (currentPage < pages);
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getVersion() {
        return 2;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList((DataSourceMigration) new FreshbooksClient1To2(this));
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksClientFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
