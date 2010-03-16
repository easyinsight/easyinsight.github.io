package com.easyinsight.scorecard;

import java.util.List;

/**
 * User: jamesboe
 * Date: Mar 9, 2010
 * Time: 11:27:16 AM
 */
public class ScorecardList {
    private List<ScorecardDescriptor> scorecardDescriptors;
    private boolean anyData;

    public ScorecardList(List<ScorecardDescriptor> scorecardDescriptors, boolean anyData) {
        this.scorecardDescriptors = scorecardDescriptors;
        this.anyData = anyData;
    }

    public ScorecardList() {
    }

    public List<ScorecardDescriptor> getScorecardDescriptors() {
        return scorecardDescriptors;
    }

    public void setScorecardDescriptors(List<ScorecardDescriptor> scorecardDescriptors) {
        this.scorecardDescriptors = scorecardDescriptors;
    }

    public boolean isAnyData() {
        return anyData;
    }

    public void setAnyData(boolean anyData) {
        this.anyData = anyData;
    }
}
