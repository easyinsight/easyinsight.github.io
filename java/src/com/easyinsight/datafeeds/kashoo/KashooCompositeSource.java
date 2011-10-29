package com.easyinsight.datafeeds.kashoo;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.users.Account;
import com.easyinsight.users.Token;
import com.easyinsight.users.TokenStorage;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:42 PM
 */
public class KashooCompositeSource extends CompositeServerDataSource {

    public KashooCompositeSource() {
        setFeedName("Kashoo");
    }

    private String ksUserName;
    private String ksPassword;

    public String getKsUserName() {
        return ksUserName;
    }

    public void setKsUserName(String ksUserName) {
        this.ksUserName = ksUserName;
    }

    public String getKsPassword() {
        return ksPassword;
    }

    public void setKsPassword(String ksPassword) {
        this.ksPassword = ksPassword;
    }

    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        try {
            if (ksUserName != null && ksPassword != null) {
                String tokenValue = getToken(ksUserName, ksPassword);
                Token tokenObj = new Token();
                tokenObj.setTokenValue(tokenValue);
                tokenObj.setTokenType(TokenStorage.KASHOO);
                tokenObj.setUserID(SecurityUtil.getUserID());
                new TokenStorage().saveToken(tokenObj, getDataFeedID(), conn);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM KASHOO WHERE DATA_SOURCE_ID = ?");
        deleteStmt.setLong(1, getDataFeedID());
        deleteStmt.executeUpdate();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO KASHOO (DATA_SOURCE_ID) VALUES (?)");
        insertStmt.setLong(1, getDataFeedID());
        insertStmt.execute();
    }

    @Override
    public String validateCredentials() {
        try {
            getToken(ksUserName, ksPassword);
            return null;
        } catch (ParsingException pe) {
            return "These credentials were rejected as invalid by Kashoo. Please double check your values for username and password.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    protected String getToken(String userName, String password) throws IOException, ParsingException {
        HttpClient client = new HttpClient();
        client.getParams().setAuthenticationPreemptive(true);
        Credentials defaultcreds = new UsernamePasswordCredentials(userName, password);
        client.getState().setCredentials(new AuthScope(AuthScope.ANY), defaultcreds);
        // TODO: change to a post to kashoo, per https://www.kashoo.com/api-docs/
        HttpMethod restMethod = new GetMethod("https://www.pivotaltracker.com/services/v3/tokens/active");
        restMethod.setRequestHeader("Accept", "application/xml");
        restMethod.setRequestHeader("Content-Type", "application/xml");
        Document doc;
        client.executeMethod(restMethod);
        doc = new Builder().build(restMethod.getResponseBodyAsStream());
        return doc.query("/token/guid/text()").get(0).getValue();
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
        types.add(FeedType.KASHOO_BUSINESSES);
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }
}
