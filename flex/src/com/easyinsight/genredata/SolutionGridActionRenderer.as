package com.easyinsight.genredata {
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.controls.Button;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class SolutionGridActionRenderer extends HBox{

    [Bindable]
    [Embed(source="../../../../assets/media_play_green.png")]
    private var playIcon:Class;

    private var exchangeItem:ExchangeItem;

    private var runButton:Button;

    private var solutionService:RemoteObject;

    public function SolutionGridActionRenderer() {
        super();setStyle("horizontalAlign", "center");
        percentWidth = 100;
        runButton = new Button();
        runButton.setStyle("icon", playIcon);
        runButton.labelPlacement = "right";
        runButton.addEventListener(MouseEvent.CLICK, viewReport);
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.determineDataSourceForEntity.addEventListener(ResultEvent.RESULT, gotMatchingDataSources);
        solutionService.installEntity.addEventListener(ResultEvent.RESULT, installedReport);
    }

    private function viewReport(event:MouseEvent):void {
        ProgressAlert.alert(this, "Determining data source...", null, solutionService.determineDataSourceForEntity);
        solutionService.determineDataSourceForEntity.send(exchangeItem.descriptor);
    }

    private function installedReport(event:ResultEvent):void {
        var descriptor:EIDescriptor = solutionService.installEntity.lastResult as EIDescriptor;
        if (descriptor is InsightDescriptor) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor, null, exchangeItem)));
        } else if (descriptor is DashboardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: descriptor.id, exchangeItem: exchangeItem})));
        }
    }

    private function onListingEvent(event:ListingChangeEvent):void {
        dispatchEvent(event);
    }

    private function gotMatchingDataSources(event:ResultEvent):void {
        var dataSources:ArrayCollection = solutionService.determineDataSourceForEntity.lastResult as ArrayCollection;
        if (dataSources.length == 0) {
            var window:NoSolutionInstalledWindow = new NoSolutionInstalledWindow();
            window.solution = exchangeItem.solutionID;
            window.addEventListener(ListingChangeEvent.LISTING_CHANGE, onListingEvent);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        } else if (dataSources.length == 1) {
            ProgressAlert.alert(this, "Preparing the report...", null, solutionService.installEntity);
            solutionService.installEntity.send(exchangeItem.descriptor, dataSources.getItemAt(0).id);
        } else {
            var dsWindow:DataSourceChoiceWindow = new DataSourceChoiceWindow();
            dsWindow.sources = dataSources;
            dsWindow.addEventListener(DataSourceSelectionEvent.DATA_SOURCE_SELECTION, dataSourceChoice, false, 0, true);
            PopUpManager.addPopUp(dsWindow, this, true);
            PopUpUtil.centerPopUp(dsWindow);
        }
    }

    private function dataSourceChoice(event:DataSourceSelectionEvent):void {
        ProgressAlert.alert(this, "Preparing the report...", null, solutionService.installEntity);
        solutionService.installEntity.send(exchangeItem.descriptor, event.dataSource.id);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(runButton);
    }

    override public function set data(val:Object):void {
        exchangeItem = val as ExchangeItem;
    }

    override public function get data():Object {
        return exchangeItem;
    }
}
}