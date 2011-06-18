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
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.events.MouseEvent;

import mx.collections.ArrayCollection;

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

    override public function set data(val:Object):void {
        exchangeItem = val as ExchangeItem;
        label = exchangeItem.name;
    }

    override public function get data():Object {
        return exchangeItem;
    }
}
}
