package com.easyinsight.scorecard;

import com.easyinsight.kpi.KPI;
import com.easyinsight.scheduler.OutboundEvent;

import java.util.List;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 11:15:01 AM
 */
public class ScorecardRefreshEvent extends OutboundEvent {

    public static final int DATA_SOURCE_NAME = 1;
    public static final int DONE = 2;

    private List<KPI> kpis;
    private long scorecardID;
    private int type;
    private String dataSourceName;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
