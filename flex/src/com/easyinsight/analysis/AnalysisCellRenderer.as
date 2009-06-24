package com.easyinsight.analysis
{
import com.easyinsight.analysis.conditions.ConditionRenderer;

import com.easyinsight.detail.DataDetailEvent;
import com.easyinsight.filtering.FilterRawData;

import flash.events.ContextMenuEvent;
import flash.events.MouseEvent;
import flash.net.URLRequest;

import flash.system.System;
import flash.ui.ContextMenu;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UITextField;
import mx.events.FlexEvent;
import mx.formatters.Formatter;

public class AnalysisCellRenderer extends UITextField implements IListItemRenderer
{
    private var _data:Object;
    private var URL:String;
    private static const emptyText:String = "";
    private var _analysisItem:AnalysisItem;
    private var _renderer:ConditionRenderer;
    private var linkShowing:Boolean = false;
    private var linkable:Boolean = false;

    private var defaultBackground:uint;

    private var dataChanged:Boolean = false;

    [Bindable]
    private var listContextMenu:ContextMenu;

    public function AnalysisCellRenderer() {
        super();
        addEventListener(MouseEvent.MOUSE_OVER, onMouseOver);
        addEventListener(MouseEvent.MOUSE_OUT, onMouseOut);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onDrilldown(event:ContextMenuEvent):void {
        drill();
    }

    private function copySelected(event:ContextMenuEvent):void {
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

    private function onMouseOver(event:MouseEvent):void {
        if (linkable && event.ctrlKey && !linkShowing) {
            setStyle("textDecoration", "underline");
            linkShowing = true;
        }
    }

    private function onMouseOut(event:MouseEvent):void {
        if (linkShowing) {
            setStyle("textDecoration", "none");
            linkShowing = false;
        }
    }

    private function onRollup(event:ContextMenuEvent):void {
        var hierarchyItem:AnalysisHierarchyItem = _analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
        if (index > 0) {
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index - 1) as HierarchyLevel;
            dispatchEvent(new HierarchyRollupEvent(hierarchyItem.hierarchyLevel.analysisItem));
        }
    }

    private function drill():void {
        var hierarchyItem:AnalysisHierarchyItem = _analysisItem as AnalysisHierarchyItem;
        var index:int = hierarchyItem.hierarchyLevels.getItemIndex(hierarchyItem.hierarchyLevel);
        if (index < (hierarchyItem.hierarchyLevels.length - 1)) {
            var dataField:String = _analysisItem.qualifiedName();
            var dataString:String = data[dataField];
            var filterRawData:FilterRawData = new FilterRawData();
            filterRawData.addPair(hierarchyItem.hierarchyLevel.analysisItem, dataString);
            hierarchyItem.hierarchyLevel = hierarchyItem.hierarchyLevels.getItemAt(index + 1) as HierarchyLevel;
            dispatchEvent(new HierarchyDrilldownEvent(HierarchyDrilldownEvent.DRILLDOWN, filterRawData));
        }
    }

    private function details(event:ContextMenuEvent):void {
        var dataField:String = _analysisItem.qualifiedName();
        var dataString:String = data[dataField];
        var filterRawData:FilterRawData = new FilterRawData();
        filterRawData.addPair(_analysisItem, dataString);
        dispatchEvent(new DataDetailEvent(filterRawData));
    }

    private function onClick(event:MouseEvent):void {
        if (linkable && event.ctrlKey) {
            if (_analysisItem is AnalysisHierarchyItem) {
                drill();
            }
        }
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
        linkable = val.hasType(AnalysisItemTypes.HIERARCHY);
    }

    public function get renderer():ConditionRenderer {
        return _renderer;
    }

    public function set renderer(val:ConditionRenderer):void {
        _renderer = val;
    }

    public function set data(value:Object):void {
        if (value != _data) {
            dataChanged = true;
            _data = value;
            if (value != null) {
                var field:String = analysisItem.qualifiedName();
                var formatter:Formatter = analysisItem.getFormatter();
                var objVal:Object = value[field];


                if (objVal == null) {
                    this.text = "";
                } else {
                    this.text = formatter.format(objVal);

                    if (renderer.hasCustomColor()) {
                        var color:uint = renderer.getColor(objVal);
                        setStyle("color", color);
                    } else {
                        //this.textColor = 0x000000;
                    }
                }
            } else {
                this.text = "";
            }
            var drilldownFunction:Function = null;
            var rollupFunction:Function = null;
            if (analysisItem is AnalysisHierarchyItem) {
                var hierarchy:AnalysisHierarchyItem = _analysisItem as AnalysisHierarchyItem;
                var index:int = hierarchy.hierarchyLevels.getItemIndex(hierarchy.hierarchyLevel);
                if (index < (hierarchy.hierarchyLevels.length - 1)) {
                    drilldownFunction = onDrilldown;
                }
                if (index > 0) {
                    rollupFunction = onRollup;
                }
            }
            PopupMenuFactory.menuFactory.createListMenu(copySelected, details,
                    drilldownFunction, rollupFunction, this);
            invalidateDisplayList();
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
        }
    }

    public function get data():Object {
        return _data;
    }

    private function navigate(event:MouseEvent):void {
        flash.net.navigateToURL(new URLRequest(URL));
    }

    public function validateProperties():void {
    }

    public function validateSize(recursive:Boolean = false):void {
    }

    public function validateDisplayList():void {
    }
}
}