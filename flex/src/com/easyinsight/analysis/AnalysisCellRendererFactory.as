package com.easyinsight.analysis
{
	import com.easyinsight.analysis.conditions.ConditionRenderer;
	
	import mx.core.IFactory;
	import mx.formatters.Formatter;

	public class AnalysisCellRendererFactory implements IFactory
	{
		private var field:String;
		private var analysisItem:AnalysisItem;
		private var formatter:Formatter;
		private var renderer:ConditionRenderer;		
		
		public function AnalysisCellRendererFactory(field:String, analysisItem:AnalysisItem, conditionRenderer:ConditionRenderer) {
			super();
			this.field = field;
			this.analysisItem = analysisItem;
			this.formatter = analysisItem.getFormatter();
			this.renderer = conditionRenderer;
		}

		public function newInstance():*
		{
			return new AnalysisCellRenderer(field, analysisItem, formatter, renderer);
		}
		
	}
}