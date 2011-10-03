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
import mx.core.UITextField;
import mx.core.UITextFormat;

public class VerticalListRowHeaderRenderer extends Label implements IListItemRenderer {

    public function VerticalListRowHeaderRenderer() {
        setStyle("textAlign", "right");
        this.percentWidth = 100;
    }

    private var value:Object;

    override public function set data(val:Object):void {
        this.value = val;
        var _analysisMeasure:AnalysisMeasure = val["baseMeasure"] as AnalysisMeasure;
        if (_analysisMeasure == null) {
            text = "";
        } else {
            text = _analysisMeasure.display;
        }
        invalidateDisplayList();
    }

    override public function get data():Object {
        return value;
    }
}
}
