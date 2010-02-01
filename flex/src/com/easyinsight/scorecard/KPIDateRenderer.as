package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.formatters.DateFormatter;
public class KPIDateRenderer extends HBox{

    private var dateLabel:Label;

    public function KPIDateRenderer() {
        super();
        dateLabel = new Label();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        setStyle("fontSize", 14);
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(dateLabel);
    }

    private var kpi:KPI;

    override public function set data(val:Object):void {
        this.kpi = val as KPI;
        if (this.kpi.kpiOutcome != null) {
            var dateFormatter:DateFormatter = new DateFormatter();
            dateLabel.text = dateFormatter.format(kpi.kpiOutcome.evaluationDate);
        }
    }

    override public function get data():Object {
        return this.kpi;
    }
}
}