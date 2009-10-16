package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.FullScreenPage;
import com.easyinsight.framework.DataService;

import mx.managers.BrowserManager;
import mx.rpc.remoting.RemoteObject;
	
	public class DescriptorAnalyzeSource implements AnalyzeSource
	{
        private var dataFeedID:int;
        private var name:String;
		private var feedService:RemoteObject;
		private var preview:Boolean;
		private var loginRequired:Boolean;
		
		public function DescriptorAnalyzeSource(dataFeedID:int, name:String)
		{
			this.dataFeedID = dataFeedID;
            this.name = name;
		}

        public function createAnalysisPopup():FullScreenPage {
            var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();
            var dataService:DataService = new DataService();
            dataService.dataFeedID = dataFeedID;
            dataAnalysisContainer.dataService = dataService;
            BrowserManager.getInstance().setTitle("Easy Insight - " + name);
            return dataAnalysisContainer;
        }
	}
}