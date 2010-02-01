package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.formatters.Formatter;
import mx.formatters.NumberFormatter;
public class KPIValueRenderer extends HBox{

    private var valueLabel:Label;

    public function KPIValueRenderer() {
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
        if (this.kpi.kpiOutcome != null) {
            var formatter:Formatter;
            if (kpi.analysisMeasure == null) {
                formatter = new NumberFormatter();
            } else {
                formatter = kpi.analysisMeasure.getFormatter();
            }
            valueLabel.text = formatter.format(kpi.kpiOutcome.outcomeValue);
        }
    }

    override public function get data():Object {
        return this.kpi;
    }
}
}