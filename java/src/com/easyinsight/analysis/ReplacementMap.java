package com.easyinsight.analysis;

import com.easyinsight.logging.LogClass;

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
        for (AnalysisItem analysisItem : fields.values()) {
            replacementMap.qualifiedNameReplacementMap.put(analysisItem.toDisplay() + "-" + analysisItem.getQualifiedSuffix(), analysisItem);
        }
        return replacementMap;
    }

    public List<AnalysisItem> getFields() {
        List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
        fields.addAll(replacementMap.values());
        fields.addAll(qualifiedNameReplacementMap.values());
        return fields;
    }

    public AnalysisItem addField(AnalysisItem analysisItem, boolean changingDataSource) throws CloneNotSupportedException {
        if (analysisItem == null) {
            return null;
        }
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
        AnalysisItem returnItem;
        if (analysisItem.getAnalysisItemID() > 0) {
            returnItem = replacementMap.get(analysisItem.getAnalysisItemID());
            if (returnItem == null) {
                LogClass.error("Could not find " + analysisItem.toDisplay() + " by ID.");
            }
        } else {
            returnItem = qualifiedNameReplacementMap.get(analysisItem.toDisplay() + "-" + analysisItem.getQualifiedSuffix());
            if (returnItem == null) {
                LogClass.error("Could not find " + analysisItem.toDisplay() + " by name.");
            }
        }
        return returnItem;
    }

    private void cleanup(AnalysisItem analysisItem, boolean changingDataSource) {
        if (changingDataSource) {
            // TODO: validate calculations and lookup tables--if necessary to create, should emit something with the report
            analysisItem.setLookupTableID(null);
        }
    }
}
