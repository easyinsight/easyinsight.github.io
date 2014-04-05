package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.DataSourceInfo;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.logging.LogClass;
import com.easyinsight.users.Account;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * User: jamesboe
 * Date: 10/16/12
 * Time: 4:22 PM
 */
public class SurveyGizmoCompositeSource extends CompositeServerDataSource {

    public static final String CONSUMER_KEY = "e64dde9c589adf8e11ebc8cf037b15aa0530b75e3";
    public static final String CONSUMER_SECRET = "c0a561312ca55d081c8d2d6b41383b59";

    private String sgToken;
    private String sgSecret;

    public String getSgToken() {
        return sgToken;
    }

    public void setSgToken(String sgToken) {
        this.sgToken = sgToken;
    }

    public String getSgSecret() {
        return sgSecret;
    }

    public void setSgSecret(String sgSecret) {
        this.sgSecret = sgSecret;
    }

    public SurveyGizmoCompositeSource() {
        setFeedName("SurveyGizmo");
    }

    @Override
    public int getDataSourceType() {
        return DataSourceInfo.COMPOSITE_PULL;
    }

    @Override
    protected List<IServerDataSourceDefinition> childDataSources(EIConnection conn) throws Exception {
        List<IServerDataSourceDefinition> defaultChildren = super.childDataSources(conn);
        Map<String, SurveyGizmoForm> forms = getForms();
        for (CompositeFeedNode existing : getCompositeFeedNodes()) {
            if (existing.getDataSourceType() == FeedType.SURVEYGIZMO_FORM.getType()) {
                FeedDefinition existingSource = new FeedStorage().getFeedDefinitionData(existing.getDataFeedID(), conn);
                SurveyGizmoFormSource wufooSource = (SurveyGizmoFormSource) existingSource;
                forms.remove(wufooSource.getFormID());
            }
        }
        for (SurveyGizmoForm form : forms.values()) {
            SurveyGizmoFormSource source = new SurveyGizmoFormSource();
            source.setFeedName(form.getName());
            source.setFormID(form.getId());
            newDefinition(source, conn, "", getUploadPolicy());
            CompositeFeedNode node = new CompositeFeedNode();
            node.setDataFeedID(source.getDataFeedID());
            node.setDataSourceType(source.getFeedType().getType());
            getCompositeFeedNodes().add(node);
            defaultChildren.add(source);
        }
        return defaultChildren;
    }


    @Override
    public void exchangeTokens(EIConnection conn, HttpServletRequest request, String externalPin) throws Exception {
        try {
            if (externalPin != null && !"".equals(externalPin)) {
                OAuthConsumer consumer = (OAuthConsumer) request.getSession().getAttribute("oauthConsumer");
                OAuthProvider provider = (OAuthProvider) request.getSession().getAttribute("oauthProvider");
                provider.retrieveAccessToken(consumer, externalPin.trim());
                sgToken = consumer.getToken();
                sgSecret = consumer.getTokenSecret();
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void configureFactory(HTMLConnectionFactory factory) {
        factory.type(HTMLConnectionFactory.TYPE_OAUTH);
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SURVEY_GIZMO WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SURVEY_GIZMO (TOKEN_KEY, SECRET_KEY, DATA_SOURCE_ID) VALUES (?, ?, ?)");
        insertStmt.setString(1, sgToken);
        insertStmt.setString(2, sgSecret);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT TOKEN_KEY, SECRET_KEY FROM SURVEY_GIZMO WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            sgToken = rs.getString(1);
            sgSecret = rs.getString(2);
        }
        getStmt.close();
    }

    @Override
    public String validateCredentials() {
        return null;
    }

    @Override
    public int getRequiredAccountTier() {
        return Account.BASIC;
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SURVEYGIZMO_COMPOSITE;
    }

    @Override
    protected Set<FeedType> getFeedTypes() {
        Set<FeedType> types = new HashSet<FeedType>();
        return types;
    }

    @Override
    protected Collection<ChildConnection> getChildConnections() {
        return new ArrayList<ChildConnection>();
    }

    public Map<String, SurveyGizmoForm> getForms() {
        Map<String, SurveyGizmoForm> forms = new HashMap<String, SurveyGizmoForm>();
        JSONObject surveys = SurveyGizmoUtils.runRequest("/survey", new HttpClient(), this, new ArrayList<NameValuePair>());
        JSONArray data = (JSONArray) surveys.get("data");
        for (Object o : data) {
            JSONObject j = (JSONObject) o;
            SurveyGizmoForm f = new SurveyGizmoForm();
            String formID = (String) j.get("id");
            f.setId(formID);
            f.setName((String) j.get("title"));
            JSONObject doc = SurveyGizmoUtils.runRequest("/survey/" + formID + "/surveyquestion", new HttpClient(), this, new ArrayList<NameValuePair>());
            JSONArray formData = (JSONArray) doc.get("data");
            for(Object oForm : data) {
                JSONObject field = (JSONObject) oForm;

            }
            forms.put(f.getId(), f);
        }
        return forms;
    }
}
