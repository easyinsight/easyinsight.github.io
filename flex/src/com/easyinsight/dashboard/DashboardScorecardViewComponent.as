package com.easyinsight.dashboard {

import com.easyinsight.scorecard.ScorecardRenderer;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;
import mx.containers.VBox;
import mx.controls.Label;

public class DashboardScorecardViewComponent extends Canvas implements IDashboardViewComponent  {

    public var dashboardScorecard:DashboardScorecard;
    private var scorecardRenderer:ScorecardRenderer;

    public function DashboardScorecardViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
    }

    /*private function exportReport():void {
        viewFactory.updateExportMetadata();
        var window:ReportExportWindow = new ReportExportWindow();
        window.report = viewFactory.report;
        window.coreView = viewFactory.getChildAt(0);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }*/

    protected override function createChildren():void {
        super.createChildren();

        scorecardRenderer = new ScorecardRenderer();
        scorecardRenderer.scorecardID = dashboardScorecard.scorecard.id;

        if (dashboardScorecard.showLabel) {
            var vbox:VBox = new VBox();
            vbox.percentHeight = 100;
            vbox.percentWidth = 100;
            vbox.setStyle("horizontalAlign", "center");
            addChild(vbox);
            var label:Label = new Label();
            label.text = dashboardScorecard.scorecard.name;
            vbox.addChild(label);
            vbox.addChild(scorecardRenderer);
        } else {
            addChild(scorecardRenderer);
        }
        /*var navigateItem:ContextMenuItem = new ContextMenuItem("View " + dashboardReport.report.name);
        navigateItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(dashboardReport.report)));
        });
        var editItem:ContextMenuItem = new ContextMenuItem("Edit " + dashboardReport.report.name);
        editItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(dashboardReport.report)));
        });
        var exportItem:ContextMenuItem = new ContextMenuItem("Export " + dashboardReport.report.name);
        exportItem.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, function(event:ContextMenuEvent):void {
            exportReport();
        });
        var menu:ContextMenu = new ContextMenu();
        menu.hideBuiltInItems();
        menu.customItems = [ navigateItem, editItem, exportItem ];*/
        //scorecardRenderer.refreshValues();
        //viewFactory.contextMenu = menu;
    }

    public function refresh(filters:ArrayCollection):void {
        /*viewFactory.filterDefinitions = filters;
        viewFactory.retrieveData();*/
        scorecardRenderer.refreshValues();
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        scorecardRenderer.refreshValues();
    }
}
}