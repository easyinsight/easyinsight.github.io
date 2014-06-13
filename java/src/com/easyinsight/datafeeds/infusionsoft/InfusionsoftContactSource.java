package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
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
public class InfusionsoftContactSource extends InfusionsoftTableSource {

    public static final String ID = "Id";
    public static final String COMPANY_ID = "CompanyID";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String DATE_CREATED = "DateCreated";
    public static final String LEAD_SOURCE_ID = "LeadSourceId";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    public static final String CONTACT_COUNT = "ContactCount";
    public static final String COMPANY = "Company";

    public InfusionsoftContactSource() {
        setFeedName("Contacts");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CONTACTS;
    }

    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(ID, new AnalysisDimension("Contact ID"));
        fieldBuilder.addField(COMPANY_ID, new AnalysisDimension("Contact Company ID"));
        fieldBuilder.addField(COMPANY, new AnalysisDimension("Contact Company Name"));
        fieldBuilder.addField(FIRST_NAME, new AnalysisDimension("First Name"));
        fieldBuilder.addField(LAST_NAME, new AnalysisDimension("Last Name"));
        fieldBuilder.addField(LEAD_SOURCE_ID, new AnalysisDimension("Lead Source ID"));
        fieldBuilder.addField(NAME, new AnalysisDimension("Contact Name"));
        fieldBuilder.addField(EMAIL, new AnalysisDimension("Email"));
        fieldBuilder.addField(DATE_CREATED, new AnalysisDateDimension("Contact Creation Date"));
        fieldBuilder.addField(CONTACT_COUNT, new AnalysisMeasure("Number of Contacts"));
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = query("Contact", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(NAME, CONTACT_COUNT));
            for (IRow row : dataSet.getRows()) {
                String firstName = row.getValue(keys.get(FIRST_NAME)).toString();
                String lastName = row.getValue(keys.get(LAST_NAME)).toString();
                String name;
                if (!empty(firstName) && !empty(lastName)) {
                    name = firstName + " " + lastName;
                } else if (!empty(firstName)) {
                    name = lastName;
                } else {
                    name = firstName;
                }
                row.addValue(keys.get(NAME), name);
                row.addValue(keys.get(CONTACT_COUNT), 1);
            }
            return dataSet;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    private boolean empty(String string) {
        return string.isEmpty() || "(Empty)".equals(string);
    }
}
