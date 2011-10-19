package com.easyinsight.analysis
{


import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.controls.Alert;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.core.UITextFormat;
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
        addEventListener(MouseEvent.ROLL_OVER, onRollOver);
        addEventListener(MouseEvent.ROLL_OUT, onRollOut);
        this.percentWidth = 100;
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
        var color:uint = 0;
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
                        color = listDefinition.summaryRowTextColor;
                    } else {
                        if (objVal.valueExtension != null) {
                            var ext:TextValueExtension = objVal.valueExtension as TextValueExtension;
                            color = ext.color;
                        } else {
                            color = listDefinition.textColor;
                        }
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

        var rext:TextReportFieldExtension = analysisItem.reportFieldExtension as TextReportFieldExtension;
        var align:String = "left";
        if (rext != null && rext.align != null) {
            align = rext.align.toLowerCase();
        }
        var tf:UITextFormat = new UITextFormat(this.systemManager, _report.getFont(), _report.fontSize, color);
        tf.align = align;
        setTextFormat(tf);
        new StandardContextWindow(analysisItem, passThrough, this, value);
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    public function get data():Object {
        return _data;
    }

}
}