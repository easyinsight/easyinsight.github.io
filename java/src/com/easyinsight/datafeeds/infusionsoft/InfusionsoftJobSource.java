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

    public static final String JOB_NOTES = "JobNotes";
    public static final String DUE_DATE = "DueDate";
    public static final String START_DATE = "StartDate";
    public static final String SHIP_CITY = "ShipCity";
    public static final String SHIP_COMPANY = "ShipCompany";
    public static final String SHIP_COUNTRY = "ShipCountry";
    public static final String SHIP_FIRST_NAME = "ShipFirstName";
    public static final String SHIP_LAST_NAME = "ShipLastName";
    public static final String SHIP_STATE = "ShipState";
    public static final String SHIP_STREET1 = "ShipStreet1";
    public static final String SHIP_STREET2 = "ShipStreet2";
    public static final String SHIP_ZIP = "ShipZip";
    public static final String ORDER_URL = "Order URL";

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
        fieldBuilder.addField(JOB_NOTES, new AnalysisDimension("Order Notes"));
        fieldBuilder.addField(DUE_DATE, new AnalysisDateDimension("Order Due Date"));
        fieldBuilder.addField(START_DATE, new AnalysisDateDimension("Order Start Date"));
        fieldBuilder.addField(SHIP_CITY, new AnalysisDimension("Order Ship City"));
        fieldBuilder.addField(SHIP_COMPANY, new AnalysisDimension("Order Ship Company"));
        fieldBuilder.addField(SHIP_FIRST_NAME, new AnalysisDimension("Order Ship First Name"));
        fieldBuilder.addField(SHIP_LAST_NAME, new AnalysisDimension("Order Ship Last Name"));
        fieldBuilder.addField(SHIP_STATE, new AnalysisDimension("Order Ship State"));
        fieldBuilder.addField(SHIP_STREET1, new AnalysisDimension("Order Ship Street 1"));
        fieldBuilder.addField(SHIP_STREET2, new AnalysisDimension("Order Ship Street 2"));
        fieldBuilder.addField(SHIP_ZIP, new AnalysisDimension("Order Ship Zip"));
        fieldBuilder.addField(ORDER_URL, new AnalysisDimension("Order URL"));
        InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
        List<CustomField> customFields = infusionsoftCompositeSource.getCache().getCustomFieldMap().get(-9);
        if (customFields != null) {
            for (CustomField customField : customFields) {
                fieldBuilder.addField(customField.key(), customField.createAnalysisItem());
            }
        }
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
            DataSet jobs = query("Job", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(JOB_COUNT, ORDER_URL));
            for (IRow row : jobs.getRows()) {
                Key orderStatusKey = new NamedKey(ORDER_STATUS);
                Value orderStatus = row.getValue(orderStatusKey);
                if ("0".equals(orderStatus.toString())) {
                    row.addValue(orderStatusKey, "Paid");
                } else {
                    row.addValue(orderStatusKey, "Unpaid");
                }
                row.addValue(keys.get(ORDER_URL), infusionsoftCompositeSource.getUrl() + "/Job/manageJob.jsp?view=edit&id=" + row.getValue(keys.get(JOB_ID)));
                row.addValue(keys.get(JOB_COUNT), 1);
            }
            return jobs;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
