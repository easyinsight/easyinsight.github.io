package com.easyinsight.analysis;

import com.easyinsight.core.Value;

/**
 * User: jamesboe
 * Date: 4/2/12
 * Time: 1:13 PM
 */
public class JoinLabelOption {
    private Value value;
    private String displayName;

    public JoinLabelOption() {
    }

    public JoinLabelOption(Value value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
