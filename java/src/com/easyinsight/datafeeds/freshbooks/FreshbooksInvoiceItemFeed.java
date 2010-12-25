package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Jul 29, 2010
 * Time: 10:06:38 AM
 */
public class FreshbooksInvoiceItemFeed extends FreshbooksFeed {
    protected FreshbooksInvoiceItemFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata, EIConnection conn) throws ReportException {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        Set<AnalysisItem> set = new HashSet<AnalysisItem>();
        set.add(analysisItem);
        DataSet dataSet = getAggregateDataSet(set, new ArrayList<FilterDefinition>(), insightRequestMetadata, null, false, conn);
        for (IRow row : dataSet.getRows()) {
            metadata.addValue(analysisItem, row.getValue(analysisItem.createAggregateKey()), insightRequestMetadata);
        }
        return metadata;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            Map<String, Key> keys = new HashMap<String, Key>();
            for (AnalysisItem analysisItem : analysisItems) {
                keys.put(analysisItem.getKey().toKeyString(), analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("invoice.list", "<page>" + requestPage + "</page>", conn);
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
                        row.addValue(keys.get(FreshbooksInvoiceLineSource.INVOICE_ID), invoiceID);
                        String lineItemID = queryField(lineItem, "line_id/text()");
                        String amountString = queryField(lineItem, "amount/text()");
                        String quantityString = queryField(lineItem, "quantity/text()");
                        String unitCostString = queryField(lineItem, "unit_cost/text()");
                        String description = queryField(lineItem, "description/text()");
                        String name = queryField(lineItem, "name/text()");
                        row.addValue(keys.get(FreshbooksInvoiceLineSource.DESCRIPTION), description);
                        row.addValue(keys.get(FreshbooksInvoiceLineSource.LINE_ID), lineItemID);
                        row.addValue(keys.get(FreshbooksInvoiceLineSource.NAME), name);
                        row.addValue(keys.get(FreshbooksInvoiceLineSource.COUNT), 1);
                        if (amountString != null) row.addValue(keys.get(FreshbooksInvoiceLineSource.AMOUNT), Double.parseDouble(amountString));
                        if (quantityString != null) row.addValue(keys.get(FreshbooksInvoiceLineSource.QUANTITY), Double.parseDouble(quantityString));
                        if (unitCostString != null) row.addValue(keys.get(FreshbooksInvoiceLineSource.UNIT_COST), Double.parseDouble(unitCostString));
                    }
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
}
