package com.easyinsight.genredata {
import com.easyinsight.framework.User;
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;
import com.easyinsight.util.UserAudit;

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

    private var exchangeItem:SolutionReportExchangeItem;

    private var runButton:Button;

    private var solutionService:RemoteObject;

    public function SolutionGridActionRenderer() {
        super();setStyle("horizontalAlign", "center");
        percentWidth = 100;
        runButton = new Button();
        runButton.setStyle("icon", playIcon);
        runButton.addEventListener(MouseEvent.CLICK, viewReport);
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.determineDataSource.addEventListener(ResultEvent.RESULT, gotMatchingDataSources);
        solutionService.installReport.addEventListener(ResultEvent.RESULT, installedReport);
    }

    private function viewReport(event:MouseEvent):void {
        if (User.getInstance() != null) {
            if (exchangeItem.exchangeData is ExchangeReportData) {
                var exchangeReportData:ExchangeReportData = exchangeItem.exchangeData as ExchangeReportData;
                solutionService.determineDataSource.send(exchangeReportData.dataSourceID);
            }
        } else {
            
        }
    }

    private function installedReport(event:ResultEvent):void {
        UserAudit.instance().audit(UserAudit.USED_REPORT_IN_EXCHANGE);
        var insightDescriptor:InsightDescriptor = solutionService.installReport.lastResult as InsightDescriptor;
        // has to emit special property here to let us decide whether or not we want to keep this report
        dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor, null, true, 0, exchangeItem.id, exchangeItem.ratingAverage, ExchangeReportData(exchangeItem.exchangeData).reportUrlKey)));
    }

    private function onListingEvent(event:ListingChangeEvent):void {
        dispatchEvent(event);
    }

    private function gotMatchingDataSources(event:ResultEvent):void {
        var dataSources:ArrayCollection = solutionService.determineDataSource.lastResult as ArrayCollection;
        if (dataSources.length == 0) {
            var window:NoSolutionInstalledWindow = new NoSolutionInstalledWindow();
            window.solution = exchangeItem.solutionID;
            window.addEventListener(ListingChangeEvent.LISTING_CHANGE, onListingEvent);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        } else if (dataSources.length == 1) {
            ProgressAlert.alert(this, "Preparing the report...", null, solutionService.installReport);
            solutionService.installReport.send(exchangeItem.id, dataSources.getItemAt(0).id);
        } else {
            var dsWindow:DataSourceChoiceWindow = new DataSourceChoiceWindow();
            dsWindow.sources = dataSources;
            dsWindow.addEventListener(DataSourceSelectionEvent.DATA_SOURCE_SELECTION, dataSourceChoice, false, 0, true);
            PopUpManager.addPopUp(dsWindow, this, true);
            PopUpUtil.centerPopUp(dsWindow);
        }
    }

    private function dataSourceChoice(event:DataSourceSelectionEvent):void {
        UserAudit.instance().audit(UserAudit.USED_REPORT_IN_EXCHANGE);
        ProgressAlert.alert(this, "Preparing the report...", null, solutionService.installReport);
        solutionService.installReport.send(exchangeItem.id, event.dataSource.id);
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(runButton);
    }

    override public function set data(val:Object):void {
        exchangeItem = val as SolutionReportExchangeItem;
    }

    override public function get data():Object {
        return exchangeItem;
    }
}
}