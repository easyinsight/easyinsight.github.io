/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/18/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisMeasure;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.core.UITextFormat;

public class VerticalListRowHeaderRenderer extends UITextField implements IListItemRenderer {

    public function VerticalListRowHeaderRenderer() {
        var tf:UITextFormat = new UITextFormat(this.systemManager, "Tahoma");
        tf.align = "right";
        setTextFormat(tf);
        this.percentWidth = 100;
    }

    private var value:Object;

    public function set data(val:Object):void {
        this.value = val;
        var _analysisMeasure:AnalysisMeasure = val["baseMeasure"] as AnalysisMeasure;
        if (_analysisMeasure == null) {
            text = "";
        } else {
            text = _analysisMeasure.display;
        }
        invalidateDisplayList();
    }

    public function get data():Object {
        return value;
    }

    public function validateProperties():void {
    }

    public function validateDisplayList():void {
    }

    public function validateSize(recursive:Boolean = false):void {
    }
}
}
