package com.easyinsight.datafeeds.solve360;

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
 * Date: 10/28/11
 * Time: 10:43 PM
 */
public class Solve360ContactsSource extends Solve360BaseSource {

    public static final String CONTACT_ID = "Contact ID";
    public static final String CONTACT_NAME = "Contact Name";
    public static final String TITLE = "Title";
    public static final String BUSINESS_ADDRESS = "Business Address";
    public static final String BUSINESS_EMAIL = "Business Email";
    public static final String BUSINESS_FAX = "Business Fax";
    public static final String BUSINESS_PHONE_DIRECT = "Business Phone (Direct)";
    public static final String BUSINESS_PHONE_EXT = "Business Phone (Ext)";
    public static final String BUSINESS_PHONE_MAIN = "Business Phone (Main)";
    public static final String CELLPHONE = "Cellular Phone";
    public static final String COMPANY = "Company";
    public static final String HOME_ADDRESS = "Home Address";
    public static final String HOME_PHONE = "Home Phone";
    public static final String OTHER_EMAIL = "Other Email";
    public static final String PERSONAL_EMAIL = "Personal Email";
    public static final String RESPONSIBLE_USER = "Responsible User";
    public static final String WEBSITE = "Website";

    public Solve360ContactsSource() {
        setFeedName("Contacts");
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_CONTACTS;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_ID, CONTACT_NAME, TITLE, BUSINESS_ADDRESS, BUSINESS_EMAIL, BUSINESS_FAX, BUSINESS_PHONE_DIRECT, BUSINESS_PHONE_EXT,
                BUSINESS_PHONE_MAIN, CELLPHONE, COMPANY, HOME_ADDRESS, HOME_PHONE, OTHER_EMAIL, PERSONAL_EMAIL, RESPONSIBLE_USER, WEBSITE);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        Solve360CompositeSource solve360CompositeSource = (Solve360CompositeSource) parentDefinition;
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID)));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_NAME)));
        analysisItems.add(new AnalysisDimension(keys.get(TITLE)));
        analysisItems.add(new AnalysisText(keys.get(BUSINESS_ADDRESS)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_EMAIL)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_FAX)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_PHONE_DIRECT)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_PHONE_EXT)));
        analysisItems.add(new AnalysisDimension(keys.get(BUSINESS_PHONE_MAIN)));
        analysisItems.add(new AnalysisDimension(keys.get(CELLPHONE)));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY)));
        analysisItems.add(new AnalysisText(keys.get(HOME_ADDRESS)));
        analysisItems.add(new AnalysisDimension(keys.get(HOME_PHONE)));
        analysisItems.add(new AnalysisDimension(keys.get(OTHER_EMAIL)));
        analysisItems.add(new AnalysisDimension(keys.get(PERSONAL_EMAIL)));
        analysisItems.add(new AnalysisDimension(keys.get(RESPONSIBLE_USER)));
        analysisItems.add(new AnalysisDimension(keys.get(WEBSITE)));
        List<AnalysisItem> customFields = solve360CompositeSource.createCustomContactFields(keys);
        analysisItems.addAll(customFields);
        return analysisItems;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Map<Integer, Contact> contacts = ((Solve360CompositeSource) parentDefinition).getOrCreateContactCache(keys);
        try {
            DataSet dataSet = new DataSet();
            for(Contact c : contacts.values()) {
                IRow row = dataSet.createRow();
                row.addValue(keys.get(CONTACT_ID), String.valueOf(c.getId()));
                row.addValue(keys.get(CONTACT_NAME), c.getName());
                row.addValue(keys.get(TITLE), c.getTitle());
                row.addValue(keys.get(BUSINESS_ADDRESS), c.getBusinessAddress());
                row.addValue(keys.get(BUSINESS_EMAIL), c.getBusinessEmail());
                row.addValue(keys.get(BUSINESS_FAX), c.getBusinessFax());
                row.addValue(keys.get(BUSINESS_PHONE_DIRECT), c.getBusinessPhoneDirect());
                row.addValue(keys.get(BUSINESS_PHONE_EXT), c.getBusinessPhoneExt());
                row.addValue(keys.get(BUSINESS_PHONE_MAIN), c.getBusinessPhoneMain());
                row.addValue(keys.get(CELLPHONE), c.getCellphone());
                row.addValue(keys.get(COMPANY), c.getCompany());                
                row.addValue(keys.get(HOME_ADDRESS), c.getHomeAddress());
                row.addValue(keys.get(HOME_PHONE), c.getHomePhone());
                row.addValue(keys.get(OTHER_EMAIL), c.getOtherEmail());
                row.addValue(keys.get(PERSONAL_EMAIL), c.getPersonalEmail());
                row.addValue(keys.get(RESPONSIBLE_USER), c.getResponsibleUser());
                row.addValue(keys.get(WEBSITE), c.getWebsite());
                for (Map.Entry<Key, Object> entry : c.getCustomFieldValues().entrySet()) {
                    if (entry.getValue() instanceof Date) {
                        row.addValue(entry.getKey(), (Date) entry.getValue());
                    } else {
                        row.addValue(entry.getKey(), (String) entry.getValue());
                    }
                }
            }
            return dataSet;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
}
