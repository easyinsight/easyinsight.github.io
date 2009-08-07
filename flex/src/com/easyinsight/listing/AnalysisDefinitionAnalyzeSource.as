package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.framework.DataService;
import com.easyinsight.framework.ModuleAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import flash.display.DisplayObject;

import mx.collections.ArrayCollection;
import mx.managers.BrowserManager;

	public class AnalysisDefinitionAnalyzeSource extends ModuleAnalyzeSource
	{
		private var insightDescriptor:InsightDescriptor;
		private var admin:Boolean;
        private var filters:ArrayCollection;
		
		public function AnalysisDefinitionAnalyzeSource(insightDescriptor:InsightDescriptor, filters:ArrayCollection = null) {
			this.insightDescriptor = insightDescriptor;
            this.filters = filters;
		}

        override public function createDirect():DisplayObject {
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