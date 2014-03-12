package com.easyinsight.datafeeds.solve360;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import nu.xom.*;
import org.apache.commons.httpclient.HttpClient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:42 PM
 */
public class Solve360CompositeSource extends CompositeServerDataSource {

    public Solve360CompositeSource() {
        setFeedName("Solve360");
    }

    private String userEmail;
    private String authKey;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAuthKey() {
        return authKey;
    }

    @Override
    protected void refreshDone() {
        super.refreshDone();
        contactMap = null;
        companyMap = null;
        customCompanyFields = null;
    }

    private Map<Integer, Contact> contactMap;
    private Map<Integer, Company> companyMap;

    private List<AnalysisItem> customCompanyFields;
    private List<AnalysisItem> customContactFields;

    public List<AnalysisItem> getCustomContactFields() {
        return customContactFields;
    }

    public List<AnalysisItem> getCustomCompanyFields() {
        return customCompanyFields;
    }

    public List<AnalysisItem> createCustomCompanyFields(Map<String, Key> keyMap) {
        //
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        try {
            HttpClient client = Solve360BaseSource.getHttpClient(userEmail, authKey);
            Document doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/companies/fields/", client, new Builder(), this);
            Nodes responseNode = doc.query("/response/fields/field");

            for (int i = 0; i < responseNode.size(); i++) {
                Element customFieldNode = (Element) responseNode.get(i);
                String customFieldID = Solve360BaseSource.queryField(customFieldNode, "name/text()");
                if (customFieldID.startsWith("custom")) {
                    String customFieldName = Solve360BaseSource.queryField(customFieldNode, "label/text()");
                    String type = Solve360BaseSource.queryField(customFieldNode, "type/text()");
                    String keyName = customFieldID;
                    Key key = keyMap.get(keyName);
                    if (key == null) {
                        key = new NamedKey(keyName);
                    }
                    if ("date".equals(type)) {
                        fields.add(new AnalysisDateDimension(key, customFieldName, AnalysisDateDimension.DAY_LEVEL));
                    } else if ("number".equals(type)) {
                        fields.add(new AnalysisMeasure(key, customFieldName, AggregationTypes.SUM));
                    } else {
                        fields.add(new AnalysisDimension(key, customFieldName));
                    }
                }
            }
            customCompanyFields = fields;
        } catch (ParsingException e) {
            LogClass.error(e);
        }
        return fields;
    }

    public List<AnalysisItem> createCustomContactFields(Map<String, Key> keyMap) {
        //
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        try {
            HttpClient client = Solve360BaseSource.getHttpClient(userEmail, authKey);
            Document doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/contacts/fields/", client, new Builder(), this);
            Nodes responseNode = doc.query("/response/fields/field");

            for (int i = 0; i < responseNode.size(); i++) {
                Element customFieldNode = (Element) responseNode.get(i);
                String customFieldID = Solve360BaseSource.queryField(customFieldNode, "name/text()");
                if (customFieldID.startsWith("custom")) {
                    String customFieldName = Solve360BaseSource.queryField(customFieldNode, "label/text()");
                    String type = Solve360BaseSource.queryField(customFieldNode, "type/text()");
                    String keyName = customFieldID;
                    Key key = keyMap.get(keyName);
                    if (key == null) {
                        key = new NamedKey(keyName);
                    }
                    if ("date".equals(type)) {
                        fields.add(new AnalysisDateDimension(key, customFieldName, AnalysisDateDimension.DAY_LEVEL));
                    } else if ("number".equals(type)) {
                        fields.add(new AnalysisMeasure(key, customFieldName, AggregationTypes.SUM));
                    } else {
                        fields.add(new AnalysisDimension(key, customFieldName));
                    }
                }
            }
            customContactFields = fields;
        } catch (ParsingException e) {
            LogClass.error(e);
        }
        return fields;
    }

    @Override
    public boolean checkDateTime(String name, Key key) {
        return false;
    }

    public Map<Integer, Company> getOrCreateCompanyCache(Map<String, Key> keyMap) {
        if (companyMap == null) {
            createCustomCompanyFields(keyMap);
            companyMap = new HashMap<Integer, Company>();
            try {
                HttpClient client = Solve360BaseSource.getHttpClient(userEmail, authKey);
                int page = 0;
                int count;
                do {
                    count = 0;
                    Document doc;
                    if (page == 0) {
                        doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/companies?layout=1&limit=1000&start=1", client, new Builder(), this);
                    } else {
                        doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/companies?layout=1&limit=1000&start=" + (page * 1000), client, new Builder(), this);
                    }

                    Nodes responseNode = doc.query("/response");
                    Node response = responseNode.get(0);
                    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < response.getChildCount(); i++) {
                        Element companyNode = (Element) response.getChild(i);
                        if (!"count".equals(companyNode.getLocalName()) && !"status".equals(companyNode.getLocalName())) {
                            count++;
                            Company c = new Company();
                            c.setCompanyId(Integer.parseInt(Solve360BaseSource.queryField(companyNode, "id/text()")));
                            c.setName(Solve360BaseSource.queryField(companyNode, "name/text()"));
                            c.setShippingAddress(Solve360BaseSource.queryField(companyNode, "shippingaddress/text()"));
                            c.setBillingAddress(Solve360BaseSource.queryField(companyNode, "billingaddress/text()"));
                            c.setMainAddress(Solve360BaseSource.queryField(companyNode, "mainaddress/text()"));
                            c.setWebsite(Solve360BaseSource.queryField(companyNode, "website/text()"));
                            c.setFaxNumber(Solve360BaseSource.queryField(companyNode, "fax/text()"));
                            c.setCompanyPhone(Solve360BaseSource.queryField(companyNode, "phone/text()"));
                            c.setResponsibleParty(Solve360BaseSource.queryField(companyNode, "assignedto/@cn"));
                            c.setCompany(Solve360BaseSource.queryField(companyNode, "company/text()"));
                            Map<Key, Object> customFieldValues = new HashMap<Key, Object>();
                            for (AnalysisItem analysisItem : customCompanyFields) {
                                String value = Solve360BaseSource.queryField(companyNode, analysisItem.getKey().toKeyString() + "/text()");
                                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                                    if (value != null) {
                                        try {
                                            customFieldValues.put(analysisItem.getKey(), df2.parse(value));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    customFieldValues.put(analysisItem.getKey(), value);
                                }
                            }
                            c.setCustomFieldValues(customFieldValues);
                            companyMap.put(c.getCompanyId(), c);
                        }
                        page++;
                    }
                } while (count == 1000);
            } catch (ParsingException e) {
                LogClass.error(e);
            }
        }

        return companyMap;
    }

    public Map<Integer, Contact> getOrCreateContactCache(Map<String, Key> keyMap) {
        if (contactMap == null) {
            createCustomContactFields(keyMap);
            contactMap = new HashMap<Integer, Contact>();
            try {
                HttpClient client = Solve360BaseSource.getHttpClient(userEmail, authKey);
                Builder builder = new Builder();
                int count;
                int page = 0;
                do {
                    count = 0;
                    Document doc;
                    if (page == 0) {
                        doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/contacts?layout=1&limit=1000", client, builder, this);
                    } else {
                        doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/contacts?layout=1&limit=1000&start=" + (page * 1000), client, builder, this);
                    }
                    Nodes responseNode = doc.query("/response");
                    Node response = responseNode.get(0);
                    DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
                    for (int i = 0; i < response.getChildCount(); i++) {
                        Element contactNode = (Element) response.getChild(i);
                        if (!"count".equals(contactNode.getLocalName()) && !"status".equals(contactNode.getLocalName())) {
                            count++;
                            Contact c = new Contact();
                            String s = Solve360BaseSource.queryField(contactNode, "id/text()");
                            c.setId(Integer.parseInt(s));
                            c.setName(Solve360BaseSource.queryField(contactNode, "name/text()"));
                            c.setTitle(Solve360BaseSource.queryField(contactNode, "jobtitle/text()"));
                            c.setBusinessAddress(Solve360BaseSource.queryField(contactNode, "businessaddress/text()"));
                            c.setBusinessEmail(Solve360BaseSource.queryField(contactNode, "businessemail/text()"));
                            c.setBusinessFax(Solve360BaseSource.queryField(contactNode, "businessfax/text()"));
                            c.setBusinessPhoneDirect(Solve360BaseSource.queryField(contactNode, "businessphonedirect/text()"));
                            c.setBusinessPhoneExt(Solve360BaseSource.queryField(contactNode, "businessphoneextension/text()"));
                            c.setBusinessPhoneMain(Solve360BaseSource.queryField(contactNode, "businessphonemain/text()"));
                            c.setCellphone(Solve360BaseSource.queryField(contactNode, "cellularphone/text()"));
                            c.setCompany(Solve360BaseSource.queryField(contactNode, "company/text()"));
                            c.setOtherEmail(Solve360BaseSource.queryField(contactNode, "otheremail/text()"));
                            c.setPersonalEmail(Solve360BaseSource.queryField(contactNode, "personalemail/text()"));
                            c.setWebsite(Solve360BaseSource.queryField(contactNode, "website/text()"));
                            c.setHomeAddress(Solve360BaseSource.queryField(contactNode, "homeaddress/text()"));
                            c.setHomePhone(Solve360BaseSource.queryField(contactNode, "homephone/text()"));
                            c.setResponsibleUser(Solve360BaseSource.queryField(contactNode, "assignedto/@cn"));
                            Map<Key, Object> customFieldValues = new HashMap<Key, Object>();
                            for (AnalysisItem analysisItem : customContactFields) {
                                String value = Solve360BaseSource.queryField(contactNode, analysisItem.getKey().toKeyString() + "/text()");
                                if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                                    if (value != null) {
                                        try {
                                            customFieldValues.put(analysisItem.getKey(), df2.parse(value));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    customFieldValues.put(analysisItem.getKey(), value);
                                }
                            }
                            c.setCustomFieldValues(customFieldValues);
                            contactMap.put(c.getId(), c);
                        }

                    }
                    page++;
                } while (count == 1000);
            } catch (ParsingException e) {
                LogClass.error(e);
            }
        }
        return contactMap;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM solve360 WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        deleteStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO solve360 (DATA_SOURCE_ID, user_email, auth_key) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, userEmail);
        insertStmt.setString(3, authKey);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement loadStmt = conn.prepareStatement("SELECT USER_EMAIL, AUTH_KEY from SOLVE360 where DATA_SOURCE_ID = ?");
        loadStmt.setLong(1, getDataFeedID());
        ResultSet rs = loadStmt.executeQuery();
        if(rs.next()) {
            userEmail = rs.getString(1);
            authKey = rs.getString(2);
        }
        loadStmt.close();
    }

    @Override
    public String validateCredentials() {
        /*try {
            getToken(ksUserName, ksPassword);
            return null;
        } catch (ParsingException pe) {
            return "These credentials were rejected as invalid by Kashoo. Please double check your values for username and password.";
        } catch (Exception e) {
            return e.getMessage();
        }*/
        return null;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SOLVE360_COMPOSITE;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        types.add(FeedType.SOLVE360_CONTACTS);
        types.add(FeedType.SOLVE360_OPPORTUNITIES);
        types.add(FeedType.SOLVE360_ACTIVITIES);
        types.add(FeedType.SOLVE360_COMPANIES);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return Arrays.asList(new ChildConnection(FeedType.SOLVE360_COMPANIES, FeedType.SOLVE360_ACTIVITIES, Solve360CompanySource.COMPANY_ID, Solve360ActivitiesSource.PARENT_COMPANY),
                new ChildConnection(FeedType.SOLVE360_COMPANIES, FeedType.SOLVE360_OPPORTUNITIES, Solve360CompanySource.COMPANY_ID, Solve360OpportunitiesSource.RELATED_COMPANY),
                new ChildConnection(FeedType.SOLVE360_CONTACTS, FeedType.SOLVE360_ACTIVITIES, Solve360ContactsSource.CONTACT_ID, Solve360ActivitiesSource.PARENT_CONTACT),
                new ChildConnection(FeedType.SOLVE360_CONTACTS, FeedType.SOLVE360_OPPORTUNITIES, Solve360ContactsSource.CONTACT_ID, Solve360OpportunitiesSource.RELATED_CONTACT),
                new ChildConnection(FeedType.SOLVE360_COMPANIES, FeedType.SOLVE360_CONTACTS, Solve360CompanySource.NAME, Solve360ContactsSource.COMPANY)
                );
    }
}
