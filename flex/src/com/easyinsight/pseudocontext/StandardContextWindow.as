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

import flash.display.InteractiveObject;
import flash.events.ContextMenuEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.system.System;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import mx.collections.ArrayCollection;
import mx.formatters.Formatter;

public class StandardContextWindow {

    private var items:Array;

    private var analysisItem:AnalysisItem;

    private var passthroughFunction:Function;
    private var passthroughObject:InteractiveObject;
    private var data:Object;

    public function StandardContextWindow(analysisItem:AnalysisItem, passthroughFunction:Function, passthroughObject:InteractiveObject, data:Object) {
        super();
        this.analysisItem = analysisItem;
        this.passthroughFunction = passthroughFunction;
        this.passthroughObject = passthroughObject;
        this.data = data;
        items = [];
        if (analysisItem is AnalysisHierarchyItem) {
            var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
            var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
            if (index < (hierarchy.hierarchyLevels.length - 1)) {
                var drilldownContextItem:ContextMenuItem = new ContextMenuItem("Drilldown");
                drilldownContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, drill);
                items.push(drilldownContextItem);
            }
            if (index > 0) {
                var rollupItem:ContextMenuItem = new ContextMenuItem("Rollup");
                rollupItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, onRollup);
                items.push(rollupItem);
            }
        }
        var copyItem:ContextMenuItem = new ContextMenuItem("Copy Value");
        copyItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, copyValue);
        items.push(copyItem);
        if (analysisItem.links.length > 0) {
            for each (var link:Link in analysisItem.links) {
                if (link is URLLink) {
                    var url:URLLink = link as URLLink;
                    var urlContextItem:ContextMenuItem = new ContextMenuItem(url.label);
                    urlContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                        
                        var url:String = data[link.label + "_link"];
                        navigateToURL(new URLRequest(url), "_blank");
                    });
                    items.push(urlContextItem);
                } else if (link is DrillThrough) {
                    var drillThrough:DrillThrough = link as DrillThrough;
                    var drillContextItem:ContextMenuItem = new ContextMenuItem(drillThrough.label);
                    drillContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function (event:ContextMenuEvent):void {

        var filterDefinition:FilterValueDefinition = new FilterValueDefinition();
        filterDefinition.field = analysisItem;
        filterDefinition.filteredValues = new ArrayCollection([data[analysisItem.qualifiedName()]]);
        filterDefinition.enabled = true;
        filterDefinition.inclusive = true;
        var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough.reportID, new ArrayCollection([ filterDefinition ]));
        executor.addEventListener(ReportNavigationEvent.TO_REPORT, onReport);
        executor.send();
    });
                    items.push(drillContextItem);
                }
            }
        }
        var menu:ContextMenu = new ContextMenu();
        menu.hideBuiltInItems();
        menu.customItems = items;
        passthroughObject.contextMenu = menu;
    }

    private function onRollup(event:ContextMenuEvent):void {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
        if (index > 0) {
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index - 1) as HierarchyLevel;
            passthroughFunction.call(passthroughObject, new HierarchyRollupEvent(hierarchyItem.hierarchyLevel.analysisItem, hierarchyItem, index - 1));
        }
    }

    private function drill(event:ContextMenuEvent):void {
        var hierarchyItem:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
        if (index < (hierarchyItem.hierarchyLevels.length - 1)) {
            var dataField:String = analysisItem.qualifiedName();
            var dataString:String = data[dataField];
            var filterRawData:FilterRawData = new FilterRawData();
            filterRawData.addPair(hierarchyItem.hierarchyLevel.analysisItem, dataString);
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index + 1) as HierarchyLevel;
            passthroughFunction.call(passthroughObject, new HierarchyDrilldownEvent(HierarchyDrilldownEvent.DRILLDOWN, filterRawData,
                    hierarchyItem, index + 1));
        }
    }

    private function onReport(event:ReportNavigationEvent):void {
        passthroughFunction.call(passthroughObject, event);
    }

    private function copyValue(event:ContextMenuEvent):void {
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
    }
}
}