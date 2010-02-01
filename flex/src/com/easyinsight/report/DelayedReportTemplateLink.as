package com.easyinsight.report {
import com.easyinsight.LoginDialog;
import com.easyinsight.framework.LoginEvent;

import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.reportpackage.ReportPackageResponse;
import com.easyinsight.util.PopUpUtil;

import flash.display.DisplayObject;

import flash.events.EventDispatcher;

import mx.controls.Alert;
import mx.core.Application;
import mx.managers.PopUpManager;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedReportTemplateLink extends EventDispatcher {
    private var reportID:int;
    private var solutionService:RemoteObject;

    public function DelayedReportTemplateLink(reportID:int)
    {
        this.reportID = reportID;
        this.solutionService = new RemoteObject();
        solutionService.destination = "solutionService";
        solutionService.getStaticReport.addEventListener(ResultEvent.RESULT, gotFeed);
        solutionService.getStaticReport.addEventListener(FaultEvent.FAULT, fault);
    }

    private function fault(event:FaultEvent):void {
        Alert.show(event.fault.message);
    }

    public function execute():void {
        solutionService.getStaticReport.send(reportID);
    }

    private function gotFeed(event:ResultEvent):void {
        var staticReport:StaticReport = solutionService.getStaticReport.lastResult as StaticReport;
        dispatchEvent(new StaticReportSource())
        if (reportTemplate.status == ReportPackageResponse.SUCCESS) {
            dispatchEvent(new AnalyzeEvent(new PackageAnalyzeSource(reportTemplate.reportPackageDescriptor)));
        } else if (reportTemplate.status == ReportPackageResponse.NEED_LOGIN) {
            var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
            loginDialog.addEventListener(LoginEvent.LOGIN, delayedFeed);
            PopUpUtil.centerPopUp(loginDialog);
        } else {
            // tried to access a data source they don't have rights to, silently fail
        }
    }

    private function delayedFeed(event:LoginEvent):void {
        solutionService.getStaticReport.send(reportID);
    }
}
}