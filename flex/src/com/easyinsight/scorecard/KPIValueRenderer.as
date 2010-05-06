package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;
import mx.formatters.Formatter;
import mx.formatters.NumberFormatter;
public class KPIValueRenderer extends UIComponent implements IListItemRenderer {

    private var valueLabel:Label;

    public function KPIValueRenderer() {
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
        if (this.kpi.kpiOutcome != null) {
            var formatter:Formatter;
            if (kpi.analysisMeasure == null) {
                formatter = new NumberFormatter();
            } else {
                formatter = kpi.analysisMeasure.getFormatter();
            }
            valueLabel.text = formatter.format(kpi.kpiOutcome.outcomeValue);
        }
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));    
    }

    public function get data():Object {
        return this.kpi;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        valueLabel.move(0,8);
        valueLabel.setActualSize(unscaledWidth, unscaledHeight);
    }
}
}