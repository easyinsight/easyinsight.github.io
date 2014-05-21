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
 * Time: 6:03 PM
 */
public class InfusionsoftJobRecurringInstanceSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String RECURRING_ID = "RecurringId";
    public static final String INVOICE_ITEM_ID = "InvoiceItemId";
    public static final String STATUS = "Status";
    public static final String AUTO_CHARGE = "AutoCharge";
    public static final String START_DATE = "StartDate";
    public static final String END_DATE = "EndDate";
    public static final String DATE_CREATED = "DateCreated";
    public static final String DESCRIPTION = "Description";

    public InfusionsoftJobRecurringInstanceSource() {
        setFeedName("Job Recurring Instance");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_JOB_RECURRING_INSTANCE;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, RECURRING_ID, INVOICE_ITEM_ID, STATUS, AUTO_CHARGE, START_DATE, END_DATE, DATE_CREATED, DESCRIPTION);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "ID"));
        analysisitems.add(new AnalysisDimension(keys.get(RECURRING_ID), "Recurring ID"));
        analysisitems.add(new AnalysisDimension(keys.get(INVOICE_ITEM_ID), "Invoice Item ID"));
        analysisitems.add(new AnalysisDimension(keys.get(STATUS), "Status"));
        analysisitems.add(new AnalysisDimension(keys.get(AUTO_CHARGE), "Auto Charge"));
        analysisitems.add(new AnalysisDimension(keys.get(DESCRIPTION), "Description"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Date Created", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisDateDimension(keys.get(END_DATE), "End Date", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisDateDimension(keys.get(START_DATE), "Start Date", AnalysisDateDimension.DAY_LEVEL, true));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("JobRecurringInstance", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
