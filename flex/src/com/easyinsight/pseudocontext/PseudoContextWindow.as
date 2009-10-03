package com.easyinsight.pseudocontext {

import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;


import com.easyinsight.analysis.DrillThrough;
import com.easyinsight.analysis.DrillThroughExecutor;
import com.easyinsight.analysis.HierarchyDrilldownEvent;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.analysis.HierarchyRollupEvent;

import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.URLLink;
import com.easyinsight.filtering.FilterRawData;

import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.report.ReportNavigationEvent;

import flash.events.KeyboardEvent;
import flash.events.MouseEvent;

import flash.net.URLRequest;
import flash.system.System;
import flash.ui.Keyboard;

import mx.collections.ArrayCollection;
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
    private var passthroughObject:Object;

    public function PseudoContextWindow(analysisItem:AnalysisItem, passthroughFunction:Function, passthroughObject:Object) {
        super();
        this.analysisItem = analysisItem;
        this.passthroughFunction = passthroughFunction;
        this.passthroughObject = passthroughObject;
        items = [];
        if (analysisItem is AnalysisHierarchyItem) {
            var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
            var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
            if (index < (hierarchy.hierarchyLevels.length - 1)) {
                var drilldownContextItem:PseudoContextItem = new PseudoContextItem("Drilldown", drill);
                items.push(drilldownContextItem);
            }
            if (index > 0) {
                var rollupItem:PseudoContextItem = new PseudoContextItem("Rollup", onRollup);
                items.push(rollupItem);
            }
        }
        var copyItem:PseudoContextItem = new PseudoContextItem("Copy Value", copyValue);
        items.push(copyItem);
        if (analysisItem.links.length > 0) {
            for each (var link:Link in analysisItem.links) {
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



    private function onCreationComplete(event:FlexEvent):void {
        stage.addEventListener(MouseEvent.CLICK, onStageClick);
        stage.addEventListener(KeyboardEvent.KEY_UP, onKeyUp);
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
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index - 1) as HierarchyLevel;
            destroy();
            passthroughFunction.call(passthroughObject, new HierarchyRollupEvent(hierarchyItem.hierarchyLevel.analysisItem));
        }
    }

    private function drill(event:MouseEvent):void {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
        if (index < (hierarchyItem.hierarchyLevels.length - 1)) {
            var dataField:String = analysisItem.qualifiedName();
            var dataString:String = data[dataField];
            var filterRawData:FilterRawData = new FilterRawData();
            filterRawData.addPair(hierarchyItem.hierarchyLevel.analysisItem, dataString);
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index + 1) as HierarchyLevel;
            destroy();
            passthroughFunction.call(passthroughObject, new HierarchyDrilldownEvent(HierarchyDrilldownEvent.DRILLDOWN, filterRawData));
        }
    }

    private function urlClick(event:MouseEvent):void {
        var link:URLLink = event.currentTarget.data as URLLink;
        var url:String = data[link.label + "_link"];
        flash.net.navigateToURL(new URLRequest(url), "_blank");
        destroy();
    }

    private function drillthroughClick(event:MouseEvent):void {
        var drillThrough:DrillThrough = event.currentTarget.data as DrillThrough;
        var filterDefinition:FilterValueDefinition = new FilterValueDefinition();
        filterDefinition.field = analysisItem;
        filterDefinition.filteredValues = new ArrayCollection([data[analysisItem.qualifiedName()]]);
        filterDefinition.enabled = true;
        filterDefinition.inclusive = true;
        var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough.reportID, new ArrayCollection([ filterDefinition ]));
        executor.addEventListener(ReportNavigationEvent.TO_REPORT, onReport);
        executor.send();
    }

    private function onReport(event:ReportNavigationEvent):void {
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