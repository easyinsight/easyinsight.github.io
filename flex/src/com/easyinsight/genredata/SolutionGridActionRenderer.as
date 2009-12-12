package com.easyinsight.genredata {
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.report.PackageAnalyzeSource;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.reportpackage.ReportPackageDescriptor;
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

    private var exchangeItem:SolutionReportExchangeItem;

    private var runButton:Button;

    private var solutionService:RemoteObject;

    public function SolutionGridActionRenderer() {
        super();setStyle("horizontalAlign", "center");
        percentWidth = 100;
        runButton = new Button();
        runButton.setStyle("icon", playIcon);
        runButton.addEventListener(MouseEvent.CLICK, viewReport);
    }

    private function viewReport(event:MouseEvent):void {
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        if (exchangeItem.exchangeData is ExchangeReportData) {
            var exchangeReportData:ExchangeReportData = exchangeItem.exchangeData as ExchangeReportData;
            solutionService.determineDataSource.addEventListener(ResultEvent.RESULT, gotMatchingDataSources);
            solutionService.installReport.addEventListener(ResultEvent.RESULT, installedReport);
            solutionService.determineDataSource.send(exchangeReportData.dataSourceID);
        } else if (exchangeItem.exchangeData is ExchangePackageData) {
            var exchangePackageData:ExchangePackageData = exchangeItem.exchangeData as ExchangePackageData;
            solutionService.determineDataSourceForPackage.addEventListener(ResultEvent.RESULT, gotMatchingDataSourcesForPackage);
            solutionService.installPackage.addEventListener(ResultEvent.RESULT, installedPackage);
            solutionService.determineDataSourceForPackage.send(exchangePackageData.packageID);
        }

    }

    private function installedPackage(event:ResultEvent):void {
        var packageData:ExchangePackageData = exchangeItem.exchangeData as ExchangePackageData;
        var packageDescriptor:ReportPackageDescriptor = solutionService.installPackage.lastResult as ReportPackageDescriptor;
        // has to emit special property here to let us decide whether or not we want to keep this report
        dispatchEvent(new AnalyzeEvent(new PackageAnalyzeSource(packageDescriptor, true, packageData.packageID, 0)));
    }

    private function installedReport(event:ResultEvent):void {
        var insightDescriptor:InsightDescriptor = solutionService.installReport.lastResult as InsightDescriptor;
        // has to emit special property here to let us decide whether or not we want to keep this report
        dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor, null, true, 0, exchangeItem.id, exchangeItem.ratingAverage)));
    }

    private function onListingEvent(event:ListingChangeEvent):void {
        dispatchEvent(event);
    }

    private function gotMatchingDataSourcesForPackage(event:ResultEvent):void {
        var packageData:ExchangePackageData = exchangeItem.exchangeData as ExchangePackageData;
        var dataSources:ArrayCollection = solutionService.determineDataSourceForPackage.lastResult as ArrayCollection;
        if (dataSources.length == 0) {
            var window:NoSolutionInstalledWindow = new NoSolutionInstalledWindow();
            window.solution = exchangeItem.solutionID;
            window.addEventListener(ListingChangeEvent.LISTING_CHANGE, onListingEvent);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        } else if (dataSources.length == 1) {
            ProgressAlert.alert(this, "Getting the report ready...", null, solutionService.installPackage);
            solutionService.installPackage.send(packageData.packageID, dataSources.getItemAt(0).id);
        } else {
            // TODO: this is a hack for now
            ProgressAlert.alert(this, "Getting the report ready...", null, solutionService.installPackage);
            solutionService.installPackage.send(packageData.packageID, dataSources.getItemAt(0).id);
        }
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
            solutionService.installReport.send(exchangeItem.id, dataSources.getItemAt(0).id);
        } else {
            // TODO: this is a hack for now
            solutionService.installReport.send(exchangeItem.id, dataSources.getItemAt(0).id);
        }
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