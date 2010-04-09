package com.easyinsight.etl;

import com.easyinsight.analysis.AnalysisItem;

import java.util.List;

/**
 * User: jamesboe
 * Date: Apr 4, 2010
 * Time: 3:24:04 PM
 */
public class LookupTable {
    private String name;
    private long dataSourceID;
    private String urlKey;
    private long lookupTableID;
    private AnalysisItem sourceField;
    private AnalysisItem targetField;
    private List<LookupPair> lookupPairs;

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public AnalysisItem getTargetField() {
        return targetField;
    }

    public void setTargetField(AnalysisItem targetField) {
        this.targetField = targetField;
    }

    public AnalysisItem getSourceField() {

        return sourceField;
    }

    public void setSourceField(AnalysisItem sourceField) {
        this.sourceField = sourceField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public long getLookupTableID() {
        return lookupTableID;
    }

    public void setLookupTableID(long lookupTableID) {
        this.lookupTableID = lookupTableID;
    }

    public List<LookupPair> getLookupPairs() {
        return lookupPairs;
    }

    public void setLookupPairs(List<LookupPair> lookupPairs) {
        this.lookupPairs = lookupPairs;
    }
}
