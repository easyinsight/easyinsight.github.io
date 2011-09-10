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

    private var text:Label;

    public function VerticalListRenderer() {
        text = new Label();
        text.setStyle("textAlign", "right");
    }

    private var textChanged:Boolean = false;

    private var dividerChanged:Boolean = false;

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        //if (dividerChanged) {
            if (showDivider) {
                graphics.beginFill(0x666666, 1);
                graphics.drawRect(0, unscaledHeight - 2, unscaledWidth, 2);
                graphics.endFill();
            } else {
                graphics.clear();
            }
        //}
        //if (textChanged) {
            if (text != null) {
                text.setActualSize(this.width, this.height);
                /*var metrics:TextLineMetrics = measureText(text.text);
                var textWidth:int = metrics.width;
                var textX:int = unscaledWidth - textWidth - metrics.x - metrics.x - 2;
                text.move(textX, 0);
                text.setActualSize(textWidth + metrics.x + metrics.x + 2, 16);*/
            }
        //}
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
            dividerChanged = showDivider != measure.underline;
            showDivider = measure.underline;
        }
        textChanged = text.text != value[_qualifiedName];
        text.text = value[_qualifiedName];
        if (dividerChanged || textChanged) {
            invalidateDisplayList();
        }
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.value;
    }
}
}
