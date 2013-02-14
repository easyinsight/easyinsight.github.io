package com.easyinsight.analysis {
import com.easyinsight.dashboard.Dashboard;

import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.collections.ArrayCollection;

import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DashboardPDFCreator {

    private var upload:RemoteObject;

    public function DashboardPDFCreator() {
        upload = new RemoteObject();
        upload.destination = "exportService";
        upload.exportDashboardToPDF.addEventListener(ResultEvent.RESULT, gotExcelID);
    }

    private static function gotExcelID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/pdf");
        navigateToURL(url, "_blank");
    }

    public function exportReportToPDF(dashboard:Dashboard, parent:UIComponent, coreView:DisplayObject, landscape:Boolean, headerObj:UIComponent = null):void {

        var pageList:ArrayCollection = MultiPagePDFCreator.exportReportToPDF(coreView, landscape, headerObj);

        ProgressAlert.alert(parent, "Generating the PDF...", null, upload.exportDashboardToPDF);
        upload.exportDashboardToPDF.send(dashboard, pageList, landscape);
    }
}
}