package com.easyinsight.dashboard {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.report.AbstractViewFactory;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.listing.AnalysisDefinitionAnalyzeSource;
import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.report.ReportExportWindow;

import com.easyinsight.util.PopUpUtil;

import flash.events.ContextMenuEvent;
import flash.ui.ContextMenu;
import flash.ui.ContextMenuItem;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;
import mx.containers.VBox;
import mx.controls.Label;
import mx.managers.PopUpManager;

public class DashboardReportViewComponent extends Canvas implements IDashboardViewComponent  {

    public var dashboardReport:DashboardReport;
    private var viewFactory:AbstractViewFactory;

    public function DashboardReportViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
    }

    private function exportReport():void {
        viewFactory.updateExportMetadata();
        var window:ReportExportWindow = new ReportExportWindow();
        window.report = viewFactory.report;
        window.coreView = viewFactory.getChildAt(0);
        PopUpManager.addPopUp(window, this, true);
        PopUpUtil.centerPopUp(window);
    }

    protected override function createChildren():void {
        super.createChildren();
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(dashboardReport.report.reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        viewFactory = controller.createEmbeddedView();
        viewFactory.reportID = dashboardReport.report.id;
        viewFactory.dataSourceID = dashboardReport.report.dataFeedID;
        if (dashboardReport.showLabel) {
            var vbox:VBox = new VBox();
            vbox.percentHeight = 100;
            vbox.percentWidth = 100;
            vbox.setStyle("horizontalAlign", "center");
            addChild(vbox);
            var label:Label = new Label();
            label.text = dashboardReport.report.name;
            vbox.addChild(label);
            vbox.addChild(viewFactory);
        } else {
            addChild(viewFactory);
        }
        var navigateItem:ContextMenuItem = new ContextMenuItem("View " + dashboardReport.report.name);
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
        menu.customItems = [ navigateItem, editItem, exportItem ];
        viewFactory.retrieveData();
        viewFactory.contextMenu = menu;
    }

    public function refresh(filters:ArrayCollection):void {
        viewFactory.filterDefinitions = filters;
        viewFactory.retrieveData();
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        viewFactory.retrieveData();
    }
}
}