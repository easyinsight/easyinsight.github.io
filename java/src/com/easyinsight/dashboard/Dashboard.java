package com.easyinsight.dashboard;


import com.easyinsight.analysis.*;
import com.easyinsight.core.AnalysisItemDescriptor;
import com.easyinsight.core.EIDescriptor;
import com.easyinsight.core.FilterDescriptor;
import com.easyinsight.core.Key;
import com.easyinsight.datafeeds.FeedConsumer;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.email.UserStub;
import com.easyinsight.scorecard.Scorecard;
import com.easyinsight.security.Roles;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.util.RandomTextGenerator;


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
        return rootElement.containedReports();
    }

    private void cleanup(AnalysisItem analysisItem, boolean changingDataSource) {
        if (changingDataSource) {
            // TODO: validate calculations and lookup tables--if necessary to create, should emit something with the report
            analysisItem.setLookupTableID(null);
        }
    }

    public Dashboard cloneDashboard(Map<Long, Scorecard> scorecardReplacmenetMap, boolean changingDataSource, List<AnalysisItem> allFields, FeedDefinition dataSource) throws CloneNotSupportedException {
        Dashboard dashboard = this.clone();
        dashboard.setTemporary(true);
        dashboard.setAuthorName(SecurityUtil.getUserName());
        dashboard.setAdministrators(Arrays.asList((FeedConsumer) new UserStub(SecurityUtil.getUserID(), null, null, null, 0, null)));
        dashboard.setCreationDate(new Date());
        dashboard.setExchangeVisible(false);
        dashboard.setRecommendedExchange(false);
        List<FilterDefinition> filterDefinitions = new ArrayList<FilterDefinition>();

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
            Key key = dataSource.getField(analysisItem.getKey().toDisplayName());
            if (key != null) {
                analysisItem.setKey(key);
            } else {
                Key clonedKey = analysisItem.getKey().clone();
                analysisItem.setKey(clonedKey);
            }
        }

        ReplacementMap replacements = ReplacementMap.fromMap(replacementMap);

        for (AnalysisItem analysisItem : replacementMap.values()) {
            analysisItem.updateIDs(replacements);
        }

        for (FilterDefinition filter : filterDefinitions) {
            filter.updateIDs(replacements);
        }

        dashboard.setFilters(filterDefinitions);
        //dashboard.getRootElement().updateReportIDs(reportReplacementMap);
        dashboard.getRootElement().updateScorecardIDs(scorecardReplacmenetMap);
        return dashboard;
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

    public void updateIDs(Map<Long, AnalysisDefinition> reportReplacementMap) {
        getRootElement().updateReportIDs(reportReplacementMap);
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
        return getRootElement().findHeaderImage();
    }

    public DashboardElement findElement(long dashboardElementID) {
        return getRootElement().findElement(dashboardElementID);
    }

    public int requiredInitCount() {
        return getRootElement().requiredInitCount();
    }
}
