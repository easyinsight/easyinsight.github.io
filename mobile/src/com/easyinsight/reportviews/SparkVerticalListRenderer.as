/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/25/11
 * Time: 1:32 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.Value;

import flash.text.TextLineMetrics;

import mx.core.UITextField;

import mx.events.FlexEvent;

import spark.components.gridClasses.GridItemRenderer;
import spark.components.supportClasses.StyleableTextField;

public class SparkVerticalListRenderer extends GridItemRenderer {

    private var text:StyleableTextField;

    public function SparkVerticalListRenderer() {
        text = new StyleableTextField();
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
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
        addElement(text);
    }

    private var value:Object;

    override public function set data(value:Object):void {
        this.value = value;
        var measure:AnalysisMeasure = value[_qualifiedName + "measure"] as AnalysisMeasure;
        if (measure != null) {
            showDivider = measure.underline;
        }
        text.text = value[_qualifiedName];
        invalidateDisplayList();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    override public function get data():Object {
        return this.value;
    }
}
}
