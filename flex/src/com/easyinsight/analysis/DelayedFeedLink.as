package com.easyinsight.analysis
{
	import com.easyinsight.LoginDialog;
	import com.easyinsight.framework.LoginEvent;
	import com.easyinsight.genredata.AnalyzeEvent;
	import com.easyinsight.listing.DescriptorAnalyzeSource;
	
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	
	import mx.controls.Alert;
	import mx.core.Application;
	import mx.managers.PopUpManager;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class DelayedFeedLink extends EventDispatcher
	{
		private var feedID:int;
		private var feedService:RemoteObject;
		
		public function DelayedFeedLink(feedID:int)
		{
			this.feedID = feedID;
			this.feedService = new RemoteObject();
			feedService.destination = "feeds";
			feedService.openFeedIfPossible.addEventListener(ResultEvent.RESULT, gotFeed);
			feedService.openFeedIfPossible.addEventListener(FaultEvent.FAULT, fault);
		}
		
		private function fault(event:FaultEvent):void {
			Alert.show(event.fault.message);
		}
		
		public function execute():void {
			feedService.openFeedIfPossible.send(feedID);
		}

		private function gotFeed(event:ResultEvent):void {
        	var feedResponse:FeedResponse = feedService.openFeedIfPossible.lastResult as FeedResponse;
        	if (feedResponse.successful) {
        		dispatchEvent(new AnalyzeEvent(new DescriptorAnalyzeSource(feedResponse.feedDescriptor)));	
        	} else {
        		var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
        		loginDialog.addEventListener(LoginEvent.LOGIN, delayedFeed);
                PopUpManager.centerPopUp(loginDialog);
        	}        	            
        }  
        
        private function delayedFeed(event:LoginEvent):void {
        	feedService.openFeedIfPossible.send(feedID);
        }
	}
}