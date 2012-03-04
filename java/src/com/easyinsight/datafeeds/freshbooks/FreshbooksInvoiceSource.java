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
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(INVOICE_ID, CLIENT_ID, AMOUNT, AMOUNT_OUTSTANDING, STATUS, PO_NUMBER, INVOICE_DATE,
                DISCOUNT, COUNT, INVOICE_NUMBER, AMOUNT_PAID);
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.FRESHBOOKS_INVOICE;
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
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
                String string = "<page>" + requestPage + "</page>";
                Document invoicesDoc = query("invoice.list", string, freshbooksCompositeSource);
                Nodes invoiceList = invoicesDoc.query("/response/invoices");
                if (invoiceList.size() > 0) {
                    Node invoicesSummaryNode = invoiceList.get(0);
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
                        addValue(row, FreshbooksInvoiceSource.INVOICE_ID, invoiceID, keys);
                        addValue(row, FreshbooksInvoiceSource.INVOICE_NUMBER, invoiceNumber, keys);
                        addValue(row, FreshbooksInvoiceSource.CLIENT_ID, clientID, keys);
                        addValue(row, FreshbooksInvoiceSource.STATUS, status, keys);
                        if (amountString != null) addValue(row, FreshbooksInvoiceSource.AMOUNT, Double.parseDouble(amountString), keys);
                        if (amountString != null) addValue(row, FreshbooksInvoiceSource.AMOUNT_OUTSTANDING, Double.parseDouble(amountOutstandingString), keys);
                        if (amountString != null) addValue(row, FreshbooksInvoiceSource.AMOUNT_PAID, Double.parseDouble(paidString), keys);
                        addValue(row, FreshbooksInvoiceSource.INVOICE_DATE, invoiceDate, keys);
                        addValue(row, FreshbooksInvoiceSource.COUNT, 1, keys);
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
        return new FreshbooksInvoiceFeed(freshbooksCompositeSource.getUrl(), freshbooksCompositeSource.getTokenKey(),
                freshbooksCompositeSource.getTokenSecretKey(), freshbooksCompositeSource);
    }
}
