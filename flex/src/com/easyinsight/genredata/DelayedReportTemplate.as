package com.easyinsight.genredata {
import com.easyinsight.LoginDialog;
import com.easyinsight.framework.LoginEvent;
import com.easyinsight.framework.User;
import com.easyinsight.listing.ListingChangeEvent;
import com.easyinsight.report.ReportAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import com.easyinsight.util.PopUpUtil;
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;
import flash.events.EventDispatcher;

import mx.collections.ArrayCollection;
import mx.core.Application;
import mx.managers.PopUpManager;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedReportTemplate extends EventDispatcher {

    private var urlKey:String;
    private var info:ReportTemplateInfo;

    private var solutionService:RemoteObject;

    public function DelayedReportTemplate(urlKey:String) {
        this.urlKey = urlKey;
        solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.determineDataSourceForURLKey.addEventListener(ResultEvent.RESULT, onDetermine);
        solutionService.installReport.addEventListener(ResultEvent.RESULT, installedReport);
    }

    public function execute():void {
        if (User.getInstance == null) {
            var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
            loginDialog.addEventListener(LoginEvent.LOGIN, delayedLogin);
            PopUpUtil.centerPopUp(loginDialog);
        } else {
            solutionService.determineDataSourceForURLKey.send(urlKey);
        }
    }

    private function delayedLogin(event:LoginEvent):void {
        solutionService.determineDataSourceForURLKey.send(urlKey);
    }

    private function onDetermine(event:ResultEvent):void {
        info = solutionService.determineDataSourceForURLKey.lastResult as ReportTemplateInfo;
        var dataSources:ArrayCollection = info.dataSources;
        if (dataSources.length == 0) {
            var window:NoSolutionInstalledWindow = new NoSolutionInstalledWindow();
            window.solution = info.solutionID;
            window.addEventListener(ListingChangeEvent.LISTING_CHANGE, onListingEvent);
            PopUpManager.addPopUp(window, Application.application as DisplayObject, true);
            PopUpUtil.centerPopUp(window);
        } else if (dataSources.length == 1) {
            ProgressAlert.alert(Application.application as DisplayObject, "Preparing the report...", null, solutionService.installReport);
            solutionService.installReport.send(info.reportID, dataSources.getItemAt(0).id);
        } else {
            var dsWindow:DataSourceChoiceWindow = new DataSourceChoiceWindow();
            dsWindow.sources = dataSources;
            dsWindow.addEventListener(DataSourceSelectionEvent.DATA_SOURCE_SELECTION, dataSourceChoice, false, 0, true);
            PopUpManager.addPopUp(dsWindow, Application.application as DisplayObject, true);
            PopUpUtil.centerPopUp(dsWindow);
        }
    }

    private function onListingEvent(event:ListingChangeEvent):void {
        dispatchEvent(event);
    }

    private function dataSourceChoice(event:DataSourceSelectionEvent):void {
        ProgressAlert.alert(Application.application as DisplayObject, "Preparing the report...", null, solutionService.installReport);
        solutionService.installReport.send(info.reportID, event.dataSource.id);    
    }

    private function installedReport(event:ResultEvent):void {
        var insightDescriptor:InsightDescriptor = solutionService.installReport.lastResult as InsightDescriptor;
        // has to emit special property here to let us decide whether or not we want to keep this report
        dispatchEvent(new AnalyzeEvent(new ReportAnalyzeSource(insightDescriptor, null, true, 0, info.reportID, 0, urlKey)));
    }
}
}