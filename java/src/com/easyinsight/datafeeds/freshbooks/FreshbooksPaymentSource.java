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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(INVOICE_ID, PAYMENT_ID, TYPE, AMOUNT, NOTES, CLIENT_ID, PAYMENT_DATE, COUNT);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_PAYMENTS;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
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

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parentDefinition;
        if (freshbooksCompositeSource.isLiveDataSource()) {
            return new DataSet();
        }
        try {
            DataSet dataSet = new DataSet();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            int requestPage = 1;
            int pages;
            int currentPage;
            do {
                Document invoicesDoc = query("payment.list", "<page>" + requestPage + "</page>", freshbooksCompositeSource);
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

    @Override
    public Feed createFeedObject(FeedDefinition parent) {
        FreshbooksCompositeSource freshbooksCompositeSource = (FreshbooksCompositeSource) parent;
        return new FreshbooksPaymentFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
