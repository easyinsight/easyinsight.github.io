package com.easyinsight.listing
{
	public interface IPerspective
	{
		function analyze():AnalyzeSource;
		function gotFocus():void;
		
		function getDefaultAnalyzeState():Boolean;
		function isKeywordSearchInstant():Boolean;
	}
}