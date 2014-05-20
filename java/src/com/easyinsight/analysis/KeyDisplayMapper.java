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
    private Map<String, List<AnalysisItem>> unqualifiedDisplayMap = new HashMap<String, List<AnalysisItem>>();

    private KeyDisplayMapper(Collection<AnalysisItem> allFields, boolean avoidKeyDisplayCollisions) {
        if (allFields != null) {

            Map<String, Collection<AnalysisItem>> startKeyMap = new HashMap<String, Collection<AnalysisItem>>();
            for (AnalysisItem analysisItem : allFields) {
                Collection<AnalysisItem> items = startKeyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    if (!avoidKeyDisplayCollisions) {
                        items = new ArrayList<AnalysisItem>(1);
                        startKeyMap.put(analysisItem.getKey().toKeyString(), items);
                    } else {
                        items = new HashSet<AnalysisItem>(1);
                        startKeyMap.put(analysisItem.getKey().toKeyString(), items);
                    }
                }
                items.add(analysisItem);
            }

            if (avoidKeyDisplayCollisions) {
                for (AnalysisItem analysisItem : allFields) {
                    Collection<AnalysisItem> items = startKeyMap.get(analysisItem.toDisplay());
                    if (items == null) {
                        items = new HashSet<AnalysisItem>(1);
                        startKeyMap.put(analysisItem.toDisplay(), items);
                    }
                    items.add(analysisItem);
                }
            }

            for (Map.Entry<String, Collection<AnalysisItem>> entry : startKeyMap.entrySet()) {
                List<AnalysisItem> values = new ArrayList<AnalysisItem>(entry.getValue());
                keyMap.put(entry.getKey(), values);
            }

            for (AnalysisItem analysisItem : allFields) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
            for (AnalysisItem analysisItem : allFields) {
                List<AnalysisItem> items = unqualifiedDisplayMap.get(analysisItem.toUnqualifiedDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    unqualifiedDisplayMap.put(analysisItem.toUnqualifiedDisplay(), items);
                }
                items.add(analysisItem);
            }
        }
    }

    public static KeyDisplayMapper create(Collection<AnalysisItem> allFields) {
        return new KeyDisplayMapper(allFields, false);
    }

    public static KeyDisplayMapper create(Collection<AnalysisItem> allFields, boolean avoidKeyDisplayCollisions) {
        return new KeyDisplayMapper(allFields, avoidKeyDisplayCollisions);
    }

    public Map<String, List<AnalysisItem>> getUnqualifiedDisplayMap() {
        return unqualifiedDisplayMap;
    }

    public Map<String, List<AnalysisItem>> getKeyMap() {
        return keyMap;
    }

    public Map<String, List<AnalysisItem>> getDisplayMap() {
        return displayMap;
    }
}
