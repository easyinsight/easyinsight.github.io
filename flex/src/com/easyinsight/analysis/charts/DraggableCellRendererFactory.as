package com.easyinsight.analysis.charts
{
	import com.easyinsight.analysis.AnalysisItem;
	import com.easyinsight.analysis.AnalysisCellRendererFactory;
	import com.easyinsight.analysis.conditions.ConditionRenderer;

	public class DraggableCellRendererFactory extends AnalysisCellRendererFactory
	{
		public function DraggableCellRendererFactory(field:String, analysisItem:AnalysisItem, conditionRenderer:ConditionRenderer)
		{
			super(field, analysisItem, conditionRenderer);
		}
		
		
	}
}