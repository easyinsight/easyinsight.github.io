package com.easyinsight.solutions
{
	import com.easyinsight.framework.User;
	
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.MouseEvent;
	import flash.events.ProgressEvent;
	import flash.events.SecurityErrorEvent;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	
	import mx.containers.HBox;
	import mx.controls.Alert;
	import mx.controls.Button;

	public class InstalledSolutionIcons extends HBox
	{
		private var solution:Solution;
		
		[Embed(source="../../../../assets/gear.png")]
        public var archiveIcon:Class;
        
        private var archiveButton:Button;        
        private var uninstallButton:Button;        
        
        private var fileRef:FileReference; 
		
		public function InstalledSolutionIcons()
		{
			super();
			archiveButton = new Button();
			archiveButton.setStyle("icon", archiveIcon);
			archiveButton.toolTip = "Download Solution Files";
			archiveButton.addEventListener(MouseEvent.CLICK, archiveCalled);			
			addChild(archiveButton);
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);			
		}
		
		private function archiveCalled(event:MouseEvent):void {
			//fileReference = new FileReference();
			var request:URLRequest = new URLRequest("/DMS/DownloadServlet");
			request.method = URLRequestMethod.GET;
			var vars:URLVariables = new URLVariables();

            vars.userName = new String(User.getInstance().userName);
            vars.password = new String(User.getInstance().password);
            vars.operation = new String(2);
            vars.fileID = new String(solution.solutionID);           
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

            fileRef.download(request, solution.solutionArchiveName); 
		}
		
		private function doEvent(event:Event):void {
			trace(event);
		}
		
		private function complete(event:Event):void {
			Alert.show("Solution files copied!");
		}
		
		override public function set data(value:Object):void {
			this.solution = value as Solution;
			archiveButton.visible = solution.solutionArchiveName != null && solution.solutionArchiveName != "";
		}
		
		override public function get data():Object {
			return this.solution;
		}
	}
}