package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.Value;
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
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftPaymentSource extends InfusionsoftTableSource {

    public static final String PAYMENT_ID = "Id";
    public static final String PAY_DATE = "PayDate";
    public static final String USER_ID = "UserId";
    public static final String PAY_AMT = "PayAmt";
    public static final String PAY_TYPE = "PayType";
    public static final String CONTACT_ID = "ContactId";
    public static final String INVOICE_ID = "InvoiceId";
    public static final String REFUND_ID = "RefundId";
    public static final String CHARGE_ID = "ChargeId";
    public static final String COMMISSION = "Commission";
    public static final String SYNCED = "Synced";


    public InfusionsoftPaymentSource() {
        setFeedName("Payments");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PAYMENT;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(PAYMENT_ID, PAY_DATE, USER_ID, PAY_AMT, PAY_TYPE, CONTACT_ID, INVOICE_ID, REFUND_ID, CHARGE_ID, COMMISSION, SYNCED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(PAYMENT_ID), "Payment Id"));
        analysisitems.add(new AnalysisDimension(keys.get(USER_ID), "User Id"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Contact Id"));
        analysisitems.add(new AnalysisDimension(keys.get(INVOICE_ID), "Invoice Id"));
        analysisitems.add(new AnalysisDimension(keys.get(REFUND_ID), "Refund Id"));
        analysisitems.add(new AnalysisDimension(keys.get(PAY_TYPE), "Payment Type"));
        analysisitems.add(new AnalysisDimension(keys.get(SYNCED), "Payment Synced"));
        analysisitems.add(new AnalysisDateDimension(keys.get(PAY_DATE), "Pay Date", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisMeasure(keys.get(PAY_AMT), "Pay Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(COMMISSION), "Commission", AggregationTypes.SUM));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet paymentSet = query("Payment", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
            return paymentSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
