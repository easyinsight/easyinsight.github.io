package com.easyinsight.analysis.options
{
import analysis.AnalysisItem;

	import analysis.AnalysisDimension;
	
	public class GroupingOption extends AnalysisItemOption
	{
		public function GroupingOption() {
			super();
		}
		
		override protected function getLabelValue():String {
			return "Grouping";
		}
		
		override public function createAnalysisItem(key:String):AnalysisItem {
			var analysisDimension:AnalysisDimension = new AnalysisDimension();
			analysisDimension.key = key;
			analysisDimension.group = true;
			return analysisDimension; 
		}	
	}
}