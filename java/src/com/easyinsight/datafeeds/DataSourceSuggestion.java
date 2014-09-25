package com.easyinsight.datafeeds;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * User: jamesboe
 * Date: 9/23/14
 * Time: 2:02 PM
 */
public class DataSourceSuggestion {

    public static final int SUGGEST_CREATE_COMPOSITE = 1;
    public static final int SUGGEST_CREATE_DASHBOARD = 2;
    public static final int SUGGEST_IMPORT_USERS = 3;
    public static final int SUGGEST_DELIVER_REPORTS = 4;

    private int suggestionType;
    private String suggestionLabel;
    private String suggestionURL;

    public DataSourceSuggestion(int suggestionType, String suggestionLabel, String suggestionURL) {
        this.suggestionType = suggestionType;
        this.suggestionLabel = suggestionLabel;
        this.suggestionURL = suggestionURL;
    }

    public String getSuggestionURL() {
        return suggestionURL;
    }

    public int getSuggestionType() {
        return suggestionType;
    }

    public String getSuggestionLabel() {
        return suggestionLabel;
    }

    public JSONObject toJSON() {
        try {
            JSONObject jo = new JSONObject();
            jo.put("type", suggestionType);
            jo.put("label", suggestionLabel);
            jo.put("url", suggestionURL);
            return jo;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
