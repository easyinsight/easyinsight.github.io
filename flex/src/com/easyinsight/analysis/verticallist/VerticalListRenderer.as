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
import mx.core.Application;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.events.FlexEvent;

public class VerticalListRenderer extends UITextField implements IListItemRenderer {

    public function VerticalListRenderer() {
        super();
        var tf:UITextFormat = new UITextFormat(Application(Application.application).systemManager, "Lucida Grande", 12);
        tf.align = "right";
        setTextFormat(tf);
        this.percentWidth = 100;
    }

    private var _qualifiedName:String;

    public function set qualifiedName(value:String):void {
        _qualifiedName = value;
    }

    private var value:Object;

    public function set data(value:Object):void {
        this.value = value;
        var measure:AnalysisMeasure = value[_qualifiedName + "measure"] as AnalysisMeasure;
        this.text = value[_qualifiedName];
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return this.value;
    }

    public function validateProperties():void {
    }

    public function validateDisplayList():void {
    }

    public function validateSize(recursive:Boolean = false):void {
    }
}
}
