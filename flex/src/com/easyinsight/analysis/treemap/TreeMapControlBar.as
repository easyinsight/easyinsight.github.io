package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.HierarchyDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MeasureDropArea;
import com.easyinsight.analysis.ReportDataEvent;

import flash.events.Event;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.ComboBox;
import mx.controls.Label;
import mx.events.FlexEvent;

public class TreeMapControlBar extends HBox implements IReportControlBar {

    private var hierarchyGrouping:ListDropAreaGrouping;
    private var areaMeasureGrouping:ListDropAreaGrouping;
    private var colorMeasureGrouping:ListDropAreaGrouping;
    private var colorSchemeBox:ComboBox;
    private var _colorScheme:int = 0;
    private var mapDefinition:TreeMapDefinition;

    public function TreeMapControlBar() {
        colorSchemeBox = new ComboBox();
        colorSchemeBox.addEventListener(Event.CHANGE, onSchemeChange);
        colorSchemeBox.dataProvider = new ArrayCollection([ "Depth", "Div-Red-Green", "Qualitative" ]);
        BindingUtils.bindProperty(colorSchemeBox, "selectedIndex", this, "colorScheme");
        hierarchyGrouping = new ListDropAreaGrouping();
        hierarchyGrouping.maxElements = 1;
        hierarchyGrouping.dropAreaType = HierarchyDropArea;
        hierarchyGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        areaMeasureGrouping = new ListDropAreaGrouping();
        areaMeasureGrouping.maxElements = 1;
        areaMeasureGrouping.dropAreaType = MeasureDropArea;
        areaMeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        colorMeasureGrouping = new ListDropAreaGrouping();
        colorMeasureGrouping.maxElements = 1;
        colorMeasureGrouping.dropAreaType = MeasureDropArea;
        colorMeasureGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
        setStyle("verticalAlign", "middle");
    }


    [Bindable]
    public function get colorScheme():int {
        return _colorScheme;
    }

    public function set colorScheme(value:int):void {
        if (_colorScheme == value) return;
        _colorScheme = value;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    private function onSchemeChange(event:Event):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA, false));
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(colorSchemeBox);
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Hierarchy:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addChild(hierarchyGrouping);
        var areaMeasureLabel:Label = new Label();
        areaMeasureLabel.text = "Area Measure:";
        areaMeasureLabel.setStyle("fontSize", 14);
        addChild(areaMeasureLabel);
        addChild(areaMeasureGrouping);
        var colorMeasureLabel:Label = new Label();
        colorMeasureLabel.text = "Color Measure:";
        colorMeasureLabel.setStyle("fontSize", 14);
        addChild(colorMeasureLabel);
        addChild(colorMeasureGrouping);

         if (mapDefinition.hierarchy != null) {
            hierarchyGrouping.addAnalysisItem(mapDefinition.hierarchy);
        }
        if (mapDefinition.measure1 != null) {
            areaMeasureGrouping.addAnalysisItem(mapDefinition.measure1);
        }
        if (mapDefinition.measure2 != null) {
            colorMeasureGrouping.addAnalysisItem(mapDefinition.measure2);
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        mapDefinition = analysisDefinition as TreeMapDefinition;
        colorScheme = mapDefinition.colorScheme - 1;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        mapDefinition.hierarchy = hierarchyGrouping.getListColumns()[0];
        mapDefinition.measure1 = areaMeasureGrouping.getListColumns()[0];
        mapDefinition.measure2 = colorMeasureGrouping.getListColumns()[0];
        mapDefinition.colorScheme = colorSchemeBox.selectedIndex + 1;
        return mapDefinition;
    }

    public function set analysisItems(analysisItems:ArrayCollection):void {
        hierarchyGrouping.analysisItems = analysisItems;
        areaMeasureGrouping.analysisItems = analysisItems;
        colorMeasureGrouping.analysisItems = analysisItems;
    }

    public function isDataValid():Boolean {
        return (hierarchyGrouping.getListColumns().length > 0 && areaMeasureGrouping.getListColumns().length > 0 &&
                colorMeasureGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            hierarchyGrouping.addAnalysisItem(analysisItem);
        } else if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            if (areaMeasureGrouping.getListColumns().length == 0) {
                areaMeasureGrouping.addAnalysisItem(analysisItem);
            } else {
                colorMeasureGrouping.addAnalysisItem(analysisItem);
            }
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}