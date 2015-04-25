package com.easyinsight.datafeeds.surveygizmo;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedType;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.storage.IDataStorage;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 2/20/14
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class SurveyGizmoQuestionSource extends SurveyGizmoBaseSource {

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
    public static final String COUNT = "Count";
    public static final String ROW_ID = "Row ID";

    @Override
    public FeedType getFeedType() {
        return FeedType.SURVEYGIZMO_QUESTION;
    }


    private transient List<AnalysisItem> queuedFields;

    public List<AnalysisItem> getQueuedFields() {
        if (queuedFields == null) {
            queuedFields = new ArrayList<>();
        }
        return queuedFields;
    }

    @Override
    protected void createFields(FieldBuilder fieldBuilder, Connection conn, FeedDefinition parentDefinition) {
        fieldBuilder.addField(COUNT, new AnalysisMeasure());
        fieldBuilder.addField(DATE_SUBMITTED, new AnalysisDateDimension());
        fieldBuilder.addField(ROW_ID, new AnalysisDimension());
        fieldBuilder.addField(LATITUDE, new AnalysisDimension());
        fieldBuilder.addField(LONGITUDE, new AnalysisDimension());
        fieldBuilder.addField(CONTACT_ID, new AnalysisDimension());
        fieldBuilder.addField(STATUS, new AnalysisDimension());
        fieldBuilder.addField(TEST_DATA, new AnalysisDimension());
        fieldBuilder.addField(ID, new AnalysisDimension());

        List<AnalysisItem> queuedFields = getQueuedFields();
        for (AnalysisItem item : queuedFields) {
            NamedKey namedKey = (NamedKey) item.getKey();
            fieldBuilder.addField(namedKey.getName(), item);
        }
    }

    private transient Set<String> npsQuestions;

    public void setNpsQuestions(Set<String> npsQuestions) {
        this.npsQuestions = npsQuestions;
    }

    public DataSet getDataSet(Map<String, Key> keys, Date now, FeedDefinition parentDefinition, IDataStorage IDataStorage, EIConnection conn, String callDataID, Date lastRefreshDate) throws ReportException {
        Set<String> lists = allListAnalysisItems();
        DataSet ds = new DataSet();
        DateFormat df = new SimpleDateFormat(SUBMIT_DATE_FORMAT);
        SurveyGizmoCompositeSource surveyGizmoCompositeSource = (SurveyGizmoCompositeSource) parentDefinition;
        HttpClient httpClient = new HttpClient();
        int page = 1;
        int i = 0;
        int totalPages;

        List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();

        do {
            JSONObject jo = SurveyGizmoUtils.runRequest("/survey/" + surveyGizmoCompositeSource.getFormID() + "/surveyresponse", httpClient, surveyGizmoCompositeSource, new ArrayList<NameValuePair>());

            JSONArray data = (JSONArray) jo.get("data");
            page++;
            totalPages = (Integer) jo.get("total_pages");

            Pattern optionPattern = Pattern.compile("\\[question\\(([A-Za-z0-9]+)\\).*\\]");
            Pattern p = Pattern.compile(".*option\\(([A-Za-z0-9]+)\\).*\\]");
            Pattern b = Pattern.compile("\\[variable\\((STANDARD_[A-Za-z0-9]+)\\).*\\]");



            for (Object o : data) {
                JSONObject survey = (JSONObject) o;
                IRow row = ds.createRow();
                row.addValue(keys.get(ROW_ID), String.valueOf(i));
                row.addValue(keys.get(COUNT), 1);
                Map<Key, List<String>> listsMap = new HashMap<>();

                Map<String, Object> cache = new HashMap<>();

                cache.put("row_id", String.valueOf(i++));

                for (Map.Entry<String, Object> entry : survey.entrySet()) {

                    if (!entry.getKey().contains("option")) {
                        Matcher m = optionPattern.matcher(entry.getKey());
                        if (m.matches()) {
                            String keyString = m.replaceAll("$1");
                            if (lists.contains(keyString)) {
                                if (!listsMap.containsKey(keys.get(keyString)))
                                    listsMap.put(keys.get(keyString), new ArrayList<>());
                                List<String> stringList = listsMap.get(keys.get(keyString));
                                if (!((String) entry.getValue()).isEmpty())
                                    stringList.add((String) entry.getValue());
                            } else {
                                if (keys.get(keyString) == null) {
                                    cache.put(keyString, entry.getValue());
                                } else {
                                    npsPopulate(row, keys.get(keyString), (String) entry.getValue(), npsQuestions, keys);
                                    row.addValue(keys.get(keyString), (String) entry.getValue());
                                }
                            }
                        }
                    } else {

                        // base question data

                        // additional question data

                        Matcher m = p.matcher(entry.getKey());
                        if (m.matches()) {
                            String keyString = m.replaceAll("$1");
                            Matcher optionMatcher = optionPattern.matcher(entry.getKey());
                            String optionVal = null;
                            if (optionMatcher.matches()) {
                                optionVal = optionMatcher.replaceAll("$1");
                            }
                            if (lists.contains(keyString)) {
                                if (!listsMap.containsKey(keys.get(keyString)))
                                    listsMap.put(keys.get(keyString), new ArrayList<String>());
                                List<String> stringList = listsMap.get(keys.get(keyString));
                                if (!((String) entry.getValue()).isEmpty())
                                    stringList.add((String) entry.getValue());
                            } else {
                                if (keys.get(keyString) == null) {
                                    if (optionVal == null) {
                                        cache.put(keyString, entry.getValue());
                                    } else {
                                        cache.put(optionVal + ":" + keyString, entry.getValue());
                                    }
                                } else {
                                    npsPopulate(row, keys.get(keyString), (String) entry.getValue(), npsQuestions, keys);
                                    row.addValue(keys.get(keyString), (String) entry.getValue());
                                }
                            }
                        }

                        // metadata info

                        Matcher bm = b.matcher(entry.getKey());
                        if (bm.matches()) {
                            String keyString = bm.replaceAll("$1");
                            String optionVal = null;
                            Matcher optionMatcher = optionPattern.matcher(entry.getKey());
                            if (optionMatcher.matches()) {
                                optionVal = optionMatcher.replaceAll("$1");
                            }
                            if (lists.contains(keyString)) {
                                if (!listsMap.containsKey(keys.get(keyString)))
                                    listsMap.put(keys.get(keyString), new ArrayList<String>());
                                List<String> stringList = listsMap.get(keys.get(keyString));
                                if (!((String) entry.getValue()).isEmpty())
                                    stringList.add((String) entry.getValue());
                            } else {
                                if (keys.get(keyString) == null) {
                                    if (optionVal == null) {
                                        cache.put(keyString, entry.getValue());
                                    } else {
                                        cache.put(optionVal + ":" + keyString, entry.getValue());
                                    }
                                } else {
                                    row.addValue(keys.get(keyString), (String) entry.getValue());
                                }
                            }
                        }
                    }


                }

                Builder builder = new Builder(survey, keys, row);
                builder.addValue(LONGITUDE, "LONG");
                builder.addValue(LATITUDE, "LAT");
                /*builder.addValue(CITY, "GEOCITY");
                builder.addValue(REFERER, "REFERER");
                builder.addValue(COMMENTS, "COMMENTS");
                builder.addValue(RESPONSE_TIME, "RESPONSETIME");
                builder.addValue(IP, "IP");
                builder.addValue(REGION, "GEOREGION");
                builder.addValue(POSTAL, "GEOPOSTAL");
                builder.addValue(DMA, "GEODMA");
                builder.addValue(COUNTRY, "GEOCOUNTRY");
                builder.addValue(USER_AGENT, "USERAGENT");*/
                builder.addRawValue(ID, "id");
                builder.addRawValue(STATUS, "status");
                builder.addRawValue(TEST_DATA, "is_test_data");
                /*builder.addRawValue(RESPONSE_ID, "responseID");*/
                builder.addRawValue(CONTACT_ID, "contact_id");

                if (cache.size() > 0) {
                    results.add(cache);
                }

                for (Map.Entry<Key, List<String>> entry : listsMap.entrySet()) {
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
        } while (page <= totalPages);

        surveyGizmoCompositeSource.setResults(results);

        return ds;
    }

    private void npsPopulate(IRow row, Key key, String value, Set<String> npsQuestions, Map<String, Key> keyMap) {


        if (key instanceof NamedKey) {
            NamedKey namedKey = (NamedKey) key;
            if (npsQuestions.contains(namedKey.getName())) {
                Integer intValue = Integer.parseInt(value);
                if (intValue >= 0 && intValue < 7) {
                    row.addValue(keyMap.get(namedKey.getName() + " Detractors"), 1);
                } else if (intValue >= 7 && intValue < 9) {
                    row.addValue(keyMap.get(namedKey.getName() + " Passives"), 1);
                } else if (intValue >= 8 && intValue < 7) {
                    row.addValue(keyMap.get(namedKey.getName() + " Promoters"), 1);
                }
            }
        }

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
