package com.easyinsight.filtering
{
	import mx.collections.ArrayCollection;
	
	
	public interface IFilter
	{
		function set filterDefinition(filterDefinition:FilterDefinition):void;
		function get filterDefinition():FilterDefinition;
		function addEventListener(type:String, listener:Function, useCapture:Boolean=false, priority:int=0, useWeakReference:Boolean=false):void;
		function set analysisItems(analysisItems:ArrayCollection):void;
        function set filterEditable(editable:Boolean):void;
        function set loadingFromReport(loading:Boolean):void;
	}
}