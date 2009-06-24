package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.framework.DataService;
import com.easyinsight.framework.LoginEvent;
    import com.easyinsight.framework.ModuleAnalyzeSource;
    import com.easyinsight.framework.User;

import flash.display.DisplayObject;
import mx.managers.BrowserManager;
import mx.rpc.remoting.RemoteObject;
	
	public class DescriptorAnalyzeSource extends ModuleAnalyzeSource
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

        override public function createDirect():DisplayObject {
            var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();
            var dataService:DataService = new DataService();
            dataService.dataFeedID = dataFeedID;
            dataAnalysisContainer.dataService = dataService;
            BrowserManager.getInstance().setTitle("Easy Insight - " + name);
            return dataAnalysisContainer;
        }
	}
}