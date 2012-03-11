/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/4/11
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {

import com.easyinsight.analysis.NamedKey;

import flash.events.Event;

import mx.binding.utils.BindingUtils;

import mx.containers.HBox;
import mx.controls.CheckBox;

public class IndexCheckbox extends HBox {

    private var analysisItem:AnalysisItemDispatcher;

    private var checkbox:CheckBox;

    public function IndexCheckbox() {
        checkbox = new CheckBox();
        percentWidth = 100;
        setStyle("horizontalAlign", "center");
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(checkbox);
    }

    /*private function onChange(event:Event):void {
        var namedKey:NamedKey = analysisItem.analysisItem.key.toBaseKey() as NamedKey;
        namedKey.indexed = checkbox.selected;
        namedKey.keyChanged = true;
    }*/

    override public function set data(val:Object):void {
        analysisItem = val as AnalysisItemDispatcher;
        BindingUtils.bindProperty(this, "selected", analysisItem,  "indexed");
    }
    
    public function get selected():Boolean {
        return checkbox.selected;
    }
    
    public function set selected(value:Boolean):void {
        checkbox.selected = value;
        var namedKey:NamedKey = analysisItem.analysisItem.key.toBaseKey() as NamedKey;
        namedKey.indexed = checkbox.selected;
        namedKey.keyChanged = true;
    }

    override public function get data():Object {
        return analysisItem;
    }
}
}
