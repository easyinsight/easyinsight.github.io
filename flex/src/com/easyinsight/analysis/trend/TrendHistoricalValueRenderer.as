package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.TrendOutcome;
import com.easyinsight.analysis.TrendReportFieldExtension;
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;
import mx.formatters.Formatter;

public class TrendHistoricalValueRenderer extends UIComponent implements IListItemRenderer {

    private var valueLabel:UITextField;

    public function TrendHistoricalValueRenderer() {
        super();
        valueLabel = new UITextField();
    }


    override protected function createChildren():void {
        super.createChildren();
        addChild(valueLabel);
    }

    private var trendOutcome:TrendOutcome;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        this.trendOutcome = val as TrendOutcome;
        if (trendOutcome != null) {
            if (trendOutcome.measure.reportFieldExtension != null && trendOutcome.measure.reportFieldExtension is TrendReportFieldExtension &&
                    (TrendReportFieldExtension(trendOutcome.measure.reportFieldExtension).date || TrendReportFieldExtension(trendOutcome.measure.reportFieldExtension).trendComparisonField)) {
                var formatter:Formatter = trendOutcome.measure.getFormatter();
                valueLabel.text = formatter.format(trendOutcome.historical.getValue());
                valueLabel.validateNow();
                var tf:UITextFormat = new UITextFormat(this.systemManager);
                tf.align = "right";
                valueLabel.setTextFormat(tf);
                invalidateProperties();
                dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
            } else {
                valueLabel.text = "";
            }
        }
    }

    public function get data():Object {
        return this.trendOutcome;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        valueLabel.setActualSize(unscaledWidth - 5, unscaledHeight);
    }
}
}