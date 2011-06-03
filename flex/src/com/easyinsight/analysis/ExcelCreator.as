package com.easyinsight.analysis {
import com.easyinsight.framework.InsightRequestMetadata;
import com.easyinsight.util.ProgressAlert;
import com.easyinsight.util.UserAudit;

import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

import mx.core.UIComponent;

public class ExcelCreator {

    public function ExcelCreator() {
        upload = new RemoteObject();
        upload.destination = "exportService";
        upload.exportToExcel.addEventListener(ResultEvent.RESULT, gotExcelID);
    }

    private var upload:RemoteObject;

    private function gotExcelID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/excel");
        navigateToURL(url, "_blank");
    }

    public function exportExcel(definition:AnalysisDefinition, parent:UIComponent):void {
        var insightMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightMetadata.utcOffset = new Date().getTimezoneOffset();
        insightMetadata.reportEditor = true;
        ProgressAlert.alert(parent, "Generating the Excel spreadsheet...", null, upload.exportToExcel);
        upload.exportToExcel.send(definition, insightMetadata);
    }
}
}