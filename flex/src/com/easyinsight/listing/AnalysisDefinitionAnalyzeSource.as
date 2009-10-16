package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.FullScreenPage;
import com.easyinsight.framework.DataService;
import com.easyinsight.solutions.InsightDescriptor;

import mx.collections.ArrayCollection;
import mx.managers.BrowserManager;

	public class AnalysisDefinitionAnalyzeSource implements AnalyzeSource
	{
		private var insightDescriptor:InsightDescriptor;
		private var admin:Boolean;
        private var filters:ArrayCollection;
		
		public function AnalysisDefinitionAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null) {
			this.insightDescriptor = insightDescriptor;
            this.filters = filters;
		}

        public function createAnalysisPopup():FullScreenPage {
            var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();
            dataAnalysisContainer.filterOverrides = filters;
            var dataService:DataService = new DataService();
            dataService.dataFeedID = insightDescriptor.dataFeedID;
            dataAnalysisContainer.dataService = dataService;
            BrowserManager.getInstance().setTitle("Easy Insight - " + insightDescriptor.name);
            dataAnalysisContainer.reportID = insightDescriptor.id;
            return dataAnalysisContainer;
        }
	}
}