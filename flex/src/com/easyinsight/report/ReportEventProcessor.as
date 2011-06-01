package com.easyinsight.report {
import com.easyinsight.analysis.EmbeddedControllerLookup;
import com.easyinsight.analysis.EmbeddedDataServiceEvent;
import com.easyinsight.analysis.EmbeddedViewFactory;
import com.easyinsight.analysis.IEmbeddedReportController;
import com.easyinsight.analysis.ReportWindowEvent;
import com.easyinsight.analysis.service.ReportRetrievalFault;
import com.easyinsight.framework.DataServiceLoadingEvent;
import com.easyinsight.skin.BackgroundImage;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.EIErrorEvent;
import com.easyinsight.util.EITitleWindow;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;
import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
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
    private var reportCanvas:ReportCanvas;

    private var _loading:Boolean;
    private var _overlayIndex:int;
    private var _stackTrace:String;

    public function ReportEventProcessor() {
        this.width = 600;
        this.height = 500;
        this.showCloseButton = true;
        addEventListener(Event.CLOSE, onClose);
    }

    [Bindable(event="loadingChanged")]
    public function get loading():Boolean {
        return _loading;
    }

    public function set loading(value:Boolean):void {
        if (_loading == value) return;
        _loading = value;
        dispatchEvent(new Event("loadingChanged"));
    }

    [Bindable(event="overlayIndexChanged")]
    public function get overlayIndex():int {
        return _overlayIndex;
    }

    public function set overlayIndex(value:int):void {
        if (_overlayIndex == value) return;
        _overlayIndex = value;
        dispatchEvent(new Event("overlayIndexChanged"));
    }

    [Bindable(event="stackTraceChanged")]
    public function get stackTrace():String {
        return _stackTrace;
    }

    public function set stackTrace(value:String):void {
        if (_stackTrace == value) return;
        _stackTrace = value;
        dispatchEvent(new Event("stackTraceChanged"));
    }

    private function onClose(event:Event):void {
        PopUpManager.removePopUp(this);
    }

    private function gotData(event:EmbeddedDataServiceEvent):void {
        loading = false;
        overlayIndex = 0;
        this.title = event.analysisDefinition.name;
    }

    protected override function createChildren():void {
        super.createChildren();
        var backgroundImage:BackgroundImage = new BackgroundImage();
        backgroundImage.applyCenterScreenLogic = false;
        addChild(backgroundImage);
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
        backgroundImage.addChild(box);
        reportCanvas = new ReportCanvas();
        BindingUtils.bindProperty(reportCanvas, "loading", this, "loading");
        BindingUtils.bindProperty(reportCanvas, "overlayIndex", this, "overlayIndex");
        BindingUtils.bindProperty(reportCanvas, "stackTrace", this, "stackTrace");
        box.addChild(reportCanvas);
        var controllerClass:Class = EmbeddedControllerLookup.controllerForType(reportType);
        var controller:IEmbeddedReportController = new controllerClass();
        var viewFactory:EmbeddedViewFactory = controller.createEmbeddedView();
        viewFactory.reportID = reportID;
        viewFactory.prefix = "";
        viewFactory.dataSourceID = dataSourceID;
        viewFactory.drillthroughFilters = reportFilters;
        reportCanvas.reportBox.addChild(viewFactory);
        viewFactory.addEventListener(EmbeddedDataServiceEvent.DATA_RETURNED, gotData);
        viewFactory.addEventListener(DataServiceLoadingEvent.LOADING_STARTED, dataLoadingEvent);
        viewFactory.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, dataLoadingEvent);
        viewFactory.addEventListener(ReportRetrievalFault.RETRIEVAL_FAULT, onRetrievalFault);
        viewFactory.addEventListener(EIErrorEvent.ERROR, onError);
        this.loading = true;
        this.overlayIndex = 1;
        viewFactory.retrieveData(false);
    }

    private function toReport(event:MouseEvent):void {
        var report:InsightDescriptor = new InsightDescriptor();
        report.dataFeedID = dataSourceID;
        report.id = reportID;
        report.reportType = reportType;
        dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, report, reportFilters));
        PopUpManager.removePopUp(this);
    }

    private function onRetrievalFault(event:ReportRetrievalFault):void {        
        overlayIndex = 2;
    }

    private function dataLoadingEvent(event:DataServiceLoadingEvent):void {
        if (event.type == DataServiceLoadingEvent.LOADING_STARTED) overlayIndex = 1;
        loading = event.type == DataServiceLoadingEvent.LOADING_STARTED;
    }

    private function onError(event:EIErrorEvent):void {
        stackTrace = event.error.getStackTrace();
        overlayIndex = 3;
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