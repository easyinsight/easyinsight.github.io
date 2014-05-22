package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDimension;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.ReportException;
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

    public InfusionsoftCompanySource() {
        setFeedName("Companies");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.INFUSIONSOFT_COMPANIES;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(ID, DATE_CREATED, ACCOUNT_ID, EMAIL, CONTACT_TYPE, LAST_NAME, FIRST_NAME, WEBSITE, COMPANY, STATE, CITY, POSTAL_CODE, LAST_UPDATED);
    }

    @Override
    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisitems = new ArrayList<AnalysisItem>();
        analysisitems.add(new AnalysisDimension(keys.get(ID), "Company ID"));
        analysisitems.add(new AnalysisDimension(keys.get(ACCOUNT_ID), "Account ID"));
        analysisitems.add(new AnalysisDimension(keys.get(EMAIL), "Email"));
        analysisitems.add(new AnalysisDimension(keys.get(CONTACT_TYPE), "Contact Type"));
        analysisitems.add(new AnalysisDimension(keys.get(LAST_NAME), "Last Name"));
        analysisitems.add(new AnalysisDimension(keys.get(FIRST_NAME), "First Name"));
        analysisitems.add(new AnalysisDimension(keys.get(WEBSITE), "Website"));
        analysisitems.add(new AnalysisDimension(keys.get(COMPANY), "Company"));
        analysisitems.add(new AnalysisDimension(keys.get(STATE), "State"));
        analysisitems.add(new AnalysisDimension(keys.get(CITY), "City"));
        analysisitems.add(new AnalysisDimension(keys.get(POSTAL_CODE), "Postal Code"));
        analysisitems.add(new AnalysisDateDimension(keys.get(DATE_CREATED), "Date Created", AnalysisDateDimension.DAY_LEVEL, true));
        analysisitems.add(new AnalysisDateDimension(keys.get(LAST_UPDATED), "Date Updated", AnalysisDateDimension.DAY_LEVEL, true));
        InfusionsoftCompositeSource infusionsoftCompositeSource = (InfusionsoftCompositeSource) parentDefinition;
        List<CustomField> customFields = infusionsoftCompositeSource.getCache().getCustomFieldMap().get(-6);
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
            return query("Company", createAnalysisItems(keys, conn, parentDefinition), (InfusionsoftCompositeSource) parentDefinition);
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
