package com.easyinsight.analysis.summary {
import com.easyinsight.WindowManagement;
import com.easyinsight.analysis.AddonReportEvent;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemUpdateEvent;
import com.easyinsight.analysis.CustomChangeEvent;
import com.easyinsight.analysis.DataServiceEvent;
import com.easyinsight.analysis.FeedMetadata;
import com.easyinsight.analysis.IReportControlBar;
import com.easyinsight.analysis.ListDropArea;
import com.easyinsight.analysis.ListDropAreaGrouping;
import com.easyinsight.analysis.MultiSummaryReportWindow;
import com.easyinsight.analysis.ReportControlBar;
import com.easyinsight.analysis.ReportDataEvent;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.SaveButton;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;

import mx.controls.Label;
import mx.managers.PopUpManager;

public class MultiSummaryControlBar extends ReportControlBar implements IReportControlBar {
    private var keyGrouping:ListDropAreaGrouping;
    private var itemGrouping:ListDropAreaGrouping;
    private var treeDefinition:MultiSummaryDefinition;


    public function MultiSummaryControlBar() {
        keyGrouping = new ListDropAreaGrouping();
        keyGrouping.maxElements = 1;
        keyGrouping.dropAreaType = ListDropArea;
        keyGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);

        itemGrouping = new ListDropAreaGrouping();
        itemGrouping.unlimited = true;
        itemGrouping.dropAreaType = ListDropArea;
        itemGrouping.addEventListener(AnalysisItemUpdateEvent.ANALYSIS_LIST_UPDATE, requestListData);
    }

    private function onUpdate(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    private var _feedMetadata:FeedMetadata;

    public function set feedMetadata(value:FeedMetadata):void {
        _feedMetadata = value;
    }

    private function onAddons(event:AddonReportEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA, false));
    }

    private function configureReports(event:MouseEvent):void {
        var window:MultiSummaryReportWindow = new MultiSummaryReportWindow();
        window.addEventListener(AddonReportEvent.NEW_ADDONS, onAddons, false, 0, true);
        window.report = treeDefinition;
        WindowManagement.manager.addWindow(window);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    override protected function createChildren():void {
        super.createChildren();



        var groupingLabel:Label = new Label();
        groupingLabel.text = "Join Field:";
        groupingLabel.setStyle("fontSize", 14);
        addChild(groupingLabel);
        keyGrouping.report = treeDefinition;
        addDropAreaGrouping(keyGrouping);
        var areaMeasureLabel:Label = new Label();
        areaMeasureLabel.text = "Fields:";
        areaMeasureLabel.setStyle("fontSize", 14);
        addChild(areaMeasureLabel);
        itemGrouping.report = treeDefinition;
        addDropAreaGrouping(itemGrouping);
        var reportsButton:SaveButton = new SaveButton();
        reportsButton.label = "Choose Nested Reports...";
        reportsButton.addEventListener(MouseEvent.CLICK, configureReports);
        addChild(reportsButton);

        if (treeDefinition.key != null) {
            keyGrouping.addAnalysisItem(treeDefinition.key);
        }
        if (treeDefinition.coreItems != null) {
            for each (var item:AnalysisItem in treeDefinition.coreItems) {
                itemGrouping.addAnalysisItem(item);
            }
        }
    }

    private function requestListData(event:AnalysisItemUpdateEvent):void {
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function set analysisDefinition(analysisDefinition:AnalysisDefinition):void {
        treeDefinition = analysisDefinition as MultiSummaryDefinition;
    }

    public function createAnalysisDefinition():AnalysisDefinition {
        treeDefinition.key = keyGrouping.getListColumns()[0];
        treeDefinition.coreItems = new ArrayCollection(itemGrouping.getListColumns());
        return treeDefinition;
    }

    public function isDataValid():Boolean {
        return (keyGrouping.getListColumns().length > 0 && itemGrouping.getListColumns().length > 0);
    }

    public function addItem(analysisItem:AnalysisItem):void {
        itemGrouping.addAnalysisItem(analysisItem);
        dispatchEvent(new ReportDataEvent(ReportDataEvent.REQUEST_DATA));
    }

    public function onCustomChangeEvent(event:CustomChangeEvent):void {
    }

    public function onDataReceipt(event:DataServiceEvent):void {
    }
}
}