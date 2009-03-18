package com.easyinsight.listing
{
import com.easyinsight.administration.feed.FeedAdministrationContainer;
	
    import com.easyinsight.framework.ModuleAnalyzeSource;
import flash.display.DisplayObject;

	public class FeedAdminAnalyzeSource extends ModuleAnalyzeSource
	{
		private var feedID:int;
		
		public function FeedAdminAnalyzeSource(feedID:int) {
			this.feedID = feedID;
		}

        override public function createDirect():DisplayObject {
            var feedAdminContainer:FeedAdministrationContainer = new FeedAdministrationContainer();
            feedAdminContainer.feedID = feedID;
            return feedAdminContainer;
        }

        override protected function getModuleName():String {
            return "/app/easyui-debug/DataSourceAdminModule.swf";
        }

        override protected function applyProperties(val:Object):void {
            super.applyProperties(val);

        }

	}
}