package com.easyinsight.dashboard {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.report.AbstractViewFactory;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;
import mx.containers.VBox;
import mx.controls.Label;

public class DashboardReportViewComponent extends Canvas implements IDashboardViewComponent  {

    public var dashboardReport:DashboardReport;
    private var viewFactory:AbstractViewFactory;

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
        viewFactory.retrieveData();
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