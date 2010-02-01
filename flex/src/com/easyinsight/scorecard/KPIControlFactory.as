package com.easyinsight.scorecard {
import mx.core.IFactory;

public class KPIControlFactory implements IFactory {

    private var scorecardID:int;

    public function KPIControlFactory(scorecardID:int) {
        this.scorecardID = scorecardID;
    }

    public function newInstance():* {
        var kpiControls:KPIControls = new KPIControls();
        kpiControls.scorecardID = scorecardID;
        return kpiControls;
    }
}
}