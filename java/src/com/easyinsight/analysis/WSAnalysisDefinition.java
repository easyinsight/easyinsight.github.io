package com.easyinsight.analysis;

import com.easyinsight.analysis.definitions.WSKPIDefinition;
import com.easyinsight.core.*;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.dataset.LimitsResults;
import com.easyinsight.intention.Intention;
import com.easyinsight.intention.IntentionSuggestion;
import com.easyinsight.intention.NewHierarchyIntention;
import com.easyinsight.intention.ReportPropertiesIntention;
import com.easyinsight.logging.LogClass;
import com.easyinsight.pipeline.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.Serializable;

import com.easyinsight.preferences.ApplicationSkin;
import com.easyinsight.preferences.ApplicationSkinSettings;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.preferences.PreferencesService;
import com.easyinsight.security.SecurityUtil;
import org.hibernate.Session;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public static final int TEXT = 41;
    public static final int TOPO = 42;
    public static final int MULTI_SUMMARY = 43;

    private String name;
    private boolean persistedCache;
    private String authorName;
    private String cachePartitionFilter;
    private boolean logReport;
    private String urlKey;
    private long analysisID;
    private long dataFeedID;
    private int reportType;
    private long reportStateID;
    private List<FilterDefinition> filterDefinitions;
    private int policy;
    private boolean cacheFilters;
    private List<AnalysisItem> addedItems = new ArrayList<AnalysisItem>();
    private boolean canSaveDirectly;
    private boolean publiclyVisible;
    private boolean marketplaceVisible;
    private boolean solutionVisible;
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
    private boolean htmlInFlash;
    private List<AnalysisItem> fieldsForDrillthrough;
    private int maxHeaderWidth = 600;
    private int cacheMinutes;
    private boolean manualButRunFirst;
    private String customFontFamily;
    private boolean useCustomFontFamily;
    private String baseDate;
    private String exportString;
    private boolean noAggregation;
    private int generalSizeLimit;
    private boolean passThroughFilters;
    private boolean enableLocalStorage;
    private boolean canSave;
    private boolean dataSourceFieldReport;
    private List<FilterSetDescriptor> filterSets;
    private boolean dataDiscoveryEnabled;

    private String defaultDate;

    private boolean publicWithKey;

    private String colorScheme;

    private ImageDescriptor headerImage;
    private String fontName = "Tahoma";
    private int fontSize = 12;
    private double backgroundAlpha = 1;
    private boolean dayAggregation;

    private List<AddonReport> addonReports;

    private boolean aggregateQueryIfPossible = true;

    private boolean rowsEditable;

    private int fetchSize;
    private boolean noDataOnNoJoin;

    public boolean isDayAggregation() {
        return dayAggregation;
    }

    public void setDayAggregation(boolean dayAggregation) {
        this.dayAggregation = dayAggregation;
    }

    public String getColorScheme() {
        return colorScheme;
    }

    public void setColorScheme(String colorScheme) {
        this.colorScheme = colorScheme;
    }

    public boolean isDataSourceFieldReport() {
        return dataSourceFieldReport;
    }

    public void setDataSourceFieldReport(boolean dataSourceFieldReport) {
        this.dataSourceFieldReport = dataSourceFieldReport;
    }

    public boolean isDataDiscoveryEnabled() {
        return dataDiscoveryEnabled;
    }

    public void setDataDiscoveryEnabled(boolean dataDiscoveryEnabled) {
        this.dataDiscoveryEnabled = dataDiscoveryEnabled;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    private List<FilterDefinition> filtersForDrillthrough;

    public boolean isPublicWithKey() {
        return publicWithKey;
    }

    public void setPublicWithKey(boolean publicWithKey) {
        this.publicWithKey = publicWithKey;
    }

    public boolean isCacheFilters() {
        return cacheFilters;
    }

    public boolean isCanSave() {
        return canSave;
    }

    public void setCanSave(boolean canSave) {
        this.canSave = canSave;
    }

    public List<FilterSetDescriptor> getFilterSets() {
        return filterSets;
    }

    public void setFilterSets(List<FilterSetDescriptor> filterSets) {
        this.filterSets = filterSets;
    }

    public void setCacheFilters(boolean cacheFilters) {
        this.cacheFilters = cacheFilters;
    }

    public String getCachePartitionFilter() {
        return cachePartitionFilter;
    }

    public void setCachePartitionFilter(String cachePartitionFilter) {
        this.cachePartitionFilter = cachePartitionFilter;
    }

    public String getExportString() {
        return exportString;
    }

    public void setExportString(String exportString) {
        this.exportString = exportString;
    }

    public boolean isHtmlInFlash() {
        return htmlInFlash;
    }

    public void setHtmlInFlash(boolean htmlInFlash) {
        this.htmlInFlash = htmlInFlash;
    }

    public List<AnalysisItem> getFieldsForDrillthrough() {
        return fieldsForDrillthrough;
    }

    public void setFieldsForDrillthrough(List<AnalysisItem> fieldsForDrillthrough) {
        this.fieldsForDrillthrough = fieldsForDrillthrough;
    }

    public List<FilterDefinition> getFiltersForDrillthrough() {
        return filtersForDrillthrough;
    }

    public void setFiltersForDrillthrough(List<FilterDefinition> filtersForDrillthrough) {
        this.filtersForDrillthrough = filtersForDrillthrough;
    }

    public boolean isNoAggregation() {
        return noAggregation;
    }

    public void setNoAggregation(boolean noAggregation) {
        this.noAggregation = noAggregation;
    }

    public boolean isPassThroughFilters() {
        return passThroughFilters;
    }

    public void setPassThroughFilters(boolean passThroughFilters) {
        this.passThroughFilters = passThroughFilters;
    }

    public boolean isAggregateQueryIfPossible() {
        return aggregateQueryIfPossible;
    }

    public void setAggregateQueryIfPossible(boolean aggregateQueryIfPossible) {
        this.aggregateQueryIfPossible = aggregateQueryIfPossible;
    }

    public String getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        this.defaultDate = defaultDate;
    }

    protected String generateDescription() {
        return "";
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public boolean isNoDataOnNoJoin() {
        return noDataOnNoJoin;
    }

    public void setNoDataOnNoJoin(boolean noDataOnNoJoin) {
        this.noDataOnNoJoin = noDataOnNoJoin;
    }

    public int getGeneralSizeLimit() {
        return generalSizeLimit;
    }

    public void setGeneralSizeLimit(int generalSizeLimit) {
        this.generalSizeLimit = generalSizeLimit;
    }

    public List<AddonReport> getAddonReports() {
        return addonReports;
    }

    public void setAddonReports(List<AddonReport> addonReports) {
        this.addonReports = addonReports;
    }

    public boolean isManualButRunFirst() {
        return manualButRunFirst;
    }

    public void setManualButRunFirst(boolean manualButRunFirst) {
        this.manualButRunFirst = manualButRunFirst;
    }

    public String getCustomFontFamily() {
        return customFontFamily;
    }

    public void setCustomFontFamily(String customFontFamily) {
        this.customFontFamily = customFontFamily;
    }

    public boolean isUseCustomFontFamily() {
        return useCustomFontFamily;
    }

    public void setUseCustomFontFamily(boolean useCustomFontFamily) {
        this.useCustomFontFamily = useCustomFontFamily;
    }

    public int getCacheMinutes() {
        return cacheMinutes;
    }

    public void setCacheMinutes(int cacheMinutes) {
        this.cacheMinutes = cacheMinutes;
    }

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

    public boolean isPersistedCache() {
        return persistedCache;
    }

    public void setPersistedCache(boolean persistedCache) {
        this.persistedCache = persistedCache;
    }

    public boolean isEnableLocalStorage() {
        return enableLocalStorage;
    }

    public void setEnableLocalStorage(boolean enableLocalStorage) {
        this.enableLocalStorage = enableLocalStorage;
    }

    public List<ReportAuditEvent> validate() {
        List<ReportAuditEvent> events = new ArrayList<>();
        return events;
    }

    public List<AnalysisItem> allAddedItems(InsightRequestMetadata insightRequestMetadata) {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        if (addedItems != null) {
            items.addAll(addedItems);
        }
        if (insightRequestMetadata != null && insightRequestMetadata.getAdditionalAnalysisItems() != null) {
            items.addAll(insightRequestMetadata.getAdditionalAnalysisItems());
        }
        if (addonReports != null) {
            for (AddonReport addonReport : addonReports) {
                Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
                List<AnalysisItem> fields = new ArrayList<AnalysisItem>();
                WSAnalysisDefinition report = new AnalysisStorage().getAnalysisDefinition(addonReport.getReportID());
                Map<String, AnalysisItem> structure = report.createStructure();
                for (AnalysisItem item : structure.values()) {
                    AnalysisItem clone;
                    if (item.hasType(AnalysisItemTypes.DATE_DIMENSION)) {
                        AnalysisDateDimension baseDate = (AnalysisDateDimension) item;
                        AnalysisDateDimension date = new AnalysisDateDimension();
                        date.setDateLevel(baseDate.getDateLevel());
                        date.setOutputDateFormat(baseDate.getOutputDateFormat());
                        date.setDateOnlyField(baseDate.isDateOnlyField() || baseDate.hasType(AnalysisItemTypes.DERIVED_DATE));
                        clone = date;
                    } else if (item.hasType(AnalysisItemTypes.MEASURE)) {
                        AnalysisMeasure baseMeasure = (AnalysisMeasure) item;
                        AnalysisMeasure measure = new AnalysisMeasure();
                        measure.setFormattingType(item.getFormattingType());
                        if (report.isPersistedCache()) {
                            measure.setAggregation(AggregationTypes.SUM);
                        } else {
                            measure.setAggregation(baseMeasure.getAggregation());
                        }
                        measure.setPrecision(baseMeasure.getPrecision());
                        measure.setMinPrecision(baseMeasure.getMinPrecision());
                        clone = measure;
                    } else {
                        clone = new AnalysisDimension();
                    }
                    clone.setOriginalDisplayName(item.toDisplay());
                    clone.setDisplayName(report.getName() + " - " + item.toDisplay());
                    clone.setUnqualifiedDisplayName(item.toUnqualifiedDisplay());
                    clone.setBasedOnReportField(item.getAnalysisItemID());
                    ReportKey reportKey = new ReportKey();
                    reportKey.setParentKey(item.getKey());
                    reportKey.setReportID(addonReport.getReportID());
                    clone.setKey(reportKey);
                    replacementMap.put(item.getAnalysisItemID(), clone);
                    fields.add(clone);
                }
                ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
                for (AnalysisItem clone : fields) {
                    clone.updateIDs(replacements);
                    items.add(clone);
                }
            }
        }
        return items;
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
                        if (filterValueDefinition.isAllOption() && filterValueDefinition.getFilteredValues() != null &&
                                filterValueDefinition.getFilteredValues().size() == 1 &&
                                filterValueDefinition.getFilteredValues().get(0) != null &&
                                "All".equals(filterValueDefinition.getFilteredValues().get(0).toString())) {
                            continue;
                        }
                    } else if (filter instanceof RollingFilterDefinition) {
                        RollingFilterDefinition rollingFilterDefinition = (RollingFilterDefinition) filter;
                        if (rollingFilterDefinition.getInterval() == MaterializedRollingFilterDefinition.ALL) {
                            continue;
                        }
                    } else if (filter instanceof FlatDateFilter) {
                        FlatDateFilter flatDateFilter = (FlatDateFilter) filter;
                        if (flatDateFilter.getValue() == 0) {
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
                analysisItem.populateNamedFilters(getFilterDefinitions());
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
                AnalysisItemRetrievalStructure filterStructure = new AnalysisItemRetrievalStructure(Pipeline.BEFORE);
                filterStructure.setNamespaceMap(structure.getNamespaceMap());
                populate(map, filter.getAnalysisItems(allItems, analysisItems, false, true, new HashSet<>(), filterStructure), insightRequestMetadata);
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
        Map<String, List<AnalysisItem>> unqualifiedDisplayMap = mapper.getUnqualifiedDisplayMap();

        if (getReportRunMarmotScript() != null) {
            StringTokenizer toker = new StringTokenizer(getReportRunMarmotScript(), "\r\n");
            while (toker.hasMoreTokens()) {
                String line = toker.nextToken();
                List<AnalysisItem> items = ReportCalculation.getAnalysisItems(line, allItems, keyMap, displayMap, unqualifiedDisplayMap, analysisItems, false, true, structure);
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

        for (AnalysisItem analysisItem : map.values()) {
            if (analysisItem.hasType(AnalysisItemTypes.DERIVED_DIMENSION)) {
                DerivedAnalysisDimension derivedAnalysisDimension = (DerivedAnalysisDimension) analysisItem;
                if (derivedAnalysisDimension.getDerivationCode().contains("loadfromjoin")) {
                    insightRequestMetadata.addPostProcessJoin(analysisItem);
                }
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
    private transient Map<UniqueKey, AnalysisItem> uniqueIteMap = new HashMap<UniqueKey, AnalysisItem>();

    @Transient
    private transient List<AnalysisItem> additionalGroupingItems = new ArrayList<AnalysisItem>();

    @Transient
    private transient Map<String, UniqueKey> fieldToUniqueMap = new HashMap<String, UniqueKey>();

    public List<AnalysisItem> getAdditionalGroupingItems() {
        return additionalGroupingItems;
    }

    public void setAdditionalGroupingItems(List<AnalysisItem> additionalGroupingItems) {
        this.additionalGroupingItems = additionalGroupingItems;
    }

    public Map<String, UniqueKey> getFieldToUniqueMap() {
        return fieldToUniqueMap;
    }

    public void setFieldToUniqueMap(Map<String, UniqueKey> fieldToUniqueMap) {
        this.fieldToUniqueMap = fieldToUniqueMap;
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
        cacheMinutes = (int) findNumberProperty(properties, "cacheMinutes", 0);
        fixedWidth = (int) findNumberProperty(properties, "fixedWidth", 0);
        backgroundAlpha = findNumberProperty(properties, "backgroundAlpha", 1);
        headerFontSize = (int) findNumberProperty(properties, "headerFontSize", 24);
        maxHeaderWidth = (int) findNumberProperty(properties, "maxHeaderWidth", 600);
        optimized = findBooleanProperty(properties, "optimized", false);
        fullJoins = findBooleanProperty(properties, "fullJoins", false);
        dataSourceFields = findBooleanProperty(properties, "dataSourceFields", false);
        persistedCache = findBooleanProperty(properties, "persistedCache", false);
        logReport = findBooleanProperty(properties, "logReport", false);
        noAggregation = findBooleanProperty(properties, "noAggregation", false);
        headerImage = findImage(properties, "headerImage", null);
        lookupTableOptimization = findBooleanProperty(properties, "lookupTableOptimization", false);
        dayAggregation = findBooleanProperty(properties, "dayAggregation", false);
        adHocExecution = findBooleanProperty(properties, "adHocExecution", false);
        cacheable = findBooleanProperty(properties, "cacheable", false);
        manualButRunFirst = findBooleanProperty(properties, "manualButRunFirst", false);
        customFontFamily = findStringProperty(properties, "customFontFamily", "");
        useCustomFontFamily = findBooleanProperty(properties, "useCustomFontFamily", false);
        generalSizeLimit = (int) findNumberProperty(properties, "generalSizeLimit", 0);
        fetchSize = (int) findNumberProperty(properties, "fetchSize", 0);
        noDataOnNoJoin = findBooleanProperty(properties, "noDataOnNoJoin", false);
        aggregateQueryIfPossible = findBooleanProperty(properties, "aggregateQueryIfPossible", true);
        htmlInFlash = findBooleanProperty(properties, "htmlInFlash", false);
        cacheFilters = findBooleanProperty(properties, "cacheFilters", false);
        cachePartitionFilter = findStringProperty(properties, "cachePartitionFilter", "");
        enableLocalStorage = findBooleanProperty(properties, "enableLocalStorage", false);
        dataDiscoveryEnabled = findBooleanProperty(properties, "dataDiscoveryEnabled", false);
        colorScheme = findStringProperty(properties, "reportColorScheme", "None");
        exportString = findStringProperty(properties, "exportString", "");
        baseDate = findStringProperty(properties, "baseDate", "");
    }

    public List<ReportProperty> createProperties() {
        List<ReportProperty> properties = new ArrayList<ReportProperty>();
        properties.add(new ReportStringProperty("fontName", fontName));
        properties.add(new ReportStringProperty("customFontFamily", customFontFamily));
        properties.add(new ReportNumericProperty("fontSize", fontSize));
        properties.add(new ReportNumericProperty("cacheMinutes", cacheMinutes));
        properties.add(new ReportNumericProperty("headerFontSize", headerFontSize));
        properties.add(new ReportNumericProperty("maxHeaderWidth", maxHeaderWidth));
        properties.add(new ReportNumericProperty("fixedWidth", fixedWidth));
        properties.add(new ReportNumericProperty("backgroundAlpha", backgroundAlpha));
        properties.add(new ReportBooleanProperty("optimized", optimized));
        properties.add(new ReportBooleanProperty("persistedCache", persistedCache));
        properties.add(new ReportBooleanProperty("fullJoins", fullJoins));
        properties.add(new ReportBooleanProperty("dataSourceFields", dataSourceFields));
        properties.add(new ReportBooleanProperty("dayAggregation", dayAggregation));
        properties.add(new ReportBooleanProperty("lookupTableOptimization", lookupTableOptimization));
        properties.add(new ReportBooleanProperty("noAggregation", noAggregation));
        properties.add(new ReportStringProperty("exportString", exportString));
        properties.add(new ReportStringProperty("baseDate", baseDate));
        properties.add(new ReportBooleanProperty("adHocExecution", adHocExecution));
        properties.add(new ReportBooleanProperty("cacheable", cacheable));
        properties.add(new ReportBooleanProperty("manualButRunFirst", manualButRunFirst));
        properties.add(new ReportBooleanProperty("logReport", logReport));
        properties.add(new ReportBooleanProperty("useCustomFontFamily", useCustomFontFamily));
        properties.add(new ReportBooleanProperty("dataDiscoveryEnabled", dataDiscoveryEnabled));
        properties.add(new ReportNumericProperty("generalSizeLimit", generalSizeLimit));
        properties.add(new ReportNumericProperty("fetchSize", fetchSize));
        properties.add(new ReportBooleanProperty("noDataOnNoJoin", noDataOnNoJoin));
        properties.add(new ReportBooleanProperty("aggregateQueryIfPossible", aggregateQueryIfPossible));
        properties.add(new ReportBooleanProperty("htmlInFlash", htmlInFlash));
        properties.add(new ReportStringProperty("cachePartitionFilter", cachePartitionFilter));
        properties.add(new ReportBooleanProperty("cacheFilters", cacheFilters));
        properties.add(new ReportStringProperty("reportColorScheme", colorScheme));
        if (headerImage != null) {
            properties.add(new ReportImageProperty("headerImage", headerImage));
        }
        properties.add(new ReportBooleanProperty("enableLocalStorage", enableLocalStorage));
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
        if (type == IntentionSuggestion.WARNING_JOIN_FAILURE) {
            return Arrays.asList((Intention) new NewHierarchyIntention(NewHierarchyIntention.CUSTOMIZE_JOINS));
        } else if (type == IntentionSuggestion.TURN_OFF_AGGREGATE_QUERY) {
            ReportPropertiesIntention reportPropertiesIntention = new ReportPropertiesIntention(IntentionSuggestion.TURN_OFF_AGGREGATE_QUERY);
            return Arrays.asList((Intention) reportPropertiesIntention);
        } else {
            return new ArrayList<Intention>();
        }
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

    public String rootHTML() {
        return "";
    }

    public String toExportHTML(EIConnection conn, InsightRequestMetadata insightRequestMetadata, boolean email) {
        throw new UnsupportedOperationException();
    }

    public List<INestedComponent> endComponents() {
        List<INestedComponent> components = new ArrayList<INestedComponent>();
        components.add(new SimpleNestedComponent());
        return components;
    }

    protected String createdOn() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if (getDateCreated() == null) {
            return sdf.format(new Date());
        }
        return sdf.format(getDateCreated());
    }

    protected String author() {
        if (getAuthorName() == null) {
            return SecurityUtil.getUserName();
        }
        return getAuthorName();
    }

    protected String summarize(List<AnalysisItem> columns) {
        StringBuilder sb = new StringBuilder();
        if (columns.size() > 0) {
            for (int i = 0; i < columns.size(); i++) {
                AnalysisItem item = columns.get(i);
                sb.append(item.toDisplay());
                if (i < (columns.size() - 2)) {
                    sb.append(", ");
                } else if (i == (columns.size() - 2)) {
                    sb.append(" and ");
                }
            }
        }

        return sb.toString();
    }

    protected String summarizeFilters(Collection<FilterDefinition> filters) {
        StringBuilder sb = new StringBuilder();
        if (filters != null && filters.size() > 0) {
            sb.append(" with ").append(filters.size()).append(" filters");
        }
        return sb.toString();
    }

    public void renderConfig(ApplicationSkin applicationSkin) {

    }

    public JSONObject toJSON(HTMLReportMetadata htmlReportMetadata, List<FilterDefinition> parentFilters) throws JSONException {

        applyStyling();
        JSONObject jo = new JSONObject();
        JSONArray filters = new JSONArray();
        for (FilterDefinition f : getFilterDefinitions()) {
            boolean found = false;
            if (!f.isShowOnReportView()) {
                continue;
            }
            for (FilterDefinition ff : parentFilters) {
                if (f.sameFilter(ff)) {
                    found = true;
                }
            }
            if (!found) {
                JSONObject j = f.toJSON(new FilterHTMLMetadata(this));
                if (j != null) {
                    filters.put(j);
                }
            }


        }
        jo.put("name", getName());
        jo.put("id", urlKey);
        jo.put("filters", filters);
        jo.put("adhoc_execution", adHocExecution);
        jo.put("local_storage", enableLocalStorage);
        jo.put("publiclyVisible", isPubliclyVisible());
        jo.put("publiclyVisibleWithKey", isPublicWithKey());
        return jo;
    }

    public void updateFromParameters(Map<String, String> parameters) {

        // find the parameter set

        /*for (Map.Entry<String, String> entry : parameters.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // find the field matching this key, replace it
        }*/
    }

    protected boolean supportsMultiField() {
        return false;
    }

    protected List<AnalysisItem> reportFieldsForMultiField() {
        return null;
    }

    protected void assignResults(List<AnalysisItem> fields) {

    }

    public void argh(List<AnalysisItem> fields) {

    }

    public void handleFieldExtensions() {

    }

    // data source fields
    // report fields
    //

    public void multiField(MultiFieldFilterDefinition multiFieldFilterDefinition) throws SQLException {
        if (!supportsMultiField()) {
            return;
        }
        List<AnalysisItem> columns = reportFieldsForMultiField();
        long id = getDataFeedID();
        FeedDefinition dataSource = new FeedStorage().getFeedDefinitionData(id);
        List<AnalysisItem> allFields;
        EIConnection conn = Database.instance().getConnection();
        try {
            allFields = dataSource.allFields(conn);
        } finally {
            Database.closeConnection(conn);
        }
        Map<Long, AnalysisItem> map = new HashMap<Long, AnalysisItem>();
        Map<String, AnalysisItem> mapByName = new HashMap<String, AnalysisItem>();
        for (AnalysisItem field : allFields) {
            map.put(field.getAnalysisItemID(), field);
            mapByName.put(field.toDisplay(), field);
        }
        if (getAddedItems() != null) {
            for (AnalysisItem item : getAddedItems()) {
                if (item.getAnalysisItemID() != 0) {
                    map.put(item.getAnalysisItemID(), item);

                }
                mapByName.put(item.toDisplay(), item);
            }
        }
        if (!multiFieldFilterDefinition.excludeReportFields()) {
            for (AnalysisItem column : columns) {
                map.put(column.getAnalysisItemID(), column);
            }
        }
        List<AnalysisItem> fields;
        Set<AnalysisItem> set = new HashSet<AnalysisItem>();
        final Map<AnalysisItem, Integer> positions = new HashMap<AnalysisItem, Integer>();
        if (multiFieldFilterDefinition.isAll()) {
            System.out.println("Using all logic...");
            int i = 0;
            if (!multiFieldFilterDefinition.excludeReportFields()) {
                for (AnalysisItem column : columns) {
                    set.add(column);
                    positions.put(column, i++);
                }
            }
            for (AnalysisItemHandle field : multiFieldFilterDefinition.getAvailableHandles()) {
                AnalysisItem item = map.get(field.getAnalysisItemID());
                if (field.getAnalysisItemID() != null && field.getAnalysisItemID() > 0 && item != null) {
                    set.add(item);
                    positions.put(item, i++);
                } else {
                    item = mapByName.get(field.getName());
                    if (item != null) {
                        set.add(item);
                        positions.put(item, i++);
                    }
                }
            }

            List<WeNeedToReplaceHibernateTag> tags = multiFieldFilterDefinition.getAvailableTags();

            conn = Database.instance().getConnection();

            try {
                PreparedStatement customFieldQueryStmt = conn.prepareStatement("SELECT custom_flag_to_tag.custom_flag FROM custom_flag_to_tag WHERE tag_id = ? AND data_source_id = ?");

                for (WeNeedToReplaceHibernateTag tag : tags) {
                    customFieldQueryStmt.setLong(1, tag.getTagID());
                    customFieldQueryStmt.setLong(2, getDataFeedID());
                    ResultSet rs = customFieldQueryStmt.executeQuery();
                    while (rs.next()) {
                        int customFlag = rs.getInt(1);
                        for (AnalysisItem analysisItem : allFields) {
                            if (analysisItem.getCustomFlag() == customFlag) {
                                positions.put(analysisItem, i++);
                                set.add(analysisItem);
                            }
                        }
                    }
                }
                customFieldQueryStmt.close();

                PreparedStatement queryStmt = conn.prepareStatement("SELECT field_to_tag.display_name FROM field_to_tag WHERE account_tag_id = ? AND field_to_tag.data_source_id = ?");
                for (WeNeedToReplaceHibernateTag tag : tags) {
                    queryStmt.setLong(1, tag.getTagID());
                    queryStmt.setLong(2, getDataFeedID());
                    ResultSet rs = queryStmt.executeQuery();
                    while (rs.next()) {
                        String fieldName = rs.getString(1);
                        AnalysisItem analysisItem = mapByName.get(fieldName);
                        if (analysisItem != null && accepts(analysisItem)) {
                            positions.put(analysisItem, i++);
                            if (set.contains(analysisItem)) {
                                set.remove(analysisItem);
                            }
                            set.add(analysisItem);
                        }
                    }
                }
                queryStmt.close();
            } finally {
                Database.closeConnection(conn);
            }

            fields = new ArrayList<AnalysisItem>(set);
        } else {
            int i = 0;
            for (AnalysisItemHandle field : multiFieldFilterDefinition.getSelectedItems()) {
                if (field.isSelected()) {
                    AnalysisItem item = map.get(field.getAnalysisItemID());
                    if (item != null) {
                        set.add(item);
                        positions.put(item, i++);
                    } else {
                        item = mapByName.get(field.getName());
                        if (item != null) {
                            set.add(item);
                            positions.put(item, i++);
                        }
                    }
                }
            }
            fields = new ArrayList<AnalysisItem>(set);
        }

        List<AnalysisItem> clones = new ArrayList<AnalysisItem>();

        for (AnalysisItem analysisItem : fields) {
            //long fieldID = analysisItem.getAnalysisItemID();
            try {
                analysisItem = analysisItem.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            clones.add(analysisItem);
        }

        final Map<String, Integer> fieldOrderingMap = new HashMap<String, Integer>();
        if (multiFieldFilterDefinition.getFieldOrdering() != null && multiFieldFilterDefinition.getFieldOrdering().size() > 0) {
            System.out.println("Using explicit field ordering...");
            int j = 0;
            for (AnalysisItemHandle handle : multiFieldFilterDefinition.getFieldOrdering()) {
                System.out.println("\tHandle " + handle.getName() + " = " + j);
                fieldOrderingMap.put(handle.getName(), j++);
            }
        }

        final boolean alphaSort = multiFieldFilterDefinition.isAlphaSort();

        Collections.sort(clones, new Comparator<AnalysisItem>() {

            public int compare(AnalysisItem analysisItem, AnalysisItem analysisItem1) {
                if (alphaSort) {
                    return analysisItem.toUnqualifiedDisplay().compareTo(analysisItem1.toUnqualifiedDisplay());
                } else if (fieldOrderingMap.isEmpty()) {
                    Integer p1 = positions.get(analysisItem);
                    Integer p2 = positions.get(analysisItem1);
                    return p1.compareTo(p2);
                } else {
                    Integer p1 = fieldOrderingMap.get(analysisItem.toDisplay());
                    Integer p2 = fieldOrderingMap.get(analysisItem1.toDisplay());
                    if (p1 == null && p2 != null) {
                        return 1;
                    }
                    if (p2 == null && p1 != null) {
                        return -1;
                    }
                    if (p1 == null && p2 == null) {
                        p1 = positions.get(analysisItem);
                        p2 = positions.get(analysisItem1);
                        return p1.compareTo(p2);
                    }
                    return p1.compareTo(p2);
                }

            }
        });

        if (set.size() > 0) {
            int i = 0;
            System.out.println("Assigned results = " + clones);
            for (AnalysisItem item : clones) {
                item.setItemPosition(i++);
            }
            assignResults(clones);
        }
    }

    protected boolean accepts(AnalysisItem analysisItem) {
        return false;
    }

    public int extensionType() {
        return 0;
    }

    public void applyStyling(EIConnection conn, int dataSourceType)  {
        Session session = Database.instance().createSession(conn);
        ApplicationSkin applicationSkin;
        try {
            if (SecurityUtil.getAccountID(false) == 0) {
                PreparedStatement uStmt = conn.prepareStatement("SELECT USER.USER_ID, ACCOUNT_ID FROM USER, user_to_analysis WHERE user_to_analysis.analysis_id = ? AND " +
                        "user_to_analysis.user_id = user.user_id");
                uStmt.setLong(1, getAnalysisID());
                ResultSet uRS = uStmt.executeQuery();
                if (uRS.next()) {
                    long userID = uRS.getLong(1);
                    long accountID = uRS.getLong(2);
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(userID, session, accountID);
                } else {
                    applicationSkin = null;
                }
                uStmt.close();
            } else {
                PreparedStatement ps = conn.prepareStatement("SELECT EXCHANGE_AUTHOR FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                ps.setLong(1, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                rs.next();
                boolean exchangeAuthor = rs.getBoolean(1);
                ps.close();

                if (exchangeAuthor) {
                    applicationSkin = new PreferencesService().getConnectionSkin(dataSourceType);
                    if (applicationSkin == null) {
                        applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                    }
                } else {
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                }
            }
            if (applicationSkin != null) {
                renderConfig(applicationSkin);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            session.close();
        }
    }

    public void applyStyling()  {
        EIConnection conn = Database.instance().getConnection();
        Session session = Database.instance().createSession(conn);
        try {
            ApplicationSkin applicationSkin;
            if (SecurityUtil.getAccountID(false) == 0) {
                PreparedStatement uStmt = conn.prepareStatement("SELECT USER.USER_ID, ACCOUNT_ID FROM USER, user_to_analysis WHERE user_to_analysis.analysis_id = ? AND " +
                        "user_to_analysis.user_id = user.user_id");
                uStmt.setLong(1, getAnalysisID());
                ResultSet uRS = uStmt.executeQuery();
                uRS.next();
                long userID = uRS.getLong(1);
                long accountID = uRS.getLong(2);
                applicationSkin = ApplicationSkinSettings.retrieveSkin(userID, session, accountID);
                uRS.close();
            } else {
                PreparedStatement ps = conn.prepareStatement("SELECT EXCHANGE_AUTHOR FROM ACCOUNT WHERE ACCOUNT_ID = ?");
                ps.setLong(1, SecurityUtil.getAccountID());
                ResultSet rs = ps.executeQuery();
                rs.next();
                boolean exchangeAuthor = rs.getBoolean(1);
                ps.close();

                if (exchangeAuthor) {
                    PreparedStatement typeStmt = conn.prepareStatement("SELECT FEED_TYPE FROM DATA_FEED WHERE DATA_FEED_ID = ?");
                    typeStmt.setLong(1, getDataFeedID());
                    ResultSet typeRS = typeStmt.executeQuery();
                    typeRS.next();
                    int dataSourceType = typeRS.getInt(1);
                    typeStmt.close();
                    applicationSkin = new PreferencesService().getConnectionSkin(dataSourceType);
                    if (applicationSkin == null) {
                        applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                    }
                } else {
                    applicationSkin = ApplicationSkinSettings.retrieveSkin(SecurityUtil.getUserID(), session, SecurityUtil.getAccountID());
                }
            }
            if (applicationSkin != null) {
                renderConfig(applicationSkin);
            }
        } catch (Exception e) {
            LogClass.error(e);
        } finally {
            session.close();
            Database.closeConnection(conn);
        }
    }


}
