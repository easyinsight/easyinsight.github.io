package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* User: jamesboe
* Date: 11/2/12
* Time: 10:19 AM
*/
public class AnalysisItemFormatMapper implements IUploadFormatMapper {

    private Map<String, Key> keyMap = new HashMap<String, Key>();

    public AnalysisItemFormatMapper(List<AnalysisItem> analysisItems) {
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.isConcrete()) {
                keyMap.put(analysisItem.toDisplay(), analysisItem.getKey());
            }
        }
    }

    public void assignPseudoKey(String label, Key key) {
        keyMap.put(label, key);
    }

    public Key reconcileKey(String keyName) {
        return keyMap.get(keyName);
    }
}
