package com.easyinsight.commands
{
	import mx.collections.ArrayCollection;
	
	public class CommandStack
	{
		private var commandStack:ArrayCollection = new ArrayCollection();
		
		public function CommandStack()
			{
			super();
		}

		public function addCommand(command:Command):void {
			command.execute();
		}
		
		public function undo():void {
			var lastCommand:Command = commandStack.getItemAt(commandStack.length - 1) as Command;
			lastCommand.undo();
		}
		
		public function redo():void {
			
		}
	}
}