package com.easyinsight.datafeeds.batchbook2;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 8/13/12
 * Time: 9:58 AM
 */
public class CustomFieldValue {
    private String customFieldID;

    public CustomFieldValue(String customFieldID) {
        this.customFieldID = customFieldID;
    }

    private Map<String, String> valueMap = new HashMap<String, String>();

    public void addValue(String id, String value) {
        valueMap.put(id, value);
    }

    public String getCustomFieldID() {
        return customFieldID;
    }

    public Map<String, String> getValueMap() {
        return valueMap;
    }
}
