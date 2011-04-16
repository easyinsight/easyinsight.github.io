package com.easyinsight.datafeeds.harvest;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import com.easyinsight.storage.IWhere;
import com.easyinsight.storage.StringWhere;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: abaldwin
 * Date: 4/4/11
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class HarvestInvoiceSource extends HarvestBaseSource {
    public static final String XMLDATEFORMAT = "yyyy-MM-dd";
    public static DateFormat DATE_FORMAT = new SimpleDateFormat(XMLDATEFORMAT);

    public static final String UPDATED_SINCE_STRING= "yyyy-MM-dd HH:mm";
    public static final DateFormat UPDATED_SINCE_FORMAT = new SimpleDateFormat(UPDATED_SINCE_STRING);

    public static final String ID = "Invoice ID";
    public static final String AMOUNT = "Invoice Amount";
    public static final String DUE_AMOUNT = "Invoice Due Amount";
    public static final String DUE_AT = "Invoice Due At";
    public static final String DUE_AT_HUMAN_FORMAT = "Invoice Due At - Human Readable";
    public static final String PERIOD_END = "Invoice Period End";
    public static final String PERIOD_START = "Invoice Period Start";
    public static final String CLIENT_ID = "Invoice - Client ID";
    public static final String CURRENCY = "Invoice Currency";
    public static final String ISSUED_AT = "Invoice Issued At";
    public static final String NOTES = "Invoice Notes";
    public static final String NUMBER = "Invoice Number";
    public static final String PURCHASE_ORDER = "Invoice Purchase Order";
    public static final String CLIENT_KEY = "Invoice Client Key";
    public static final String STATE = "Invoice State";
    public static final String TAX = "Invoice Tax %";
    public static final String TAX_2 = "Invoice Tax 2 %";
    public static final String TAX_AMOUNT = "Invoice Tax Amount";
    public static final String TAX_2_AMOUNT = "Invoice Tax 2 Amount";

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, AMOUNT, DUE_AMOUNT, DUE_AT, DUE_AT_HUMAN_FORMAT, PERIOD_START, PERIOD_END,
                CLIENT_ID, CURRENCY, ISSUED_AT, NOTES, NUMBER, PURCHASE_ORDER, CLIENT_KEY, STATE, TAX, TAX_2,
                TAX_AMOUNT, TAX_2_AMOUNT);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        AnalysisItem idDimension = new AnalysisDimension(keys.get(ID), true);
        idDimension.setHidden(true);
        analysisItems.add(idDimension);
        AnalysisItem clientId = new AnalysisDimension(keys.get(CLIENT_ID), true);
        clientId.setHidden(true);
        analysisItems.add(clientId);
        analysisItems.add(new AnalysisMeasure(keys.get(AMOUNT), AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(DUE_AMOUNT), DUE_AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisDateDimension(keys.get(DUE_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(DUE_AT_HUMAN_FORMAT), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(PERIOD_START), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDateDimension(keys.get(PERIOD_END), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisDimension(keys.get(CURRENCY), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(ISSUED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisText(keys.get(NOTES)));
        analysisItems.add(new AnalysisDimension(keys.get(NUMBER), true));
        analysisItems.add(new AnalysisDimension(keys.get(PURCHASE_ORDER), true));
        analysisItems.add(new AnalysisDimension(keys.get(CLIENT_KEY), true));
        analysisItems.add(new AnalysisDimension(keys.get(STATE), true));
        analysisItems.add(new AnalysisMeasure(keys.get(TAX), AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(keys.get(TAX_2), AggregationTypes.AVERAGE));
        analysisItems.add(new AnalysisMeasure(keys.get(TAX_AMOUNT), TAX_AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        analysisItems.add(new AnalysisMeasure(keys.get(TAX_2_AMOUNT), TAX_2_AMOUNT, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY));
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        HarvestCompositeSource source = (HarvestCompositeSource) parentDefinition;
        HttpClient client = getHttpClient(source.getUsername(), source.getPassword());
        Builder builder = new Builder();
        Nodes invoiceNodes = null;
        try {
            int page = 1;
            DataSet ds = new DataSet();
            do {
                String request = "/invoices?page=" + page++;
                if(lastRefreshDate != null) {
                    request = request + "&updated_since=" + UPDATED_SINCE_FORMAT.format(lastRefreshDate);
                }
                Document invoices = runRestRequest(request, client, builder, source.getUrl(), true, source, false);
                invoiceNodes = invoices.query("/invoices/invoice");
                for(int i = 0;i < invoiceNodes.size();i++) {
                    if(lastRefreshDate != null) {
                        ds = new DataSet();

                    }
                    IRow row = ds.createRow();
                    Node curInvoice = invoiceNodes.get(i);
                    String id = queryField(curInvoice, "id/text()");
                    String amount = queryField(curInvoice, "amount/text()");
                    String dueAt = queryField(curInvoice, "due-at/text()");
                    String dueAtHumanFormat = queryField(curInvoice,  "due-at-human-format/text()");
                    String periodStart = queryField(curInvoice, "period-start/text()");
                    String periodEnd = queryField(curInvoice, "period-end/text()");
                    String clientId = queryField(curInvoice, "client-id/text()");
                    String currency = queryField(curInvoice, "currency/text()");
                    String issuedAt = queryField(curInvoice, "issued-at/text()");
                    String notes = queryField(curInvoice, "notes/text()");
                    String number = queryField(curInvoice, "number/text()");
                    String purchaseOrder = queryField(curInvoice, "purchase-order/text()");
                    String clientKey = queryField(curInvoice, "client-key/text()");
                    String state = queryField(curInvoice, "state/text()");
                    String tax = queryField(curInvoice, "tax/text()");
                    String tax2 = queryField(curInvoice, "tax2/text()");
                    String taxAmount = queryField(curInvoice,  "tax-amount/text()");
                    String tax2Amount = queryField(curInvoice, "tax-amount2/text()");
                    row.addValue(keys.get(ID), id);
                    if(amount != null && amount.length() > 0)
                        row.addValue(keys.get(AMOUNT), Double.parseDouble(amount));
                    if(dueAt != null && dueAt.length() > 0)
                        row.addValue(keys.get(DUE_AT), DATE_FORMAT.parse(dueAt));
                    row.addValue(keys.get(DUE_AT_HUMAN_FORMAT), dueAtHumanFormat);
                    if(periodStart != null && periodStart.length() > 0)
                        row.addValue(keys.get(PERIOD_START), DATE_FORMAT.parse(periodStart));
                    if(periodEnd != null && periodEnd.length() > 0)
                        row.addValue(keys.get(PERIOD_END), DATE_FORMAT.parse(periodEnd));
                    row.addValue(keys.get(CLIENT_ID), clientId);
                    row.addValue(keys.get(CURRENCY), currency);
                    row.addValue(keys.get(ISSUED_AT), issuedAt);
                    row.addValue(keys.get(NOTES), notes);
                    row.addValue(keys.get(NUMBER), number);
                    row.addValue(keys.get(PURCHASE_ORDER), purchaseOrder);
                    row.addValue(keys.get(CLIENT_KEY), clientKey);
                    row.addValue(keys.get(STATE), state);
                    if(tax != null && tax.length() > 0)
                        row.addValue(keys.get(TAX), Double.parseDouble(tax));
                    if(tax2 != null && tax2.length() > 0)
                        row.addValue(keys.get(TAX_2), Double.parseDouble(tax2));
                    if(taxAmount != null && taxAmount.length() > 0)
                        row.addValue(keys.get(TAX_AMOUNT), Double.parseDouble(taxAmount));
                    if(tax2Amount != null && tax2Amount.length() > 0)
                        row.addValue(keys.get(TAX_2_AMOUNT), Double.parseDouble(tax2Amount));
                    if(lastRefreshDate != null) {
                        IWhere where = new StringWhere(keys.get(ID), row.getValue(keys.get(ID)).toString());
                        dataStorage.updateData(ds, Arrays.asList(where));
                    }
                }

            } while(invoiceNodes != null && invoiceNodes.size() > 0);
            if(lastRefreshDate == null) {
                dataStorage.insertData(ds);
            }
        } catch (ParsingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected boolean clearsData() {
        return false;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HARVEST_INVOICES;
    }

    public HarvestInvoiceSource() {
        setFeedName("Invoices");
    }
}
