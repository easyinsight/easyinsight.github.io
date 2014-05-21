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
public class InfusionsoftInvoiceSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String CONTACT_ID = "ContactId";
    public static final String JOB_ID = "JobId";
    public static final String DATE_CREATED = "DateCreated";
    public static final String INVOICE_TOTAL = "InvoiceTotal";
    public static final String TOTAL_PAID = "TotalPaid";
    public static final String TOTAL_DUE = "TotalDue";
    public static final String PAY_STATUS = "PayStatus";
    public static final String CREDIT_STATUS = "CreditStatus";
    public static final String REFUND_STATUS = "RefundStatus";
    public static final String PAY_PLAN_STATUS = "PayPlanStatus";
    public static final String AFFILIATE_ID = "AffiliateId";
    public static final String LEAD_AFFILIATE_ID = "LeadAffiliateId";
    public static final String INVOICE_TYPE = "InvoiceType";
    public static final String DESCRIPTION = "Description";
    public static final String SYNCED = "Synced";

    public InfusionsoftInvoiceSource() {
        setFeedName("Invoices");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_INVOICES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, CONTACT_ID, JOB_ID, DATE_CREATED, INVOICE_TOTAL, TOTAL_PAID,
                TOTAL_DUE, PAY_STATUS, CREDIT_STATUS, REFUND_STATUS, PAY_PLAN_STATUS,
                AFFILIATE_ID, LEAD_AFFILIATE_ID, INVOICE_TYPE, DESCRIPTION, SYNCED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Invoice ID"));
        analysisitems.add(new AnalysisDimension(keys.get(JOB_ID), "Job ID"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Date Created", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisMeasure(keys.get(INVOICE_TOTAL), "Invoice Total", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(TOTAL_PAID), "Total Paid", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(TOTAL_DUE), "Total Due", AggregationTypes.SUM));
        analysisitems.add(new AnalysisDimension(keys.get(PAY_STATUS), "Pay Status"));
        analysisitems.add(new AnalysisDimension(keys.get(CREDIT_STATUS), "Credit Status"));
        analysisitems.add(new AnalysisDimension(keys.get(REFUND_STATUS), "Refund Status"));
        analysisitems.add(new AnalysisDimension(keys.get(PAY_PLAN_STATUS), "Pay Plan Status"));
        analysisitems.add(new AnalysisDimension(keys.get(AFFILIATE_ID), "Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(LEAD_AFFILIATE_ID), "Lead Affiliate ID"));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), "DESCRIPTION"));
        analysisitems.add(new AnalysisDimension(keys.get(INVOICE_TYPE), "Invoice Type"));
        analysisitems.add(new AnalysisDimension(keys.get(SYNCED), "Invoice Synced"));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Invoice", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}