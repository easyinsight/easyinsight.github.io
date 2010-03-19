package com.easyinsight.scorecard {
import mx.core.IFactory;

public class KPIControlFactory implements IFactory {

    private var scorecardID:int;
    private var groupID:int;

    public function KPIControlFactory(scorecardID:int, groupID:int) {
        this.scorecardID = scorecardID;
        this.groupID = groupID;
    }

    public function newInstance():* {
        var kpiControls:KPIControls = new KPIControls();
        kpiControls.scorecardID = scorecardID;
        kpiControls.groupID = groupID;
        return kpiControls;
    }
}
}