package com.easyinsight.dashboard {

import flash.events.Event;

import mx.collections.ArrayCollection;
import mx.containers.VBox;
import mx.managers.PopUpManager;

public class DashboardReportEditorComponent extends VBox implements IDashboardEditorComponent {

    public var report:DashboardReport;

    public var dashboardEditorMetadata:DashboardEditorMetadata;

    public function DashboardReportEditorComponent() {
        super();
        this.percentWidth = 100;
        horizontalScrollPolicy = "off";
        verticalScrollPolicy = "off";
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return reportComp.obtainPreferredSizeInfo();
    }

    private var reportComp:DashboardReportViewComponent = new DashboardReportViewComponent();

    protected override function createChildren():void {
        super.createChildren();
        if (!dashboardEditorMetadata.dashboard.absoluteSizing) {
            percentHeight = 100;
        }
        reportComp.dashboardReport = report;
        reportComp.dashboardEditorMetadata = dashboardEditorMetadata;
        reportComp.elementID = String(DashboardElementFactory.counter++);
        addChild(reportComp);
    }

    public function save():void {
    }

    public function validate(results:Array):void {

    }

    public function edit():void {
        var window:DashboardEditWindow = new DashboardEditWindow();
        window.dashboardElement = report;
        window.addEventListener(Event.CHANGE, onChange, false, 0, true);
        PopUpManager.addPopUp(window, this, true);
    }

    private function onChange(event:Event):void {
        reportComp.invalidateProperties();
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