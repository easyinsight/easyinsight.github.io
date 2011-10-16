/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/4/11
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.TrendOutcome;
import com.easyinsight.analysis.Value;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.Application;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;

public class TrendGroupingRenderer extends UIComponent implements IListItemRenderer {

    private var _grouping:AnalysisItem;
    private var text:UITextField;

    public function TrendGroupingRenderer() {
        super();
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        text = new UITextField();
        addChild(text);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        text.width = this.width;
        text.height = this.height;
    }

    public function set grouping(value:AnalysisItem):void {
        _grouping = value;
    }

    private var outcome:TrendOutcome;

    public function get data():Object {
        return outcome;
    }

    public function set data(value:Object):void {
        this.outcome = value as TrendOutcome;
        var name:String = _grouping.qualifiedName();
        var val:Value = outcome.dimensions[name];
        if (val == null) {
            text.text = "";
        } else {
            text.text = _grouping.getFormatter().format(val.getValue());
        }
        text.validateNow();
        var tf:UITextFormat = new UITextFormat(Application(Application.application).systemManager, "Lucida Grande");
        //tf.align = "right";
        text.setTextFormat(tf);
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }
}
}
