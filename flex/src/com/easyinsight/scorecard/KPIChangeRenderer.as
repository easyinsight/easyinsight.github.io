package com.easyinsight.scorecard {
import com.easyinsight.analysis.formatter.PercentageNumberFormatter;
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;

public class KPIChangeRenderer extends HBox{

    private var valueLabel:Label;

    public function KPIChangeRenderer() {
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
            var formatter:PercentageNumberFormatter = new PercentageNumberFormatter();
            formatter.precision = 2;
            valueLabel.text = formatter.format(kpi.kpiOutcome.percentChange);
        }
    }

    override public function get data():Object {
        return this.kpi;
    }
}
}