package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class HarvestPaymentSource extends HarvestBaseSource {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static DateFormat DATE_FORMAT = new SimpleDateFormat(XMLDATEFORMAT);

    public static final String UPDATED_SINCE_STRING= "yyyy-MM-dd HH:mm";
    public static final DateFormat UPDATED_SINCE_FORMAT = new SimpleDateFormat(UPDATED_SINCE_STRING);

    public static final String ID = "Payment ID";
    public static final String AMOUNT = "Payment Amount";
    public static final String PAYMENT_NOTES = "Payment Notes";
    public static final String PAYMENT_PAID_AT = "Payment Paid At";
    public static final String PAYMENT_RECORDED_AT = "Payment Recorded At";
    public static final String PAYMENT_INVOICE_ID = "Payment Invoice ID";

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, AMOUNT, PAYMENT_NOTES, PAYMENT_PAID_AT, PAYMENT_RECORDED_AT, PAYMENT_INVOICE_ID);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(PAYMENT_INVOICE_ID), true));
        analysisItems.add(new AnalysisText(keys.get(PAYMENT_NOTES)));
        analysisItems.add(new AnalysisMeasure(keys.get(AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisDateDimension(keys.get(PAYMENT_PAID_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(PAYMENT_RECORDED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        Nodes invoiceNodes;
        try {
            int page = 1;
            DataSet ds = new DataSet();
            do {
                String request = "/invoices?page=" + page++;
                Document invoices = runRestRequest(request, client, builder, source.getUrl(), true, source, false);
                invoiceNodes = invoices.query("/invoices/invoice");
                for(int i = 0;i < invoiceNodes.size();i++) {
                    Node curInvoice = invoiceNodes.get(i);
                    String invoiceID = queryField(curInvoice, "id/text()");
                    String paymentRequest = "/invoices/" + invoiceID + "/payments";
                    Document payments = runRestRequest(paymentRequest, client, builder, source.getUrl(), true, source, false);
                    Nodes paymentNodes = payments.query("/payments/payment");
                    for (int j = 0; j < paymentNodes.size(); j++) {
                        IRow row = ds.createRow();
                        Node curPayment = invoiceNodes.get(i);
                        String amount = queryField(curPayment, "amount/text()");
                        row.addValue(PAYMENT_INVOICE_ID, invoiceID);
                        row.addValue(ID, queryField(curPayment, "id/text()"));
                        row.addValue(PAYMENT_NOTES, queryField(curPayment, "notes/text()"));
                        String paidAt = queryField(curPayment, "paid-at/text()");
                        String recordedAt = queryField(curPayment, "recorded-at/text()");
                        if(amount != null && amount.length() > 0)
                            row.addValue(keys.get(AMOUNT), Double.parseDouble(amount));
                        if(paidAt != null && paidAt.length() > 0)
                            row.addValue(keys.get(PAYMENT_PAID_AT), DATE_FORMAT.parse(paidAt));
                        if(recordedAt != null && recordedAt.length() > 0)
                            row.addValue(keys.get(PAYMENT_RECORDED_AT), DATE_FORMAT.parse(recordedAt));
                    }
                }

            } while(invoiceNodes != null && invoiceNodes.size() > 0);
            IDataStorage.insertData(ds);
        } catch (ReportException re) {
            // going to ignore on this case...
            re.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected boolean clearsData(FeedDefinition parentSource) {
        return true;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_PAYMENT;
    }

    public HarvestPaymentSource() {
        setFeedName("Payment");
    }
}
