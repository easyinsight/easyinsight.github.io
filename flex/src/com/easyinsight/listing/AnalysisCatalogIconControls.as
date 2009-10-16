package com.easyinsight.listing
{
	import com.easyinsight.LoginDialog;
	import com.easyinsight.framework.User;

import com.easyinsight.genredata.AnalyzeEvent;
    import com.easyinsight.solutions.InsightDescriptor;
    import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Alert;
	import mx.controls.Button;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class AnalysisCatalogIconControls extends HBox
	{
		[Embed(source="../../../../assets/media_play_green.png")]
        public var playIcon:Class;
        
        [Embed(source="../../../../assets/data_next.png")]
        public var subscribeIcon:Class;
        
        private var playButton:Button;
        private var subscribeButton:Button;
        
        private var insightDescriptor:InsightDescriptor;
		
		public function AnalysisCatalogIconControls()
		{
			super();
			playButton = new Button();
			playButton.setStyle("icon", playIcon);
			playButton.toolTip = "Analyze";
			addChild(playButton);
			playButton.addEventListener(MouseEvent.CLICK, analyze);
			subscribeButton = new Button();
			subscribeButton.setStyle("icon", subscribeIcon);
			subscribeButton.toolTip = "Add to My Data";			
			addChild(subscribeButton);
			subscribeButton.addEventListener(MouseEvent.CLICK, subscribe);			
			this.setStyle("paddingLeft", 5);
			this.setStyle("paddingRight", 5);
		}
		
		override public function set data(value:Object):void {
			this.insightDescriptor = value as InsightDescriptor;
			
		}
		
		override public function get data():Object {
			return this.insightDescriptor;
		}
		
		private function analyze(event:MouseEvent):void {
			dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(this.insightDescriptor)));
		}
		
		public function subscribe(event:MouseEvent):void {
			var user:User = User.getInstance();
			if (user == null) {
				var loginContainer:LoginDialog = LoginDialog(PopUpManager.createPopUp(this, LoginDialog, true));
			} else {
				var remoteService:RemoteObject = new RemoteObject();
				remoteService.destination = "insightDescriptor";
				remoteService.subscribeToAnalysis.addEventListener(ResultEvent.RESULT, successfulSubscription);
				remoteService.subscribeAnalysis.addEventListener(FaultEvent.FAULT, failedSubscription);
				remoteService.subscribeToAnalysis.send(insightDescriptor.id);
			}
		}
		
		private function successfulSubscription(event:ResultEvent):void {
			Alert.show("This insight will now appear on your My Data page.");
			subscribeButton.visible = false;
		}
		
		private function failedSubscription(event:FaultEvent):void {
			Alert.show(event.fault.message);
		}
	}
}