package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.DataStorage;
import nu.xom.*;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 7, 2010
 * Time: 10:56:45 AM
 */
public class CCContactSource extends ConstantContactBaseSource {

    public static final String CONTACT_NAME = "Contact Name";
    public static final String CONTACT_STATUS = "Contact Status";
    public static final String CONTACT_EMAIL = "Contact Email";
    public static final String CONTACT_FIRST_NAME = "Contact First Name";
    public static final String CONTACT_LAST_NAME = "Contact Last Name";
    public static final String CONTACT_JOB_TITLE = "Contact Job Title";
    public static final String CONTACT_COMPANY = "Contact Company";
    public static final String CONTACT_HOME_PHONE = "Contact Home Phone";
    public static final String CONTACT_WORK_PHONE = "Contact Work Phone";
    public static final String CONTACT_CITY = "Contact City";
    public static final String CONTACT_STATE = "Contact State";
    public static final String CONTACT_COUNTRY = "Contact Country";
    public static final String CONTACT_POSTAL = "Contact Postal Code";
    public static final String CONTACT_NOTE = "Contact Note";
    public static final String CONTACT_COUNT = "Contact Count";
    public static final String CONTACT_URL = "Contact URL";
    public static final String CONTACT_ID = "Contact ID";
    public static final String CONTACT_OPT_IN_SOURCE = "Contact Opt In Source";
    public static final String CONTACT_OPT_IN_DATE = "Contact Opt In Date";
    public static final String CONTACT_CREATED_ON = "Contact Created On";
    public static final String CONTACT_EMAIL_TYPE = "Contact Email Type";
    public static final String CONTACT_UPDATED_ON = "Contact Updated On";
    public static final String CONTACT_CUSTOM_FIELD1 = "Contact Custom Field 1";
    public static final String CONTACT_CUSTOM_FIELD2 = "Contact Custom Field 2";
    public static final String CONTACT_CUSTOM_FIELD3 = "Contact Custom Field 3";
    public static final String CONTACT_CUSTOM_FIELD4 = "Contact Custom Field 4";
    public static final String CONTACT_CUSTOM_FIELD5 = "Contact Custom Field 5";
    public static final String CONTACT_CUSTOM_FIELD6 = "Contact Custom Field 6";
    public static final String CONTACT_CUSTOM_FIELD7 = "Contact Custom Field 7";
    public static final String CONTACT_CUSTOM_FIELD8 = "Contact Custom Field 8";
    public static final String CONTACT_CUSTOM_FIELD9 = "Contact Custom Field 9";
    public static final String CONTACT_CUSTOM_FIELD10 = "Contact Custom Field 10";
    public static final String CONTACT_CUSTOM_FIELD11 = "Contact Custom Field 11";
    public static final String CONTACT_CUSTOM_FIELD12 = "Contact Custom Field 12";
    public static final String CONTACT_CUSTOM_FIELD13 = "Contact Custom Field 13";
    public static final String CONTACT_CUSTOM_FIELD14 = "Contact Custom Field 14";
    public static final String CONTACT_CUSTOM_FIELD15 = "Contact Custom Field 15";
    public static final String CONTACT_CUSTOM_FIELD16 = "Contact Custom Field 16";
    public static final String CONTACT_CUSTOM_FIELD17 = "Contact Custom Field 17";
    public static final String CONTACT_CUSTOM_FIELD18 = "Contact Custom Field 18";    

    public CCContactSource() {
        setFeedName("Contacts");
    }

    @NotNull
    @Override
    protected List<String> getKeys() {
        return Arrays.asList(CONTACT_NAME, CONTACT_STATUS, CONTACT_EMAIL, CONTACT_FIRST_NAME, CONTACT_LAST_NAME,
                CONTACT_JOB_TITLE, CONTACT_COMPANY, CONTACT_HOME_PHONE, CONTACT_WORK_PHONE, CONTACT_CITY,
                CONTACT_STATE, CONTACT_COUNTRY, CONTACT_POSTAL, CONTACT_NOTE, CONTACT_COUNT, CONTACT_URL,
                CONTACT_ID, CONTACT_OPT_IN_SOURCE, CONTACT_OPT_IN_DATE, CONTACT_EMAIL_TYPE, CONTACT_UPDATED_ON,
                CONTACT_CUSTOM_FIELD1, CONTACT_CUSTOM_FIELD2, CONTACT_CUSTOM_FIELD3, CONTACT_CUSTOM_FIELD4,
                CONTACT_CUSTOM_FIELD5, CONTACT_CUSTOM_FIELD6, CONTACT_CUSTOM_FIELD7, CONTACT_CUSTOM_FIELD8,
                CONTACT_CUSTOM_FIELD9, CONTACT_CUSTOM_FIELD10, CONTACT_CUSTOM_FIELD11, CONTACT_CUSTOM_FIELD12,
                CONTACT_CUSTOM_FIELD13, CONTACT_CUSTOM_FIELD14, CONTACT_CUSTOM_FIELD15, CONTACT_CUSTOM_FIELD16,
                CONTACT_CUSTOM_FIELD17, CONTACT_CUSTOM_FIELD18, CONTACT_CREATED_ON);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, DataSet dataSet, Connection conn) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(new AnalysisDimension(keys.get(CONTACT_NAME), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_STATUS), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_EMAIL), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_FIRST_NAME), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_LAST_NAME), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_COMPANY), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_JOB_TITLE), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_HOME_PHONE), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_WORK_PHONE), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_CITY), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_STATE), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_COUNTRY), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_NOTE), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_URL), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_OPT_IN_SOURCE), true));
        items.add(new AnalysisDimension(keys.get(CONTACT_EMAIL_TYPE), true));
        items.add(new AnalysisDateDimension(keys.get(CONTACT_OPT_IN_DATE), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(CONTACT_UPDATED_ON), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisDateDimension(keys.get(CONTACT_CREATED_ON), true, AnalysisDateDimension.DAY_LEVEL));
        items.add(new AnalysisZipCode(keys.get(CONTACT_POSTAL), true));
        items.add(new AnalysisMeasure(keys.get(CONTACT_COUNT), AggregationTypes.SUM));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD1), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD2), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD3), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD4), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD5), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD6), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD7), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD8), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD9), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD10), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD11), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD12), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD13), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD14), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD15), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD16), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD17), "Custom Fields"));
        items.add(AnalysisDimension.withFolder(keys.get(CONTACT_CUSTOM_FIELD18), "Custom Fields"));
        return items;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.CONSTANT_CONTACT_CONTACTS;
    }

    @Override
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, DataStorage dataStorage, EIConnection conn) throws ReportException {
        try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            DataSet dataSet = new DataSet();

            boolean hasMoreData;

            Document doc = query("https://api.constantcontact.com/ws/customers/"+ccSource.getCcUserName()+"/contacts", ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);

            do {

                hasMoreData = false;

                Nodes nodes = doc.query("/feed/entry");

                for (int i = 0; i < nodes.size(); i++) {
                    IRow row = dataSet.createRow();
                    Node node = nodes.get(i);
                    String contactIDLink = queryField(node, "id/text()");
                    String id = contactIDLink.split("/")[7];
                    Document details = query(contactIDLink, ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
                    row.addValue(CONTACT_EMAIL, queryField(details, "/entry/content/Contact/EmailAddress/text()"));
                    row.addValue(CONTACT_EMAIL_TYPE, queryField(details, "/entry/content/Contact/EmailType/text()"));
                    row.addValue(CONTACT_ID, id);
                    row.addValue(CONTACT_STATUS, queryField(details, "/entry/content/Contact/Status/text()"));
                    row.addValue(CONTACT_NAME, queryField(details, "/entry/content/Contact/Name/text()"));
                    row.addValue(CONTACT_FIRST_NAME, queryField(details, "/entry/content/Contact/FirstName/text()"));
                    row.addValue(CONTACT_LAST_NAME, queryField(details, "/entry/content/Contact/LastName/text()"));
                    row.addValue(CONTACT_COMPANY, queryField(details, "/entry/content/Contact/CompanyName/text()"));
                    row.addValue(CONTACT_JOB_TITLE, queryField(details, "/entry/content/Contact/JobTitle/text()"));
                    row.addValue(CONTACT_HOME_PHONE, queryField(details, "/entry/content/Contact/HomePhone/text()"));
                    row.addValue(CONTACT_WORK_PHONE, queryField(details, "/entry/content/Contact/WorkPhone/text()"));
                    row.addValue(CONTACT_CITY, queryField(details, "/entry/content/Contact/City/text()"));
                    row.addValue(CONTACT_STATE, queryField(details, "/entry/content/Contact/StateName/text()"));
                    row.addValue(CONTACT_COUNTRY, queryField(details, "/entry/content/Contact/CountryName/text()"));
                    row.addValue(CONTACT_POSTAL, queryField(details, "/entry/content/Contact/PostalCode/text()"));
                    row.addValue(CONTACT_NOTE, queryField(details, "/entry/content/Contact/Note/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD1, queryField(details, "/entry/content/Contact/CustomField1/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD2, queryField(details, "/entry/content/Contact/CustomField2/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD3, queryField(details, "/entry/content/Contact/CustomField3/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD4, queryField(details, "/entry/content/Contact/CustomField4/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD5, queryField(details, "/entry/content/Contact/CustomField5/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD6, queryField(details, "/entry/content/Contact/CustomField6/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD7, queryField(details, "/entry/content/Contact/CustomField7/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD8, queryField(details, "/entry/content/Contact/CustomField8/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD9, queryField(details, "/entry/content/Contact/CustomField9/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD10, queryField(details, "/entry/content/Contact/CustomField10/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD11, queryField(details, "/entry/content/Contact/CustomField11/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD12, queryField(details, "/entry/content/Contact/CustomField12/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD13, queryField(details, "/entry/content/Contact/CustomField13/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD14, queryField(details, "/entry/content/Contact/CustomField14/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD15, queryField(details, "/entry/content/Contact/CustomField15/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD16, queryField(details, "/entry/content/Contact/CustomField16/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD17, queryField(details, "/entry/content/Contact/CustomField17/text()"));
                    row.addValue(CONTACT_CUSTOM_FIELD18, queryField(details, "/entry/content/Contact/CustomField18/text()"));
                    row.addValue(CONTACT_CREATED_ON, new DateValue(DATE_FORMAT.parse(queryField(details, "/entry/content/Contact/InsertTime/text()"))));
                    row.addValue(CONTACT_UPDATED_ON, new DateValue(DATE_FORMAT.parse(queryField(details, "/entry/content/Contact/LastUpdateTime/text()"))));
                    row.addValue(CONTACT_COUNT, 1);
                }
                Nodes links = doc.query("/feed/link");

                for (int i = 0; i < links.size(); i++) {
                    Element link = (Element) links.get(i);
                    Attribute attribute = link.getAttribute("rel");
                    if (attribute != null && "next".equals(attribute.getValue())) {
                        String linkURL = link.getAttribute("href").getValue();
                        hasMoreData = true;
                        doc = query("https://api.constantcontact.com" + linkURL, ccSource.getTokenKey(), ccSource.getTokenSecret(), parentDefinition);
                        break;
                    }
                }
            } while (hasMoreData);
            return dataSet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
