package com.easyinsight.scorecard {
import com.easyinsight.analysis.formatter.PercentageNumberFormatter;
import com.easyinsight.framework.User;
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.formatters.DateFormatter;
import mx.formatters.Formatter;

public class KPIChangeRenderer extends UIComponent implements IListItemRenderer {

    private var valueLabel:Label;

    public function KPIChangeRenderer() {
        super();
        valueLabel = new Label();
        valueLabel.setStyle("fontSize", 14);
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(valueLabel);
    }

    private var kpi:KPI;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        this.kpi = val as KPI;
        if (this.kpi.kpiOutcome != null && this.kpi.kpiOutcome.directional) {
            var formatter:PercentageNumberFormatter = new PercentageNumberFormatter();
            formatter.precision = 2;
            valueLabel.text = formatter.format(kpi.kpiOutcome.percentChange);
            var df:DateFormatter = new DateFormatter();
            df.formatString = User.getInstance().getDateFormat();
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
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));   
    }

    public function get data():Object {
        return this.kpi;
    }
    
    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        valueLabel.move(5,8);
        valueLabel.setActualSize(unscaledWidth, unscaledHeight);
    }
}
}