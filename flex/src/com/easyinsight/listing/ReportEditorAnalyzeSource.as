package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.FullScreenPage;
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.framework.DataService;
import com.easyinsight.solutions.InsightDescriptor;

import mx.collections.ArrayCollection;
import mx.managers.BrowserManager;

	public class ReportEditorAnalyzeSource implements AnalyzeSource
	{
		private var report:AnalysisDefinition;
        public var tutorialMode:int;
		
		public function ReportEditorAnalyzeSource(report:AnalysisDefinition, tutorialMode:int) {
			this.report = report;
            this.tutorialMode = tutorialMode;
		}

        public function createAnalysisPopup():FullScreenPage {
            var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();
            var dataService:DataService = new DataService();
            dataService.dataFeedID = report.dataFeedID;
            dataAnalysisContainer.tutorialOnStart = tutorialMode;
            dataAnalysisContainer.dataService = dataService;
            BrowserManager.getInstance().setTitle("Easy Insight - " + report.name);
            dataAnalysisContainer.analysisDefinition = report;
            return dataAnalysisContainer;
        }
	}
}