package com.easyinsight.pseudocontext {
import com.easyinsight.analysis.AnalysisDateDimension;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItemChangeEvent;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.DrillThrough;
import com.easyinsight.analysis.DrillThroughEvent;
import com.easyinsight.analysis.DrillThroughExecutor;
import com.easyinsight.analysis.ReportWindowEvent;
import com.easyinsight.analysis.HierarchyDrilldownEvent;
import com.easyinsight.analysis.HierarchyLevel;
import com.easyinsight.analysis.HierarchyRollupEvent;
import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.URLLink;
import com.easyinsight.filtering.FilterRawData;
import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.display.InteractiveObject;
import flash.events.ContextMenuEvent;
import flash.events.Event;
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
    private var filterDefinitions:ArrayCollection;
    private var copyText:String;

    private var altKey:String;

    public function StandardContextWindow(analysisItem:AnalysisItem, passthroughFunction:Function, passthroughObject:InteractiveObject, data:Object,
                                          includeDrills:Boolean = true, filterDefinitions:ArrayCollection = null, copyText:String = null, additionalOptions:Array = null,
            altKey:String = null) {
        super();
        this.analysisItem = analysisItem;
        this.passthroughFunction = passthroughFunction;
        this.passthroughObject = passthroughObject;
        this.filterDefinitions = filterDefinitions;
        this.data = data;
        this.copyText = copyText;
        this.altKey = altKey;
        items = [];
        if (analysisItem is AnalysisHierarchyItem) {
            var hierarchy:AnalysisHierarchyItem = analysisItem as AnalysisHierarchyItem;
            if (includeDrills) {
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

        var copyItem:ContextMenuItem = new ContextMenuItem("Copy Value");
        copyItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, copyValue);
        items.push(copyItem);
        if (analysisItem.links.length > 0) {
            for each (var link:Link in analysisItem.links) {
                composeLink(link);
            }
        }
        if (additionalOptions != null) {
            for each (var additionalItem:ContextMenuItem in additionalOptions) {
                items.push(additionalItem);
            }
        }
        var menu:ContextMenu = new ContextMenu();
        menu.hideBuiltInItems();
        menu.customItems = items;
        passthroughObject.contextMenu = menu;
    }



    private function defineDateLink(targetLevel:int, label:String):ContextMenuItem {
        var item:ContextMenuItem = new ContextMenuItem(label);
        item.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            var date:AnalysisDateDimension = analysisItem as AnalysisDateDimension;
            date.dateLevel = targetLevel;
            passthroughFunction.call(passthroughObject, new AnalysisItemChangeEvent(date));
        });
        return item;
    }

    private function composeLink(link:Link):void {
        if (link is URLLink) {
            var url:URLLink = link as URLLink;
            var urlContextItem:ContextMenuItem = new ContextMenuItem(url.label);
            urlContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
                var key:String = altKey != null ? altKey : "";
                var url:String = data[key + link.label + "_link"];
                navigateToURL(new URLRequest(url), "_blank");
            });
            items.push(urlContextItem);
        } else if (link is DrillThrough) {
            var drillThrough:DrillThrough = link as DrillThrough;
            var drillContextItem:ContextMenuItem = new ContextMenuItem(drillThrough.label);
            drillContextItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function (event:ContextMenuEvent):void {
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            });
            items.push(drillContextItem);
        }
    }

    private function onDrill(event:DrillThroughEvent):void {
        var filters:ArrayCollection;
        if (filterDefinitions == null) {
            if (analysisItem.hasType(AnalysisItemTypes.DIMENSION)) {
                var filterDefinition:FilterValueDefinition = new FilterValueDefinition();
                filterDefinition.field = analysisItem;
                filterDefinition.singleValue = true;
                filterDefinition.filteredValues = new ArrayCollection([data[analysisItem.qualifiedName()]]);
                filterDefinition.enabled = true;
                filterDefinition.inclusive = true;
                filters = new ArrayCollection([ filterDefinition ]);
            } else {
                filters = new ArrayCollection();
            }
        } else {
            filters = filterDefinitions;
        }
        if (event.drillThrough.miniWindow) {
            onReport(new ReportWindowEvent(event.report.id, 0, 0, filters, InsightDescriptor(event.report).dataFeedID, InsightDescriptor(event.report).reportType));
        } else {
            onReport(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.report, filters));
        }

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

    private function onReport(event:Event):void {
        passthroughFunction.call(passthroughObject, event);
    }

    private function copyValue(event:ContextMenuEvent):void {
        var text:String;
        if (copyText == null) {
            var field:String = analysisItem.qualifiedName();
            var formatter:Formatter = analysisItem.getFormatter();
            var objVal:Object = data[field];

            if (objVal == null) {
                text = "";
            } else {
                text = formatter.format(objVal);
            }
        } else {
            text = copyText;
        }
        System.setClipboard(text);
    }
}
}