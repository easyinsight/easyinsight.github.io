package com.easyinsight.filtering
{
	import mx.collections.ArrayCollection;
	
	
	public interface IEmbeddedFilter
	{
		function set filterDefinition(filterDefinition:FilterDefinition):void;
		function get filterDefinition():FilterDefinition;
		function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void;		
        function set showLabel(show:Boolean):void;
	}
}