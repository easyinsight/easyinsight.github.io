package com.easyinsight.filtering
{
	public interface IFilterDetailEditor
	{
		function makeUpdates():FilterDefinition;
		function set filterDefinition(filterDefinition:FilterDefinition):void;
	}
}