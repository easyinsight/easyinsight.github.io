package com.easyinsight.analysis.gauge {
import com.easyinsight.analysis.CustomChangeEvent;

public class GaugeTypeEvent extends CustomChangeEvent{

    public var gaugeType:int;

    public function GaugeTypeEvent(gaugeType:int) {
        super();
        this.gaugeType = gaugeType;
    }
}
}