package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
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
 * Time: 6:49:41 PM
 */
public class FreshbooksStaffSource extends FreshbooksBaseSource {
    public static final String STAFF_ID = "Staff ID";
    public static final String USERNAME = "Staff Username";
    public static final String FIRST_NAME = "Staff First Name";
    public static final String LAST_NAME = "Staff Last Name";
    public static final String NAME = "Staff Name";
    public static final String EMAIL = "Staff Email";
    public static final String RATE = "Rate";
    public static final String BUSINESS_PHONE = "Staff Business Phone";
    public static final String STREET1 = "Staff Street Line 1";
    public static final String STREET2 = "Staff Street Line 2";
    public static final String CITY = "Staff City";
    public static final String STATE = "Staff State";
    public static final String COUNTRY = "Staff Country";
    public static final String COUNT = "Staff Count";

    public FreshbooksStaffSource() {
        setFeedName("Staff");
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(STAFF_ID, USERNAME, FIRST_NAME, LAST_NAME, EMAIL, RATE,
                BUSINESS_PHONE, STREET1, STREET2, CITY, STATE, COUNTRY, COUNT, NAME);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_STAFF;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.STAFF_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.USERNAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.FIRST_NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.LAST_NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.EMAIL), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.BUSINESS_PHONE), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.STREET1), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.STREET2), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.CITY), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.STATE), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksStaffSource.COUNTRY), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksStaffSource.RATE), FreshbooksStaffSource.RATE, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(FreshbooksStaffSource.COUNT), AggregationTypes.SUM));
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
                String string = "<page>" + requestPage + "</page>";
                Document invoicesDoc = query("staff.list", string, freshbooksCompositeSource);
                Nodes staffNodes = invoicesDoc.query("/response/staff_members");
                if (staffNodes.size() == 0) {
                    return dataSet;
                }
                Node invoicesSummaryNode = staffNodes.get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/staff_members/member");
                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);

                    String firstName = queryField(invoice, "first_name/text()");
                    String lastName = queryField(invoice, "last_name/text()");
                    String name = firstName + " " + lastName;
                    String userName = queryField(invoice, "username/text()");
                    String email = queryField(invoice, "email/text()");
                    String staffID = queryField(invoice, "staff_id/text()");

                    IRow row = dataSet.createRow();
                    addValue(row, FreshbooksStaffSource.FIRST_NAME, firstName, keys);
                    addValue(row, FreshbooksStaffSource.STAFF_ID, staffID, keys);
                    addValue(row, FreshbooksStaffSource.LAST_NAME, lastName, keys);
                    addValue(row, FreshbooksStaffSource.NAME, name, keys);
                    addValue(row, FreshbooksStaffSource.USERNAME, userName, keys);
                    addValue(row, FreshbooksStaffSource.EMAIL, email, keys);
                }
                requestPage++;
            } while (currentPage < pages);

            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksStaffFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
