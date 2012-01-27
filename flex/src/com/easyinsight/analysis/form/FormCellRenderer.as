package com.easyinsight.analysis.form
{
import com.easyinsight.analysis.*;

import com.easyinsight.pseudocontext.StandardContextWindow;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.containers.Box;
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.TextArea;
import mx.core.UIComponent;


public class FormCellRenderer extends Box
{
    private var _data:Object;
    private var _analysisItem:AnalysisItem;
    private var _report:FormReport;

    public function FormCellRenderer() {
        super();
        addEventListener(MouseEvent.ROLL_OVER, onRollOver);
        addEventListener(MouseEvent.ROLL_OUT, onRollOut);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private var defaultLink:Link;

    public function set report(value:FormReport):void {
        _report = value;
    }

    private function onClick(event:MouseEvent):void {
        if (defaultLink != null) {
            if (defaultLink is URLLink) {
                var urlLink:URLLink = defaultLink as URLLink;
                var url:String = data[urlLink.label + "_link"];
                try {
                    navigateToURL(new URLRequest(url), "_blank");
                } catch (e:Error) {
                    Alert.show(e.message);
                }
            } else if (defaultLink is DrillThrough) {
                var drillThrough:DrillThrough = defaultLink as DrillThrough;
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough, data, analysisItem, _report);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            }
        }
    }

    private function onDrill(event:DrillThroughEvent):void {
        if (event.drillThrough.miniWindow) {
            dispatchEvent(new ReportWindowEvent(event.drillThroughResponse.descriptor.id, 0, 0, event.drillThroughResponse.filters,
                    InsightDescriptor(event.drillThroughResponse.descriptor).dataFeedID,
                    InsightDescriptor(event.drillThroughResponse.descriptor).reportType));
        } else {
            dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.drillThroughResponse.descriptor,
                    event.drillThroughResponse.filters));
        }
    }

    private function onRollOver(event:MouseEvent):void {
        if (hyperlinked) {
            textComp.setStyle("textDecoration", "underline");
        }
    }

    private function onRollOut(event:MouseEvent):void {
        if (hyperlinked) {
            textComp.setStyle("textDecoration", "none");
        }
    }

    private var hyperlinked:Boolean = false;

    private var textComp:UIComponent;

    override protected function createChildren():void {
        super.createChildren();

        try {
            var objVal:Value = _data[_analysisItem.qualifiedName()];
            defaultLink = _analysisItem.defaultLink();
            var text:String = _analysisItem.getFormatter().format(objVal.getValue());

            if (_analysisItem.hasType(AnalysisItemTypes.TEXT)) {
                var textFieldTextArea:TextArea = new TextArea();
                textFieldTextArea.width = 400;
                textFieldTextArea.setStyle("fontSize", _report.fontSize);
                textFieldTextArea.setStyle("fontFamily", _report.getFont());
                textFieldTextArea.setStyle("fontWeight", "normal");
                textFieldTextArea.setStyle("borderStyle", "none");
                textFieldTextArea.setStyle("backgroundAlpha", 0);
                textFieldTextArea.editable = false;
                textFieldTextArea.text = text;
                this.textComp = textFieldTextArea;
                addChild(textFieldTextArea);
            } else {
                if (text.length > 70) {
                    var textArea:TextArea = new TextArea();
                    textArea.width = 400;
                    textArea.setStyle("fontSize", _report.fontSize);
                    textArea.setStyle("fontFamily", _report.getFont());
                    textArea.setStyle("fontWeight", "normal");
                    textArea.setStyle("borderStyle", "none");
                    textArea.setStyle("backgroundAlpha", 0);
                    textArea.editable = false;
                    textArea.text = text;
                    this.textComp = textArea;
                    addChild(textArea);
                } else {
                    var label:Label = new Label();
                    label.width = 400;
                    label.setStyle("fontWeight", "normal");
                    label.setStyle("fontSize", _report.fontSize);
                    label.setStyle("fontFamily", _report.getFont());
                    label.text = text;
                    this.textComp = label;
                    addChild(label);
                }
            }
            if (defaultLink != null && objVal != null && objVal.type() != Value.EMPTY) {
                hyperlinked = true;
            }
            new StandardContextWindow(analysisItem, passThrough, this, _data, _report);
        } catch(e:Error) {
            Alert.show(e.message);
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
    }

    override public function set data(value:Object):void {
        _data = value;
    }

    override public function get data():Object {
        return _data;
    }

}
}