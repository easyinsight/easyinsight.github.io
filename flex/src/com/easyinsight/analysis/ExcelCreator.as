package com.easyinsight.analysis {
import com.easyinsight.framework.InsightRequestMetadata;
import com.easyinsight.util.ProgressAlert;

import flash.display.DisplayObject;

import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.controls.PopUpButton;
import mx.core.Application;

import mx.core.UIComponent;
import mx.managers.PopUpManager;

import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

import mx.core.UIComponent;

public class ExcelCreator {

    public function ExcelCreator() {
        upload = new RemoteObject();
        upload.destination = "exportService";
        upload.exportToExcel.addEventListener(ResultEvent.RESULT, gotExcelID);
        upload.exportToExcel2007.addEventListener(ResultEvent.RESULT, gotExcelID);
    }

    private var upload:RemoteObject;

    private var format:Boolean = false;

    private function gotExcelID(event:ResultEvent):void {
        var response:ExcelResponse;
        if (format) {
            response = upload.exportToExcel2007.lastResult as ExcelResponse;
        } else {
            response = upload.exportToExcel.lastResult as ExcelResponse;
        }


        if (response.reportFault) {
            var window:UIComponent = response.reportFault.createFaultWindow();
            PopUpManager.addPopUp(window, DisplayObject(Application.application), true);
        } else {
            var url:URLRequest = new URLRequest("/app/excel");
            navigateToURL(url, "_self");
        }
    }

    public function exportExcel(definition:AnalysisDefinition, parent:UIComponent, format2007:Boolean):void {
        this.format = format2007;
        var insightMetadata:InsightRequestMetadata = new InsightRequestMetadata();
        insightMetadata.utcOffset = new Date().getTimezoneOffset();
        if (format2007) {
            ProgressAlert.alert(parent, "Generating the Excel spreadsheet...", null, upload.exportToExcel2007);
            upload.exportToExcel2007.send(definition, insightMetadata);
        } else {
            ProgressAlert.alert(parent, "Generating the Excel spreadsheet...", null, upload.exportToExcel);
            upload.exportToExcel.send(definition, insightMetadata);
        }
    }
}
}