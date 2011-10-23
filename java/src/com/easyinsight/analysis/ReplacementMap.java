package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 10/13/11
 * Time: 3:56 PM
 */
public class ReplacementMap {

    private Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
    private Map<String, AnalysisItem> qualifiedNameReplacementMap = new HashMap<String, AnalysisItem>();

    public static ReplacementMap fromMap(Map<Long, AnalysisItem> fields) {
        ReplacementMap replacementMap = new ReplacementMap();
        replacementMap.replacementMap = fields;
        return replacementMap;
    }

    public List<AnalysisItem> getFields() {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.addAll(replacementMap.values());
        fields.addAll(qualifiedNameReplacementMap.values());
        return fields;
    }

    public AnalysisItem addField(AnalysisItem analysisItem, boolean changingDataSource) throws CloneNotSupportedException {
        AnalysisItem exists = getField(analysisItem);
        if (exists == null) {
            exists = analysisItem.clone();
            cleanup(exists, changingDataSource);
            replacementMap.put(analysisItem.getAnalysisItemID(), exists);
            qualifiedNameReplacementMap.put(analysisItem.toDisplay() + "-" + analysisItem.getQualifiedSuffix(), exists);
        }
        return exists;
    }

    public AnalysisItem getField(AnalysisItem analysisItem) {
        if (analysisItem.getAnalysisItemID() > 0) {
            return replacementMap.get(analysisItem.getAnalysisItemID());
        } else {
            return qualifiedNameReplacementMap.get(analysisItem.toDisplay() + "-" + analysisItem.getQualifiedSuffix());
        }
    }

    private void cleanup(AnalysisItem analysisItem, boolean changingDataSource) {
        if (changingDataSource) {
            // TODO: validate calculations and lookup tables--if necessary to create, should emit something with the report
            analysisItem.setLookupTableID(null);
        }
    }
}
