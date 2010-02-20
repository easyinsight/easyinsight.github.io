package com.easyinsight.scorecard {
import com.easyinsight.analysis.formatter.PercentageNumberFormatter;
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.formatters.DateFormatter;
import mx.formatters.Formatter;

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
        if (this.kpi.kpiOutcome != null && this.kpi.kpiOutcome.directional) {
            var formatter:PercentageNumberFormatter = new PercentageNumberFormatter();
            formatter.precision = 2;
            valueLabel.text = formatter.format(kpi.kpiOutcome.percentChange);
            var df:DateFormatter = new DateFormatter();
            var timeString:String;
            var time:int = kpi.dayWindow;
            if (time == 1) {
                timeString = "1 day";
            } else if (time < 7) {
                timeString = time + " days";
            } else if (time == 7) {
                timeString = "1 week";
            } else if (time % 7 == 0) {
                timeString = (time / 7) + " weeks";
            } else {
                timeString = time + " days";
            }
            var measureFormatter:Formatter = kpi.analysisMeasure.getFormatter();
            var str:String = kpi.name + " has a current value of " + measureFormatter.format(kpi.kpiOutcome.outcomeValue) +
                             ". This value has changed " + formatter.format(kpi.kpiOutcome.percentChange) + " from the value of " +
                             measureFormatter.format(kpi.kpiOutcome.previousValue) + " " + timeString + " ago.";            
            valueLabel.toolTip = str;
        } else {
            valueLabel.text = "";
        }
    }

    override public function get data():Object {
        return this.kpi;
    }
}
}