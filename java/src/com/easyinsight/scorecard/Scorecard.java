package com.easyinsight.scorecard;

import com.easyinsight.kpi.KPI;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Jan 18, 2010
 * Time: 2:29:00 PM
 */
public class Scorecard {
    // need goals here...

    private List<KPI> kpis = new ArrayList<KPI>();
    
    private String name;
    private String description;
    private boolean exchangeVisible;
    private long scorecardID;
    private String urlKey;
    private boolean accountVisible;
    private long dataSourceID;

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
