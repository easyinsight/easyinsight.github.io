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
    private long scorecardID;
    private int scorecardOrder;

    public int getScorecardOrder() {
        return scorecardOrder;
    }

    public void setScorecardOrder(int scorecardOrder) {
        this.scorecardOrder = scorecardOrder;
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
