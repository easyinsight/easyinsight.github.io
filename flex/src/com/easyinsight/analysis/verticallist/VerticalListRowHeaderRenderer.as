/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/18/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisMeasure;

import flash.text.TextLineMetrics;

import mx.controls.Label;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;

public class VerticalListRowHeaderRenderer extends UIComponent implements IListItemRenderer {

    private var text:Label;

    public function VerticalListRowHeaderRenderer() {
        text = new Label();
        setStyle("backgroundColor", 0xFFFFFF);
        this.percentWidth = 100;
        //text.setStyle("textAlign", "right");
        //text.percentWidth = 100;
        this.height = 18;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (text != null) {
            var metrics:TextLineMetrics = measureText(text.text);
            var textWidth:int = metrics.width;
            var textX:int = unscaledWidth - textWidth - metrics.x - metrics.x - 2;
            text.move(textX, 0);
            text.setActualSize(textWidth + metrics.x + metrics.x + 2, 16);
        }
        if (showDivider) {
            graphics.beginFill(0x666666, 1);
            graphics.drawRect(0, unscaledHeight - 2, unscaledWidth, 2);
            graphics.endFill();
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(text);
    }

    private var showDivider:Boolean;

    private var value:Object;

    public function set data(val:Object):void {
        this.value = val;
        addChild(text);
        var _analysisMeasure:AnalysisMeasure = val["baseMeasure"] as AnalysisMeasure;
        if (_analysisMeasure == null) {
            text.text = "";
        } else {
            text.text = _analysisMeasure.display;
            showDivider = _analysisMeasure.underline;
        }
        invalidateDisplayList();
    }

    public function get data():Object {
        return value;
    }
}
}
