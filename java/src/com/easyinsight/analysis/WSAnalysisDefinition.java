package com.easyinsight.analysis;

import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.core.Key;
import com.easyinsight.scrubbing.DataScrub;

import java.util.*;
import java.io.Serializable;

import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:35:07 PM
 */
public abstract class WSAnalysisDefinition implements Serializable {
    private String name;
    private long analysisID;
    private long dataFeedID;
    private List<FilterDefinition> filterDefinitions;
    private List<DataScrub> dataScrubs;
    private int policy;
    private String genre;
    private List<Tag> tagCloud = new ArrayList<Tag>();
    private List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
    private boolean rootDefinition;
    private boolean canSaveDirectly;
    private boolean publiclyVisible;
    private boolean marketplaceVisible;
    private boolean visibleAtFeedLevel;

    public boolean isVisibleAtFeedLevel() {
        return visibleAtFeedLevel;
    }

    public void setVisibleAtFeedLevel(boolean visibleAtFeedLevel) {
        this.visibleAtFeedLevel = visibleAtFeedLevel;
    }

    public boolean isPubliclyVisible() {
        return publiclyVisible;
    }

    public void setPubliclyVisible(boolean publiclyVisible) {
        this.publiclyVisible = publiclyVisible;
    }

    public boolean isMarketplaceVisible() {
        return marketplaceVisible;
    }

    public void setMarketplaceVisible(boolean marketplaceVisible) {
        this.marketplaceVisible = marketplaceVisible;
    }

    //private LimitsMetadata limitsMetadata;

    /*public LimitsMetadata getLimitsMetadata() {
        return limitsMetadata;
    }

    public void setLimitsMetadata(LimitsMetadata limitsMetadata) {
        this.limitsMetadata = limitsMetadata;
    }*/

    public boolean isCanSaveDirectly() {
        return canSaveDirectly;
    }

    public void setCanSaveDirectly(boolean canSaveDirectly) {
        this.canSaveDirectly = canSaveDirectly;
    }

    public boolean isRootDefinition() {
        return rootDefinition;
    }

    public void setRootDefinition(boolean rootDefinition) {
        this.rootDefinition = rootDefinition;
    }

    public List<AnalysisItem> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<AnalysisItem> addedItems) {
        this.addedItems = addedItems;
    }

    public List<Tag> getTagCloud() {
        return tagCloud;
    }

    public void setTagCloud(List<Tag> tagCloud) {
        this.tagCloud = tagCloud;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getPolicy() {
        return policy;
    }

    public void setPolicy(int policy) {
        this.policy = policy;
    }

    public List<DataScrub> getDataScrubs() {
        return dataScrubs;
    }

    public void setDataScrubs(List<DataScrub> dataScrubs) {
        this.dataScrubs = dataScrubs;
    }

    public List<FilterDefinition> getFilterDefinitions() {
        return filterDefinitions;
    }

    public void setFilterDefinitions(List<FilterDefinition> filterDefinitions) {
        this.filterDefinitions = filterDefinitions;
    }

    public abstract String getDataFeedType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAnalysisID() {
        return analysisID;
    }

    public void setAnalysisID(long analysisID) {
        this.analysisID = analysisID;
    }

    public long getDataFeedID() {
        return dataFeedID;
    }

    public void setDataFeedID(long dataFeedID) {
        this.dataFeedID = dataFeedID;
    }

    public abstract Set<AnalysisItem> getAllAnalysisItems();

    public List<Key> getColumnKeys(List<AnalysisItem> allItems) {
        Set<Key> columnSet = new HashSet<Key>();
        Set<AnalysisItem> analysisItems = getAllAnalysisItems();
        for (AnalysisItem analysisItem : analysisItems) {
            List<AnalysisItem> items = analysisItem.getAnalysisItems(allItems, analysisItems);
            for (AnalysisItem item : items) {
                //if (item.getAnalysisItemID()) {
                    columnSet.add(item.getKey());
                //}
            }
        }
        if (getFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : getFilterDefinitions()) {
                List<AnalysisItem> items = filterDefinition.getField().getAnalysisItems(allItems, analysisItems);
                for (AnalysisItem item : items) {
                    //if (item.getAnalysisItemID() != 0) {
                        columnSet.add(item.getKey());
                    //}
                }
                //columnSet.add(filterDefinition.getField().getKey());
            }
        }
        if (getDataScrubs() != null) {
            for (DataScrub dataScrub : getDataScrubs()) {
                columnSet.addAll(dataScrub.createNeededKeys(analysisItems));
            }
        }
        for (AnalysisItem analysisItem : getLimitFields()) {
            columnSet.add(analysisItem.getKey());
        }
        return new ArrayList<Key>(columnSet);
    }

    public LimitsResults applyLimits(DataSet dataSet) {
        return new LimitsResults(false, dataSet.getRows().size(), dataSet.getRows().size());
    }

    public List<AnalysisItem> getLimitFields() {
        return new ArrayList<AnalysisItem>();
    }

    public Map<String, AnalysisItem> createStructure() {
        Map<String, AnalysisItem> structure = new HashMap<String, AnalysisItem>();
        createReportStructure(structure);
        return structure;
    }

    protected abstract void createReportStructure(Map<String, AnalysisItem> structure);

    public abstract void populateFromReportStructure(Map<String, AnalysisItem> structure);

    @Nullable
    protected AnalysisItem firstItem(String key, Map<String, AnalysisItem> structure) {
        String compositeKey = key + "-" + 0;
        return structure.get(compositeKey);
    }

    protected List<AnalysisItem> items(String key, Map<String, AnalysisItem> structure) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        boolean found = true;
        int i = 0;
        while (found) {
            String compositeKey = key + "-" + i;
            AnalysisItem value = structure.get(compositeKey);
            if (value == null) {
                found = false;
            } else {
                items.add(value);
            }
            i++;
        }
        return items;
    }

    protected void addItems(String key, List<AnalysisItem> items, Map<String, AnalysisItem> structure) {
        for (int i = 0; i < items.size(); i++) {
            String compositeKey = key + "-" + i;
            structure.put(compositeKey, items.get(i));
        }
    }
}
