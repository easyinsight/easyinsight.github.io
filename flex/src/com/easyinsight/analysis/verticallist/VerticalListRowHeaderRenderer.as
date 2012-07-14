/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/18/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisMeasure;

import mx.controls.Label;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.Application;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;

public class VerticalListRowHeaderRenderer extends UIComponent implements IListItemRenderer {

    private var text:UITextField;

    public function VerticalListRowHeaderRenderer() {
        super();
        text = new UITextField();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private var value:Object;

    override protected function createChildren():void {
        super.createChildren();
        addChild(text);
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        text.width = this.width;
        text.height = this.height;
    }

    public function set data(value:Object):void {
        this.value = value;
        if (value == null) {
            return;
        }
        var _analysisMeasure:AnalysisMeasure = value["baseMeasure"] as AnalysisMeasure;
        if (_analysisMeasure == null) {
            text.text = "";
        } else {
            text.text = _analysisMeasure.display;
        }
        text.validateNow();
        var tf:UITextFormat = new UITextFormat(Application(Application.application).systemManager, "Lucida Grande", 10);
        tf.align = "right";
        text.setTextFormat(tf);
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.value;
    }
}
}
