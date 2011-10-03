/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/11
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisMeasure;

import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;

public class VerticalListRenderer extends Label implements IListItemRenderer {

    public function VerticalListRenderer() {
        /*var tf:UITextFormat = new UITextFormat(this.systemManager, "Tahoma");
        tf.align = "right";
        setTextFormat(tf);*/
        setStyle("textAlign", "right");
        this.percentWidth = 100;
    }

    private var _qualifiedName:String;

    public function set qualifiedName(value:String):void {
        _qualifiedName = value;
    }

    private var value:Object;

    override public function set data(value:Object):void {
        this.value = value;
        var measure:AnalysisMeasure = value[_qualifiedName + "measure"] as AnalysisMeasure;
        this.text = value[_qualifiedName];
    }

    override public function get data():Object {
        return this.value;
    }
}
}
