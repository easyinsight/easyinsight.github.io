package com.easyinsight.filtering
{
	public interface IFilterDetailEditor
	{
		function makeUpdates():FilterDefinition;
		function set filterDefinition(filterDefinition:FilterDefinition):void;
        function set feedID(feedID:int):void;
        function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0,
                useWeakReference:Boolean = false):void;
	}
}