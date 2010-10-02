package com.easyinsight.etl
{
import com.easyinsight.framework.User;
import com.easyinsight.genredata.AnalyzeEvent;
	import flash.events.EventDispatcher;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class DelayedLookupTableLink extends EventDispatcher
	{
		private var feedService:RemoteObject;
		private var analysisID:String;
		
		public function DelayedLookupTableLink(analysisID:String)
		{
			this.analysisID = analysisID;
			this.feedService = new RemoteObject();
			feedService.destination = "feeds";
			feedService.openLookupTableIfPossible.addEventListener(ResultEvent.RESULT, gotLookupTable);
		}
		
		public function execute():void {
            if (User.getInstance().guestUser) return;
			feedService.openLookupTableIfPossible.send(analysisID);
		}

		private function gotLookupTable(event:ResultEvent):void {
        	var id:int = feedService.openLookupTableIfPossible.lastResult as int;
        	if (id > 0) {
        		dispatchEvent(new AnalyzeEvent(new LookupTableSource(id)));
        	} else {
                // silently fail, user trying to spoof an ID
            }
        }  
	}
}