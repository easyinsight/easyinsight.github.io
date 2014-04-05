package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: jamesboe
 * Date: 3/25/14
 * Time: 10:25 AM
 */
public class SurveyGizmoMultiAnswerSource extends SurveyGizmoBaseSource {

    public static final String LONGITUDE = "Longitude";
    public static final String LATITUDE = "Latitude";
    public static final String CITY = "City";
    public static final String REFERER = "Referer";
    public static final String COMMENTS = "Comments";
    public static final String RESPONSE_TIME = "Response Time";
    public static final String IP = "IP";
    public static final String DATE_SUBMITTED = "Date Submitted";
    public static final String STATUS = "Status";
    public static final String ID = "ID";
    public static final String RESPONSE_ID = "Response ID";
    public static final String REGION = "Region";
    public static final String POSTAL = "Postal";
    public static final String DMA = "DMA";
    public static final String COUNTRY = "Country";
    public static final String USER_AGENT = "User Agent";
    public static final String VARIABLE_FORMAT = "[variable(\"STANDARD_%s\")]";
    public static final String TEST_DATA = "Test Data";
    public static final String CONTACT_ID = "Contact ID";
    public static final String SUBMIT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String DELIMITER = "|";

    private String questionID;

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    @NotNull
    @Override
    protected List<String> getKeys(FeedDefinition parentDefinition) {
        return new ArrayList<String>();
    }

    @Override
    public FeedType getFeedType() {
        return FeedType.SURVEYGIZMO_QUESTION;
    }

    @Override
    public void customStorage(Connection conn) throws SQLException {
        super.customStorage(conn);
        PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM SURVEYGIZMO_MULTI_QUESTION_SOURCE WHERE DATA_SOURCE_ID = ?");
        clearStmt.setLong(1, getDataFeedID());
        clearStmt.executeUpdate();
        clearStmt.close();
        PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO SURVEYGIZMO_MULTI_QUESTION_SOURCE (QUESTION_ID, DATA_SOURCE_ID) VALUES (?, ?)");
        insertStmt.setString(1, questionID);
        insertStmt.setLong(2, getDataFeedID());
        insertStmt.execute();
        insertStmt.close();
    }

    @Override
    public void customLoad(Connection conn) throws SQLException {
        super.customLoad(conn);
        PreparedStatement getStmt = conn.prepareStatement("SELECT QUESTION_ID FROM SURVEYGIZMO_MULTI_QUESTION_SOURCE WHERE DATA_SOURCE_ID = ?");
        getStmt.setLong(1, getDataFeedID());
        ResultSet rs = getStmt.executeQuery();
        if (rs.next()) {
            questionID = rs.getString(1);
        }
        getStmt.close();
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        SurveyGizmoCompositeSource surveyGizmoCompositeSource;
        SurveyGizmoFormSource formSource = (SurveyGizmoFormSource) parentDefinition;
        try {
            surveyGizmoCompositeSource = (SurveyGizmoCompositeSource) new FeedStorage().getFeedDefinitionData(parentDefinition.getParentSourceID(), conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        HttpClient httpClient = new HttpClient();
        JSONObject doc = SurveyGizmoUtils.runRequest("/survey/" + formSource.getFormID() + "/surveyquestion", httpClient, surveyGizmoCompositeSource, new ArrayList<NameValuePair>());
        JSONArray data = (JSONArray) doc.get("data");
        for(Object o : data) {
            JSONObject field = (JSONObject) o;
            String title = (String) ((JSONObject) field.get("title")).get("English");
            String id = String.valueOf(field.get("id"));
            String subType = (String) field.get("_subtype");
            String type = (String) field.get("_type");

            System.out.println("title: " + title);
            System.out.println("id: " + id);
            System.out.println("type: " + type);
            System.out.println("subtype: " + subType);
            System.out.println("\t" + field);

            // monday, tuesday etc are multi_textbox, containing options attribute with array of more questions
            // this question sucks, doesn't it? = id 5, SurveyQuestion, multi_textbox
            if (type.contains("multi")) {

            } else if("SurveyQuestion".equals(type) && !"instructions".equals(subType)) {
                if("slider".equals(subType))
                    fieldBuilder.addField(id, title, new AnalysisMeasure());
                else if("checkbox".equals(subType)) {
                    AnalysisList a = new AnalysisList();
                    a.setDelimiter("\\" + DELIMITER);
                    fieldBuilder.addField(id, title, a);
                }
                else
                    fieldBuilder.addField(id, title, new AnalysisDimension());
            }
        }
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Set<String> lists = allListAnalysisItems();
        DataSet ds = new DataSet();
        DateFormat df = new SimpleDateFormat(SUBMIT_DATE_FORMAT);
        SurveyGizmoCompositeSource surveyGizmoCompositeSource;
        SurveyGizmoFormSource formSource = (SurveyGizmoFormSource) parentDefinition;
        try {
            surveyGizmoCompositeSource = (SurveyGizmoCompositeSource) new FeedStorage().getFeedDefinitionData(parentDefinition.getParentSourceID(), conn);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        HttpClient httpClient = new HttpClient();
        JSONObject jo = SurveyGizmoUtils.runRequest("/survey/" + formSource.getFormID() + "/surveyresponse", httpClient, surveyGizmoCompositeSource, new ArrayList<NameValuePair>());
        JSONArray data = (JSONArray) jo.get("data");
        Pattern p = Pattern.compile("\\[question\\(([A-Za-z0-9]+)\\).*\\]");
        for(Object o : data) {
            JSONObject survey = (JSONObject) o;
            IRow row = ds.createRow();
            Map<Key, List<String>> listsMap = new HashMap<Key, List<String>>();
            for(Map.Entry<String, Object> entry : survey.entrySet()) {

                Matcher m = p.matcher(entry.getKey());
                if(m.matches()) {
                    String keyString = m.replaceAll("$1");
                    if(lists.contains(keyString)) {
                        if(!listsMap.containsKey(keys.get(keyString)))
                            listsMap.put(keys.get(keyString), new ArrayList<String>());
                        List<String> stringList = listsMap.get(keys.get(keyString));
                        if(!((String) entry.getValue()).isEmpty())
                            stringList.add((String) entry.getValue());
                    } else {
                        row.addValue(keys.get(keyString), (String) entry.getValue());
                    }
                }
            }

            for(Map.Entry<Key, List<String>> entry : listsMap.entrySet()) {
                row.addValue(entry.getKey(), StringUtils.join(entry.getValue(), DELIMITER));
            }

            //Builder b = new Builder(survey, keys, row);

            String dateSubmitted = String.valueOf(survey.get("datesubmitted"));
            try {
                Date d = df.parse(dateSubmitted);
                row.addValue(keys.get(DATE_SUBMITTED), d);
            } catch (ParseException e) {

            }
        }

        return ds;
    }

    private Set<String> allListAnalysisItems() {
        Set<String> s = new HashSet<String>();
        for(AnalysisItem i : getFields()) {
            if(i instanceof AnalysisList) {
                s.add(i.getKey().toKeyString());
            }
        }
        return s;
    }

    private class Builder {
        private JSONObject survey;
        private Map<String, Key> keys;
        private IRow row;

        private Builder(JSONObject survey, Map<String, Key> keys, IRow row) {
            this.survey = survey;
            this.keys = keys;
            this.row = row;
        }

        public void addValue(String key, String location) {
            row.addValue(keys.get(key), String.valueOf(survey.get(String.format(VARIABLE_FORMAT, location))));
        }
        public void addRawValue(String key, String location) {
            row.addValue(keys.get(key), String.valueOf(survey.get(location)));
        }
    }
}
