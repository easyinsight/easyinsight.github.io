package com.easyinsight.datafeeds.infusionsoft;

import com.easyinsight.analysis.*;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;

import java.util.Map;

/**
 * User: jamesboe
 * Date: 12/16/13
 * Time: 2:51 PM
 */
class CustomField {
    private int id;
    private int type;
    private String name;
    private String label;

    CustomField(int id, int type, String name, String label) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public AnalysisItem createAnalysisItem(Map<String, Key> keys) {
        String field = String.valueOf(getId());
        Key key = keys.get(field);
        if (key == null) {
            key = new NamedKey(field);
            keys.put(field, key);
        }
        if (getType() == 11 || getType() == 12) {
            return new AnalysisMeasure(key, name, AggregationTypes.SUM);
        } else if (getType() == 3) {
            return new AnalysisMeasure(key, name, AggregationTypes.SUM, true, FormattingConfiguration.CURRENCY);
        } else if (getType() == 4) {
            return new AnalysisMeasure(key, name, AggregationTypes.SUM, true, FormattingConfiguration.PERCENTAGE);
        } else if (getType() == 13) {
            return new AnalysisDateDimension(key, name, AnalysisDateDimension.DAY_LEVEL, true);
        } else if (getType() == 14) {
            return new AnalysisDateDimension(key, name, AnalysisDateDimension.DAY_LEVEL, true);
        } else {
            return new AnalysisDimension(key, name);
        }
    }
}

