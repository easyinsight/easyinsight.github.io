package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
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
public class FreshbooksInvoiceFeed extends FreshbooksFeed {
    protected FreshbooksInvoiceFeed(String url, String tokenKey, String tokenSecretKey) {
        super(url, tokenKey, tokenSecretKey);
    }

    @Override
    public AnalysisItemResultMetadata getMetadata(AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        AnalysisItemResultMetadata metadata = analysisItem.createResultMetadata();
        Set<AnalysisItem> set = new HashSet<AnalysisItem>();
        set.add(analysisItem);
        DataSet dataSet = getAggregateDataSet(set, new ArrayList<FilterDefinition>(), insightRequestMetadata, null, false);
        for (IRow row : dataSet.getRows()) {
            metadata.addValue(analysisItem, row.getValue(analysisItem.createAggregateKey()), insightRequestMetadata);
        }
        return metadata;
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode) throws TokenMissingException {
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
                Document invoicesDoc = query("invoice.list", "<page>" + requestPage + "</page>");
                Node invoicesSummaryNode = invoicesDoc.query("/response/invoices").get(0);
                String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                pages = Integer.parseInt(pageString);
                currentPage = Integer.parseInt(currentPageString);
                Nodes invoices = invoicesDoc.query("/response/invoices/invoice");

                for (int i = 0; i < invoices.size(); i++) {
                    Node invoice = invoices.get(i);
                    String invoiceID = queryField(invoice, "invoice_id/text()");
                    String invoiceNumber = queryField(invoice, "number/text()");
                    String clientID = queryField(invoice, "client_id/text()");
                    String status = queryField(invoice, "status/text()");
                    String amountString = queryField(invoice, "amount/text()");
                    String amountOutstandingString = queryField(invoice, "amount_outstanding/text()");
                    String paidString = queryField(invoice, "paid/text()");
                    String invoiceDateString = queryField(invoice, "date/text()");
                    Date invoiceDate = df.parse(invoiceDateString);
                    IRow row = dataSet.createRow();
                    row.addValue(keys.get(FreshbooksInvoiceSource.INVOICE_ID), invoiceID);
                    row.addValue(keys.get(FreshbooksInvoiceSource.INVOICE_NUMBER), invoiceNumber);
                    row.addValue(keys.get(FreshbooksInvoiceSource.CLIENT_ID), clientID);
                    row.addValue(keys.get(FreshbooksInvoiceSource.STATUS), status);
                    if (amountString != null) row.addValue(keys.get(FreshbooksInvoiceSource.AMOUNT), Double.parseDouble(amountString));
                    if (amountOutstandingString != null) row.addValue(keys.get(FreshbooksInvoiceSource.AMOUNT_OUTSTANDING), Double.parseDouble(amountOutstandingString));
                    if (paidString != null) row.addValue(keys.get(FreshbooksInvoiceSource.AMOUNT_PAID), Double.parseDouble(paidString));
                    row.addValue(keys.get(FreshbooksInvoiceSource.INVOICE_DATE), invoiceDate);
                    row.addValue(keys.get(FreshbooksInvoiceSource.COUNT), 1);
                }
                requestPage++;
            } while (currentPage < pages);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public DataSet getDetails(Collection<FilterDefinition> filters) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
