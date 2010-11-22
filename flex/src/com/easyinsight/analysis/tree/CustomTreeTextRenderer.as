package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.managers.PopUpManager;

public class CustomTreeTextRenderer extends UITextField implements IListItemRenderer {

    private var _analysisItem:AnalysisItem;
    private var _data:Object;
    private var _report:AnalysisDefinition;

    public function CustomTreeTextRenderer() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
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

    private function onClick(event:MouseEvent):void {
        var window:PseudoContextWindow = new PseudoContextWindow(_analysisItem, passThrough, this, _report, data);
        PopUpManager.addPopUp(window, this);
        window.x = event.stageX + 5;
        window.y = event.stageY + 5;
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