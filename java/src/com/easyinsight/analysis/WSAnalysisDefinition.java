package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSKPIDefinition;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.pipeline.*;

import java.sql.SQLException;
import java.util.*;
import java.io.Serializable;

import com.easyinsight.preferences.ImageDescriptor;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Transient;

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
    public static final int SUMMARY = 40;

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
    private boolean recommendedExchange;
    private boolean autoSetupDelivery;
    private boolean cacheable;
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
    private boolean adHocExecution;
    private int headerFontSize = 24;
    private int maxHeaderWidth = 600;

    private ImageDescriptor headerImage;
    private String fontName = "Tahoma";
    private int fontSize = 12;
    private double backgroundAlpha = 1;

    private boolean rowsEditable;

    public int getMaxHeaderWidth() {
        return maxHeaderWidth;
    }

    public void setMaxHeaderWidth(int maxHeaderWidth) {
        this.maxHeaderWidth = maxHeaderWidth;
    }

    public int getHeaderFontSize() {
        return headerFontSize;
    }

    public void setHeaderFontSize(int headerFontSize) {
        this.headerFontSize = headerFontSize;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public boolean isAdHocExecution() {
        return adHocExecution;
    }

    public void setAdHocExecution(boolean adHocExecution) {
        this.adHocExecution = adHocExecution;
    }

    public boolean isRowsEditable() {
        return rowsEditable;
    }

    public void setRowsEditable(boolean rowsEditable) {
        this.rowsEditable = rowsEditable;
    }

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

    public boolean isRecommendedExchange() {
        return recommendedExchange;
    }

    public void setRecommendedExchange(boolean recommendedExchange) {
        this.recommendedExchange = recommendedExchange;
    }

    public boolean isAutoSetupDelivery() {
        return autoSetupDelivery;
    }

    public void setAutoSetupDelivery(boolean autoSetupDelivery) {
        this.autoSetupDelivery = autoSetupDelivery;
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
                if (filter.isEnabled() && !filter.isTemplateFilter()) {
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

    private void populate(Map<AnalysisItem, AnalysisItem> map, AnalysisItem analysisItem, InsightRequestMetadata insightRequestMetadata) {
        AnalysisItem existing = map.get(analysisItem);
        if (existing == null) {
            map.put(analysisItem, analysisItem);
        } else {
            insightRequestMetadata.getPipelines(existing).addAll(insightRequestMetadata.getPipelines(analysisItem));
        }
    }

    private void populate(Map<AnalysisItem, AnalysisItem> map, List<AnalysisItem> analysisItems, InsightRequestMetadata insightRequestMetadata) {
        for (AnalysisItem analysisItem : analysisItems) {
            populate(map, analysisItem, insightRequestMetadata);
        }
    }

    public Set<AnalysisItem> getColumnItems(List<AnalysisItem> allItems, AnalysisItemRetrievalStructure structure, InsightRequestMetadata insightRequestMetadata) {
        Map<AnalysisItem, AnalysisItem> map = new HashMap<AnalysisItem, AnalysisItem>();
        //Set<AnalysisItem> columnSet = new HashSet<AnalysisItem>();
        Set<AnalysisItem> analysisItems = getAllAnalysisItems();
        analysisItems.remove(null);
        for (AnalysisItem analysisItem : analysisItems) {
            insightRequestMetadata.assign(analysisItem, Pipeline.LAST);
            if (analysisItem.isValid()) {
                //columnSet.add(analysisItem);
                populate(map, analysisItem, insightRequestMetadata);
            }
        }
        boolean joinPipeline = insightRequestMetadata.getIntermediatePipelines() == null || insightRequestMetadata.getIntermediatePipelines().isEmpty();
        if (joinPipeline) {
            for (AnalysisItem analysisItem : analysisItems) {
                insightRequestMetadata.pipelineAssign(analysisItem);
            }
        }
        /*for (AnalysisItem analysisItem : analysisItems) {
            insightRequestMetadata.pipelineAssign(analysisItem);
        }*/


        for (AnalysisItem analysisItem : analysisItems) {
            if (analysisItem.isValid()) {
                List<AnalysisItem> items = analysisItem.getAnalysisItems(allItems, analysisItems, false, true, new HashSet<AnalysisItem>(), structure);
                for (AnalysisItem item : items) {
                    //if (item.getAnalysisItemID()) {
                    if (!map.keySet().contains(item)) {
                        populate(map, item, insightRequestMetadata);
                    }
                    //}
                }
                List<AnalysisItem> linkItems = analysisItem.addLinkItems(allItems);
                for (AnalysisItem item : linkItems) {
                    if (!map.keySet().contains(item)) {
                        populate(map, item, insightRequestMetadata);
                    }
                }
            }
        }
        if (retrieveFilterDefinitions() != null) {
            for (FilterDefinition filter : retrieveFilterDefinitions()) {
                insightRequestMetadata.pipelineAssign(filter);
                populate(map, filter.getAnalysisItems(allItems, analysisItems, false, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(Pipeline.BEFORE)), insightRequestMetadata);
            }
        }
        for (AnalysisItem analysisItem : getLimitFields()) {
            if (!map.keySet().contains(analysisItem)) {
                populate(map, analysisItem, insightRequestMetadata);
            }
        }
        KeyDisplayMapper mapper = KeyDisplayMapper.create(allItems);
        Map<String, List<AnalysisItem>> keyMap = mapper.getKeyMap();
        Map<String, List<AnalysisItem>> displayMap = mapper.getDisplayMap();
        if (getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, allItems, keyMap, displayMap, analysisItems, false, true, structure);
                populate(map, items, insightRequestMetadata);
            }
        }
        if (uniqueIteMap != null) {
            Set<Long> ids = new HashSet<Long>();
            for (AnalysisItem analysisItem : map.keySet()) {
                Key key = analysisItem.getKey();
                long dsID = toID(key);
                if (dsID != 0) {
                    ids.add(dsID);
                }
            }
            List<AnalysisItem> uniqueFields = new ArrayList<AnalysisItem>();
            for (Long id : ids) {
                AnalysisDimension analysisDimension = (AnalysisDimension) uniqueIteMap.get(id);
                if (analysisDimension != null) {
                    analysisDimension.setGroup(false);
                    uniqueFields.add(analysisDimension);
                }
            }
            populate(map, uniqueFields, insightRequestMetadata);
        }
        if (!additionalGroupingItems.isEmpty()) {
            populate(map, additionalGroupingItems, insightRequestMetadata);
        }

        if (!joinPipeline) {
            for (AnalysisItem analysisItem : map.values()) {
                insightRequestMetadata.pipelineAssign(analysisItem);
            }
        }

        return new HashSet<AnalysisItem>(map.values());
    }

    private long toID(Key key) {
        if (key instanceof DerivedKey) {
            DerivedKey derivedKey = (DerivedKey) key;
            Key next = derivedKey.getParentKey();
            if (next instanceof NamedKey) {
                return derivedKey.getFeedID();
            }
            return toID(next);
        }
        return 0;
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

    @Transient
    private transient Map<Long, AnalysisItem> uniqueIteMap = new HashMap<Long, AnalysisItem>();

    @Transient
    private transient List<AnalysisItem> additionalGroupingItems = new ArrayList<AnalysisItem>();

    @Transient
    private transient Map<String, Long> fieldToUniqueMap = new HashMap<String, Long>();

    public List<AnalysisItem> getAdditionalGroupingItems() {
        return additionalGroupingItems;
    }

    public void setAdditionalGroupingItems(List<AnalysisItem> additionalGroupingItems) {
        this.additionalGroupingItems = additionalGroupingItems;
    }

    public Map<String, Long> getFieldToUniqueMap() {
        return fieldToUniqueMap;
    }

    public void setFieldToUniqueMap(Map<String, Long> fieldToUniqueMap) {
        this.fieldToUniqueMap = fieldToUniqueMap;
    }

    public Map<Long, AnalysisItem> getUniqueIteMap() {
        return uniqueIteMap;
    }

    public void setUniqueIteMap(Map<Long, AnalysisItem> uniqueIteMap) {
        this.uniqueIteMap = uniqueIteMap;
    }

    public boolean hasCustomResultsBridge() {
        return false;
    }

    public ResultsBridge getCustomResultsBridge() {
        return null;
    }

    public void tweakReport(Map<AnalysisItem, AnalysisItem> aliasMap) {
    }

    public void untweakReport(Map<AnalysisItem, AnalysisItem> aliasMap) {
    }

    public List<IComponent> createComponents() {
        return new ArrayList<IComponent>();
    }

    public void populateProperties(List<ReportProperty> properties) {
        fontName = findStringProperty(properties, "fontName", "Tahoma");
        fontSize = (int) findNumberProperty(properties, "fontSize", 12);
        fixedWidth = (int) findNumberProperty(properties, "fixedWidth", 0);
        backgroundAlpha =  findNumberProperty(properties, "backgroundAlpha", 1);
        headerFontSize =  (int) findNumberProperty(properties, "headerFontSize", 24);
        maxHeaderWidth =  (int) findNumberProperty(properties, "maxHeaderWidth", 600);
        optimized =  findBooleanProperty(properties, "optimized", false);
        fullJoins =  findBooleanProperty(properties, "fullJoins", false);
        dataSourceFields =  findBooleanProperty(properties, "dataSourceFields", false);
        headerImage =  findImage(properties, "headerImage", null);
        lookupTableOptimization =  findBooleanProperty(properties, "lookupTableOptimization", false);
        adHocExecution = findBooleanProperty(properties, "adHocExecution", false);
        cacheable = findBooleanProperty(properties, "cacheable", false);
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = new ArrayList<ReportProperty>();
        properties.add(new ReportStringProperty("fontName", fontName));
        properties.add(new ReportNumericProperty("fontSize", fontSize));
        properties.add(new ReportNumericProperty("headerFontSize", headerFontSize));
        properties.add(new ReportNumericProperty("maxHeaderWidth", maxHeaderWidth));
        properties.add(new ReportNumericProperty("fixedWidth", fixedWidth));
        properties.add(new ReportNumericProperty("backgroundAlpha", backgroundAlpha));
        properties.add(new ReportBooleanProperty("optimized", optimized));
        properties.add(new ReportBooleanProperty("fullJoins", fullJoins));
        properties.add(new ReportBooleanProperty("dataSourceFields", dataSourceFields));
        properties.add(new ReportBooleanProperty("lookupTableOptimization", lookupTableOptimization));
        properties.add(new ReportBooleanProperty("adHocExecution", adHocExecution));
        properties.add(new ReportBooleanProperty("cacheable", cacheable));
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

    protected List<MultiColor> multiColorProperty(List<ReportProperty> properties, String property) {
        for (ReportProperty reportProperty : properties) {
            if (reportProperty.getPropertyName().equals(property)) {
                ReportMultiColorProperty reportMultiColorProperty = (ReportMultiColorProperty) reportProperty;
                return reportMultiColorProperty.toMultiColorList();
            }
        }
        return new ArrayList<MultiColor>();
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

    public List<EIDescriptor> allItems(List<AnalysisItem> dataSourceItems, AnalysisItemRetrievalStructure structure) {
        List<EIDescriptor> allItems = new ArrayList<EIDescriptor>();
        Set<AnalysisItem> items = getColumnItems(dataSourceItems, structure, new InsightRequestMetadata());
        for (AnalysisItem analysisItem : items) {
            allItems.add(new AnalysisItemDescriptor(analysisItem));
            if (analysisItem.getFilters() != null) {
                for (FilterDefinition filterDefinition : analysisItem.getFilters()) {
                    allItems.add(new FilterDescriptor(filterDefinition));
                }
            }
            allItems.addAll(analysisItem.getKey().getDescriptors());
        }
        for (AnalysisItem addedItem : getAddedItems()) {
            allItems.add(new AnalysisItemDescriptor(addedItem));
            if (addedItem.getFilters() != null) {
                for (FilterDefinition filterDefinition : addedItem.getFilters()) {
                    allItems.add(new FilterDescriptor(filterDefinition));
                }
            }
            allItems.addAll(addedItem.getKey().getDescriptors());
        }
        for (FilterDefinition filterDefinition : getFilterDefinitions()) {
            allItems.add(new FilterDescriptor(filterDefinition));
        }
        return allItems;
    }

    public List<String> javaScriptIncludes() {
        List<String> list = new ArrayList<String>();
        list.add("/js/visualizations/util.js");
        return list;
    }

    public List<String> cssIncludes() {
        return new ArrayList<String>();
    }

    public String toHTML(String targetDiv, HTMLReportMetadata htmlReportMetadata) {
        String timezoneOffset = "timezoneOffset='+new Date().getTimezoneOffset()+'";
        return "$.get('/app/htmlExport?reportID="+getUrlKey()+"&embedded="+htmlReportMetadata.isEmbedded()+"&"+timezoneOffset+"&'+ strParams, function(data) { Utils.noData(data, function() { $('#"+targetDiv+" .reportArea').html(data); }, null, '" + targetDiv + "');});";
    }

    public String rootHTML() {
        return "";
    }

    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata) {
        throw new UnsupportedOperationException();
    }

    public List<INestedComponent> endComponents() {
        List<INestedComponent> components = new ArrayList<INestedComponent>();
        components.add(new SimpleNestedComponent());
        return components;
    }
}
