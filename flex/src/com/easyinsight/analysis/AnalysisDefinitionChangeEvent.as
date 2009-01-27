package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisDefinitionChangeEvent extends Event
	{
		public static const ANALYSIS_DEFINITION_CHANGE:String = "analysisDefinitionChange";
		
		public var analysisDefinition:AnalysisDefinition;
		
		public function AnalysisDefinitionChangeEvent(analysisDefinition:AnalysisDefinition)
		{
			super(ANALYSIS_DEFINITION_CHANGE);
			this.analysisDefinition = analysisDefinition;
		}
		
		override public function clone():Event {
			return new AnalysisDefinitionChangeEvent(analysisDefinition);
		}
	}
}