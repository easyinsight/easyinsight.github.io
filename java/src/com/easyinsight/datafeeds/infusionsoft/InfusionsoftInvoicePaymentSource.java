package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.IDataStorage;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: 12/30/13
 * Time: 5:08 PM
 */
public class InfusionsoftInvoicePaymentSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String INVOICE_ID = "InvoiceId";
    public static final String AMT = "Amt";
    public static final String PAY_DATE = "PayDate";
    public static final String PAY_STATUS = "PayStatus";
    public static final String PAYMENT_ID = "PaymentId";
    public static final String SKIP_COMMISSION = "SkipCommission";

    public InfusionsoftInvoicePaymentSource() {
        setFeedName("Invoice Payment");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_INVOICE_PAYMENT;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, INVOICE_ID, AMT, PAY_DATE, PAY_STATUS, PAYMENT_ID, SKIP_COMMISSION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(INVOICE_ID), "Invoice ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PAYMENT_ID), "Payment ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PAY_STATUS), "Pay Status"));
        analysisitems.add(new AnalysisDimension(keys.get(SKIP_COMMISSION), "Skip Commission"));
        analysisitems.add(new AnalysisMeasure(keys.get(AMT), "Invoice Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisDateDimension(keys.get(PAY_DATE), "Pay Date", AnalysisDateDimension.DAY_LEVEL));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("InvoicePayment", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
