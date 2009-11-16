package com.easyinsight.analysis
{
    import mx.events.CloseEvent;
	import com.easyinsight.framework.User;
	import com.easyinsight.util.PNGEnc;
	
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.utils.ByteArray;
	
	import mx.controls.Alert;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class PNGCreator
	{
		private var upload:RemoteObject;
		private var fileRef:FileReference;
        private var pngID:int;
		
		public function PNGCreator()
		{
			upload = new RemoteObject();			
			upload.destination = "userUpload";
			upload.uploadPNG.addEventListener(ResultEvent.RESULT, gotPNGID);	
		}

        private function alertListener(event:CloseEvent):void {
            if (event.detail == Alert.OK) {
                var request:URLRequest = new URLRequest("/app/DownloadServlet");
                request.method = URLRequestMethod.GET;
                var vars:URLVariables = new URLVariables();

                if (User.getInstance() != null) {
                    vars.userName = new String(User.getInstance().userName);
                    vars.password = new String(User.getInstance().password);
                }
                vars.operation = new String(1);
                vars.fileID = new String(pngID);
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

                fileRef.download(request, "export" + pngID + ".png");
            }
        }
		
		private function gotPNGID(event:ResultEvent):void {
			pngID = upload.uploadPNG.lastResult as int;
            Alert.show("Click to start download of the image.", "Alert",
		                		Alert.OK | Alert.CANCEL, null, alertListener, null, Alert.CANCEL);

		}
		
		private function doEvent(event:Event):void {
			trace(event);
		}
		
		private function complete(event:Event):void {
			Alert.show("Image saved!");
		}

		public function draw(renderable:DisplayObject):void {			
			var bd:BitmapData = new BitmapData(renderable.width, renderable.height);
			bd.draw(renderable);
			var ba:ByteArray = PNGEnc.encode(bd);
			upload.uploadPNG.send(ba);
		}
	}
}