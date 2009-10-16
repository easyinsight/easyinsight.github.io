package com.easyinsight.listing
{
import com.easyinsight.FullScreenPage;
import com.easyinsight.administration.feed.FeedAdministrationContainer;


public class FeedAdminAnalyzeSource implements AnalyzeSource
	{
		private var feedID:int;
		
		public function FeedAdminAnalyzeSource(feedID:int) {
			this.feedID = feedID;
		}

        public function createAnalysisPopup():FullScreenPage {
            var feedAdminContainer:FeedAdministrationContainer = new FeedAdministrationContainer();
            feedAdminContainer.feedID = feedID;
            return feedAdminContainer;
        }

	}
}