package com.easyinsight.analysis
{
	import com.easyinsight.framework.LoginEvent;

import com.easyinsight.framework.PerspectiveInfo;
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
	import flash.events.EventDispatcher;
	
	import mx.controls.Alert;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;

	public class DelayedFeedAdminLink extends EventDispatcher
	{
		private var feedID:String;
		private var feedService:RemoteObject;
		
		public function DelayedFeedAdminLink(feedID:String)
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
            if (User.getInstance().guestUser) return;
			feedService.openFeedIfPossible.send(feedID);
		}

		private function gotFeed(event:ResultEvent):void {
        	var feedResponse:FeedResponse = feedService.openFeedIfPossible.lastResult as FeedResponse;
        	if (feedResponse.status == FeedResponse.SUCCESS) {
        		//dispatchEvent(new AnalyzeEvent(new FeedAdminAnalyzeSource(feedResponse.feedDescriptor.dataFeedID)));
        		dispatchEvent(new AnalyzeEvent(new PerspectiveInfo(PerspectiveInfo.DATA_SOURCE_ADMIN, {feedID: feedResponse.feedDescriptor.id})));
            } else {
        		// tried to access a data source they don't have rights to, silently fail
        	}        	            
        }  
        
        private function delayedFeed(event:LoginEvent):void {
        	feedService.openFeedIfPossible.send(feedID);
        }
	}
}