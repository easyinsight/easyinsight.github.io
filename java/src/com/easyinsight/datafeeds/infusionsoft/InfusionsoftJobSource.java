package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
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
 * Date: 4/24/13
 * Time: 11:30 AM
 */
public class InfusionsoftJobSource extends InfusionsoftTableSource {

    public static final String JOB_ID = "Id";
    public static final String JOB_TITLE = "JobTitle";
    public static final String ORDER_TYPE = "OrderType";
    public static final String ORDER_STATUS = "OrderStatus";
    public static final String CONTACT_ID = "ContactId";
    public static final String PRODUCT_ID = "ProductId";
    public static final String JOB_STATUS = "JobStatus";
    public static final String DATE_CREATED = "DateCreated";

    public InfusionsoftJobSource() {
        setFeedName("Job");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_JOBS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(JOB_ID, JOB_TITLE, ORDER_TYPE, ORDER_STATUS, CONTACT_ID, PRODUCT_ID, JOB_STATUS, DATE_CREATED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(JOB_ID), "Job ID"));
        analysisitems.add(new AnalysisDimension(keys.get(JOB_TITLE), "Job Title"));
        analysisitems.add(new AnalysisDimension(keys.get(ORDER_TYPE), "Order Type"));
        analysisitems.add(new AnalysisDimension(keys.get(ORDER_STATUS), "Order Status"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_ID), "Job Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(PRODUCT_ID), "Job Product ID"));
        analysisitems.add(new AnalysisDimension(keys.get(JOB_STATUS), "Job Status"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Date Created", AnalysisDateDimension.DAY_LEVEL, true));
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return query("Job", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
