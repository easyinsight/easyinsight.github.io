package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.HierarchyDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.controls.Label;
public class TreeControlBar extends ReportControlBar implements IReportControlBar {
    private var hierarchyGrouping:ListDropAreaGrouping;
    private var itemGrouping:ListDropAreaGrouping;
    private var treeDefinition:TreeDefinition;

    public function TreeControlBar() {
        hierarchyGrouping = new ListDropAreaGrouping();
        hierarchyGrouping.maxElements = 1;
        hierarchyGrouping.dropAreaType = HierarchyDropArea;
        hierarchyGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);

        itemGrouping = new ListDropAreaGrouping();
        itemGrouping.unlimited = true;
        itemGrouping.dropAreaType = ListDropArea;
        itemGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    [Embed(source="../../../../../assets/table_edit.png")]
    public var tableEditIcon:Class;

    private function editLimits(event:MouseEvent):void {

    }

    override protected function createChildren():void {
        super.createChildren();
        var pieEditButton:Button = new Button();
        pieEditButton.setStyle("icon", tableEditIcon);
        pieEditButton.toolTip = "Edit Tree Properties...";
        pieEditButton.addEventListener(MouseEvent.CLICK, editLimits);
        addChild(pieEditButton);
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Hierarchy:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addDropAreaGrouping(hierarchyGrouping);
        var areaMeasureLabel:Label = new Label();
        areaMeasureLabel.text = "Fields:";
        areaMeasureLabel.setStyle("fontSize", 14);
        addChild(areaMeasureLabel);
        addDropAreaGrouping(itemGrouping);

        if (treeDefinition.hierarchy != null) {
            hierarchyGrouping.addAnalysisItem(treeDefinition.hierarchy);
        }
        if (treeDefinition.items != null) {
            for each (var item:AnalysisItem in treeDefinition.items) {
                itemGrouping.addAnalysisItem(item);
            }
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        treeDefinition = analysisDefinition as TreeDefinition;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        treeDefinition.hierarchy = hierarchyGrouping.getListColumns()[0];
        treeDefinition.items = new ArrayCollection(itemGrouping.getListColumns());
        return treeDefinition;
    }

    public function isDataValid():Boolean {
        return (hierarchyGrouping.getListColumns().length > 0 && itemGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
        if (analysisItem.hasType(AnalysisItemTypes.HIERARCHY)) {
            hierarchyGrouping.addAnalysisItem(analysisItem);
        } else {
            itemGrouping.addAnalysisItem(analysisItem);
        }
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}