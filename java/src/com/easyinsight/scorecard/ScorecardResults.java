package com.easyinsight.scorecard;

import com.easyinsight.analysis.ReportFault;
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIOutcome;

import java.util.ArrayList;
import java.util.List;

/**
 * User: jamesboe
 * Date: Feb 24, 2010
 * Time: 8:28:52 AM
 */
public class ScorecardResults {
    private ReportFault reportFault;
    private List<KPIOutcome> outcomes = new ArrayList<KPIOutcome>();

    public ReportFault getReportFault() {
        return reportFault;
    }

    public void setReportFault(ReportFault reportFault) {
        this.reportFault = reportFault;
    }

    public List<KPIOutcome> getOutcomes() {
        return outcomes;
    }

    public void setOutcomes(List<KPIOutcome> outcomes) {
        this.outcomes = outcomes;
    }
}
