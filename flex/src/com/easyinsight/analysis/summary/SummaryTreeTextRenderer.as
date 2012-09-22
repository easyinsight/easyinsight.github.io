/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.analysis.TextReportFieldExtension;
import com.easyinsight.analysis.TreeRow;
import com.easyinsight.analysis.Value;
import com.easyinsight.pseudocontext.StandardContextWindow;

import flash.events.Event;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.formatters.Formatter;

public class SummaryTreeTextRenderer extends UITextField implements IListItemRenderer {

    private var _analysisItem:AnalysisItem;
    private var _data:Object;
    private var _report:AnalysisDefinition;

    public function SummaryTreeTextRenderer() {
        super();
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

    private var _depth:int;

    public function get depth():int {
        return _depth;
    }

    public function set depth(value:int):void {
        _depth = value;
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
        if (analysisItem.reportFieldExtension != null && analysisItem.reportFieldExtension is TextReportFieldExtension) {
            var ext:TextReportFieldExtension = analysisItem.reportFieldExtension as TextReportFieldExtension;
            if (ext.wordWrap) {
                this.multiline = true;
                this.wordWrap = true;
            }
        }
        var treeDef:SummaryDefinition = _report as SummaryDefinition;
        var index:int = -1;
        for (var i:int = 0; i < AnalysisHierarchyItem(treeDef.hierarchy).hierarchyLevels.length; i++) {
            var level:HierarchyLevel = AnalysisHierarchyItem(treeDef.hierarchy).hierarchyLevels.getItemAt(i) as HierarchyLevel;
            if (level.analysisItem == analysisItem) {
                depth = i;
            }
        }

        var color:uint = depth == 0 ? 0xFFFFFF : 0x000000;
        trace("depth = " + depth + ", color = " + color);
        var tf:UITextFormat = new UITextFormat(this.systemManager, _report.getFont(), _report.fontSize, color);
        setTextFormat(tf);
        /*var treeRow:TreeRow = value as TreeRow;
        var formatter:Formatter = analysisItem.getFormatter();
        var field:String = analysisItem.qualifiedName();
        var text:String = "";
        if (treeRow.values[field] is Value) {
            var objVal:Value = treeRow.values[field];
            if (objVal == null) {
                text = "";
            } else {
                text = formatter.format(objVal.getValue());
            }
        } else {
            if (treeRow.values[field] != null) {
                text = formatter.format(treeRow.values[field]);
            } else {
                text = "";
            }

        }*/
        this.text = text;
        new StandardContextWindow(analysisItem, passThrough, this, value, _report, false);
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }
}
}