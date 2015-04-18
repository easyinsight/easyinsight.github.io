package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.*;
import com.easyinsight.datafeeds.basecampnext.BasecampNextAccount;
import com.easyinsight.datafeeds.composite.ChildConnection;
import com.easyinsight.datafeeds.composite.CompositeServerDataSource;
import com.easyinsight.html.RedirectUtil;
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

    public static final String CONSUMER_KEY = "4037947e4dc35d9a2714aeda946b21da055280906";
    public static final String CONSUMER_SECRET = "a0ba8943df9c4f07cd4a03516fa5ee98";

    private String sgToken;
    private String sgSecret;

    private String formID;

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getFormID() {
        return formID;
    }

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
        SurveyGizmoQuestionSource questionSource = null;
        Map<String, SurveyGizmoMultiQuestionSource> map = new HashMap<>();
        for (IServerDataSourceDefinition existing : defaultChildren) {
            if (existing.getFeedType().getType() == FeedType.SURVEYGIZMO_QUESTION.getType()) {
                questionSource = (SurveyGizmoQuestionSource) existing;
            } else if (existing.getFeedType().getType() == FeedType.SURVEYGIZMO_MULTIPLE.getType()) {
                SurveyGizmoMultiQuestionSource multiQuestionSource = (SurveyGizmoMultiQuestionSource) existing;
                map.put(multiQuestionSource.getParentQuestionID(), multiQuestionSource);
            }
        }
        if (formID != null) {
            HttpClient httpClient = new HttpClient();

            JSONObject doc = SurveyGizmoUtils.runRequest("/survey/" + formID + "/surveyquestion", httpClient, this, new ArrayList<>());

            Map<String, String> subQuestionSKUtoParent = new HashMap<>();

            Map<String, Integer> tableTypeMap = new HashMap<>();

            JSONArray data = (JSONArray) doc.get("data");
            for (Object o : data) {
                JSONObject field = (JSONObject) o;
                String title = (String) ((JSONObject) field.get("title")).get("English");
                String id = String.valueOf(field.get("id"));
                String subType = (String) field.get("_subtype");
                String type = (String) field.get("_type");

                if ("SurveyQuestion".equals(type) && "table".equals(subType)) {
                    SurveyGizmoMultiQuestionSource source;
                    if (!map.containsKey(id)) {

                        source = new SurveyGizmoMultiQuestionSource();
                        source.setParentQuestionID(id);
                        if (title.length() > 25) {
                            title = title.substring(0, 22) + "...";
                        }
                        source.setFeedName(title);
                        newDefinition(source, conn, "", getUploadPolicy());
                        CompositeFeedNode node = new CompositeFeedNode();
                        node.setDataFeedID(source.getDataFeedID());
                        node.setDataSourceType(source.getFeedType().getType());
                        getCompositeFeedNodes().add(node);
                        defaultChildren.add(source);
                        map.put(id, source);
                    } else {
                        source = map.get(id);
                    }

                    Map properties = (Map) field.get("properties");
                    if (properties.containsKey("map_key")) {
                        String mapKey = properties.get("map_key").toString();
                        if (mapKey.equals("table-radio")) {
                            source.setTableType(2);
                            tableTypeMap.put(id, 2);
                        } else {
                            source.setTableType(1);
                            tableTypeMap.put(id, 1);
                        }
                    }

                    List<Integer> subQuestionSKUs = (List<Integer>) field.get("sub_question_skus");
                    for (Integer sku : subQuestionSKUs) {
                        subQuestionSKUtoParent.put(String.valueOf(sku), id);
                    }
                } else if ("SurveyQuestion".equals(type) && "checkbox".equals(subType)) {
                    SurveyGizmoMultiQuestionSource source;
                    if (!map.containsKey(id)) {

                        source = new SurveyGizmoMultiQuestionSource();
                        source.setParentQuestionID(id);
                        if (title.length() > 25) {
                            title = title.substring(0, 22) + "...";
                        }
                        source.setFeedName(title);
                        newDefinition(source, conn, "", getUploadPolicy());
                        CompositeFeedNode node = new CompositeFeedNode();
                        node.setDataFeedID(source.getDataFeedID());
                        node.setDataSourceType(source.getFeedType().getType());
                        getCompositeFeedNodes().add(node);
                        defaultChildren.add(source);
                        map.put(id, source);
                        source.setTableType(3);

                    } else {
                        source = map.get(id);
                    }
                    List<Map> options = (List<Map>) field.get("options");
                    List<AnalysisItem> fields = new ArrayList<>();
                    for (Map option : options) {
                        AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(id + ":" + option.get("id").toString()), option.get("value").toString(), AggregationTypes.AVERAGE);
                        measure.setPrecision(2);
                        measure.setMinPrecision(2);
                        fields.add(measure);
                    }
                    source.getQueuedFields().addAll(fields);
                } else if ("SurveyQuestion".equals(type) && "rank".equals(subType)) {
                    SurveyGizmoMultiQuestionSource source;
                    if (!map.containsKey(id)) {

                        source = new SurveyGizmoMultiQuestionSource();
                        source.setParentQuestionID(id);
                        if (title.length() > 25) {
                            title = title.substring(0, 22) + "...";
                        }
                        source.setFeedName(title);
                        newDefinition(source, conn, "", getUploadPolicy());
                        CompositeFeedNode node = new CompositeFeedNode();
                        node.setDataFeedID(source.getDataFeedID());
                        node.setDataSourceType(source.getFeedType().getType());
                        getCompositeFeedNodes().add(node);
                        defaultChildren.add(source);
                        map.put(id, source);

                    } else {
                        source = map.get(id);
                    }
                    List<Map> options = (List<Map>) field.get("options");
                    List<AnalysisItem> fields = new ArrayList<>();
                    for (Map option : options) {
                        AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(id + ":" + option.get("id").toString()), option.get("value").toString(), AggregationTypes.AVERAGE);
                        measure.setPrecision(2);
                        measure.setMinPrecision(2);
                        fields.add(measure);
                    }
                    source.getQueuedFields().addAll(fields);
                }
            }


            if (questionSource == null) {

                questionSource = new SurveyGizmoQuestionSource();
                questionSource.setFeedName("Main Questions");
                newDefinition(questionSource, conn, "", getUploadPolicy());
                CompositeFeedNode node = new CompositeFeedNode();
                node.setDataFeedID(questionSource.getDataFeedID());
                node.setDataSourceType(questionSource.getFeedType().getType());
                getCompositeFeedNodes().add(node);
                defaultChildren.add(questionSource);
            }

            Map<String, Map<String, String>> multiTextData = new HashMap<>();

            for (Object o : data) {
                JSONObject field = (JSONObject) o;
                String title = (String) ((JSONObject) field.get("title")).get("English");
                String id = String.valueOf(field.get("id"));
                String subType = (String) field.get("_subtype");
                String type = (String) field.get("_type");
                List<AnalysisItem> analysisItems = new ArrayList<>();

                if ("SurveyQuestion".equals(type)  && !"instructions".equals(subType) && !"table".equals(subType)) {

                    if ("multi_textbox".equals(subType)) {

                        String tableID = subQuestionSKUtoParent.get(id);
                        Integer tableType = tableTypeMap.get(tableID);

                        if (tableType != null && tableType == 1) {
                            Map<String, String> qIDs = multiTextData.get(tableID);
                            if (qIDs == null) {
                                qIDs = new HashMap<>();
                                analysisItems.add(new AnalysisDimension(new NamedKey("Category")));
                                List<Map> options = (List<Map>) field.get("options");
                                for (Map option : options) {
                                    String optionID = option.get("id").toString();
                                    String value = option.get("value").toString();
                                    AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(tableID + ":" + optionID), value, AggregationTypes.AVERAGE);
                                    measure.setPrecision(2);
                                    measure.setMinPrecision(2);
                                    analysisItems.add(measure);
                                }
                                multiTextData.put(tableID, qIDs);
                            }
                            qIDs.put(id, title);
                        } else {
                            List<Map> options = (List<Map>) field.get("options");
                            for (Map option : options) {
                                String optionID = option.get("id").toString();
                                String value = option.get("value").toString();
                                analysisItems.add(new AnalysisDimension(new NamedKey(id + ":" + optionID), value));
                            }
                        }
                    } else if ("radio".equals(subType)) {
                        String tableID = subQuestionSKUtoParent.get(id);
                        Integer tableType = tableTypeMap.get(tableID);
                        if (tableType != null && tableType == 2) {
                            Map<String, String> qIDs = multiTextData.get(tableID);
                            if (qIDs == null) {
                                qIDs = new HashMap<>();
                                analysisItems.add(new AnalysisDimension(new NamedKey("Category")));
                                List<Map> options = (List<Map>) field.get("options");
                                for (Map option : options) {
                                    String optionID = option.get("id").toString();
                                    String value = option.get("value").toString();
                                    AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(tableID + ":" + optionID), value, AggregationTypes.AVERAGE);
                                    measure.setPrecision(2);
                                    measure.setMinPrecision(2);
                                    analysisItems.add(measure);
                                }
                                multiTextData.put(tableID, qIDs);
                            }
                            qIDs.put(id, title);
                        } else {
                            AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(id), title, AggregationTypes.AVERAGE);
                            measure.setPrecision(2);
                            measure.setMinPrecision(2);
                            analysisItems.add(measure);
                        }
                    } else if ("checkbox".equals(subType)) {

                    } else if ("rank".equals(subType)) {

                    } else {

                        Map properties = (Map) field.get("properties");
                        if (properties.containsKey("subtype")) {
                            String dateSub = properties.get("subtype").toString();
                            if ("DATE".equals(dateSub)) {
                                analysisItems.add(new AnalysisDateDimension(new NamedKey(id), title, AnalysisDateDimension.DAY_LEVEL));
                            }
                        }

                        if (analysisItems.size() == 0) {
                            if ("nps".equals(subType)) {
                                {
                                    AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(id), title, AggregationTypes.AVERAGE);
                                    measure.setPrecision(2);
                                    measure.setMinPrecision(2);
                                    analysisItems.add(measure);
                                }
                                {
                                    AnalysisMeasure promoterMeasure = new AnalysisMeasure(new NamedKey(id), title + " Promoters", AggregationTypes.AVERAGE);
                                    promoterMeasure.setPrecision(2);
                                    promoterMeasure.setMinPrecision(2);
                                    analysisItems.add(promoterMeasure);
                                }
                                {
                                    AnalysisMeasure promoterMeasure = new AnalysisMeasure(new NamedKey(id), title + " Detractors", AggregationTypes.AVERAGE);
                                    promoterMeasure.setPrecision(2);
                                    promoterMeasure.setMinPrecision(2);
                                    analysisItems.add(promoterMeasure);
                                }
                                {
                                    AnalysisMeasure promoterMeasure = new AnalysisMeasure(new NamedKey(id), title + " Passives", AggregationTypes.AVERAGE);
                                    promoterMeasure.setPrecision(2);
                                    promoterMeasure.setMinPrecision(2);
                                    analysisItems.add(promoterMeasure);
                                }
                            } else if ("slider".equals(subType)) {
                                AnalysisMeasure measure = new AnalysisMeasure(new NamedKey(id), title, AggregationTypes.AVERAGE);
                                measure.setPrecision(2);
                                measure.setMinPrecision(2);
                                analysisItems.add(measure);
                            } else {
                                analysisItems.add(new AnalysisDimension(new NamedKey(id), title));
                            }
                        }
                    }
                }

                if (analysisItems.size() > 0) {
                    for (AnalysisItem analysisItem : analysisItems) {
                        String parent = subQuestionSKUtoParent.get(id);
                        if (parent != null) {
                            SurveyGizmoMultiQuestionSource multiQuestionSource = map.get(parent);
                            multiQuestionSource.getQueuedFields().add(analysisItem);
                            Map<String, String> qIDs = multiTextData.get(parent);
                            if (qIDs != null) {
                                multiQuestionSource.setqIDs(qIDs);
                            }
                        } else {
                            questionSource.getQueuedFields().add(analysisItem);
                        }
                    }
                }
            }
        }

        return defaultChildren;
    }

    @Override
    protected List<CompositeFeedConnection> getAdditionalConnections() throws SQLException {
        List<CompositeFeedConnection> connections = new ArrayList<>();
        Map<Long, FeedDefinition> map = new HashMap<>();
        long questionSourceID = 0;
        for (CompositeFeedNode child : getCompositeFeedNodes()) {
            FeedDefinition childDef = new FeedStorage().getFeedDefinitionData(child.getDataFeedID());
            map.put(child.getDataFeedID(), childDef);
            if (child.getDataSourceType() == FeedType.SURVEYGIZMO_QUESTION.getType()) {
                questionSourceID = childDef.getDataFeedID();
            }
        }

        for (CompositeFeedNode node : getCompositeFeedNodes()) {
            if (node.getDataSourceType() == FeedType.SURVEYGIZMO_MULTIPLE.getType()) {
                {
                    FeedDefinition sourceDef = map.get(questionSourceID);
                    FeedDefinition targetDef = map.get(node.getDataFeedID());
                    Key sourceKey = sourceDef.getField("Row ID");
                    Key targetKey = targetDef.getField(node.getDataSourceName() + " Row ID");
                    connections.add(new CompositeFeedConnection(questionSourceID, node.getDataFeedID(), sourceKey, targetKey));
                }
            }
        }

        return connections;
    }

    private transient List<Map<String, Object>> results;

    public List<Map<String, Object>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, Object>> results) {
        this.results = results;
    }

    protected List<IServerDataSourceDefinition> sortSources(List<IServerDataSourceDefinition> children) {
        List<IServerDataSourceDefinition> end = new ArrayList<IServerDataSourceDefinition>();
        Set<Integer> set = new HashSet<Integer>();
        for (IServerDataSourceDefinition s : children) {
            if (s.getFeedType().getType() == FeedType.SURVEYGIZMO_QUESTION.getType()) {
                set.add(s.getFeedType().getType());
                end.add(s);
            }
        }
        for (IServerDataSourceDefinition s : children) {
            if (!set.contains(s.getFeedType().getType())) {
                end.add(s);
            }
        }
        return end;
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
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SURVEY_GIZMO (TOKEN_KEY, SECRET_KEY, DATA_SOURCE_ID, FORM_ID) VALUES (?, ?, ?, ?)");
        insertStmt.setString(1, sgToken);
        insertStmt.setString(2, sgSecret);
        insertStmt.setLong(3, getDataFeedID());
        insertStmt.setString(4, formID);
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT TOKEN_KEY, SECRET_KEY, FORM_ID FROM SURVEY_GIZMO WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            sgToken = rs.getString(1);
            sgSecret = rs.getString(2);
            formID = rs.getString(3);
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

    /*public Map<String, SurveyGizmoForm> getForms() {
        // christine_salmon@waters.com
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
    }*/

    public Collection<BasecampNextAccount> getAvailableSurveys() {
        Collection<BasecampNextAccount> availableSurveys = new ArrayList<>();
        JSONObject surveys = SurveyGizmoUtils.runRequest("/survey", new HttpClient(), this, new ArrayList<NameValuePair>());
        JSONArray data = (JSONArray) surveys.get("data");
        for (Object o : data) {
            JSONObject j = (JSONObject) o;
            BasecampNextAccount survey = new BasecampNextAccount();
            survey.setId((String) j.get("id"));
            survey.setName((String) j.get("title"));
            availableSurveys.add(survey);
        }
        return availableSurveys;
    }

    public String postOAuthSetup(HttpServletRequest request) {
        if (getFormID() == null || "".equals(getFormID())) {
            return RedirectUtil.getURL(request, "/app/html/dataSources/" + getApiKey() + "/surveyGizmoSurveySelection");
        } else {
            return null;
        }
    }
}
