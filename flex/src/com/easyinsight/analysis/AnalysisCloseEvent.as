package com.easyinsight.analysis
{
import flash.display.DisplayObject;
import flash.events.Event;

	public class AnalysisCloseEvent extends Event
	{
		public static var ANALYSIS_CLOSE:String = "analysisClose";
		public var analysisDefinition:AnalysisDefinition;
		public var dataAnalysisContainer:DisplayObject;
		
		public function AnalysisCloseEvent(dataAnalysisContainer:DisplayObject, analysisDefinition:AnalysisDefinition = null)
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