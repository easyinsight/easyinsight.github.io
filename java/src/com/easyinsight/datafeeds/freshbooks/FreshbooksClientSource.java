package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.users.Credentials;
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
    public static final String COUNT = "Client Count";

    public FreshbooksClientSource() {
        setFeedName("Clients");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(FIRST_NAME, LAST_NAME, NAME, USERNAME, PRIMARY_STREET1, PRIMARY_STREET2, CITY,
                STATE, POSTAL, COUNTRY, ORGANIZATION, WORK_PHONE, COUNT, CLIENT_ID, EMAIL);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_CLIENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Credentials credentials, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksClientSource.FIRST_NAME), true));
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

    public DataSet getDataSet(Credentials credentials, Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksClientFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey());
    }
}
