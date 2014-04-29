package com.easyinsight.datafeeds;

import org.jetbrains.annotations.Nullable;

/**
* User: jamesboe
* Date: 2/10/14
* Time: 10:52 PM
*/
public class HTMLConnectionProperty {

    public static final int STRING = 0;
    public static final int INTEGER = 1;
    public static final int TEXT = 2;

    private String field;
    private String property;
    private String safeProperty;
    private String explanation;
    private boolean password;
    private int type;

    HTMLConnectionProperty(String field, String property, @Nullable String explanation, boolean password, int type) {
        this.field = field;
        this.property = property;
        safeProperty = "p" + property;
        this.explanation = explanation;
        this.password = password;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public String getField() {
        return field;
    }

    public String getProperty() {
        return property;
    }

    public String getSafeProperty() {
        return safeProperty;
    }

    public String getExplanation() {
        return explanation;
    }

    public boolean isPassword() {
        return password;
    }
}
