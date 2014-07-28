package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.ServerDataSourceDefinition;
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
public class InfusionsoftCompanySource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String ACCOUNT_ID = "AccountId";
    public static final String EMAIL = "Email";
    public static final String FIRST_NAME = "FirstName";
    public static final String CONTACT_TYPE = "ContactType";
    public static final String LAST_NAME = "LastName";
    public static final String WEBSITE = "Website";
    public static final String COMPANY = "Company";
    public static final String DATE_CREATED = "DateCreated";
    public static final String LAST_UPDATED = "LastUpdated";
    public static final String STATE = "State";
    public static final String CITY = "City";
    public static final String POSTAL_CODE = "PostalCode";
    public static final String COMPANY_COUNT = "Company Count";
    public static final String COMPANY_URL = "Company URL";

    public InfusionsoftCompanySource() {
        setFeedName("Companies");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_COMPANIES;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension());
        fieldBuilder.addField(ACCOUNT_ID, new AnalysisDimension());
        fieldBuilder.addField(EMAIL, new AnalysisDimension());
        fieldBuilder.addField(CONTACT_TYPE, new AnalysisDimension());
        fieldBuilder.addField(LAST_NAME, new AnalysisDimension());
        fieldBuilder.addField(WEBSITE, new AnalysisDimension());
        fieldBuilder.addField(COMPANY, new AnalysisDimension());
        fieldBuilder.addField(DATE_CREATED, new AnalysisDimension());
        fieldBuilder.addField(LAST_UPDATED, new AnalysisDimension());
        fieldBuilder.addField(STATE, new AnalysisDimension());
        fieldBuilder.addField(CITY, new AnalysisDimension());
        fieldBuilder.addField(POSTAL_CODE, new AnalysisDimension());
        fieldBuilder.addField(COMPANY_URL, new AnalysisDimension());
        fieldBuilder.addField(COMPANY_COUNT, new AnalysisMeasure());
        InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
        List<CustomField> customFields = infusionsoftCompositeSource.getCache().getCustomFieldMap().get(-6);
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
            DataSet dataSet = query("Company", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(COMPANY_COUNT, COMPANY_URL));
            for (IRow row : dataSet.getRows()) {
                row.addValue(keys.get(COMPANY_URL), infusionsoftCompositeSource.getUrl() + "/Company/manageCompany.jsp?view=edit&id=" + row.getValue(keys.get(ID)));
                row.addValue(keys.get(COMPANY_COUNT), 1);
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
