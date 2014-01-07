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

    public InfusionsoftContactSource() {
        setFeedName("Contacts");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_CONTACTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, COMPANY_ID, FIRST_NAME, LAST_NAME, DATE_CREATED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Contact ID"));
        analysisitems.add(new AnalysisDimension(keys.get(COMPANY_ID), "Contact Company ID"));
        analysisitems.add(new AnalysisDimension(keys.get(FIRST_NAME), "First Name"));
        analysisitems.add(new AnalysisDimension(keys.get(LAST_NAME), "Last Name"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Contact Creation Date", AnalysisDateDimension.DAY_LEVEL));
        Key nameKey = keys.get(NAME);
        if (nameKey == null) {
            nameKey = new NamedKey(NAME);
        }
        analysisitems.add(new AnalysisDimension(nameKey, "Name"));
        Key leadSourceID = keys.get(LEAD_SOURCE_ID);
        if (leadSourceID == null) {
            leadSourceID = new NamedKey(LEAD_SOURCE_ID);
        }
        Key email = keys.get(EMAIL);
        if (email == null) {
            email = new NamedKey(EMAIL);
        }
        analysisitems.add(new AnalysisDimension(leadSourceID, "Lead Source ID"));
        analysisitems.add(new AnalysisDimension(email, "Email"));
        InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
        List<CustomField> customFields = infusionsoftCompositeSource.getCache().getCustomFieldMap().get(-1);
        if (customFields != null) {
            for (CustomField customField : customFields) {
                analysisitems.add(customField.createAnalysisItem(keys));
            }
        }
        return analysisitems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            DataSet dataSet = query("Contact", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition, Arrays.asList(NAME));
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
