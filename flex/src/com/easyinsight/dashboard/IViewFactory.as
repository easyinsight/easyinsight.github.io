package com.easyinsight.dashboard
{
	import com.easyinsight.analysis.AnalysisDefinition;
	
	import flash.display.DisplayObject;
	
	public interface IViewFactory
	{
		function createDisplay(analysisDefinition:AnalysisDefinition):DisplayObject;
	}
}