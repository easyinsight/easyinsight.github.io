/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/9/11
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.genredata {
import com.easyinsight.dashboard.DashboardDescriptor;
import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.controls.Alert;

import mx.controls.LinkButton;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class SolutionNameRenderer extends LinkButton {

    private var exchangeItem:ExchangeItem;
    private var solutionService:RemoteObject;

    public function SolutionNameRenderer() {
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.determineDataSourceForEntity.addEventListener(ResultEvent.RESULT, gotMatchingDataSources);
        solutionService.installEntity.addEventListener(ResultEvent.RESULT, installedReport);
        addEventListener(MouseEvent.CLICK, viewReport);
        setStyle("textDecoration", "underline");
        setStyle("textAlign", "left");
    }

    private function viewReport(event:MouseEvent):void {
        ProgressAlert.alert(this, "Determining data source for install...", null, solutionService.determineDataSourceForEntity);
        solutionService.determineDataSourceForEntity.send(exchangeItem.descriptor);
    }

    private function gotMatchingDataSources(event:ResultEvent):void {
        var dataSources:ArrayCollection = solutionService.determineDataSourceForEntity.lastResult as ArrayCollection;
        var validDataSources:ArrayCollection = new ArrayCollection();
        for each (var ds:DataSourceDescriptor in dataSources) {
            if (ds.prebuilts == null || ds.prebuilts.length == 0) {
                validDataSources.addItem(ds);
            }
        }
        if (validDataSources.length == 0 && dataSources.length == 1) {
            Alert.show("You've already installed this prebuilt onto your data source.");
        } else if (validDataSources.length == 0 && dataSources.length > 1) {
            Alert.show("You've already installed this prebuilt onto all of your data sources.");
        } else if (dataSources.length == 0) {
            var window:NoSolutionInstalledWindow = new NoSolutionInstalledWindow();
            window.solution = exchangeItem.solutionID;
            window.addEventListener(ListingChangeEvent.LISTING_CHANGE, onListingEvent);
            PopUpManager.addPopUp(window, this, true);
            PopUpUtil.centerPopUp(window);
        } else if (validDataSources.length == 1) {
            var dsd:DataSourceDescriptor = dataSources.getItemAt(0) as DataSourceDescriptor;
            ProgressAlert.alert(this, "Installing...", null, solutionService.installEntity);
            solutionService.installEntity.send(exchangeItem.descriptor, dsd.id);
        } else {
            var dsWindow:DataSourceChoiceWindow = new DataSourceChoiceWindow();
            dsWindow.sources = validDataSources;
            dsWindow.addEventListener(DataSourceSelectionEvent.DATA_SOURCE_SELECTION, dataSourceChoice, false, 0, true);
            PopUpManager.addPopUp(dsWindow, this, true);
            PopUpUtil.centerPopUp(dsWindow);
        }
    }

    private function dataSourceChoice(event:DataSourceSelectionEvent):void {
        ProgressAlert.alert(this, "Installing...", null, solutionService.installEntity);
        solutionService.installEntity.send(exchangeItem.descriptor, event.dataSource.id);
    }

    private function installedReport(event:ResultEvent):void {
        var descriptor:EIDescriptor = solutionService.installEntity.lastResult as EIDescriptor;
        if (descriptor is InsightDescriptor) {
            var insightDescriptor:InsightDescriptor = descriptor as InsightDescriptor;
            dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor)));
        } else if (descriptor is DashboardDescriptor ){
            dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DASHBOARD_VIEW, {dashboardID: descriptor.id})));
        }
    }

    private function onListingEvent(event:ListingChangeEvent):void {
        dispatchEvent(event);
    }

    override public function set data(val:Object):void {
        exchangeItem = val as ExchangeItem;
        label = exchangeItem.name;
    }

    override public function get data():Object {
        return exchangeItem;
    }
}
}
