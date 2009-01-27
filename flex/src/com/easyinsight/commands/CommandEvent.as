package com.easyinsight.commands
{
	import flash.events.Event;

	public class CommandEvent extends Event
	{
		public var command:ICommand;
		public static const COMMAND:String = "command";
		
		public function CommandEvent(command:ICommand)
		{
			super(COMMAND, true, true);
			this.command = command;
		}
		
		override public function clone():Event {
			return new CommandEvent(command);
		}
	}
}