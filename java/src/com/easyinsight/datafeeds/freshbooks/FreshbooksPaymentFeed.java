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
 * Time: 3:41:40 PM
 */
public class FreshbooksPaymentFeed extends FreshbooksFeed {
    protected FreshbooksPaymentFeed(String url, String tokenKey, String tokenSecretKey, FreshbooksCompositeSource parentSource) {
        super(url, tokenKey, tokenSecretKey);
    }

    @Override
    public DataSet getAggregateDataSet(Set<AnalysisItem> analysisItems, Collection<FilterDefinition> filters, InsightRequestMetadata insightRequestMetadata, List<AnalysisItem> allAnalysisItems, boolean adminMode, EIConnection conn) throws ReportException {
        try {
            Map<String, Collection<Key>> keys = new HashMap<String, Collection<Key>>();
            for (AnalysisItem analysisItem : analysisItems) {
                Collection<Key> keyColl = keys.get(analysisItem.getKey().toKeyString());
                if (keyColl == null) {
                    keyColl = new ArrayList<Key>();
                    keys.put(analysisItem.getKey().toKeyString(), keyColl);
                }
                keyColl.add(analysisItem.createAggregateKey());
            }
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("payment.list", "<page>" + requestPage + "</page>", conn);
                Nodes paymentSummaryNodes = invoicesDoc.query("/response/payments");
                if (paymentSummaryNodes.size() > 0) {
                    Node invoicesSummaryNode = paymentSummaryNodes.get(0);
                    String pageString = invoicesSummaryNode.query("@pages").get(0).getValue();
                    String currentPageString = invoicesSummaryNode.query("@page").get(0).getValue();
                    pages = Integer.parseInt(pageString);
                    currentPage = Integer.parseInt(currentPageString);
                    Nodes invoices = invoicesDoc.query("/response/payments/payment");

                    for (int i = 0; i < invoices.size(); i++) {
                        Node invoice = invoices.get(i);
                        String invoiceID = queryField(invoice, "invoice_id/text()");
                        String paymentID = queryField(invoice, "payment_id/text()");
                        String type = queryField(invoice, "type/text()");
                        String clientID = queryField(invoice, "client_id/text()");
                        String notes = queryField(invoice, "notes/text()");
                        String amountString = queryField(invoice, "amount/text()");
                        String invoiceDateString = queryField(invoice, "date/text()");
                        Date invoiceDate = df.parse(invoiceDateString);
                        IRow row = dataSet.createRow();
                        addValue(row, FreshbooksPaymentSource.INVOICE_ID, invoiceID, keys);
                        addValue(row, FreshbooksPaymentSource.PAYMENT_ID, paymentID, keys);
                        addValue(row, FreshbooksPaymentSource.CLIENT_ID, clientID, keys);
                        addValue(row, FreshbooksPaymentSource.TYPE, type, keys);
                        addValue(row, FreshbooksPaymentSource.NOTES, notes, keys);
                        if (amountString != null) {
                            addValue(row, FreshbooksPaymentSource.AMOUNT, Double.parseDouble(amountString), keys);
                        }
                        addValue(row, FreshbooksPaymentSource.PAYMENT_DATE, invoiceDate, keys);
                        addValue(row, FreshbooksPaymentSource.COUNT, 1, keys);
                    }
                } else {
                    break;
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
