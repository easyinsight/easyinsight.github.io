package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;

public class CustomTreeTextRenderer extends UITextField implements IListItemRenderer {

    private var _analysisItem:AnalysisItem;
    private var _data:Object;

    public function CustomTreeTextRenderer() {
        super();
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }

    public function validateProperties():void {
    }

    public function validateDisplayList():void {
    }

    public function validateSize(recursive:Boolean = false):void {
    }

    public function get data():Object {
        return _data;
    }

    public function set data(value:Object):void {
        this._data = value;        
        new StandardContextWindow(analysisItem, passThrough, this, value, false);
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }
}
}