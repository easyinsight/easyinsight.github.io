package com.easyinsight.pseudocontext {
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItemChangeEvent;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.DrillThrough;
import com.easyinsight.analysis.DrillThroughEvent;
import com.easyinsight.analysis.DrillThroughExecutor;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.analysis.HierarchyRollupEvent;
import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.ReportWindowEvent;
import com.easyinsight.analysis.URLLink;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.ContextMenuEvent;
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.system.System;
import flash.ui.Keyboard;

import mx.containers.Box;
import mx.containers.VBox;
import mx.controls.Label;
import mx.events.FlexEvent;
import mx.formatters.Formatter;
import mx.managers.PopUpManager;

public class PseudoContextWindow extends VBox {

    private var items:Array;

    private var analysisItem:AnalysisItem;

    private var passthroughFunction:Function;
    private var passthroughObject:EventDispatcher;

    private var analysisDefinition:AnalysisDefinition;

    public function PseudoContextWindow(analysisItem:AnalysisItem, passthroughFunction:Function, passthroughObject:EventDispatcher, analysisDefinition:AnalysisDefinition,
            data:Object) {
        super();
        this.analysisItem = analysisItem;
        this.data = data;
        this.passthroughFunction = passthroughFunction;
        this.passthroughObject = passthroughObject;
        this.analysisDefinition = analysisDefinition;
        items = [];
        if (analysisDefinition.showDrilldown(analysisItem)) {
            var drilldownContextItem:PseudoContextItem = new PseudoContextItem("Drilldown", drill);
            items.push(drilldownContextItem);
        }
        if (analysisDefinition.showRollup(analysisItem)) {
            var rollupItem:PseudoContextItem = new PseudoContextItem("Rollup", onRollup);
            items.push(rollupItem);
        }
        if (analysisItem is AnalysisHierarchyItem) {
            var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
            //var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
            for each (var level:HierarchyLevel in hierarchy.hierarchyLevels) {
                var childItem:AnalysisItem = level.analysisItem;
                if (data[childItem.qualifiedName()]) {
                    for each (var hierarchyLink:Link in childItem.links) {
                        composeLink(hierarchyLink);
                    }
                }
            }
        }
        if (analysisItem is AnalysisDateDimension) {
            var date:AnalysisDateDimension = analysisItem as AnalysisDateDimension;
            if (date.dateLevel == AnalysisItemTypes.YEAR_LEVEL) {
                items.push(defineDateLink(AnalysisItemTypes.QUARTER_OF_YEAR, "Quarter of Year"));
                items.push(defineDateLink(AnalysisItemTypes.MONTH_LEVEL, "Month of Year"));
                items.push(defineDateLink(AnalysisItemTypes.WEEK_LEVEL, "Week of Year"));
            } else if (date.dateLevel == AnalysisItemTypes.QUARTER_OF_YEAR) {
                items.push(defineDateLink(AnalysisItemTypes.YEAR_LEVEL, "Year"));
                items.push(defineDateLink(AnalysisItemTypes.MONTH_LEVEL, "Month of Year"));
            } else if (date.dateLevel == AnalysisItemTypes.MONTH_LEVEL) {
                items.push(defineDateLink(AnalysisItemTypes.YEAR_LEVEL, "Year"));
                items.push(defineDateLink(AnalysisItemTypes.QUARTER_OF_YEAR, "Quarter of Year"));
                items.push(defineDateLink(AnalysisItemTypes.WEEK_LEVEL, "Week of Year"));
            } else if (date.dateLevel == AnalysisItemTypes.WEEK_LEVEL) {
                items.push(defineDateLink(AnalysisItemTypes.YEAR_LEVEL, "Year"));
                items.push(defineDateLink(AnalysisItemTypes.QUARTER_OF_YEAR, "Quarter of Year"));
                items.push(defineDateLink(AnalysisItemTypes.MONTH_LEVEL, "Month of Year"));
                items.push(defineDateLink(AnalysisItemTypes.DAY_LEVEL, "Day of Year"));
            } else if (date.dateLevel == AnalysisItemTypes.DAY_LEVEL) {
                items.push(defineDateLink(AnalysisItemTypes.MONTH_LEVEL, "Month of Year"));
                items.push(defineDateLink(AnalysisItemTypes.WEEK_LEVEL, "Week of Year"));
            }
        }
        var copyItem:PseudoContextItem = new PseudoContextItem("Copy Value", copyValue);
        items.push(copyItem);
        if (analysisItem.links.length > 0) {
            for each (var link:Link in analysisItem.links) {
                composeLink(link);
            }
        }

        addEventListener(FlexEvent.CREATION_COMPLETE, onCreationComplete);
        setStyle("borderStyle", "solid");
        setStyle("borderThickness", 1);
        setStyle("cornerRadius", 5);
        setStyle("dropShadowEnabled", true);
        this.width = 200;
        setStyle("paddingTop", 10);
        setStyle("paddingBottom", 10);
        setStyle("backgroundColor", 0xFFFFFF);
        setStyle("backgroundAlpha", 1);
    }

    private function defineDateLink(targetLevel:int, label:String):PseudoContextItem {
        return new PseudoContextItem(label, function(event:ContextMenuEvent):void {
            var date:AnalysisDateDimension = analysisItem as AnalysisDateDimension;
            date.dateLevel = targetLevel;
            passthroughFunction.call(passthroughObject, new AnalysisItemChangeEvent(date));
        }, null);
    }

    private function composeLink(link:Link):void {
        if (link is URLLink) {
            var url:URLLink = link as URLLink;
            var urlContextItem:PseudoContextItem = new PseudoContextItem(url.label, urlClick, url);
            items.push(urlContextItem);
        } else if (link is DrillThrough) {
            var drillThrough:DrillThrough = link as DrillThrough;
            var drillContextItem:PseudoContextItem = new PseudoContextItem(drillThrough.label, drillthroughClick, drillThrough);
            items.push(drillContextItem);
        }
    }


    private function onCreationComplete(event:FlexEvent):void {
        stage.addEventListener(MouseEvent.CLICK, onStageClick, false, 0, true);
        stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp, false, 0, true);
    }

    private function onKeyUp(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.ESCAPE) {
            destroy();
        }
    }

    public function destroy():void {
        stage.removeEventListener(MouseEvent.CLICK, onStageClick);
        stage.removeEventListener(KeyboardEvent.KEY_UP, onKeyUp);
        PopUpManager.removePopUp(this);
    }

    private function onStageClick(event:MouseEvent):void {
        if (!hitTestPoint(event.stageX, event.stageY)) {
            destroy();
        }
    }

    override protected function createChildren():void {
        super.createChildren();

        for (var i:int = 0; i < items.length; i++) {
            var item:PseudoContextItem = items[i];
            var labelBox:Box = new Box();
            labelBox.setStyle("paddingLeft", 10);
            labelBox.setStyle("paddingRight", 10);
            labelBox.setStyle("backgroundColor", 0xFFFFFF);
            labelBox.mouseChildren = false;
            labelBox.percentWidth = 100;
            var label:Label = new Label();
            label.setStyle("fontSize", 13);
            label.text = item.label;
            label.maxWidth = 150;
            labelBox.data = item.data;
            labelBox.addEventListener(MouseEvent.CLICK, item.click);
            labelBox.addEventListener(MouseEvent.MOUSE_OVER, labelMouseOver);
            labelBox.addEventListener(MouseEvent.MOUSE_OUT, labelMouseOut);
            labelBox.addChild(label);
            addChild(labelBox);
        }
    }

    private function labelMouseOver(event:MouseEvent):void {
        var label:Box = event.currentTarget as Box;
        if (label.getStyle("backgroundColor") == 0xFFFFFF) {
            label.setStyle("backgroundColor", 0x3370ce);
        }
    }

    private function labelMouseOut(event:MouseEvent):void {
        var label:Box = event.currentTarget as Box;
        if (label.getStyle("backgroundColor") == 0x3370ce) {
            label.setStyle("backgroundColor", 0xFFFFFF);
        }
    }

    private function onRollup(event:MouseEvent):void {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
        if (index > 0) {
            var previousParent:AnalysisItem = hierarchyItem.hierarchyLevel.analysisItem;
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index - 1) as HierarchyLevel;
            destroy();
            passthroughFunction.call(passthroughObject, new HierarchyRollupEvent(previousParent, hierarchyItem, index - 1));
        }
    }

    private function drill(event:MouseEvent):void {
        var drillEvent:Event = analysisDefinition.drill(analysisItem, data);
        if (drillEvent != null) {
            passthroughFunction.call(passthroughObject, drillEvent);
        }
        destroy();
    }

    private function urlClick(event:MouseEvent):void {
        var link:URLLink = event.currentTarget.data as URLLink;
        var url:String = data[link.label + "_link"];
        navigateToURL(new URLRequest(url), "_blank");
        destroy();
    }

    private function drillthroughClick(event:MouseEvent):void {
        var drillThrough:DrillThrough = event.currentTarget.data as DrillThrough;
        var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough, data, analysisItem);
        executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
        executor.send();
    }

    private function onDrill(event:DrillThroughEvent):void {
        if (event.drillThrough.miniWindow) {
            onReport(new ReportWindowEvent(event.drillThroughResponse.descriptor.id, 0, 0, event.drillThroughResponse.filters,
                    InsightDescriptor(event.drillThroughResponse.descriptor).dataFeedID,
                    InsightDescriptor(event.drillThroughResponse.descriptor).reportType));
        } else {
            onReport(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.drillThroughResponse.descriptor,
                    event.drillThroughResponse.filters));
        }
    }

    private function onReport(event:Event):void {
        destroy();
        passthroughFunction.call(passthroughObject, event);
    }

    private function copyValue(event:MouseEvent):void {
        var field:String = analysisItem.qualifiedName();
        var formatter:Formatter = analysisItem.getFormatter();
        var objVal:Object = data[field];
        var text:String;
        if (objVal == null) {
            text = "";
        } else {
            text = formatter.format(objVal);
        }
        System.setClipboard(text);
        destroy();
    }
}
}