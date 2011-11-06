package com.easyinsight.analysis {
import com.adobe.images.JPGEncoder;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.framework.InsightRequestMetadata;

import com.easyinsight.util.ProgressAlert;

import flash.display.BitmapData;
import flash.display.DisplayObject;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.utils.ByteArray;

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

    private function gotExcelID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/pdf");
        navigateToURL(url, "_blank");
    }

    public function exportReportToPDF(dashboard:Dashboard, parent:UIComponent, coreView:DisplayObject):void {
        var jpgStream:ByteArray = null;
        var jpgSource:BitmapData = new BitmapData(coreView.width, coreView.height);
        jpgSource.draw(coreView);
        var jpgEncoder:JPGEncoder = new JPGEncoder(85);
        jpgStream = jpgEncoder.encode(jpgSource);
        ProgressAlert.alert(parent, "Generating the PDF...", null, upload.exportDashboardToPDF);
        upload.exportDashboardToPDF.send(dashboard, jpgStream, coreView.width, coreView.height);
    }
}
}