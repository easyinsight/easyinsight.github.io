package com.easyinsight.filtering
{
import mx.collections.ArrayCollection;

public interface IFilterDetailEditor
	{
		function makeUpdates():FilterDefinition;
		function set filterDefinition(filterDefinition:FilterDefinition):void;
        function set feedID(feedID:int):void;
        function set fields(value:ArrayCollection):void;
        function addEventListener(type:String, listener:Function, useCapture:Boolean = false, priority:int = 0,
                useWeakReference:Boolean = false):void;
	}
}