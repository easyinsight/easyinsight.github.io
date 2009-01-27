package com.easyinsight.analysis
{
	import com.easyinsight.FullScreenPage;
	
	import flash.events.Event;

	public class AnalysisCloseEvent extends Event
	{
		public static var ANALYSIS_CLOSE:String = "analysisClose";
		public var analysisDefinition:AnalysisDefinition;
		public var dataAnalysisContainer:FullScreenPage;
		
		public function AnalysisCloseEvent(dataAnalysisContainer:FullScreenPage, analysisDefinition:AnalysisDefinition = null)
		{
			super(ANALYSIS_CLOSE);
			this.dataAnalysisContainer = dataAnalysisContainer;
			this.analysisDefinition = analysisDefinition;
		}
		
		override public function clone():Event {
			return new AnalysisCloseEvent(this.dataAnalysisContainer, this.analysisDefinition);
		}
	}
}