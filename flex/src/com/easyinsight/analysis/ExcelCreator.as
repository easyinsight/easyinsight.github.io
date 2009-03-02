package com.easyinsight.analysis {
public class ExcelCreator {
    import mx.events.CloseEvent;
	import com.easyinsight.framework.User;
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;

	import mx.controls.Alert;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

    private var upload:RemoteObject;
		private var fileRef:FileReference;
        private var excelID:int;

		public function ExcelCreator()
		{
			upload = new RemoteObject();
			upload.destination = "exportService";
			upload.exportToExcel.addEventListener(ResultEvent.RESULT, gotExcelID);
		}

        private function alertListener(event:CloseEvent):void {
            if (event.detail == Alert.OK) {
                var request:URLRequest = new URLRequest("/app/DownloadServlet");
                request.method = URLRequestMethod.GET;
                var vars:URLVariables = new URLVariables();

                vars.userName = new String(User.getInstance().userName);
                vars.password = new String(User.getInstance().password);
                vars.operation = new String(3);
                vars.fileID = new String(excelID);
                request.data = vars;

                fileRef = new FileReference();
                fileRef.addEventListener(Event.CANCEL, doEvent);
                fileRef.addEventListener(Event.COMPLETE, complete);
                fileRef.addEventListener(Event.OPEN, doEvent);
                fileRef.addEventListener(Event.SELECT, doEvent);
                fileRef.addEventListener(HTTPStatusEvent.HTTP_STATUS, doEvent);
                fileRef.addEventListener(IOErrorEvent.IO_ERROR, doEvent);
                fileRef.addEventListener(ProgressEvent.PROGRESS, doEvent);
                fileRef.addEventListener(SecurityErrorEvent.SECURITY_ERROR, doEvent);

                fileRef.download(request, "export" + excelID + ".xls");
            }
        }

		private function gotExcelID(event:ResultEvent):void {
			excelID = upload.exportToExcel.lastResult as int;
            Alert.show("Click to start download of the Excel spreadsheet.", "Alert",
		                		Alert.OK | Alert.CANCEL, null, alertListener, null, Alert.CANCEL);

		}

		private function doEvent(event:Event):void {
			trace(event);
		}

		private function complete(event:Event):void {
			Alert.show("Excel spreadsheet saved!");
		}

		public function exportExcel(definition:AnalysisDefinition):void {
			upload.exportToExcel.send(definition);
		}
	}
}