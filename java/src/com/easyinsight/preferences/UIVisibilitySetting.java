package com.easyinsight.preferences;

/**
 * User: jamesboe
 * Date: Mar 20, 2010
 * Time: 11:51:27 AM
 */
public class UIVisibilitySetting {
    private String key;
    private boolean selected;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
