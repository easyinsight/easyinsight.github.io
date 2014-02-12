package com.easyinsight.datafeeds;

import org.jetbrains.annotations.Nullable;

/**
* User: jamesboe
* Date: 2/10/14
* Time: 10:52 PM
*/
public class HTMLConnectionProperty {
    private String field;
    private String property;
    private String safeProperty;
    private String explanation;
    private boolean password;

    HTMLConnectionProperty(String field, String property, @Nullable String explanation, boolean password) {
        this.field = field;
        this.property = property;
        safeProperty = "p" + property;
        this.explanation = explanation;
        this.password = password;
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
