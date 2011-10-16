package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.TrendOutcome;
import com.easyinsight.analysis.formatter.PercentageNumberFormatter;
import com.easyinsight.framework.User;
import com.easyinsight.kpi.KPI;

import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;
import mx.formatters.DateFormatter;
import mx.formatters.Formatter;

public class TrendChangeRenderer extends UIComponent implements IListItemRenderer {

    private var valueLabel:UITextField;

    public function TrendChangeRenderer() {
        super();
        valueLabel = new UITextField();
        /*var tf:UITextFormat = new UITextFormat(this.systemManager, "Lucida Grande");
        tf.align = "right";
        valueLabel.setTextFormat(tf);*/
        this.percentWidth = 100;
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
            var now:Number = trendOutcome.now.getValue() as Number;
            var previous:Number = trendOutcome.historical.getValue() as Number;
            var formatter:PercentageNumberFormatter = new PercentageNumberFormatter();
            formatter.precision = 2;
            var change:Number = (now - previous) / previous * 100;
            valueLabel.text = formatter.format(change);
            valueLabel.validateNow();
            var tf:UITextFormat = new UITextFormat(this.systemManager, "Lucida Grande");
            tf.align = "right";
            valueLabel.setTextFormat(tf);
            invalidateProperties();
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
        }
    }

    public function get data():Object {
        return this.trendOutcome;
    }
    
    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        //valueLabel.move(5,8);
        valueLabel.setActualSize(unscaledWidth - 5, unscaledHeight);
    }
}
}