package com.easyinsight.intention;

/**
 * User: jamesboe
 * Date: 9/17/11
 * Time: 8:21 PM
 */
public class IntentionSuggestion {

    public static final int PROBLEM = 1;
    public static final int WARNING = 2;
    public static final int OTHER = 3;
    public static final int YOU_SHOULD_DO_THIS = 4;

    public static final int SCOPE_DATA_SOURCE = 1;
    public static final int SCOPE_REPORT = 2;

    public static final int OLD_DATA = 1;

    public static final int SUGGESTION_DEAL_SETUP = 2;
    public static final int SUGGESTION_CASE_SETUP = 3;
    public static final int SUGGESTION_ACTIVITY_SETUP = 4;
    public static final int SUGGESTION_LAST_CASE_NOTE = 7;
    public static final int SUGGESTION_LAST_CONTACT_NOTE = 8;
    public static final int SUGGESTION_LAST_DEAL_NOTE = 9;
    public static final int SUGGESTION_LAST_COMPANY_NOTE = 10;
    public static final int SUGGESTION_NOTE_CONFIG = 12;
    public static final int SUGGESTION_TASK_SETUP = 14;

    public static final int MILESTONE_FILTER = 5;

    public static final int ADD_SUMMARY_ROW = 6;
    public static final int NEW_HIERARCHY = 13;
    public static final int CONFIGURE_GAUGE = 15;
    public static final int SUGGESTION_TASK_ADD = 16;
    public static final int WARNING_JOIN_FAILURE = 17;
    public static final int FILTERED_FIELD = 18;
    public static final int DISTINCT_COUNT = 19;
    public static final int WARNING_MESSAGE = 20;
    public static final int CONFIGURE_TRENDING = 21;
    public static final int TURN_OFF_AGGREGATE_QUERY = 22;
    public static final int CONFIGURE_DATE_COMPARISON = 23;
    public static final int FULL_JOINS = 24;

    private String headline;
    private String description;
    private int scope;
    private int type;
    private Integer priority;
    private boolean requiresServerCallback = true;

    public IntentionSuggestion() {
    }

    public IntentionSuggestion(String headline, String description, int scope, int type, Integer priority) {
        this.headline = headline;
        this.description = description;
        this.scope = scope;
        this.type = type;
        this.priority = priority;
    }

    public IntentionSuggestion(String headline, String description, int scope, int type, Integer priority, boolean requiresServerCallback) {
        this(headline, description, scope, type, priority);
        this.requiresServerCallback = requiresServerCallback;
    }

    public boolean isRequiresServerCallback() {
        return requiresServerCallback;
    }

    public void setRequiresServerCallback(boolean requiresServerCallback) {
        this.requiresServerCallback = requiresServerCallback;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
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
