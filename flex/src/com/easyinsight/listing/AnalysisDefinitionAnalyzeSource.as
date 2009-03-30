package com.easyinsight.listing
{
import com.easyinsight.DataAnalysisContainer;
import com.easyinsight.framework.DataService;
import com.easyinsight.framework.ModuleAnalyzeSource;
import com.easyinsight.solutions.InsightDescriptor;
import flash.display.DisplayObject;
import mx.managers.BrowserManager;

	public class AnalysisDefinitionAnalyzeSource extends ModuleAnalyzeSource
	{
		private var insightDescriptor:InsightDescriptor;
		private var admin:Boolean;
		
		public function AnalysisDefinitionAnalyzeSource(insightDescriptor:InsightDescriptor, admin:Boolean = false) {
			this.insightDescriptor = insightDescriptor;
			this.admin = admin;
		}

        override public function createDirect():DisplayObject {
            var dataAnalysisContainer:DataAnalysisContainer = new DataAnalysisContainer();
            var dataService:DataService = new DataService();
            dataService.dataFeedID = insightDescriptor.dataFeedID;
            dataAnalysisContainer.dataService = dataService;
            BrowserManager.getInstance().setTitle("Easy Insight - " + insightDescriptor.name);
            dataAnalysisContainer.reportID = insightDescriptor.id;
            //dataAnalysisContainer.openAnalysis(analysisDefinition);
            return dataAnalysisContainer;
        }
	}
}