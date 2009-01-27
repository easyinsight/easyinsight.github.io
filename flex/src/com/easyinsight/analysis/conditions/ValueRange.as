package com.easyinsight.analysis.conditions
{
	import mx.collections.ArrayCollection;
	
	public interface ValueRange
	{
		function addValue(object:Object):void;
		function getRangeValue(dataPoint:Number):Number;
	}
}