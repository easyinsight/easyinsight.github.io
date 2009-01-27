package com.easyinsight.analysis
{
	import flash.events.Event;

	public class AnalysisDefinitionLoadedEvent extends Event
	{
		public static const DEFINITION_LOADED:String = "analysisDefinitionLoaded";
		private var analysisDefinition:AnalysisDefinition;
		
		public function AnalysisDefinitionLoadedEvent(analysisDefinition:AnalysisDefinition)
		{
			super(DEFINITION_LOADED);
			this.analysisDefinition = analysisDefinition;
		}
		
		public function getAnalysisDefinition():AnalysisDefinition {
			return analysisDefinition;
		}
	}
}