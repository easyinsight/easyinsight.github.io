package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.Feed;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 28, 2010
 * Time: 6:49:19 PM
 */
public class FreshbooksPaymentSource extends FreshbooksBaseSource {
    public static final String INVOICE_ID = "Invoice ID";
    public static final String PAYMENT_ID = "Payment ID";
    public static final String TYPE = "Payment Type";
    public static final String AMOUNT = "Invoice Amount";
    public static final String NOTES = "Notes";
    public static final String CLIENT_ID = "Client ID";
    public static final String PAYMENT_DATE = "Payment Date";
    public static final String COUNT = "Payment Count";

    public FreshbooksPaymentSource() {
        setFeedName("Payments");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(INVOICE_ID, PAYMENT_ID, TYPE, AMOUNT, NOTES, CLIENT_ID, PAYMENT_DATE, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_PAYMENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksPaymentSource.INVOICE_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksPaymentSource.PAYMENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksPaymentSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksPaymentSource.TYPE), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksPaymentSource.NOTES), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksPaymentSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksPaymentSource.AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisDateDimension(keys.get(FreshbooksPaymentSource.PAYMENT_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksPaymentFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
