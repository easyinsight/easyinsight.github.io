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
import mx.graphics.ImageSnapshot;
import mx.graphics.codec.JPEGEncoder;
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
        var snapshot:ImageSnapshot = ImageSnapshot.captureImage(coreView, 0, new JPEGEncoder(85));
        var bytes:ByteArray = snapshot.data;
        ProgressAlert.alert(parent, "Generating the PDF...", null, upload.exportDashboardToPDF);
        upload.exportDashboardToPDF.send(dashboard, bytes, coreView.width, coreView.height);
    }
}
}