package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
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
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksStaffFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
