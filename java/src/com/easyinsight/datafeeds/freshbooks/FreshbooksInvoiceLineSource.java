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
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(INVOICE_ID, LINE_ID, NAME, AMOUNT, DESCRIPTION, UNIT_COST, QUANTITY, ITEM_ID, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_LINE_ITEMS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
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
                Document invoicesDoc = query("invoice.list", string, freshbooksCompositeSource);
                Nodes invoiceList = invoicesDoc.query("/response/invoices");
                if (invoiceList.size() > 0) {
                    Node invoicesSummaryNode = invoicesDoc.query("/response/invoices").get(0);
                    String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                    String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                    pages = Integer.parseInt(pageString);
                    currentPage = Integer.parseInt(currentPageString);
                    Nodes invoices = invoicesDoc.query("/response/invoices/invoice");

                    for (int i = 0; i < invoices.size(); i++) {
                        Node invoice = invoices.get(i);
                        String invoiceID = queryField(invoice, "invoice_id/text()");
                        Nodes lineItems = invoice.query("lines/line");
                        for (int j = 0; j < lineItems.size(); j++) {
                            Node lineItem = lineItems.get(j);
                            IRow row = dataSet.createRow();
                            addValue(row, FreshbooksInvoiceLineSource.INVOICE_ID, invoiceID, keys);
                            String lineItemID = queryField(lineItem, "line_id/text()");
                            String amountString = queryField(lineItem, "amount/text()");
                            String quantityString = queryField(lineItem, "quantity/text()");
                            String unitCostString = queryField(lineItem, "unit_cost/text()");
                            String description = queryField(lineItem, "description/text()");
                            String name = queryField(lineItem, "name/text()");
                            addValue(row, FreshbooksInvoiceLineSource.DESCRIPTION, description, keys);
                            addValue(row, FreshbooksInvoiceLineSource.LINE_ID, lineItemID, keys);
                            addValue(row, FreshbooksInvoiceLineSource.NAME, name, keys);
                            addValue(row, FreshbooksInvoiceLineSource.COUNT, 1, keys);
                            if (amountString != null) addValue(row, FreshbooksInvoiceLineSource.AMOUNT, Double.parseDouble(amountString), keys);
                            if (amountString != null) addValue(row, FreshbooksInvoiceLineSource.QUANTITY, Double.parseDouble(quantityString), keys);
                            if (amountString != null) addValue(row, FreshbooksInvoiceLineSource.UNIT_COST, Double.parseDouble(unitCostString), keys);
                        }
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
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksInvoiceItemFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
