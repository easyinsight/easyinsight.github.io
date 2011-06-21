package com.easyinsight.analysis
{


import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.events.FlexEvent;
import mx.formatters.Formatter;
import mx.managers.CursorManager;
import mx.managers.PopUpManager;


public class AnalysisCellRenderer extends UITextField implements IListItemRenderer
{
    private var _data:Object;
    private var _analysisItem:AnalysisItem;
    private var _selectionEnabled:Boolean;
    private var _report:AnalysisDefinition;
    private var _rolloverIcon:Class;

    public function AnalysisCellRenderer() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
        addEventListener(MouseEvent.ROLL_OVER, onRollOver);
        addEventListener(MouseEvent.ROLL_OUT, onRollOut);
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function set rolloverIcon(value:Class):void {
        _rolloverIcon = value;
    }

    private function onRollOver(event:MouseEvent):void {
        if (_rolloverIcon && !_selectionEnabled) {
            CursorManager.setCursor(_rolloverIcon);
        }
    }

    private function onRollOut(event:MouseEvent):void {
        if (_rolloverIcon && !_selectionEnabled) {
            CursorManager.removeAllCursors();
        }
    }

    public function set selectionEnabled(value:Boolean):void {
        _selectionEnabled = value;
    }

    private function onClick(event:MouseEvent):void {
        if (!_selectionEnabled && event.shiftKey) {
            var window:PseudoContextWindow = new PseudoContextWindow(_analysisItem, passThrough, this, _report, data);
            PopUpManager.addPopUp(window, this);
            window.x = event.stageX + 5;
            window.y = event.stageY + 5;
        }
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
        if (_analysisItem != null) {
            toolTip = _analysisItem.tooltip;
        }
    }

    public function validateProperties():void {
        validateNow();
    }

    public function validateSize(recursive:Boolean = false):void {
        validateNow();
    }

    public function validateDisplayList():void {
        validateNow();
    }

    public function set data(value:Object):void {
        _data = value;
        if (value != null) {
            var field:String = analysisItem.qualifiedName();
            var formatter:Formatter = analysisItem.getFormatter();
            if (value[field] is Value) {
                var objVal:Value = value[field];
                if (objVal == null) {
                    this.text = "";
                } else {
                    this.text = formatter.format(objVal.getValue());
                }
                if (_report is ListDefinition) {
                    var listDefinition:ListDefinition = _report as ListDefinition;
                    if (objVal.summary) {
                        setColor(listDefinition.summaryRowTextColor);
                    } else {
                        setColor(listDefinition.textColor);
                    }
                }
            } else {
                if (value[field] != null) {
                    this.text = formatter.format(value[field]);
                } else {
                    this.text = "";
                }

            }
        } else {
            this.text = "";
        }
        new StandardContextWindow(analysisItem, passThrough, this, value);
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return _data;
    }

}
}