/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/25/11
 * Time: 1:30 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisMeasure;

import mx.events.FlexEvent;

import spark.components.gridClasses.GridItemRenderer;
import spark.components.supportClasses.StyleableTextField;

public class SparkVerticalListRowHeaderRenderer extends GridItemRenderer {

    private var text:StyleableTextField;

    public function SparkVerticalListRowHeaderRenderer() {
        text = new StyleableTextField();
    }

    override protected function createChildren():void {
        super.createChildren();
        addElement(text);
    }

    private var value:Object;

    override public function set data(value:Object):void {
        this.value = value;
        if (value != null) {
            var _analysisMeasure:AnalysisMeasure = value["baseMeasure"] as AnalysisMeasure;
        }
        if (value == null || _analysisMeasure == null) {
            text.text = "";
        } else {
            text.text = _analysisMeasure.display;
        }

        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    override public function get data():Object {
        return this.value;
    }
}
}
