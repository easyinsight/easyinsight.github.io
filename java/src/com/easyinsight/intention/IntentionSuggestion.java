package com.easyinsight.intention;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 8:21 PM
 */
public class IntentionSuggestion {

    public static final int SCOPE_DATA_SOURCE = 1;
    public static final int SCOPE_REPORT = 2;

    private String headline;
    private String description;
    private int scope;
    private int type;

    public IntentionSuggestion() {
    }

    public IntentionSuggestion(String headline, String description, int scope, int type) {
        this.headline = headline;
        this.description = description;
        this.scope = scope;
        this.type = type;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScope() {
        return scope;
    }

    public void setScope(int scope) {
        this.scope = scope;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
