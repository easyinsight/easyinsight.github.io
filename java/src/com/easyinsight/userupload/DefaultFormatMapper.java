package com.easyinsight.userupload;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Key;
import com.easyinsight.core.NamedKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* User: jamesboe
* Date: 11/2/12
* Time: 10:18 AM
*/
public class DefaultFormatMapper implements IUploadFormatMapper {

    private Map<String, Key> keyMap;

    public DefaultFormatMapper(List<AnalysisItem> analysisItems) {
        if (analysisItems != null) {
            keyMap = new HashMap<String, Key>();
            for (AnalysisItem analysisItem : analysisItems) {
                keyMap.put(((NamedKey) analysisItem.getKey()).getName(), analysisItem.getKey());
            }
        }
    }

    public Key reconcileKey(String keyName) {
        Key key;
        if (keyMap == null) {
            key = new NamedKey(keyName);
        } else {
            key = keyMap.get(keyName);
        }
        return key;
    }
}
