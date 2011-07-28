package com.easyinsight.dashboard {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.PopupMenuFactory;
import com.easyinsight.report.ReportSetupEvent;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.controls.Label;

public class DashboardReportViewComponent extends Canvas implements IDashboardViewComponent  {

    public var dashboardReport:DashboardReport;
    private var viewFactory:EmbeddedViewFactory;

    public function DashboardReportViewComponent() {
        super();
        percentWidth = 100;
        percentHeight = 100;
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

        viewFactory.addEventListener(ReportSetupEvent.REPORT_SETUP, onReportSetup);
        viewFactory.setup();
        viewFactory.contextMenu = PopupMenuFactory.reportFactory.createReportContextMenu(dashboardReport.report, viewFactory, this);
    }

    private var additionalFilters:ArrayCollection;

    public function updateAdditionalFilters(filters:ArrayCollection):void {
        additionalFilters = filters;
    }

    private var setup:Boolean;

    private var queued:Boolean;

    private function onReportSetup(event:ReportSetupEvent):void {
        viewFactory.filterDefinitions = event.reportInfo.report.filterDefinitions;
        viewFactory.additionalFilterDefinitions = additionalFilters;
        setup = true;
        if (queued) {
            queued = false;
            retrievedDataOnce = true;
            viewFactory.retrieveData();
        }
        /*if (_activeStatus) {
            Alert.show("active status = " + _activeStatus);
            viewFactory.retrieveData();
        }*/
    }

    public function refresh(filters:ArrayCollection):void {
        if (setup) {
            retrievedDataOnce = true;
            viewFactory.additionalFilterDefinitions = additionalFilters;
            viewFactory.retrieveData();
        } else {
            queued = true;
        }
    }

    public function retrieveData(refreshAllSources:Boolean = false):void {
        if (setup) {
            retrievedDataOnce = true;
            viewFactory.additionalFilterDefinitions = additionalFilters;
            viewFactory.retrieveData();
        } else {
            queued = true;
        }
    }

    private var retrievedDataOnce:Boolean;

    public function initialRetrieve():void {
        if (!retrievedDataOnce) {
            if (setup) {
                retrievedDataOnce = true;
                viewFactory.additionalFilterDefinitions = additionalFilters;
                viewFactory.retrieveData();
            } else {
                queued = true;
            }
        }
    }
}
}