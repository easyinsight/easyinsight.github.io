package com.easyinsight.analysis {
import com.adobe.images.JPGEncoder;
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

public class PDFCreator {

    private var upload:RemoteObject;

    public function PDFCreator() {
        upload = new RemoteObject();
        upload.destination = "exportService";
        upload.exportToPDF.addEventListener(ResultEvent.RESULT, gotExcelID);
    }

    private function gotExcelID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/pdf");
        navigateToURL(url, "_blank");
    }

    public function exportReportToPDF(report:AnalysisDefinition, parent:UIComponent, coreView:DisplayObject):void {
        var jpgStream:ByteArray = null;
        if (report.reportType == AnalysisDefinition.LIST ||
                report.reportType == AnalysisDefinition.TREE ||
                report.reportType == AnalysisDefinition.CROSSTAB) {
            // server will generate the actual PDF table
        } else {
            var jpgSource:BitmapData = new BitmapData(coreView.width, coreView.height);
            jpgSource.draw(coreView);
            var jpgEncoder:JPGEncoder = new JPGEncoder(85);
            jpgStream = jpgEncoder.encode(jpgSource);
        }
        var insightMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightMetadata.utcOffset = new Date().getTimezoneOffset();
        insightMetadata.reportEditor = true;
        ProgressAlert.alert(parent, "Generating the PDF...", null, upload.exportToPDF);
        upload.exportToPDF.send(report, insightMetadata, jpgStream, coreView.width, coreView.height);
    }
}
}