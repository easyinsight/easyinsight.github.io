package com.easyinsight.analysis.form
{
import com.easyinsight.analysis.*;


import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;
import flash.events.MouseEvent;

import mx.containers.Box;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.TextArea;

import mx.managers.PopUpManager;


public class VerticalFormCellRenderer extends VBox
{
    private var _data:Object;
    private var _analysisItem:AnalysisItem;
    private var _report:FormReport;
    private var _labelTop:Boolean;

    public function VerticalFormCellRenderer() {
        super();
        addEventListener(MouseEvent.CLICK, onClick);
    }


    public function set labelTop(value:Boolean):void {
        _labelTop = value;
    }

    public function set report(value:FormReport):void {
        _report = value;
    }

    override protected function createChildren():void {
        super.createChildren();

        try {
            var objVal:Value = _data[_analysisItem.qualifiedName()];
            var text:String = _analysisItem.getFormatter().format(objVal.getValue());

            if (_analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                var textFieldTextArea:TextArea = new TextArea();
                textFieldTextArea.width = 400;
                textFieldTextArea.setStyle("fontSize", _report.fontSize);
                textFieldTextArea.setStyle("fontFamily", _report.fontName);
                textFieldTextArea.setStyle("fontWeight", "normal");
                textFieldTextArea.setStyle("borderStyle", "none");
                textFieldTextArea.setStyle("backgroundAlpha", 0);
                textFieldTextArea.editable = false;
                textFieldTextArea.text = text;
                addChild(textFieldTextArea);
            } else {
                if (text.length > 70) {
                    var textArea:TextArea = new TextArea();
                    textArea.width = 400;
                    textArea.setStyle("fontSize", _report.fontSize);
                    textArea.setStyle("fontFamily", _report.fontName);
                    textArea.setStyle("fontWeight", "normal");
                    textArea.setStyle("borderStyle", "none");
                    textArea.setStyle("backgroundAlpha", 0);
                    textArea.editable = false;
                    textArea.text = text;
                    addChild(textArea);
                } else {
                    var label:Label = new Label();
                    //label.width = 400;
                    label.setStyle("fontWeight", "normal");
                    label.setStyle("fontSize", _report.fontSize);
                    label.setStyle("fontFamily", _report.fontName);
                    label.text = text;
                    addChild(label);
                }
            }
            var fieldLabel:Label = new Label();
            fieldLabel.setStyle("fontSize", _report.labelFontSize);
            fieldLabel.setStyle("fontFamily", _report.labelFont);
            fieldLabel.text = _analysisItem.display;
            if (_labelTop) {
                addChildAt(fieldLabel, 0);
            } else {
                addChild(fieldLabel);
            }
            new StandardContextWindow(analysisItem, passThrough, this, _data);
        } catch(e:Error) {
            Alert.show(e.message);
        }
    }

    private function onClick(event:MouseEvent):void {
            var window:PseudoContextWindow = new PseudoContextWindow(_analysisItem, passThrough, this, _report, this.data);
            PopUpManager.addPopUp(window, this);
            window.x = event.stageX + 5;
            window.y = event.stageY + 5;
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
    }

    override public function set data(value:Object):void {
        _data = value;
    }

    override public function get data():Object {
        return _data;
    }

}
}