package com.easyinsight.analysis
{
	import com.easyinsight.util.PNGEnc;
	
	import flash.display.BitmapData;
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.FileReference;
	import flash.utils.ByteArray;
	
	import mx.controls.Alert;
	
	public class PNGCreator
	{
		private var fileRef:FileReference;
		
		public function PNGCreator()
		{
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
            fileRef = new FileReference();
            fileRef.addEventListener(Event.CANCEL, doEvent);
            fileRef.addEventListener(Event.COMPLETE, complete);
            fileRef.addEventListener(Event.OPEN, doEvent);
            fileRef.addEventListener(Event.SELECT, doEvent);
            fileRef.addEventListener(HTTPStatusEvent.HTTP_STATUS, doEvent);
            fileRef.addEventListener(IOErrorEvent.IO_ERROR, doEvent);
            fileRef.addEventListener(ProgressEvent.PROGRESS, doEvent);
            fileRef.addEventListener(SecurityErrorEvent.SECURITY_ERROR, doEvent);
            fileRef.save(ba, "export.png");
		}
	}
}