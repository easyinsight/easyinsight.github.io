package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
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
public class InfusionsoftJobSource extends InfusionsoftTableSource {

    public static final String JOB_ID = "Id";
    public static final String JOB_TITLE = "JobTitle";
    public static final String ORDER_TYPE = "OrderType";
    public static final String ORDER_STATUS = "OrderStatus";
    public static final String CONTACT_ID = "ContactId";
    public static final String PRODUCT_ID = "ProductId";
    public static final String JOB_STATUS = "JobStatus";
    public static final String DATE_CREATED = "DateCreated";
    public static final String JOB_COUNT = "JobCount";

    public InfusionsoftJobSource() {
        setFeedName("Order");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_JOBS;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(JOB_ID, new AnalysisDimension("Job ID"));
        fieldBuilder.addField(JOB_TITLE, new AnalysisDimension("Job Title"));
        fieldBuilder.addField(ORDER_TYPE, new AnalysisDimension("Order Type"));
        fieldBuilder.addField(ORDER_STATUS, new AnalysisDimension("Order Status"));
        fieldBuilder.addField(CONTACT_ID, new AnalysisDimension("Contact ID"));
        fieldBuilder.addField(PRODUCT_ID, new AnalysisDimension("Product ID"));
        fieldBuilder.addField(JOB_STATUS, new AnalysisDimension("Job Status"));
        fieldBuilder.addField(DATE_CREATED, new AnalysisDateDimension("Date Created"));
        fieldBuilder.addField(JOB_COUNT, new AnalysisMeasure("Number of Jobs"));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet jobs = query("Job", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(JOB_COUNT));
            for (IRow row : jobs.getRows()) {
                Key orderStatusKey = new NamedKey(ORDER_STATUS);
                Value orderStatus = row.getValue(orderStatusKey);
                if ("0".equals(orderStatus.toString())) {
                    row.addValue(orderStatusKey, "Paid");
                } else {
                    row.addValue(orderStatusKey, "Unpaid");
                }
            }
            return jobs;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
