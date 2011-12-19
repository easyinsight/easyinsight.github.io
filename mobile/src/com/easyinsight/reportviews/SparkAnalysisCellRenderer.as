/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/14/11
 * Time: 10:14 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.TextReportFieldExtension;
import com.easyinsight.analysis.TextValueExtension;
import com.easyinsight.analysis.Value;
import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.report.ReportNavigationEvent;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.core.DPIClassification;

import mx.formatters.Formatter;

import spark.components.LabelItemRenderer;
import spark.skins.spark.DefaultGridItemRenderer;

public class SparkAnalysisCellRenderer extends DefaultGridItemRenderer {

    private var _report:AnalysisDefinition;
    private var _analysisItem:AnalysisItem;
    
    public function SparkAnalysisCellRenderer() {
        super();
        addEventListener(MouseEvent.MOUSE_DOWN, onClick);
    }

    private function onClick(event:MouseEvent):void {
        if (_analysisItem != null && _analysisItem.links != null && _analysisItem.links.length > 0) {
            addEventListener(MouseEvent.MOUSE_UP, onMouseUp);
            addEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
        }
    }

    private function onMouseMove(event:MouseEvent):void {
        removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
        removeEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
    }

    private function onMouseUp(event:MouseEvent):void {
        removeEventListener(MouseEvent.MOUSE_UP, onMouseUp);
        removeEventListener(MouseEvent.MOUSE_MOVE, onMouseMove);
        var blah:MobileContextWindow = new MobileContextWindow();
        blah.addEventListener(ReportNavigationEvent.TO_REPORT, passThrough, false, 0, true);
        blah.analysisItem = _analysisItem;
        blah.data = value;
        blah.passthroughObject = this;
        blah.passthroughFunction = passThrough;
        blah.x = event.localX;
        blah.y = event.localY;
        blah.open(parent, true);
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function set analysisItem(value:AnalysisItem):void {
        _analysisItem = value;
    }
    
    private var value:Object;

    override public function set data(value:Object):void {
        super.data = value;
        this.value = value;
        var linkable:Boolean = _analysisItem.links != null && _analysisItem.links.length > 0;
        var color:uint = linkable ? 0x0000FF : 0;
        var objVal:Value = value[_analysisItem.qualifiedName()];
        if (objVal != null) {
            if (_report is ListDefinition) {
                var listDefinition:ListDefinition = _report as ListDefinition;
                if (objVal.summary) {
                    color = listDefinition.summaryRowTextColor;
                } else {
                    if (objVal.valueExtension != null) {
                        var ext:TextValueExtension = objVal.valueExtension as TextValueExtension;
                        color = ext.color;
                    }/* else {
                        color = listDefinition.textColor;
                    }*/
                }
            }
            var rext:TextReportFieldExtension = _analysisItem.reportFieldExtension as TextReportFieldExtension;
            var align:String = "left";
            if (rext != null && rext.align != null) {
                align = rext.align.toLowerCase();
            }
            setStyle("color", color);
            setStyle("textAlign", align);
        }
    }
    
    override public function get data():Object {
        return this.value;
    }
}
}
