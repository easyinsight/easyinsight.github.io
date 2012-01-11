package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSKPIDefinition;
import com.easyinsight.database.Database;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.pipeline.CleanupComponent;
import com.easyinsight.pipeline.IComponent;
import com.easyinsight.pipeline.ResultsBridge;

import java.sql.SQLException;
import java.util.*;
import java.io.Serializable;

import com.easyinsight.preferences.ImageDescriptor;
import org.jetbrains.annotations.Nullable;

/**
 * User: James Boe
 * Date: Jan 10, 2008
 * Time: 7:35:07 PM
 */
public abstract class WSAnalysisDefinition implements Serializable {

    public static final int LIST = 1;
    public static final int CROSSTAB = 2;
    public static final int MAP = 3;
    public static final int COLUMN = 4;
    public static final int COLUMN3D = 5;
    public static final int BAR = 6;
    public static final int BAR3D = 7;
    public static final int PIE = 8;
    public static final int PIE3D = 9;
    public static final int LINE = 10;
    public static final int LINE3D = 11;
    public static final int AREA = 12;
    public static final int AREA3D = 13;
    public static final int PLOT = 14;
    public static final int BUBBLE = 15;
    public static final int GAUGE = 16;
    public static final int TREE = 17;
    public static final int TREE_MAP = 18;
    public static final int MM_LINE = 18;
    public static final int MAP_WORLD = 20;
    public static final int MAP_USA = 21;
    public static final int MAP_ASIA = 22;
    public static final int MAP_AMERICAS = 23;
    public static final int MAP_EUROPE = 24;
    public static final int MAP_MIDDLE_EAST = 25;
    public static final int TIMELINE = 26;
    public static final int HEATMAP = 27;
    public static final int MAP_AFRICA = 28;
    public static final int GANTT = 29;
    public static final int FORM = 30;
    public static final int STACKED_COLUMN = 31;
    public static final int STACKED_BAR = 32;
    public static final int VERTICAL_LIST = 33;
    public static final int VERTICAL_LIST_COMBINED = 34;
    public static final int TREND = 35;
    public static final int DIAGRAM = 36;
    public static final int TREND_GRID = 37;
    public static final int YTD = 38;
    public static final int COMPARE_YEARS = 39;

    private String name;
    private String authorName;
    private boolean logReport;
    private String urlKey;
    private long analysisID;
    private long dataFeedID;
    private int reportType;
    private long reportStateID;
    private List<FilterDefinition> filterDefinitions;
    private int policy;
    private List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
    private boolean canSaveDirectly;
    private boolean publiclyVisible;
    private boolean marketplaceVisible;
    private boolean solutionVisible;
    private boolean visibleAtFeedLevel;
    private Date dateCreated;
    private Date dateUpdated;
    private String description;
    private boolean temporaryReport;
    private int fixedWidth;
    private boolean accountVisible;
    private List<JoinOverride> joinOverrides;
    private boolean optimized;
    private boolean fullJoins;
    private String marmotScript;
    private String reportRunMarmotScript;
    private int folder;
    private boolean dataSourceFields;
    private boolean lookupTableOptimization;

    private ImageDescriptor headerImage;
    private String fontName = "Tahoma";
    private int fontSize = 12;
    private double backgroundAlpha = 1;

    public boolean isLookupTableOptimization() {
        return lookupTableOptimization;
    }

    public void setLookupTableOptimization(boolean lookupTableOptimization) {
        this.lookupTableOptimization = lookupTableOptimization;
    }

    public boolean isDataSourceFields() {
        return dataSourceFields;
    }

    public void setDataSourceFields(boolean dataSourceFields) {
        this.dataSourceFields = dataSourceFields;
    }

    public boolean isLogReport() {
        return logReport;
    }

    public void setLogReport(boolean logReport) {
        this.logReport = logReport;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }

    public String getReportRunMarmotScript() {
        return reportRunMarmotScript;
    }

    public void setReportRunMarmotScript(String reportRunMarmotScript) {
        this.reportRunMarmotScript = reportRunMarmotScript;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public boolean isOptimized() {
        return optimized;
    }

    public void setOptimized(boolean optimized) {
        this.optimized = optimized;
    }

    public double getBackgroundAlpha() {
        return backgroundAlpha;
    }

    public void setBackgroundAlpha(double backgroundAlpha) {
        this.backgroundAlpha = backgroundAlpha;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public List<JoinOverride> getJoinOverrides() {
        return joinOverrides;
    }

    public void setJoinOverrides(List<JoinOverride> joinOverrides) {
        this.joinOverrides = joinOverrides;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public boolean isTemporaryReport() {
        return temporaryReport;
    }

    public void setTemporaryReport(boolean temporaryReport) {
        this.temporaryReport = temporaryReport;
    }

    public boolean isSolutionVisible() {
        return solutionVisible;
    }

    public void setSolutionVisible(boolean solutionVisible) {
        this.solutionVisible = solutionVisible;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public long getReportStateID() {
        return reportStateID;
    }

    public void setReportStateID(long reportStateID) {
        this.reportStateID = reportStateID;
    }

    public int getReportType() {
        return reportType;
    }

    public void setReportType(int reportType) {
        this.reportType = reportType;
    }

    /**
     * Clears out collections to the minimum necessary for data display. Do not use this
     * if you're returning the report definition for the report editor.
     */
    public void optimizeSize() {
        filterDefinitions = null;
        addedItems = null;
    }

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

    public boolean isCanSaveDirectly() {
        return canSaveDirectly;
    }

    public void setCanSaveDirectly(boolean canSaveDirectly) {
        this.canSaveDirectly = canSaveDirectly;
    }

    public List<AnalysisItem> getAddedItems() {
        return addedItems;
    }

    public void setAddedItems(List<AnalysisItem> addedItems) {
        this.addedItems = addedItems;
    }

    public int getPolicy() {
        return policy;
    }

    public void setPolicy(int policy) {
        this.policy = policy;
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

    public int getFixedWidth() {
        return fixedWidth;
    }

    public void setFixedWidth(int fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void updateMetadata() {
        
    }

    public abstract Set<AnalysisItem> getAllAnalysisItems();

    public List<FilterDefinition> retrieveFilterDefinitions() {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        if (filterDefinitions != null) {
            for (FilterDefinition filter : filterDefinitions) {
                if (filter.isEnabled()) {
                    if (filter instanceof FilterValueDefinition) {
                        FilterValueDefinition filterValueDefinition = (FilterValueDefinition) filter;
                        if (filterValueDefinition.isAllOption() && filterValueDefinition.getFilteredValues().size() == 1 &&
                                "All".equals(filterValueDefinition.getFilteredValues().get(0).toString())) {
                            continue;
                        }
                    } else if (filter instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                        if (rollingFilterDefinition.getInterval() == MaterializedRollingFilterDefinition.ALL) {
                            continue;
                        }
                    }
                    if (filter.isTrendFilter()) {
                        if (!(this instanceof WSKPIDefinition)) {
                            continue;
                        }
                    }
                    filters.add(filter);
                }
            }
        }
        return filters;
    }

    public Set<AnalysisItem> getColumnItems(List<AnalysisItem> allItems) {
        Set<AnalysisItem> columnSet = new HashSet<AnalysisItem>();
        Set<AnalysisItem> analysisItems = getAllAnalysisItems();
        analysisItems.remove(null);
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.isValid()) {
                columnSet.add(analysisItem);
            }
        }
        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.isValid()) {
                List<AnalysisItem> items = analysisItem.getAnalysisItems(allItems, analysisItems, false, true, CleanupComponent.AGGREGATE_CALCULATIONS);
                for (AnalysisItem item : items) {
                    //if (item.getAnalysisItemID()) {
                    if (!columnSet.contains(item)) {
                        columnSet.add(item);
                    }
                    //}
                }
                List<AnalysisItem> linkItems = analysisItem.addLinkItems(allItems);
                for (AnalysisItem item : linkItems) {
                    if (!columnSet.contains(item)) {
                        columnSet.add(item);
                    }
                }
            }
        }
        if (retrieveFilterDefinitions() != null) {
            for (FilterDefinition filterDefinition : retrieveFilterDefinitions()) {
                columnSet.addAll(filterDefinition.getAnalysisItems(allItems, analysisItems, false, true, CleanupComponent.AGGREGATE_CALCULATIONS));
            }
        }
        for (AnalysisItem analysisItem : getLimitFields()) {
            if (!columnSet.contains(analysisItem)) {
                columnSet.add(analysisItem);
            }
        }
        Map<String, List<AnalysisItem>> keyMap = new HashMap<String, List<AnalysisItem>>();
            Map<String, List<AnalysisItem>> displayMap = new HashMap<String, List<AnalysisItem>>();
            for (AnalysisItem analysisItem : allItems) {
                List<AnalysisItem> items = keyMap.get(analysisItem.getKey().toKeyString());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    keyMap.put(analysisItem.getKey().toKeyString(), items);
                }
                items.add(analysisItem);
            }
            for (AnalysisItem analysisItem : allItems) {
                List<AnalysisItem> items = displayMap.get(analysisItem.toDisplay());
                if (items == null) {
                    items = new ArrayList<AnalysisItem>(1);
                    displayMap.put(analysisItem.toDisplay(), items);
                }
                items.add(analysisItem);
            }
        if (getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, allItems, keyMap, displayMap, analysisItems, false, true, CleanupComponent.AGGREGATE_CALCULATIONS);
                columnSet.addAll(items);
            }
        }
        return columnSet;
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

    public abstract void createReportStructure(Map<String, AnalysisItem> structure);

    public abstract void populateFromReportStructure(Map<String, AnalysisItem> structure);



    @Nullable
    protected AnalysisItem firstItem(String key, Map<String, AnalysisItem> structure) {
        String compositeKey = key + "-" + 0;
        AnalysisItem analysisItem = structure.get(compositeKey);
        if (analysisItem != null) {
            analysisItem = (AnalysisItem) Database.deproxy(analysisItem);
        }
        return analysisItem;
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
                value = (AnalysisItem) Database.deproxy(value);
                items.add(value);
            }
            i++;
        }
        Collections.sort(items, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                return new Integer(analysisItem.getItemPosition()).compareTo(analysisItem1.getItemPosition());
            }
        });
        return items;
    }

    protected void addItems(String key, List<AnalysisItem> items, Map<String, AnalysisItem> structure) {
        if (items != null) {
            for (int i = 0; i < items.size(); i++) {
                String compositeKey = key + "-" + i;
                AnalysisItem analysisItem = items.get(i);
                if (analysisItem != null) {
                    structure.put(compositeKey, analysisItem);
                    analysisItem.setItemPosition(i);
                }
            }
        }
    }

    public void applyFilters(List<FilterDefinition> drillThroughFilters) {
        for (FilterDefinition filterDefinition : drillThroughFilters) {
            boolean foundFilter = false;
            for (FilterDefinition ourDefinition : retrieveFilterDefinitions()) {
                if (ourDefinition.getField().getKey().equals(filterDefinition.getField().getKey())) {
                    // match the filter?
                    if (ourDefinition instanceof FilterValueDefinition && filterDefinition instanceof FilterValueDefinition) {
                        FilterValueDefinition ourFilterValueDefinition = (FilterValueDefinition) ourDefinition;
                        FilterValueDefinition sourceFilterValueDefinition = (FilterValueDefinition) filterDefinition;
                        ourFilterValueDefinition.setFilteredValues(sourceFilterValueDefinition.getFilteredValues());
                        foundFilter = true;
                    }
                }
            }
            if (!foundFilter) {
                filterDefinitions.add(filterDefinition);
            }
        }
    }

    public boolean hasCustomResultsBridge() {
        return false;
    }

    public ResultsBridge getCustomResultsBridge() {
        return null;
    }

    public void tweakReport(DataSet dataSet) {
    }

    public List<IComponent> createComponents() {
        return new ArrayList<IComponent>();
    }

    public void populateProperties(List<ReportProperty> properties) {
        fontName = findStringProperty(properties, "fontName", "Tahoma");
        fontSize = (int) findNumberProperty(properties, "fontSize", 12);
        fixedWidth = (int) findNumberProperty(properties, "fixedWidth", 0);
        backgroundAlpha =  findNumberProperty(properties, "backgroundAlpha", 1);
        optimized =  findBooleanProperty(properties, "optimized", false);
        fullJoins =  findBooleanProperty(properties, "fullJoins", false);
        dataSourceFields =  findBooleanProperty(properties, "dataSourceFields", false);
        headerImage =  findImage(properties, "headerImage", null);
        lookupTableOptimization =  findBooleanProperty(properties, "lookupTableOptimization", false);
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = new ArrayList<ReportProperty>();
        properties.add(new ReportStringProperty("fontName", fontName));
        properties.add(new ReportNumericProperty("fontSize", fontSize));
        properties.add(new ReportNumericProperty("fixedWidth", fixedWidth));
        properties.add(new ReportNumericProperty("backgroundAlpha", backgroundAlpha));
        properties.add(new ReportBooleanProperty("optimized", optimized));
        properties.add(new ReportBooleanProperty("fullJoins", fullJoins));
        properties.add(new ReportBooleanProperty("dataSourceFields", dataSourceFields));
        properties.add(new ReportBooleanProperty("lookupTableOptimization", lookupTableOptimization));
        if (headerImage != null) {
            properties.add(new ReportImageProperty("headerImage", headerImage));
        }
        return properties;
    }

    public boolean isFullJoins() {
        return fullJoins;
    }

    public void setFullJoins(boolean fullJoins) {
        this.fullJoins = fullJoins;
    }

    protected String findStringProperty(List<ReportProperty> properties, String property, String defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportStringProperty reportStringProperty = (ReportStringProperty) reportProperty;
                return reportStringProperty.getValue() != null ? reportStringProperty.getValue() : defaultValue;
            }
        }
        return defaultValue;
    }

    protected boolean findBooleanProperty(List<ReportProperty> properties, String property, boolean defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportBooleanProperty reportBooleanProperty = (ReportBooleanProperty) reportProperty;
                return reportBooleanProperty.getValue();
            }
        }
        return defaultValue;
    }

    protected double findNumberProperty(List<ReportProperty> properties, String property, double defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportNumericProperty reportNumericProperty = (ReportNumericProperty) reportProperty;
                return reportNumericProperty.getValue();
            }
        }
        return defaultValue;
    }

    protected ImageDescriptor findImage(List<ReportProperty> properties, String property, @Nullable ImageDescriptor defaultValue) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportImageProperty reportImageProperty = (ReportImageProperty) reportProperty;
                return reportImageProperty.createImageDescriptor();
            }
        }
        return defaultValue;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public List<IntentionSuggestion> suggestIntentions(WSAnalysisDefinition report) {
        return new ArrayList<IntentionSuggestion>();
    }

    public List<Intention> createIntentions(List<AnalysisItem> fields, int type) throws SQLException {
        return new ArrayList<Intention>();
    }
}
