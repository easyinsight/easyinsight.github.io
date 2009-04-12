package com.easyinsight.analysis.tree {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.HierarchyDropArea;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.ReportDataEvent;
import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Label;
public class TreeControlBar extends HBox implements IReportControlBar {
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

    override protected function createChildren():void {
        super.createChildren();
        var groupingLabel:Label = new Label();
        groupingLabel.text = "Hierarchy:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        addChild(hierarchyGrouping);
        var areaMeasureLabel:Label = new Label();
        areaMeasureLabel.text = "Fields:";
        areaMeasureLabel.setStyle("fontSize", 14);
        addChild(areaMeasureLabel);
        addChild(itemGrouping);

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

    public function set analysisItems(analysisItems:ArrayCollection):void {
        hierarchyGrouping.analysisItems = analysisItems;
        itemGrouping.analysisItems = analysisItems;
    }

    public function isDataValid():Boolean {
        return (hierarchyGrouping.getListColumns().length > 0 && itemGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {

    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}