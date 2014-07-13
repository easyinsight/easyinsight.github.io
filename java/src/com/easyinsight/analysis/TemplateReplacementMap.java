package com.easyinsight.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: jamesboe
 * Date: 7/4/14
 * Time: 9:58 AM
 */
public class TemplateReplacementMap extends ReplacementMap {

    private Map<String, AnalysisItem> map = new HashMap<>();
    private Map<String, AnalysisItem> lookupMap = new HashMap<>();

    public TemplateReplacementMap(Map<String, AnalysisItem> lookupMap) {
        this.lookupMap = lookupMap;
    }

    @Override
    public List<AnalysisItem> getFields() {
        return new ArrayList<>(map.values());
    }

    @Override
    public AnalysisItem addField(AnalysisItem analysisItem, boolean changingDataSource) throws CloneNotSupportedException {
        if (analysisItem == null) {
            return null;
        }
        AnalysisItem exists = getField(analysisItem);
        if (exists == null) {
            AnalysisItem field = lookupMap.get(analysisItem.toOriginalDisplayName());
            if (field == null) {
                throw new RuntimeException("Could not find " + analysisItem.toOriginalDisplayName());
            }
            exists = field.clone();
            if (analysisItem.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                AnalysisDateDimension source = (AnalysisDateDimension) analysisItem;
                AnalysisDateDimension target = (AnalysisDateDimension) exists;
                target.setDateLevel(source.getDateLevel());
            }
            //exists = analysisItem.clone();
            cleanup(exists, changingDataSource);
            map.put(analysisItem.toOriginalDisplayName(), exists);
        }
        return exists;
    }

    @Override
    public AnalysisItem addField(AnalysisItem analysisItem, boolean changingDataSource, long analysisItemID) throws CloneNotSupportedException {
        return addField(analysisItem, changingDataSource);
    }

    @Override
    public AnalysisItem getField(AnalysisItem analysisItem) {
        return map.get(analysisItem.toOriginalDisplayName());
    }
}
