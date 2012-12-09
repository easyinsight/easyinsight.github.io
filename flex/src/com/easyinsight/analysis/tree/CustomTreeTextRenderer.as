package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DrillThrough;
import com.easyinsight.analysis.DrillThroughEvent;
import com.easyinsight.analysis.DrillThroughExecutor;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.ReportWindowEvent;
import com.easyinsight.analysis.TextReportFieldExtension;
import com.easyinsight.analysis.TreeRow;
import com.easyinsight.analysis.URLLink;
import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;

import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.controls.Alert;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.managers.PopUpManager;

public class CustomTreeTextRenderer extends UITextField implements IListItemRenderer {

    private var _analysisItem:AnalysisItem;
    private var _data:Object;
    private var _report:AnalysisDefinition;

    public function CustomTreeTextRenderer() {
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

    private var defaultLink:Link;

    private var hyperlinked:Boolean;

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
                TreeRow(data).values[analysisItem.qualifiedName()] = TreeRow(data).groupingColumn;
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough, data, analysisItem, _report);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            }
        }
    }

    private function onDrill(event:DrillThroughEvent):void {
        if (event.drillThrough.miniWindow) {
            dispatchEvent(new ReportWindowEvent(event.drillThroughResponse.descriptor.id, 0, 0, event.drillThroughResponse.filters, InsightDescriptor(event.drillThroughResponse.descriptor).dataFeedID,
                    InsightDescriptor(event.drillThroughResponse.descriptor).reportType));
        } else {
            dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.drillThroughResponse.descriptor, event.drillThroughResponse.filters));
        }
    }

    private function onRollOver(event:MouseEvent):void {
        if (hyperlinked) {
            if (utf != null) {
                setTextFormat(hyperlinkedUTF);
                invalidateProperties();
            }
        }
    }

    private var hyperlinkedUTF:UITextFormat;
    private var utf:UITextFormat;

    private function onRollOut(event:MouseEvent):void {
        if (hyperlinked) {
            if (utf != null) {
                setTextFormat(utf);
                invalidateProperties();
            }
        }
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
        var treeDef:TreeDefinition = _report as TreeDefinition;
        var index:int = -1;
        for (var i:int = 0; i < AnalysisHierarchyItem(treeDef.hierarchy).hierarchyLevels.length; i++) {
            var level:HierarchyLevel = AnalysisHierarchyItem(treeDef.hierarchy).hierarchyLevels.getItemAt(i) as HierarchyLevel;
            if (level.analysisItem == analysisItem) {
                var defaultLink:Link = null;
                if (_analysisItem.links != null) {
                    for each (var link:Link in _analysisItem.links) {
                        if (link.defaultLink) {
                            defaultLink = link;
                            break;
                        }
                    }
                }
                this.defaultLink = defaultLink;
                hyperlinked = defaultLink != null;
                depth = i;
            }
        }



        var color:uint = depth == 0 ? 0xFFFFFF : 0x000000;
        utf = new UITextFormat(this.systemManager, _report.getFont(), _report.fontSize, color);
        setTextFormat(utf);
        hyperlinkedUTF = new UITextFormat(this.systemManager, _report.getFont(), _report.fontSize, color, null, null, true);

        if (hyperlinked && !hasLinks) {
            hasLinks = true;
            addEventListener(MouseEvent.ROLL_OVER, onRollOver);
            addEventListener(MouseEvent.ROLL_OUT, onRollOut);
            addEventListener(MouseEvent.CLICK, onClick);
        } else if (!hyperlinked && hasLinks) {
            hasLinks = false;
            removeEventListener(MouseEvent.ROLL_OVER, onRollOver);
            removeEventListener(MouseEvent.ROLL_OUT, onRollOut);
            removeEventListener(MouseEvent.CLICK, onClick);
        }

        new StandardContextWindow(analysisItem, passThrough, this, value, _report, false);
    }

    private var hasLinks:Boolean;

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }
}
}