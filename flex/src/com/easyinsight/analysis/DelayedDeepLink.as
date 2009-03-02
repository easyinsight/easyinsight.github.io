package com.easyinsight.analysis
{
	import com.easyinsight.LoginDialog;
	import com.easyinsight.framework.LoginEvent;
	import com.easyinsight.genredata.AnalyzeEvent;
	import com.easyinsight.listing.AnalysisDefinitionAnalyzeSource;
	
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	
	import mx.core.Application;
	import mx.managers.PopUpManager;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	public class DelayedDeepLink extends EventDispatcher
	{
		private var analysisService:RemoteObject;
		private var analysisID:int;
		
		public function DelayedDeepLink(analysisID:int)
		{
			this.analysisID = analysisID;
			this.analysisService = new RemoteObject();
			analysisService.destination = "analysisDefinition";
			analysisService.openAnalysisIfPossible.addEventListener(ResultEvent.RESULT, gotAnalysisDefinition);		
		}
		
		public function execute():void {
			analysisService.openAnalysisIfPossible.send(analysisID);
		}

		private function gotAnalysisDefinition(event:ResultEvent):void {
        	var insightResponse:InsightResponse = analysisService.openAnalysisIfPossible.lastResult as InsightResponse;
        	if (insightResponse.successful) {
        		dispatchEvent(new AnalyzeEvent(new AnalysisDefinitionAnalyzeSource(insightResponse.definition)));	
        	} else {
        		var loginDialog:LoginDialog = LoginDialog(PopUpManager.createPopUp(Application.application as DisplayObject, LoginDialog, true));
        		loginDialog.addEventListener(LoginEvent.LOGIN, delayedAnalysis);
                PopUpManager.centerPopUp(loginDialog);
        	}        	            
        }  
        
        private function delayedAnalysis(event:LoginEvent):void {
        	analysisService.openAnalysisIfPossible.send(analysisID);
        }
	}
}