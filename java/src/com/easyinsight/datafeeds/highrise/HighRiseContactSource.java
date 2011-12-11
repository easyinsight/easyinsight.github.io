package com.easyinsight.datafeeds.highrise;

import com.easyinsight.analysis.*;
import com.easyinsight.core.DateValue;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.core.NumericValue;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.DataSourceMigration;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.storage.IDataStorage;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: Mar 23, 2010
 * Time: 1:35:19 PM
 */
public class HighRiseContactSource extends HighRiseBaseSource {
    public static final String CONTACT_NAME = "Contact Name";
    public static final String CONTACT_ID = "Contact ID";
    public static final String COMPANY_ID = "Company ID";
    public static final String TAGS = "Tags";
    public static final String TITLE = "Title";
    public static final String OWNER = "Contact Owner";
    public static final String CREATED_AT = "Created At";
    public static final String COUNT = "Count";

    public static final String BACKGROUND = "Contact Background";
    public static final String ZIP_CODE = "Contact Zip Code";

    public static final String CONTACT_WORK_EMAIL = "Contact Work Email";
    public static final String CONTACT_HOME_EMAIL = "Contact Home Email";
    public static final String CONTACT_OTHER_EMAIL = "Contact Other Email";
    public static final String CONTACT_MOBILE_PHONE = "Contact Mobile Phone";
    public static final String CONTACT_OFFICE_PHONE = "Contact Work Phone";
    public static final String CONTACT_HOME_PHONE = "Contact Home Phone";
    public static final String CONTACT_FAX_PHONE = "Contact Fax Phone";
    public static final String CONTACT_STREET = "Contact Street";
    public static final String CONTACT_CITY = "Contact City";
    public static final String CONTACT_STATE = "Contact State";
    public static final String CONTACT_COUNTRY = "Contact Country";

    public HighRiseContactSource() {
        setFeedName("Contact");
    }

    @NotNull
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return Arrays.asList(CONTACT_NAME, COMPANY_ID, TAGS, OWNER, CREATED_AT, COUNT, TITLE, CONTACT_ID, ZIP_CODE, BACKGROUND,
                CONTACT_WORK_EMAIL, CONTACT_MOBILE_PHONE, CONTACT_OFFICE_PHONE, CONTACT_HOME_PHONE, CONTACT_FAX_PHONE, CONTACT_HOME_EMAIL,
                CONTACT_STREET, CONTACT_CITY, CONTACT_STATE, CONTACT_COUNTRY, CONTACT_OTHER_EMAIL);
    }

    public List<AnalysisItem> createAnalysisItems(Map<String, Key> keys, Connection conn, FeedDefinition parentDefinition) {
        List<AnalysisItem> analysisItems = new ArrayList<AnalysisItem>();
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_NAME), true));
        analysisItems.add(new AnalysisZipCode(keys.get(ZIP_CODE), true));
        analysisItems.add(new AnalysisDimension(keys.get(BACKGROUND), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(TITLE), true));
        analysisItems.add(new AnalysisDimension(keys.get(COMPANY_ID), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_WORK_EMAIL), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_HOME_EMAIL), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_OTHER_EMAIL), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_MOBILE_PHONE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_OFFICE_PHONE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_FAX_PHONE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_STREET), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_CITY), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_STATE), true));
        analysisItems.add(new AnalysisDimension(keys.get(CONTACT_COUNTRY), true));
        analysisItems.add(new AnalysisList(keys.get(TAGS), true, ","));
        analysisItems.add(new AnalysisDimension(keys.get(OWNER), true));
        analysisItems.add(new AnalysisDateDimension(keys.get(CREATED_AT), true, AnalysisDateDimension.DAY_LEVEL));
        analysisItems.add(new AnalysisMeasure(keys.get(COUNT), AggregationTypes.SUM));
        try {
            HighRiseCompositeSource compositeSource = (HighRiseCompositeSource) parentDefinition;
            Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, (EIConnection) conn);
            HttpClient client = getHttpClient(token.getTokenValue(), "");
            HighriseCustomFieldsCache cache = compositeSource.getOrCreateCustomFieldCache(client, null);
            for (Map.Entry<String, String> entry : cache.getCustomFields().entrySet()) {
                Key key = keys.get(entry.getKey());
                if (key == null) {
                    key = new NamedKey(entry.getKey());
                }
                System.out.println("Key = " + entry.getKey() + " for " + entry.getValue());
                analysisItems.add(new AnalysisDimension(key, "Contact " + entry.getValue()));
            }
        } catch (ReportException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return analysisItems;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.HIGHRISE_CONTACTS;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) {
        HighRiseCompositeSource highRiseCompositeSource = (HighRiseCompositeSource) parentDefinition;
        String url = highRiseCompositeSource.getUrl();

        DateFormat deadlineFormat = new SimpleDateFormat(XMLDATEFORMAT);

        DataSet ds = new DataSet();
        Token token = new TokenStorage().getToken(SecurityUtil.getUserID(), TokenStorage.HIGHRISE_TOKEN, parentDefinition.getDataFeedID(), false, conn);
        HttpClient client = getHttpClient(token.getTokenValue(), "");
        Builder builder = new Builder();
        try {
            HighriseCache highriseCache = highRiseCompositeSource.getOrCreateCache(client);
            int offset = 0;
            int contactCount;
            do {
                Document companies;
                if (offset == 0) {
                    companies = runRestRequest("/people.xml?", client, builder, url, true, false, parentDefinition);
                } else {
                    companies = runRestRequest("/people.xml?n=" + offset, client, builder, url, true, false, parentDefinition);
                }
                Nodes companyNodes = companies.query("/people/person");
                loadingProgress(0, 1, "Synchronizing with contacts...", callDataID);
                contactCount = 0;
                for (int i = 0; i < companyNodes.size(); i++) {
                    IRow row = ds.createRow();
                    Node companyNode = companyNodes.get(i);
                    String firstName = queryField(companyNode, "first-name/text()");
                    String lastName = queryField(companyNode, "last-name/text()");
                    String name;
                    if (firstName == null) {
                        name = lastName;
                    } else if (lastName == null) {
                        name = firstName;
                    } else {
                        name = firstName + " " + lastName;
                    }
                    String title = queryField(companyNode, "title/text()");

                    row.addValue(TITLE, title);

                    row.addValue(CONTACT_NAME, name);

                    String background = queryField(companyNode, "background/text()");
                    row.addValue(BACKGROUND, background);

                    Nodes contactDataNodes = companyNode.query("contact-data/addresses/address");
                    if (contactDataNodes.size() > 0) {
                        Node contactDataNode = contactDataNodes.get(0);
                        String zip = queryField(contactDataNode, "zip/text()");
                        row.addValue(ZIP_CODE, zip);
                        row.addValue(CONTACT_CITY, queryField(contactDataNode, "city/text()"));
                        row.addValue(CONTACT_STATE, queryField(contactDataNode, "state/text()"));
                        row.addValue(CONTACT_COUNTRY, queryField(contactDataNode, "country/text()"));
                        row.addValue(CONTACT_STREET, queryField(contactDataNode, "street/text()"));
                    }

                    Nodes emailNodes = companyNode.query("contact-data/email-addresses/email-address");
                    for (int j = 0; j < emailNodes.size(); j++) {
                        Node emailNode = emailNodes.get(j);
                        String location = queryField(emailNode, "location/text()");
                        String email = queryField(emailNode, "address/text()");
                        if ("Work".equals(location)) {
                            row.addValue(CONTACT_WORK_EMAIL, email);
                        } else if ("Home".equals(location)) {
                            row.addValue(CONTACT_HOME_EMAIL, email);
                        } else if ("Other".equals(location)) {
                            row.addValue(CONTACT_OTHER_EMAIL, email);
                        }
                    }

                    Nodes phoneNodes = companyNode.query("contact-data/phone-numbers/phone-number");
                    for (int j = 0; j < phoneNodes.size(); j++) {
                        Node phoneNode = phoneNodes.get(j);
                        String phoneNumber = queryField(phoneNode, "number/text()");
                        String location = queryField(phoneNode, "location/text()");                        
                        if ("Mobile".equals(location)) {
                            row.addValue(CONTACT_MOBILE_PHONE, phoneNumber);
                        } else if ("Work".equals(location)) {
                            row.addValue(CONTACT_OFFICE_PHONE, phoneNumber);
                        } else if ("Fax".equals(location)) {
                            row.addValue(CONTACT_FAX_PHONE, phoneNumber);
                        } else if ("Home".equals(location)) {
                            row.addValue(CONTACT_HOME_PHONE, phoneNumber);
                        }
                    }

                    String id = queryField(companyNode, "id/text()");
                    row.addValue(CONTACT_ID, id);

                    String companyID = queryField(companyNode, "company-id/text()");
                    row.addValue(COMPANY_ID, companyID);
                    highRiseCompositeSource.getContactToCompanyCache().put(id, companyID);
                    Date createdAt = deadlineFormat.parse(queryField(companyNode, "created-at/text()"));
                    row.addValue(CREATED_AT, new DateValue(createdAt));
                    row.addValue(COUNT, new NumericValue(1));
                    String personId = queryField(companyNode, "author-id/text()");
                    String responsiblePartyName = highriseCache.getUserName(personId);
                    row.addValue(OWNER, responsiblePartyName);

                    Nodes tagNodes = companyNode.query("tags/tag/name/text()");
                    if (tagNodes.size() > 0) {
                        StringBuilder tagBuilder = new StringBuilder();
                        for (int j = 0; j < tagNodes.size(); j++) {
                            String tag = tagNodes.get(j).getValue();
                            tagBuilder.append(tag).append(",");
                        }
                        String tagString = tagBuilder.substring(0, tagBuilder.length() - 1);
                        row.addValue(TAGS, tagString);
                    }
                    contactDataNodes = companyNode.query("subject_datas/subject_data");
                    if (contactDataNodes.size() > 0) {
                        for (int j = 0; j < contactDataNodes.size(); j++) {
                            Node contactDataNode = contactDataNodes.get(j);
                            String subjectFieldID = queryField(contactDataNode, "subject_field_id/text()");
                            String value = queryField(contactDataNode, "value/text()");
                            Key key = keys.get(subjectFieldID);
                            if (key != null) {
                                row.addValue(key, value);
                            }
                        }
                    }
                    contactCount++;
                }
                offset += 500;
                IDataStorage.insertData(ds);
                ds = new DataSet();
            } while(contactCount == 500);

        } catch (ReportException re) {
            throw re;
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public int getVersion() {
        return 4;
    }

    @Override
    public List<DataSourceMigration> getMigrations() {
        return Arrays.asList(new HighRiseContact1To2(this), new HighRiseContact2To3(this), new HighRiseContact3To4(this));
    }
}
