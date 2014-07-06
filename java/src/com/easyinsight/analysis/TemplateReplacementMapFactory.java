package com.easyinsight.analysis;

import java.util.HashMap;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 7/4/14
 * Time: 9:58 AM
 */
public class TemplateReplacementMapFactory extends ReplacementMapFactory {

    private Map<String, AnalysisItem> lookupMap = new HashMap<>();

    public TemplateReplacementMapFactory(Map<String, AnalysisItem> lookupMap) {
        this.lookupMap = lookupMap;
    }

    @Override
    public ReplacementMap createMap() {
        return new TemplateReplacementMap(lookupMap);
    }
}
