/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/1/11
 * Time: 2:08 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.dashboard.DashboardReport;
import com.easyinsight.dashboard.IDashboardViewComponent;
import com.easyinsight.dashboard.SizeInfo;
import com.easyinsight.filtering.FilterDefinition;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

import spark.components.Group;

public class DashboardReportMobileComponent extends Group implements IDashboardViewComponent {

    public var dashboardReport:DashboardReport;

    public function DashboardReportMobileComponent() {
        this.percentHeight = 100;
        this.percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        createReportDisplay();
    }

    private var reportComponent:ReportComponent;

    private var filterMap:Object = new Object();

    private var loadingPopup:LoadingPopup = new LoadingPopup();

    private var report:AnalysisDefinition;

    private function createReportDisplay():void {
        reportComponent = new ReportComponent(dashboardReport.report, this);
        reportComponent.addEventListener(ReportComponentEvent.GOT_DATA, onData);
        var reportView:IReportView = reportComponent.getReportView();
        var component:UIComponent = reportView as UIComponent;
        component.percentHeight = 100;
        component.percentWidth = 100;
        addElement(component);
        //initialRetrieve();
    }

    private function onData(event:ReportComponentEvent):void {
        this.report = event.report;
        loadingPopup.close();
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (loadingPopup != null) {
            loadingPopup.x = this.width / 2 - loadingPopup.width / 2;
            loadingPopup.y = this.height / 2 - loadingPopup.height / 2;
        }
    }

    private function createAdditionalFilters(filterMap:Object):ArrayCollection {
        var filterColl:ArrayCollection = new ArrayCollection();
        for (var id:String in filterMap) {
            var filters:Object = filterMap[id];
            if (filters != null) {
                var filterList:ArrayCollection = filters as ArrayCollection;
                for each (var filter:FilterDefinition in filterList) {
                    filterColl.addItem(filter);
                }
            }
        }
        return filterColl;
    }

    public function refresh():void {
        retrievedDataOnce = true;
        trace("retrieving data on " + dashboardReport.report.name);
        var filters:ArrayCollection = createAdditionalFilters(filterMap);
        loadingPopup.open(this);
        reportComponent.retrieveData(filters);
    }

    private var retrievedDataOnce:Boolean;

    public function initialRetrieve():void {
        if (!retrievedDataOnce) {
            retrievedDataOnce = true;
            var filters:ArrayCollection = createAdditionalFilters(filterMap);
            loadingPopup.open(this);
            trace("initial retrieve  data on " + dashboardReport.report.name);
            reportComponent.retrieveData(filters);
        }
    }

    public function updateAdditionalFilters(filterMap:Object):void {
        trace("updating additional filters on " + dashboardReport.report.name);
        if (filterMap != null) {
            for (var id:String in filterMap) {
                var filters:Object = filterMap[id];
                if (filters != null) {
                    this.filterMap[id] = filters;
                }
            }
        }
    }

    public function reportCount():ArrayCollection {
        var reports:ArrayCollection = new ArrayCollection();
        reports.addItem(report);
        return reports;
    }

    public function obtainPreferredSizeInfo():SizeInfo {
        return null;
    }

    public function toggleFilters(showFilters:Boolean):void {
    }
}
}
