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
 * Date: Jul 22, 2010
 * Time: 7:07:42 PM
 */
public class FreshbooksInvoiceSource extends FreshbooksBaseSource {

    public static final String INVOICE_ID = "Invoice ID";
    public static final String INVOICE_NUMBER = "Invoice Number";
    public static final String CLIENT_ID = "Invoice Client ID";
    public static final String AMOUNT = "Invoice Amount";
    public static final String AMOUNT_OUTSTANDING = "Invoice Amount Outstanding";
    public static final String AMOUNT_PAID = "Invoice Amount Paid";
    public static final String STATUS = "Invoice Status";
    public static final String PO_NUMBER = "PO Number";
    public static final String INVOICE_DATE = "Invoice Date";
    public static final String DISCOUNT = "Discount";
    public static final String COUNT = "Invoice Count";

    public FreshbooksInvoiceSource() {
        setFeedName("Invoices");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(INVOICE_ID, CLIENT_ID, AMOUNT, AMOUNT_OUTSTANDING, STATUS, PO_NUMBER, INVOICE_DATE,
                DISCOUNT, COUNT, INVOICE_NUMBER, AMOUNT_PAID);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_INVOICE;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceSource.INVOICE_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceSource.INVOICE_NUMBER), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceSource.CLIENT_ID), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceSource.STATUS), true));
        items.add(new AnalysisDimension(keys.get(FreshbooksInvoiceSource.PO_NUMBER), true));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceSource.COUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceSource.DISCOUNT), AggregationTypes.SUM));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceSource.AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceSource.AMOUNT_OUTSTANDING), AMOUNT_OUTSTANDING, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisMeasure(keys.get(FreshbooksInvoiceSource.AMOUNT_PAID), AMOUNT_PAID, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        items.add(new AnalysisDateDimension(keys.get(FreshbooksInvoiceSource.INVOICE_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        return items;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) {
        return new DataSet();
    }

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksInvoiceFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey());
    }
}
