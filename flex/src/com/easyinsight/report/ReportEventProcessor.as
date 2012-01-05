package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.ReportWindowEvent;
import com.easyinsight.analysis.service.ReportRetrievalFault;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.EIErrorEvent;
import com.easyinsight.util.EITitleWindow;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.Event;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Button;
import mx.managers.PopUpManager;

public class ReportEventProcessor extends EITitleWindow {

    private var reportFilters:ArrayCollection;
    private var reportID:int;
    private var dataSourceID:int;
    private var reportType:int;



    public function ReportEventProcessor() {
        this.width = 600;
        this.height = 500;
        this.showCloseButton = true;
        addEventListener(Event.CLOSE, onClose);
        setStyle("backgroundColor", 0xF8F8F8);
    }

    private function onClose(event:Event):void {
        PopUpManager.removePopUp(this);
    }

    private function gotData(event:EmbeddedDataServiceEvent):void {
        this.title = event.analysisDefinition.name;
    }

    protected override function createChildren():void {
        super.createChildren();
        var box:VBox = new VBox();
        box.percentHeight = 100;
        box.percentWidth = 100;
        box.setStyle("paddingLeft", 10);
        box.setStyle("paddingRight", 10);
        box.setStyle("paddingTop", 10);
        box.setStyle("paddingBottom", 10);
        var controls:HBox = new HBox();
        controls.setStyle("horizontalAlign", "center");
        controls.percentWidth = 100;
        var toReportButton:Button = new Button();
        toReportButton.styleName = "grayButton";
        toReportButton.addEventListener(MouseEvent.CLICK, toReport);
        toReportButton.label = "Navigate to Report";
        controls.addChild(toReportButton);
        box.addChild(controls);
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        viewFactory = controller.createEmbeddedView();
        viewFactory.reportID = reportID;
        viewFactory.dataSourceID = dataSourceID;
        viewFactory.drillthroughFilters = reportFilters;
        box.addChild(viewFactory);
        viewFactory.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, gotData);
        viewFactory.addEventListener(ReportSetupEvent.REPORT_SETUP, onReportSetup);
        viewFactory.setup();
        addChild(box);
    }

    private var viewFactory:EmbeddedViewFactory;

    private function onReportSetup(event:ReportSetupEvent):void {
        viewFactory.filterDefinitions = event.reportInfo.report.filterDefinitions;
        viewFactory.refresh();
    }

    private function toReport(event:MouseEvent):void {
        var report:InsightDescriptor = new InsightDescriptor();
        report.dataFeedID = dataSourceID;
        report.id = reportID;
        report.reportType = reportType;
        dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, report, reportFilters));
        PopUpManager.removePopUp(this);
    }

    public static function fromEvent(event:ReportWindowEvent, parent:DisplayObject):ReportEventProcessor {
        var window:ReportEventProcessor = new ReportEventProcessor();
        window.reportID = event.reportID;
        window.reportFilters = event.filters;
        window.dataSourceID = event.dataSourceID;
        window.reportType = event.reportType;
        PopUpManager.addPopUp(window, parent, true);
        if (event.x == 0 && event.y == 0) {
            PopUpUtil.centerPopUp(window);
        } else {
            window.x = event.x;
            window.y = event.y;
        }
        return window;
    }
}
}