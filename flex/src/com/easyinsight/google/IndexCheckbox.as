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
import mx.binding.utils.ChangeWatcher;

import mx.containers.HBox;
import mx.controls.CheckBox;
import mx.events.FlexEvent;

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
        checkbox.addEventListener(Event.CHANGE, onChange);
        BindingUtils.bindProperty(checkbox, "selected", this,  "indexed");
        //BindingUtils.bindProperty(this, "indexed", checkbox,  "selected");
        addChild(checkbox);
    }

    private function onChange(event:Event):void {
        //var namedKey:NamedKey = analysisItem.analysisItem.key.toBaseKey() as NamedKey;
        //namedKey.indexed = checkbox.selected;
        this.indexed = checkbox.selected;
        //namedKey.keyChanged = true;
    }

    private var change2:ChangeWatcher;
    private var change3:ChangeWatcher;

    override public function set data(val:Object):void {
        if (analysisItem != null) {
            change2.unwatch();
            change3.unwatch();
        }
        analysisItem = val as AnalysisItemDispatcher;
        change3 = BindingUtils.bindProperty(this, "indexed", analysisItem,  "indexed");
        change2 = BindingUtils.bindProperty(analysisItem, "indexed", this,  "indexed");
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    private var _indexed:Boolean;

    [Bindable(event="indexedChanged")]
    public function get indexed():Boolean {
        return _indexed;
    }

    public function set indexed(value:Boolean):void {
        if (_indexed == value) return;
        _indexed = value;
        //checkbox.selected = value;
        dispatchEvent(new Event("indexedChanged"));
    }

    /*public function get indexed():Boolean {
        var namedKey:NamedKey = analysisItem.analysisItem.key.toBaseKey() as NamedKey;
        return namedKey.indexed;
    }
    
    public function set indexed(value:Boolean):void {
        var namedKey:NamedKey = analysisItem.analysisItem.key.toBaseKey() as NamedKey;
        namedKey.indexed = value;
        namedKey.keyChanged = true;
    }*/

    override public function get data():Object {
        return analysisItem;
    }
}
}
