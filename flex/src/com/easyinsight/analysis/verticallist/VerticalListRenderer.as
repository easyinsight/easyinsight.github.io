/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/11
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.Value;

import flash.display.Shape;
import flash.text.TextLineMetrics;

import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.events.FlexEvent;

import mx.formatters.Formatter;

public class VerticalListRenderer extends UIComponent implements IListItemRenderer {

    private var text:UITextField;

    public function VerticalListRenderer() {
        text = new UITextField();
        text.setStyle("fontFamily", "Verdana");
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (text != null) {
            var metrics:TextLineMetrics = measureText(text.text);
            var textWidth:int = metrics.width;
            var textX:int = unscaledWidth - textWidth - metrics.x - metrics.x;
            text.move(textX, 0);
            text.setActualSize(textWidth + metrics.x + metrics.x, 16);
        }
        if (showDivider) {
            graphics.beginFill(0x666666, 1);
            graphics.drawRect(0, unscaledHeight - 2, unscaledWidth, 2);
            graphics.endFill();
        }
    }

    private var _qualifiedName:String;

    public function set qualifiedName(value:String):void {
        _qualifiedName = value;
    }

    private var showDivider:Boolean;

    override protected function createChildren():void {
        super.createChildren();
        addChild(text);
    }

    private var value:Object;

    public function set data(value:Object):void {
        this.value = value;
        var measure:AnalysisMeasure = value[_qualifiedName + "measure"] as AnalysisMeasure;
        if (measure != null) {
            showDivider = measure.underline;
        }
        text.text = value[_qualifiedName];
        /*if (val != null) {
            var num:Number = val.toNumber();
            var formatter:Formatter = measure.getFormatter();
            text.text = formatter.format(num);
        } else {
            text.text = "";
        }*/
        invalidateDisplayList();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.value;
    }
}
}
