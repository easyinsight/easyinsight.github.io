package com.easyinsight.analysis;

import java.util.*;

/**
 * User: jamesboe
 * Date: 1/19/13
 * Time: 9:45 AM
 */
public class KeyDisplayMapper {
    private Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
    private Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();

    private KeyDisplayMapper(Collection<AnalysisItem> allFields) {
        if (allFields != null) {
            for (AnalysisItem analysisItem : allFields) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }
            for (AnalysisItem analysisItem : allFields) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
            /*for (Map.Entry<String, List<AnalysisItem>> entry : keyMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    Collections.sort(entry.getValue(), new Comparator<AnalysisItem>() {

                        public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                            return ((Integer) analysisItem.getFieldType()).compareTo(analysisItem1.getFieldType());
                        }
                    });
                }
            }
            for (Map.Entry<String, List<AnalysisItem>> entry : displayMap.entrySet()) {
                if (entry.getValue().size() > 1) {
                    Collections.sort(entry.getValue(), new Comparator<AnalysisItem>() {

                        public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                            return ((Integer) analysisItem.getFieldType()).compareTo(analysisItem1.getFieldType());
                        }
                    });
                }
            }*/
        }
    }

    public static KeyDisplayMapper create(Collection<AnalysisItem> allFields) {
        return new KeyDisplayMapper(allFields);
    }

    public Map<String, List<AnalysisItem>> getKeyMap() {
        return keyMap;
    }

    public Map<String, List<AnalysisItem>> getDisplayMap() {
        return displayMap;
    }
}
