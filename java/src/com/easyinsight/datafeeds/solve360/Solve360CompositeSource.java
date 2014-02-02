package com.easyinsight.datafeeds.solve360;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.analysis.IRow;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    private Map<Integer, Contact> contactMap;
    private Map<Integer, Company> companyMap;

    public Map<Integer, Company> getOrCreateCompanyCache() {
        if (companyMap == null) {
            companyMap = new HashMap<Integer, Company>();
            try {
                HttpClient client = Solve360BaseSource.getHttpClient(userEmail, authKey);
                Document doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/companies?layout=1", client, new Builder(), this);
                Nodes responseNode = doc.query("/response");
                Node response = responseNode.get(0);
                for (int i = 0; i < response.getChildCount(); i++) {
                    Element companyNode = (Element) response.getChild(i);
                    if (!"count".equals(companyNode.getLocalName()) && !"status".equals(companyNode.getLocalName())) {
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
                        companyMap.put(c.getCompanyId(), c);
                    }
                }
            } catch (ParsingException e) {
                LogClass.error(e);
            }
        }

        return companyMap;
    }

    public Map<Integer, Contact> getOrCreateContactCache() {
        if (contactMap == null) {
            contactMap = new HashMap<Integer, Contact>();
            try {
                HttpClient client = Solve360BaseSource.getHttpClient(userEmail, authKey);
                Document doc = Solve360BaseSource.runRestRequest("https://secure.solve360.com/contacts?layout=1", client, new Builder(), this);
                Nodes responseNode = doc.query("/response");
                Node response = responseNode.get(0);
                for (int i = 0; i < response.getChildCount(); i++) {
                    Element contactNode = (Element) response.getChild(i);
                    if (!"count".equals(contactNode.getLocalName()) && !"status".equals(contactNode.getLocalName())) {
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
                        contactMap.put(c.getId(), c);
                    }
                }
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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO solve360 (DATA_SOURCE_ID, user_email, auth_key) VALUES (?, ?, ?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.setString(2, userEmail);
        insertStmt.setString(3, authKey);
        insertStmt.execute();
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
        return new ArrayList<ChildConnection>();
    }
}
