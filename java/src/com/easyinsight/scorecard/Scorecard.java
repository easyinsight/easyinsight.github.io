package com.easyinsight.scorecard;

import com.easyinsight.analysis.*;
import com.easyinsight.analysis.definitions.WSKPIDefinition;
import com.easyinsight.analysis.definitions.WSTrendGridDefinition;
import com.easyinsight.core.InsightDescriptor;
import com.easyinsight.core.NamedKey;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.kpi.KPI;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.Roles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 2:29:00 PM
 */
public class Scorecard implements Cloneable {
    // need goals here...

    private List<KPI> kpis = new ArrayList<KPI>();
    
    private String name;
    private String description;
    private boolean exchangeVisible;
    private long scorecardID;
    private String urlKey;
    private boolean accountVisible;
    private long dataSourceID;
    private int folder;

    public Scorecard clone(FeedDefinition target, List<AnalysisItem> allFields) throws CloneNotSupportedException {
        Scorecard scorecard = (Scorecard) super.clone();
        scorecard.setScorecardID(0);
        scorecard.setUrlKey(null);
        List<KPI> clonedKPIs = new ArrayList<KPI>();
        for (KPI kpi : kpis) {
            clonedKPIs.add(kpi.clone(target, allFields, true));
        }
        scorecard.setKpis(clonedKPIs);
        return scorecard;
    }

    public InsightDescriptor createTrendGridReport() {
        try {
            WSTrendGridDefinition trendGridDefinition = new WSTrendGridDefinition();
            trendGridDefinition.setReportType(WSAnalysisDefinition.TREND_GRID);
            createTrendReport(trendGridDefinition);
            WSAnalysisDefinition savedReport = new AnalysisService().saveAnalysisDefinition(trendGridDefinition);
            return new InsightDescriptor(savedReport.getAnalysisID(), savedReport.getName(), savedReport.getDataFeedID(),
                    savedReport.getReportType(), savedReport.getUrlKey(), Roles.OWNER, savedReport.isAccountVisible());
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
    }
    
    private void createTrendReport(WSKPIDefinition kpiDefinition) throws CloneNotSupportedException {
        kpiDefinition.setDayWindow(7);
        List<AnalysisItem> measures = new ArrayList<AnalysisItem>();
        for (KPI kpi : kpis) {
            measures.add(kpi.createMeasure());
        }
        kpiDefinition.setMeasures(measures);
        kpiDefinition.setFilterName("Date");
        RollingFilterDefinition rollingFilterDefinition = new RollingFilterDefinition();
        AnalysisDateDimension proxyItem = new AnalysisDateDimension(new NamedKey("Date"), true, AnalysisDateDimension.DAY_LEVEL);
        rollingFilterDefinition.setField(proxyItem);
        rollingFilterDefinition.setTrendFilter(true);
        rollingFilterDefinition.setInterval(MaterializedRollingFilterDefinition.WEEK);
        rollingFilterDefinition.setIntrinsic(true);
        kpiDefinition.setFilterDefinitions(Arrays.asList((FilterDefinition) rollingFilterDefinition));
        kpiDefinition.setName(name);
        kpiDefinition.setAccountVisible(accountVisible);
        kpiDefinition.setDataFeedID(dataSourceID);
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }

    public long getDataSourceID() {
        return dataSourceID;
    }

    public void setDataSourceID(long dataSourceID) {
        this.dataSourceID = dataSourceID;
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

    public String getUrlKey() {
        return urlKey;
    }

    public void setUrlKey(String urlKey) {
        this.urlKey = urlKey;
    }

    public boolean isAccountVisible() {
        return accountVisible;
    }

    public void setAccountVisible(boolean accountVisible) {
        this.accountVisible = accountVisible;
    }

    public long getScorecardID() {
        return scorecardID;
    }

    public void setScorecardID(long scorecardID) {
        this.scorecardID = scorecardID;
    }

    public List<KPI> getKpis() {
        return kpis;
    }

    public void setKpis(List<KPI> kpis) {
        this.kpis = kpis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
