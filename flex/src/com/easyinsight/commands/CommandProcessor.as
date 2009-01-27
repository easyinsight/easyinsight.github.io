package com.easyinsight.commands
{
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	
	public class CommandProcessor
	{
		private var commandStack:IList = new ArrayCollection();
		private var redoStack:IList = new ArrayCollection();
		
		public function CommandProcessor() {	
		}

		public function addCommand(command:ICommand):void {
			redoStack.removeAll();
			commandStack.addItem(command);
			command.execute();			
		}
		
		public function undo():void {
			if (commandStack.length > 0) {
				var command:ICommand = commandStack.removeItemAt(commandStack.length - 1) as ICommand;
				var undoPossible:Boolean = command.undo();
				if (!undoPossible) {
					// alert the user that they *can't* undo at this point
				} else {
					redoStack.addItem(command);
				}
			}
		}
		
		public function redo():void {
			if (redoStack.length > 0) {
				var command:ICommand = redoStack.removeItemAt(redoStack.length - 1) as ICommand;
                // add it back to command stack...
                commandStack.addItem(command);
				command.execute();	
			}
		}
	}
}