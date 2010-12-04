package com.easyinsight.dashboard {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.report.AbstractViewFactory;

import mx.collections.ArrayCollection;
import mx.containers.Canvas;

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
        addChild(viewFactory);
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