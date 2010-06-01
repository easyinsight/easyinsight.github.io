package com.easyinsight.report {
import com.easyinsight.framework.LoginEvent;

import com.easyinsight.genredata.AnalyzeEvent;
import com.easyinsight.reportpackage.ReportPackageResponse;

import flash.events.EventDispatcher;

import mx.controls.Alert;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DelayedPackageLink extends EventDispatcher {
    private var packageID:String;
    private var packageService:RemoteObject;

    public function DelayedPackageLink(packageID:String)
    {
        this.packageID = packageID;
        this.packageService = new RemoteObject();
        packageService.destination = "reportPackageService";
        packageService.openPackageIfPossible.addEventListener(ResultEvent.RESULT, gotFeed);
        packageService.openPackageIfPossible.addEventListener(FaultEvent.FAULT, fault);
    }

    private function fault(event:FaultEvent):void {
        Alert.show(event.fault.message);
    }

    public function execute():void {
        packageService.openPackageIfPossible.send(packageID);
    }

    private function gotFeed(event:ResultEvent):void {
        var packageResponse:ReportPackageResponse = packageService.openPackageIfPossible.lastResult as ReportPackageResponse;
        if (packageResponse.status == ReportPackageResponse.SUCCESS) {
            dispatchEvent(new AnalyzeEvent(new PackageAnalyzeSource(packageResponse.reportPackageDescriptor)));
        } else {
            // tried to access a data source they don't have rights to, silently fail
        }
    }

    private function delayedFeed(event:LoginEvent):void {
        packageService.openPackageIfPossible.send(packageID);
    }
}
}