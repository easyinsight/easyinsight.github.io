package com.easyinsight.solutions
{
import flash.events.Event;
import flash.events.HTTPStatusEvent;
import flash.events.IOErrorEvent;
import flash.events.SecurityErrorEvent;
import flash.events.ProgressEvent;
import mx.controls.Alert;
import com.easyinsight.framework.User;
import flash.net.URLRequestMethod;
import flash.net.URLVariables;
import flash.net.URLRequest;
import flash.net.FileReference;
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.managers.PopUpManager;
	import mx.rpc.remoting.RemoteObject;

	public class AvailableSolutionIcons extends HBox
	{
		private var solution:Solution;
		
        [Embed(source="../../../../assets/component_blue_add.png")]
        private var installIcon:Class;

        [Embed(source="../../../../assets/gear.png")]
        public var archiveIcon:Class;

        private var archiveButton:Button;
        
        private var installButton:Button;
        private var solutionService:RemoteObject;

        private var fileRef:FileReference;
		
		public function AvailableSolutionIcons()
		{
			super();
			installButton = new Button();
			installButton.setStyle("icon", installIcon);
			installButton.toolTip = "Install";
			installButton.addEventListener(MouseEvent.CLICK, installCalled);
			addChild(installButton);
            archiveButton = new Button();
			archiveButton.setStyle("icon", archiveIcon);
			archiveButton.toolTip = "Download Solution Files";
			archiveButton.addEventListener(MouseEvent.CLICK, archiveCalled);
			addChild(archiveButton);
			setStyle("horizontalAlign", "center");
            this.percentWidth = 100;
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
		
		private function documentCalled(event:MouseEvent):void {
			// This solution contains the following:
			// 5 
			// 2 prebuilt data feeds
			// 25 prebuilt insights
			//  
		}
		
		private function installCalled(event:MouseEvent):void {
			var window:SolutionInstallationWindow = SolutionInstallationWindow(PopUpManager.createPopUp(this.parent, SolutionInstallationWindow, true));
			window.solution = this.solution;
			PopUpManager.centerPopUp(window);			
		}

		override public function set data(value:Object):void {
			this.solution = value as Solution;
            archiveButton.visible = solution.solutionArchiveName != null && solution.solutionArchiveName != "";
            installButton.visible = solution.installable;
		}
		
		override public function get data():Object {
			return this.solution;
		}		
	}
}