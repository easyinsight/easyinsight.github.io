package com.easyinsight.datafeeds.constantcontact;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import nu.xom.*;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
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
    protected List<String> getKeys(FeedDefinition parentDefinition) {
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

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
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
    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        try {
            return cleanRetrieval((ConstantContactCompositeSource) parentDefinition, IDataStorage);
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        /*try {
            ConstantContactCompositeSource ccSource = (ConstantContactCompositeSource) parentDefinition;
            ContactListCache cache = ccSource.getOrCreateContactListCache();
            cache.getOrCreateContactLists(ccSource);
            Set<Contact> contacts = cache.getContacts();
            DataSet ds = new DataSet();
            for (Contact contact : contacts) {
                IRow row = ds.createRow();
                row.addValue(CONTACT_EMAIL, contact.getEmail());
                row.addValue(CONTACT_ID, contact.getEmail());
                row.addValue(CONTACT_WORK_PHONE, contact.getWorkPhone());
                row.addValue(CONTACT_HOME_PHONE, contact.getHomePhone());
                row.addValue(CONTACT_CITY, contact.getCity());
                row.addValue(CONTACT_STATE, contact.getState());
                row.addValue(CONTACT_COUNTRY, contact.getCountry());
                row.addValue(CONTACT_POSTAL, contact.getPostal());
                row.addValue(keys.get(CONTACT_CREATED_ON), contact.getCreatedOn());
                row.addValue(keys.get(CONTACT_UPDATED_ON), contact.getUpdatedOn());
                row.addValue(CONTACT_COMPANY, contact.getCompany());
                row.addValue(CONTACT_JOB_TITLE, contact.getTitle());
                row.addValue(CONTACT_NAME, contact.getName());
                row.addValue(keys.get(CONTACT_OPT_IN_DATE), contact.getCreatedOn());
                row.addValue(CONTACT_OPT_IN_SOURCE, contact.getSource());
                row.addValue(CONTACT_LAST_NAME, contact.getLastName());
                row.addValue(CONTACT_CUSTOM_FIELD1, contact.getCustomField1());
                row.addValue(CONTACT_CUSTOM_FIELD2, contact.getCustomField2());
                row.addValue(CONTACT_CUSTOM_FIELD3, contact.getCustomField3());
                row.addValue(CONTACT_CUSTOM_FIELD4, contact.getCustomField4());
                row.addValue(CONTACT_CUSTOM_FIELD5, contact.getCustomField5());
                row.addValue(CONTACT_CUSTOM_FIELD6, contact.getCustomField6());
                row.addValue(CONTACT_CUSTOM_FIELD7, contact.getCustomField7());
                row.addValue(CONTACT_CUSTOM_FIELD8, contact.getCustomField8());
                row.addValue(CONTACT_CUSTOM_FIELD9, contact.getCustomField9());
                row.addValue(CONTACT_CUSTOM_FIELD10, contact.getCustomField10());
                row.addValue(CONTACT_CUSTOM_FIELD11, contact.getCustomField11());
                row.addValue(CONTACT_CUSTOM_FIELD12, contact.getCustomField12());
                row.addValue(CONTACT_CUSTOM_FIELD13, contact.getCustomField13());
                row.addValue(CONTACT_CUSTOM_FIELD14, contact.getCustomField14());
                row.addValue(CONTACT_CUSTOM_FIELD15, contact.getCustomField15());
                row.addValue(CONTACT_COUNT, 1);
            }
            return ds;
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
    }

    protected boolean clearsData(FeedDefinition parentSource) {
        return true;
    }

    @Override
    protected String getUpdateKeyName() {
        return CONTACT_ID;
    }

    private DataSet cleanRetrieval(ConstantContactCompositeSource ccSource, IDataStorage IDataStorage) throws Exception, OAuthMessageSignerException, OAuthCommunicationException, IOException, ParsingException, ParseException {
        DataSet dataSet = new DataSet();

        HttpClient client = new HttpClient();
        Map result = query("https://api.constantcontact.com/v2/contacts?limit=500&api_key=" + ConstantContactCompositeSource.KEY, ccSource, client);
        Map meta = (Map) result.get("meta");
        String nextLink = null;
        if (meta != null) {
            Map pagination = (Map) meta.get("pagination");
            if (pagination != null) {
                Object nextLinkObject = pagination.get("next_link");
                if (nextLinkObject != null) {
                    nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                }
            }
        }

        int size = 0;
        boolean hasMoreData;
        do {
            List results = (List) result.get("results");
            hasMoreData = false;
            for (Object obj : results) {
                Map node = (Map) obj;
                IRow row = dataSet.createRow();
                row.addValue(CONTACT_EMAIL, ((Map) ((List) node.get("email_addresses")).get(0)).get("email_address").toString());
                row.addValue(CONTACT_ID, node.get("id").toString());
                row.addValue(CONTACT_NAME, node.get("first_name").toString() + " " + node.get("last_name"));
                row.addValue(CONTACT_FIRST_NAME, node.get("first_name").toString());
                row.addValue(CONTACT_LAST_NAME, node.get("last_name").toString());
                row.addValue(CONTACT_COMPANY, node.get("company_name").toString());
                row.addValue(CONTACT_JOB_TITLE, node.get("job_title").toString());
                row.addValue(CONTACT_HOME_PHONE, node.get("home_phone").toString());
                row.addValue(CONTACT_WORK_PHONE, node.get("work_phone").toString());
                List addresses = (List) node.get("addresses");
                if (addresses != null && addresses.size() > 0) {
                    Map addressNode = (Map) addresses.get(0);
                    row.addValue(CONTACT_CITY, addressNode.get("city").toString());
                    row.addValue(CONTACT_STATE, addressNode.get("state_code").toString());
                    row.addValue(CONTACT_COUNTRY, addressNode.get("country_code").toString());
                    row.addValue(CONTACT_POSTAL, addressNode.get("postal_code").toString());
                }
                List customFields = (List) node.get("custom_fields");
                if (customFields != null) {
                    for (Object customFieldObj : customFields) {
                        Map customFieldMap = (Map) customFieldObj;
                        String name = customFieldMap.get("name").toString();
                        String value = customFieldMap.get("value").toString();
                        if (name.equals("CustomField1")) {
                            row.addValue(CONTACT_CUSTOM_FIELD1, value);
                        } else if (name.equals("CustomField2")) {
                            row.addValue(CONTACT_CUSTOM_FIELD2, value);
                        } else if (name.equals("CustomField2")) {
                            row.addValue(CONTACT_CUSTOM_FIELD2, value);
                        } else if (name.equals("CustomField3")) {
                            row.addValue(CONTACT_CUSTOM_FIELD3, value);
                        } else if (name.equals("CustomField4")) {
                            row.addValue(CONTACT_CUSTOM_FIELD4, value);
                        } else if (name.equals("CustomField5")) {
                            row.addValue(CONTACT_CUSTOM_FIELD5, value);
                        } else if (name.equals("CustomField6")) {
                            row.addValue(CONTACT_CUSTOM_FIELD6, value);
                        } else if (name.equals("CustomField7")) {
                            row.addValue(CONTACT_CUSTOM_FIELD7, value);
                        } else if (name.equals("CustomField8")) {
                            row.addValue(CONTACT_CUSTOM_FIELD8, value);
                        } else if (name.equals("CustomField9")) {
                            row.addValue(CONTACT_CUSTOM_FIELD9, value);
                        } else if (name.equals("CustomField10")) {
                            row.addValue(CONTACT_CUSTOM_FIELD10, value);
                        } else if (name.equals("CustomField11")) {
                            row.addValue(CONTACT_CUSTOM_FIELD11, value);
                        } else if (name.equals("CustomField12")) {
                            row.addValue(CONTACT_CUSTOM_FIELD12, value);
                        } else if (name.equals("CustomField13")) {
                            row.addValue(CONTACT_CUSTOM_FIELD13, value);
                        } else if (name.equals("CustomField14")) {
                            row.addValue(CONTACT_CUSTOM_FIELD14, value);
                        } else if (name.equals("CustomField15")) {
                            row.addValue(CONTACT_CUSTOM_FIELD15, value);
                        }
                    }
                }
                row.addValue(CONTACT_COUNT, 1);
                row.addValue(CONTACT_CREATED_ON, new DateValue(DATE_FORMAT.parse(node.get("created_date").toString())));
                row.addValue(CONTACT_UPDATED_ON, new DateValue(DATE_FORMAT.parse(node.get("created_date").toString())));
                size++;
                if (size == 250) {
                    IDataStorage.insertData(dataSet);
                    dataSet = new DataSet();
                }
            }
            if (nextLink != null) {
                result = query(nextLink, ccSource, client);
                meta = (Map) result.get("meta");
                nextLink = null;
                if (meta != null) {
                    Map pagination = (Map) meta.get("pagination");
                    if (pagination != null) {
                        Object nextLinkObject = pagination.get("next_link");
                        if (nextLinkObject != null) {
                            nextLink = "https://api.constantcontact.com" + nextLinkObject.toString() + "&api_key=" + ConstantContactCompositeSource.KEY;
                        }
                    }
                }
                hasMoreData = true;
            }
        } while (hasMoreData);
        IDataStorage.insertData(dataSet);
        return null;
    }
}
