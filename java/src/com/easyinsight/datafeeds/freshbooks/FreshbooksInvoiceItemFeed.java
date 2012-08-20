package com.easyinsight.datafeeds.freshbooks;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

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
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            FreshbooksCompositeSource parent = (FreshbooksCompositeSource) getParentSource(conn);
            if (!parent.isLiveDataSource()) {
                return super.getAggregateDataSet(analysisItems, filters, insightRequestMetadata, allAnalysisItems, adminMode, conn);
            }
            Map<String, Collection<Key>> keys = new HashMap<String, Collection<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                if (analysisItem.isDerived()) {
                    continue;
                }
                Collection<Key> keyColl = keys.get(analysisItem.getKey().toKeyString());
                if (keyColl == null) {
                    keyColl = new ArrayList<Key>();
                    keys.put(analysisItem.getKey().toKeyString(), keyColl);
                }
                keyColl.add(analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                String string = "<page>" + requestPage + "</page>";
                Document invoicesDoc = query("invoice.list", string, conn);
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
}
