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
 * Date: 12/22/10
 * Time: 3:40 PM
 */
public class FreshbooksInvoiceLineSource extends FreshbooksBaseSource {

    public static final String LINE_ID = "Line Item ID";

    public static final String AMOUNT = "Line Item Amount";

    public static final String NAME = "Line Item Name";
    public static final String DESCRIPTION = "Line Item Description";
    public static final String UNIT_COST = "Line Item Unit Cost";
    public static final String QUANTITY = "Line Item Quantity";
    public static final String ITEM_ID = "Line Item - Item ID";
    public static final String INVOICE_ID = "Line Item - Invoice ID";
    public static final String COUNT = "Line Item Count";

    public FreshbooksInvoiceLineSource() {
        setFeedName("Invoice Lines");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(INVOICE_ID, LINE_ID, NAME, AMOUNT, DESCRIPTION, UNIT_COST, QUANTITY, ITEM_ID, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_LINE_ITEMS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceLineSource.INVOICE_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceLineSource.DESCRIPTION), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceLineSource.NAME), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceLineSource.ITEM_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceLineSource.LINE_ID), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceLineSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceLineSource.QUANTITY), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceLineSource.AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceLineSource.UNIT_COST), UNIT_COST, AggregationTypes.AVERAGE, true, FormattingConfiguration.CURRENCY));
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksInvoiceItemFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
