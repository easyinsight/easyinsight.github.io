package com.easyinsight.dashboard {

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.managers.PopUpManager;

public class DashboardReportEditorComponent extends VBox implements IDashboardEditorComponent {

    public var report:DashboardReport;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardReportEditorComponent() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return new SizeInfo();
    }

    private var reportComp:DashboardReportViewComponent = new DashboardReportViewComponent();

    protected override function createChildren():void {
        super.createChildren();
        reportComp.dashboardReport = report;
        reportComp.dashboardEditorMetadata = dashboardEditorMetadata;
        reportComp.elementID = String(DashboardElementFactory.counter++);
        addChild(reportComp);
    }

    public function save():void {
    }

    public function validate():Boolean {
        return true;
    }

    public function edit():void {
        var window:DashboardEditWindow = new DashboardEditWindow();
        window.dashboardElement = report;
        PopUpManager.addPopUp(window, this, true);
    }

    public function refresh():void {
        reportComp.refresh();
    }

    public function updateAdditionalFilters(filterMap:Object):void {
        reportComp.updateAdditionalFilters(filterMap);
    }

    public function initialRetrieve():void {
        reportComp.initialRetrieve();
    }

    public function reportCount():ArrayCollection {
        return reportComp.reportCount();
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}