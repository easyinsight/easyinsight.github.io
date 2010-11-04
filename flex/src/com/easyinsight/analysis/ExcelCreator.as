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
        upload.exportReportIDToExcel.addEventListener(ResultEvent.RESULT, gotExcelByReportID);
    }

    private var upload:RemoteObject;
    private var report:AnalysisDefinition;

    private function gotExcelID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/excel");
        navigateToURL(url, "_blank");

    }

    private function gotExcelByReportID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/excel");
        navigateToURL(url, "_blank");
    }

    public function exportExcel(definition:AnalysisDefinition, parent:UIComponent):void {
        report = definition;
        var insightMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightMetadata.utcOffset = new Date().getTimezoneOffset();
        UserAudit.instance().audit(UserAudit.EXPORTED_TO_EXCEL);
        ProgressAlert.alert(parent, "Generating the Excel spreadsheet...", null, upload.exportToExcel);
        upload.exportToExcel.send(definition, insightMetadata);
    }
}
}