package com.easyinsight.dashboard;


import com.easyinsight.analysis.*;
import com.easyinsight.core.AnalysisItemDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.FilterDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.email.UserStub;
import com.easyinsight.preferences.ImageDescriptor;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.Serializable;
import java.util.*;

/**
 * User: jamesboe
 * Date: Nov 26, 2010
 * Time: 1:24:38 PM
 */
public class Dashboard implements Cloneable, Serializable {
    private DashboardElement rootElement;
    private long id;
    private String name;
    private String urlKey;
    private boolean accountVisible;
    private List<FeedConsumer> administrators = new ArrayList<FeedConsumer>();
    private long dataSourceID;
    private List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
    private String description;
    private boolean exchangeVisible;
    private Date creationDate;
    private Date updateDate;
    private String authorName;
    private boolean temporary;
    private boolean publicVisible;
    private boolean publicWithKey;
    private int padding;
    private int backgroundColor;
    private int borderThickness;
    private int borderColor;
    private int headerStyle;
    private String ytdMonth;
    private boolean overrideYTD;
    private boolean recommendedExchange;
    private DataSourceInfo dataSourceInfo;
    private int role;
    private String marmotScript;
    private int folder;
    private boolean absoluteSizing;
    private boolean reportAccessProblem;
    private int stackFill1Start;
    private int stackFill1SEnd;
    private int stackFill2Start;
    private int stackFill2End;
    private boolean fillStackHeaders;
    private int reportHorizontalPadding = 20;
    private Link defaultDrillthrough;
    private Map<String, FilterDefinition> overridenFilters = new HashMap<String, FilterDefinition>();

    private ImageDescriptor headerImage;
    private boolean imageFullHeader;
    private int headerTextColor;
    private int headerBackgroundColor;
    private int version;
    private boolean enableLocalStorage;
    private long tabletVersion;
    private long phoneVersion;
    private List<SavedConfiguration> configurations;
    private String colorSet;
    private int reportHeaderBackgroundColor = 0xDDDDDD;
    private int reportHeaderTextColor;

    public int getReportHeaderBackgroundColor() {
        return reportHeaderBackgroundColor;
    }

    public void setReportHeaderBackgroundColor(int reportHeaderBackgroundColor) {
        this.reportHeaderBackgroundColor = reportHeaderBackgroundColor;
    }

    public int getReportHeaderTextColor() {
        return reportHeaderTextColor;
    }

    public void setReportHeaderTextColor(int reportHeaderTextColor) {
        this.reportHeaderTextColor = reportHeaderTextColor;
    }

    public String getColorSet() {
        return colorSet;
    }

    public void setColorSet(String colorSet) {
        this.colorSet = colorSet;
    }

    public boolean isPublicWithKey() {
        return publicWithKey;
    }

    public void setPublicWithKey(boolean publicWithKey) {
        this.publicWithKey = publicWithKey;
    }

    public List<SavedConfiguration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<SavedConfiguration> configurations) {
        this.configurations = configurations;
    }

    public long getTabletVersion() {
        return tabletVersion;
    }

    public void setTabletVersion(long tabletVersion) {
        this.tabletVersion = tabletVersion;
    }

    public long getPhoneVersion() {
        return phoneVersion;
    }

    public void setPhoneVersion(long phoneVersion) {
        this.phoneVersion = phoneVersion;
    }

    public boolean isEnableLocalStorage() {
        return enableLocalStorage;
    }

    public void setEnableLocalStorage(boolean enableLocalStorage) {
        this.enableLocalStorage = enableLocalStorage;
    }

    public Map<String, FilterDefinition> getOverridenFilters() {
        return overridenFilters;
    }

    public void setOverridenFilters(Map<String, FilterDefinition> overridenFilters) {
        this.overridenFilters = overridenFilters;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Link getDefaultDrillthrough() {
        return defaultDrillthrough;
    }

    public void setDefaultDrillthrough(Link defaultDrillthrough) {
        this.defaultDrillthrough = defaultDrillthrough;
    }

    public ImageDescriptor getHeaderImage() {
        return headerImage;
    }

    public void setHeaderImage(ImageDescriptor headerImage) {
        this.headerImage = headerImage;
    }

    public int getHeaderTextColor() {
        return headerTextColor;
    }

    public void setHeaderTextColor(int headerTextColor) {
        this.headerTextColor = headerTextColor;
    }

    public int getHeaderBackgroundColor() {
        return headerBackgroundColor;
    }

    public void setHeaderBackgroundColor(int headerBackgroundColor) {
        this.headerBackgroundColor = headerBackgroundColor;
    }

    public int getReportHorizontalPadding() {
        return reportHorizontalPadding;
    }

    public void setReportHorizontalPadding(int reportHorizontalPadding) {
        this.reportHorizontalPadding = reportHorizontalPadding;
    }

    public boolean isFillStackHeaders() {
        return fillStackHeaders;
    }

    public void setFillStackHeaders(boolean fillStackHeaders) {
        this.fillStackHeaders = fillStackHeaders;
    }

    public int getStackFill1Start() {
        return stackFill1Start;
    }

    public void setStackFill1Start(int stackFill1Start) {
        this.stackFill1Start = stackFill1Start;
    }

    public int getStackFill1SEnd() {
        return stackFill1SEnd;
    }

    public void setStackFill1SEnd(int stackFill1SEnd) {
        this.stackFill1SEnd = stackFill1SEnd;
    }

    public int getStackFill2Start() {
        return stackFill2Start;
    }

    public void setStackFill2Start(int stackFill2Start) {
        this.stackFill2Start = stackFill2Start;
    }

    public int getStackFill2End() {
        return stackFill2End;
    }

    public void setStackFill2End(int stackFill2End) {
        this.stackFill2End = stackFill2End;
    }

    public boolean isReportAccessProblem() {
        return reportAccessProblem;
    }

    public void setReportAccessProblem(boolean reportAccessProblem) {
        this.reportAccessProblem = reportAccessProblem;
    }

    public boolean isAbsoluteSizing() {
        return absoluteSizing;
    }

    public void setAbsoluteSizing(boolean absoluteSizing) {
        this.absoluteSizing = absoluteSizing;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public void setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public String getMarmotScript() {
        return marmotScript;
    }

    public void setMarmotScript(String marmotScript) {
        this.marmotScript = marmotScript;
    }

    public boolean isOverrideYTD() {
        return overrideYTD;
    }

    public void setOverrideYTD(boolean overrideYTD) {
        this.overrideYTD = overrideYTD;
    }

    public String getYtdMonth() {
        return ytdMonth;
    }

    public void setYtdMonth(String ytdMonth) {
        this.ytdMonth = ytdMonth;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public Dashboard clone() throws CloneNotSupportedException {
        Dashboard dashboard = (Dashboard) super.clone();
        dashboard.setId(0);
        dashboard.setUrlKey(RandomTextGenerator.generateText(20));
        dashboard.setRootElement(rootElement.clone());
        return dashboard;
    }

    public DataSourceInfo getDataSourceInfo() {
        return dataSourceInfo;
    }

    public void setDataSourceInfo(DataSourceInfo dataSourceInfo) {
        this.dataSourceInfo = dataSourceInfo;
    }

    public boolean isRecommendedExchange() {
        return recommendedExchange;
    }

    public void setRecommendedExchange(boolean recommendedExchange) {
        this.recommendedExchange = recommendedExchange;
    }

    public boolean isTemporary() {
        return temporary;
    }

    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExchangeVisible() {
        return exchangeVisible;
    }

    public void setExchangeVisible(boolean exchangeVisible) {
        this.exchangeVisible = exchangeVisible;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<FilterDefinition> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterDefinition> filters) {
        this.filters = filters;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
    }

    public List<FeedConsumer> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<FeedConsumer> administrators) {
        this.administrators = administrators;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public DashboardElement getRootElement() {
        return rootElement;
    }

    public void setRootElement(DashboardElement rootElement) {
        this.rootElement = rootElement;
    }

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Long> containedReports() {
        Set<Long> reports = rootElement.containedReports();
        if (defaultDrillthrough != null) {
            reports.add(((DrillThrough) defaultDrillthrough).getReportID());
        }
        return reports;
    }

    private static void cleanup(AnalysisItem analysisItem, boolean changingDataSource) {
        if (changingDataSource) {
            // TODO: validate calculations and lookup tables--if necessary to create, should emit something with the report
            analysisItem.setLookupTableID(null);
            analysisItem.setBasedOnReportField(null);
        }
    }

    public DashboardSaveMetadata cloneDashboard(Map<Long, Scorecard> scorecardReplacmenetMap, boolean changingDataSource, List<AnalysisItem> allFields, FeedDefinition dataSource)
            throws CloneNotSupportedException {
        return cloneDashboard(scorecardReplacmenetMap, changingDataSource, allFields, dataSource, new ReplacementMapFactory());
    }

    public DashboardSaveMetadata cloneDashboard(Map<Long, Scorecard> scorecardReplacmenetMap, boolean changingDataSource, List<AnalysisItem> allFields, FeedDefinition dataSource,
                                    ReplacementMapFactory factory) throws CloneNotSupportedException {
        Dashboard dashboard = this.clone();
        dashboard.setTemporary(true);
        dashboard.setAuthorName(SecurityUtil.getUserName());
        dashboard.setAdministrators(Arrays.asList((FeedConsumer) new UserStub(SecurityUtil.getUserID(), null, null, null, 0, null)));
        dashboard.setCreationDate(new Date());
        dashboard.setExchangeVisible(false);
        dashboard.setRecommendedExchange(false);
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();

        if (getDefaultDrillthrough() != null) {
            Link clone = getDefaultDrillthrough().clone();
            dashboard.setDefaultDrillthrough(clone);
        }

        List<AnalysisItem> fields = new ArrayList<>();
        ReplacementMap targetMap;
        if (factory instanceof TemplateReplacementMapFactory) {
            ReplacementMap replacementMap = factory.createMap();
            targetMap = replacementMap;
            for (FilterDefinition persistableFilterDefinition : this.filters) {
                filterDefinitions.add(persistableFilterDefinition.clone());
                List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
                for (AnalysisItem item : filterItems) {
                    replacementMap.addField(item, changingDataSource);
                }
            }


            for (AnalysisItem analysisItem : replacementMap.getFields()) {
                analysisItem.updateIDs(replacementMap);
            }

            for (FilterDefinition filter : filterDefinitions) {
                filter.updateIDs(replacementMap);
            }

            dashboard.setFilters(filterDefinitions);

            fields.addAll(replacementMap.getFields());

            FilterOverrideVisitor visitor = new FilterOverrideVisitor(replacementMap, allFields, changingDataSource, dataSource, fields);
            dashboard.visit(visitor);
        } else {
            Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
            for (FilterDefinition persistableFilterDefinition : this.filters) {
                filterDefinitions.add(persistableFilterDefinition.clone());
                List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
                for (AnalysisItem item : filterItems) {
                    if (replacementMap.get(item.getAnalysisItemID()) == null) {
                        AnalysisItem clonedItem = item.clone();
                        cleanup(clonedItem, changingDataSource);
                        replacementMap.put(item.getAnalysisItemID(), clonedItem);
                    }
                }
            }



            for (AnalysisItem analysisItem : replacementMap.values()) {
                System.out.println("Looking for a match for " + analysisItem.toDisplay());
                AnalysisItem parent = dataSource.findAnalysisItemByDisplayName(analysisItem.toDisplay());
                if (parent != null) {
                    System.out.println("\tFound match by name of " + parent.toDisplay());
                    analysisItem.setKey(parent.getKey());
                } else {
                    System.out.println("\tNo match, looking by key");
                    Key key = dataSource.getField(analysisItem.getKey().toDisplayName());
                    if (key != null) {
                        analysisItem.setKey(key);
                    } else {
                        Key clonedKey = analysisItem.getKey().clone();
                        analysisItem.setKey(clonedKey);
                    }
                }
            }

            ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);
            targetMap = replacements;

            for (AnalysisItem analysisItem : replacementMap.values()) {
                analysisItem.updateIDs(replacements);
            }

            for (FilterDefinition filter : filterDefinitions) {
                filter.updateIDs(replacements);
            }

            dashboard.setFilters(filterDefinitions);

            fields.addAll(replacements.getFields());

            FilterOverrideVisitor visitor = new FilterOverrideVisitor(replacements, allFields, changingDataSource, dataSource, fields);
            dashboard.visit(visitor);
        }

        //dashboard.getRootElement().updateReportIDs(reportReplacementMap);
        dashboard.getRootElement().updateScorecardIDs(scorecardReplacmenetMap);

        DashboardSaveMetadata saveMetadata = new DashboardSaveMetadata(dashboard, targetMap, fields);

        return saveMetadata;
    }

    private static class FilterOverrideVisitor implements IDashboardVisitor {

        private ReplacementMap replacementMap;
        private List<AnalysisItem> allFields;
        private boolean changingDataSource;
        private FeedDefinition dataSource;
        private List<AnalysisItem> fields;

        private FilterOverrideVisitor(ReplacementMap replacementMap, List<AnalysisItem> allFields, boolean changingDataSource, FeedDefinition dataSource,
                                      List<AnalysisItem> fields) {
            this.replacementMap = replacementMap;
            this.allFields = allFields;
            this.changingDataSource = changingDataSource;
            this.dataSource = dataSource;
            this.fields = fields;
        }

        @Override
        public void accept(DashboardElement dashboardElement) {
            try {
                if (dashboardElement.getFilters() != null && dashboardElement.getFilters().size() > 0) {
                    if (replacementMap instanceof TemplateReplacementMap) {
                        System.out.println("...");
                        List<FilterDefinition> filterDefinitions = new ArrayList<>();
                        for (FilterDefinition persistableFilterDefinition : dashboardElement.getFilters()) {
                            System.out.println("\tinspecting " +persistableFilterDefinition.getField().toDisplay());
                            filterDefinitions.add(persistableFilterDefinition.clone());
                            List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
                            for (AnalysisItem item : filterItems) {
                                replacementMap.addField(item, true);
                            }
                        }


                        for (AnalysisItem analysisItem : replacementMap.getFields()) {
                            analysisItem.updateIDs(replacementMap);
                        }

                        for (FilterDefinition filter : filterDefinitions) {
                            filter.updateIDs(replacementMap);
                        }

                        fields.addAll(replacementMap.getFields());

                        dashboardElement.setFilters(filterDefinitions);
                    } else {
                        System.out.println("...");
                        Map<Long, AnalysisItem> replacementMap = new HashMap<Long, AnalysisItem>();
                        List<FilterDefinition> filterDefinitions = new ArrayList<>();
                        for (FilterDefinition persistableFilterDefinition : dashboardElement.getFilters()) {
                            System.out.println("\t" + persistableFilterDefinition.getField().toDisplay());
                            filterDefinitions.add(persistableFilterDefinition.clone());
                            List<AnalysisItem> filterItems = persistableFilterDefinition.getAnalysisItems(allFields, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
                            for (AnalysisItem item : filterItems) {
                                if (replacementMap.get(item.getAnalysisItemID()) == null) {
                                    AnalysisItem clonedItem = item.clone();
                                    cleanup(clonedItem, changingDataSource);
                                    replacementMap.put(item.getAnalysisItemID(), clonedItem);
                                }
                            }
                        }



                        for (AnalysisItem analysisItem : replacementMap.values()) {
                            System.out.println("Looking for a match for " + analysisItem.toDisplay());
                            AnalysisItem parent = dataSource.findAnalysisItemByDisplayName(analysisItem.toDisplay());
                            if (parent != null) {
                                System.out.println("\tFound match by name of " + parent.toDisplay());
                                analysisItem.setKey(parent.getKey());
                            } else {
                                System.out.println("\tNo match, looking by key");
                                Key key = dataSource.getField(analysisItem.getKey().toDisplayName());
                                if (key != null) {
                                    analysisItem.setKey(key);
                                } else {
                                    Key clonedKey = analysisItem.getKey().clone();
                                    analysisItem.setKey(clonedKey);
                                }
                            }
                        }

                        ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);

                        for (AnalysisItem analysisItem : replacementMap.values()) {
                            analysisItem.updateIDs(replacements);
                        }

                        for (FilterDefinition filter : filterDefinitions) {
                            filter.updateIDs(replacements);
                        }

                        fields.addAll(replacements.getFields());

                        dashboardElement.setFilters(filterDefinitions);
                    }
                }
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Set<Long> containedScorecards() {
        return rootElement.containedScorecards();
    }

    public boolean isPublicVisible() {
        return publicVisible;
    }

    public void setPublicVisible(boolean publicVisible) {
        this.publicVisible = publicVisible;
    }

    public int getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(int headerStyle) {
        this.headerStyle = headerStyle;
    }

    public void visit(IDashboardVisitor dashboardVisitor) {
        rootElement.visit(dashboardVisitor);
    }

    public static void main(String[] args) {
        System.out.println(Integer.parseInt("CCCCCC", 16));
    }

    public void updateIDs(Map<Long, AnalysisDefinition> reportReplacementMap, List<AnalysisItem> allFields, boolean changingDataSource, FeedDefinition dataSource) {
        if (getDefaultDrillthrough() != null) {
            getDefaultDrillthrough().updateReportIDs(reportReplacementMap, new HashMap<Long, Dashboard>());
        }
        getRootElement().updateReportIDs(reportReplacementMap, allFields, changingDataSource, dataSource);
    }
    
    public List<EIDescriptor> allItems(List<AnalysisItem> dataSourceItems) {
        List<EIDescriptor> eiDescs = new ArrayList<EIDescriptor>();
        eiDescs.add(new DashboardDescriptor(getName(), getId(), getUrlKey(), getDataSourceID(), Roles.OWNER, null, accountVisible));
        eiDescs.addAll(getRootElement().allItems(dataSourceItems));
        for (FilterDefinition filterDefinition : filters) {
            eiDescs.add(new FilterDescriptor(filterDefinition));
            List<AnalysisItem> items = filterDefinition.getAnalysisItems(dataSourceItems, new ArrayList<AnalysisItem>(), true, true, new HashSet<AnalysisItem>(), new AnalysisItemRetrievalStructure(null));
            for (AnalysisItem item : items) {
                eiDescs.add(new AnalysisItemDescriptor(item));
                eiDescs.addAll(item.getKey().getDescriptors());
            }
        }
        return eiDescs;
    }

    public Collection<? extends FilterDefinition> filtersForReport(long reportID) {
        List<FilterDefinition> filters = new ArrayList<FilterDefinition>();
        filters.addAll(getFilters());
        filters.addAll(getRootElement().filtersForReport(reportID));
        return filters;
    }

    public DashboardUIProperties findHeaderImage() {
        DashboardUIProperties p = new DashboardUIProperties(this.getHeaderBackgroundColor(), this.getHeaderImage());
        p.setImageFullHeader(this.isImageFullHeader());
        return p;
    }

    public DashboardElement findElement(long dashboardElementID) {
        return getRootElement().findElement(dashboardElementID);
    }

    public int requiredInitCount() {
        return getRootElement().requiredInitCount();
    }

    public JSONObject toJSON(FilterHTMLMetadata metadata) throws JSONException {
        JSONObject dashboard = new JSONObject();
        JSONArray filterArray = new JSONArray();
        if (getOverridenFilters() != null) {
            for (FilterDefinition filter : getFilters()) {
                FilterDefinition overrideFilter = getOverridenFilters().get(String.valueOf(filter.getFilterID()));
                if (overrideFilter != null) {
                    filter.override(overrideFilter);
                    filter.setEnabled(overrideFilter.isEnabled());
                }
            }
        }
        for(FilterDefinition filter : filters) {
            filterArray.put(filter.toJSON(metadata));
        }
        dashboard.put("filters", filterArray);
        dashboard.put("base", getRootElement().toJSON(metadata, new ArrayList<FilterDefinition>(filters)));
        dashboard.put("id", getId());
        dashboard.put("name", getName());
        dashboard.put("key", getUrlKey());
        dashboard.put("local_storage", enableLocalStorage);
        dashboard.put("publiclyVisible", isPublicVisible());
        dashboard.put("publiclyVisibleWithKey", isPublicWithKey());

        JSONArray configurations = new JSONArray();
        if (getConfigurations() != null) {
            for(SavedConfiguration sc : getConfigurations()) {
                configurations.put(sc.toJSON());
            }
        }
        dashboard.put("configurations", configurations);
        JSONObject styles = new JSONObject();
        styles.put("main_stack_start", String.format("#%06X", stackFill1Start & 0xFFFFFF));
        styles.put("alternative_stack_start", String.format("#%06X", stackFill2Start & 0xFFFFFF));
        styles.put("report_header_background", String.format("#%06X", reportHeaderBackgroundColor & 0xFFFFFF));
        styles.put("report_header_text", String.format("#%06X", reportHeaderTextColor & 0xFFFFFF));

        dashboard.put("styles", styles);
        return dashboard;
    }

    public boolean isImageFullHeader() {
        return imageFullHeader;
    }

    public void setImageFullHeader(boolean imageFullHeader) {
        this.imageFullHeader = imageFullHeader;
    }
}
