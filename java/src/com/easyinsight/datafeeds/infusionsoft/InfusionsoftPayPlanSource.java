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
 * Time: 5:58 PM
 */
public class InfusionsoftPayPlanSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String INVOICE_ID = "InvoiceId";
    public static final String AMT_DUE = "AmtDue";
    public static final String FIRST_PAY_AMT = "FirstPayAmt";
    public static final String DATE_DUE = "DateDue";
    public static final String INIT_DATE = "InitDate";
    public static final String START_DATE = "StartDate";
    public static final String TYPE = "Type";

    public InfusionsoftPayPlanSource() {
        setFeedName("Pay Plan");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_PAY_PLAN;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, INVOICE_ID, AMT_DUE, FIRST_PAY_AMT, DATE_DUE, INIT_DATE, START_DATE, TYPE);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(INVOICE_ID), "Invoice ID"));
        analysisitems.add(new AnalysisDimension(keys.get(TYPE), "Type"));
        analysisitems.add(new AnalysisMeasure(keys.get(AMT_DUE), "Amount Due", AggregationTypes.SUM));
        analysisitems.add(new AnalysisMeasure(keys.get(FIRST_PAY_AMT), "First Payment Amount", AggregationTypes.SUM));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_DUE), "Date Due", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisDateDimension(keys.get(INIT_DATE), "Init Date", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisDateDimension(keys.get(START_DATE), "Start Date", AnalysisDateDimension.DAY_LEVEL, true));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("PayPlan", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}