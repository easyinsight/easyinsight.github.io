package com.easyinsight.scorecard {
import com.easyinsight.analysis.formatter.PercentageNumberFormatter;
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;

public class KPITimeRenderer extends HBox{

    private var valueLabel:Label;

    public function KPITimeRenderer() {
        super();
        valueLabel = new Label();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        setStyle("fontSize", 14);
        this.percentWidth = 100;
        this.percentHeight = 100;
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(valueLabel);
    }

    private var kpi:KPI;

    override public function set data(val:Object):void {
        this.kpi = val as KPI;
        if (this.kpi.kpiOutcome != null && this.kpi.kpiOutcome.directional) {
            var time:int = kpi.dayWindow;
            if (time == 1) {
                valueLabel.text = "1 day";
            } else if (time < 7) {
                valueLabel.text = time + " days";
            } else if (time == 7) {
                valueLabel.text = "1 week";
            } else if (time % 7 == 0) {
                valueLabel.text = (time / 7) + " weeks";
            } else {
                valueLabel.text = time + " days";
            }            
        } else {
            valueLabel.text = "";
        }
    }

    override public function get data():Object {
        return this.kpi;
    }
}
}