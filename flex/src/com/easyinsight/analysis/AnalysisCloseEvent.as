package com.easyinsight.analysis
{
import flash.display.DisplayObject;
import flash.events.Event;

	public class AnalysisCloseEvent extends Event
	{
		public static var ANALYSIS_CLOSE:String = "analysisClose";
		public var analysisDefinition:AnalysisDefinition;
		public var dataAnalysisContainer:DisplayObject;
        public var targetPerspective:String;
        public var properties:Object;
		
		public function AnalysisCloseEvent(dataAnalysisContainer:DisplayObject, analysisDefinition:AnalysisDefinition = null,
                targetPerspective:String = null, properties:Object = null)
		{
			super(ANALYSIS_CLOSE);
			this.dataAnalysisContainer = dataAnalysisContainer;
			this.analysisDefinition = analysisDefinition;
            this.targetPerspective = targetPerspective;
            this.properties = properties;
		}
		
		override public function clone():Event {
			return new AnalysisCloseEvent(this.dataAnalysisContainer, this.analysisDefinition, this.targetPerspective, this.properties);
		}
	}
}