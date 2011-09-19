package com.easyinsight.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:50 PM
 */
public class CrosstabMapWrapper {
    private Map<String, CrosstabValue> map = new HashMap<String, CrosstabValue>();

    public Map<String, CrosstabValue> getMap() {
        return map;
    }

    public void setMap(Map<String, CrosstabValue> map) {
        this.map = map;
    }
}
