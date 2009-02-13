package com.easyinsight.listing
{
	public interface IPerspective
	{
		function gotFocus():void;
		
		function getDefaultAnalyzeState():Boolean;
		function isKeywordSearchInstant():Boolean;
	}
}