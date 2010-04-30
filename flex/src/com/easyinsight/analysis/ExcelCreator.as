package com.easyinsight.analysis {
import com.easyinsight.framework.CredentialsCache;
import com.easyinsight.framework.InsightRequestMetadata;
import com.easyinsight.util.ProgressAlert;

import flash.utils.ByteArray;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;

public class ExcelCreator {
    import mx.events.CloseEvent;

	import flash.events.Event;

	import flash.net.FileReference;
	

	import mx.controls.Alert;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

    private var upload:RemoteObject;
		private var fileRef:FileReference;
        private var excelID:int;
        private var report:AnalysisDefinition;

    private var excelData:ByteArray;

		public function ExcelCreator()
		{
			upload = new RemoteObject();
			upload.destination = "exportService";
			upload.exportToExcel.addEventListener(ResultEvent.RESULT, gotExcelID);
			upload.exportReportIDToExcel.addEventListener(ResultEvent.RESULT, gotExcelByReportID);
		}

        private function alertListener(event:CloseEvent):void {
            if (event.detail == Alert.OK) {
                fileRef = new FileReference();
                fileRef.addEventListener(Event.COMPLETE, complete);
                fileRef.save(excelData, "export" + excelID + ".xls");
            }
        }

		private function gotExcelID(event:ResultEvent):void {
			excelData = upload.exportToExcel.lastResult as ByteArray;
            var msg:String = "Click to start download of the Excel spreadsheet.";
            if(report.reportType != AnalysisDefinition.LIST) {
                msg += " The report will be saved in a list format.";
            }
            Alert.show(msg, "Alert", Alert.OK | Alert.CANCEL, null, alertListener, null, Alert.CANCEL);

		}

        private function gotExcelByReportID(event:ResultEvent):void {
			excelData = upload.exportReportIDToExcel.lastResult as ByteArray;
            var msg:String = "Click to start download of the Excel spreadsheet.";
            Alert.show(msg, "Alert", Alert.OK | Alert.CANCEL, null, alertListener, null, Alert.CANCEL);
		}

		private function complete(event:Event):void {
			Alert.show("Excel spreadsheet saved!");
		}

		public function exportExcel(definition:AnalysisDefinition, parent:UIComponent):void {
            report = definition;
            var insightMetadata:InsightRequestMetadata = new InsightRequestMetadata();
            insightMetadata.credentialFulfillmentList = CredentialsCache.getCache().createCredentials();
            ProgressAlert.alert(parent, "Generating the Excel spreadsheet...", null, upload.exportToExcel);
			upload.exportToExcel.send(definition, insightMetadata);
		}

        public function exportReportIDToExcel(reportID:int, filters:ArrayCollection, hierarchies:ArrayCollection, parent:UIComponent):void {
            var insightMetadata:InsightRequestMetadata = new InsightRequestMetadata();
            insightMetadata.credentialFulfillmentList = CredentialsCache.getCache().createCredentials();
            ProgressAlert.alert(parent, "Generating the Excel spreadsheet...", null, upload.exportReportIDToExcel);
            upload.exportReportIDToExcel.send(reportID, filters, hierarchies, insightMetadata);    
        }
	}
}