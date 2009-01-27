package com.easyinsight.commands
{
	public interface ICommand
	{
		function execute():void;
		function undo():Boolean;		
	}
}