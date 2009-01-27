package com.easyinsight.analysis
{
	import com.easyinsight.commands.ICommand;

	public class DragAreaDeleteCommand implements ICommand
	{
		private var dropArea:DropArea;
		private var dropAreaGrouping:ListDropAreaGrouping;
		private var dropAreaGroupingState:Array;
		
		public function DragAreaDeleteCommand(dropArea:DropArea) {
			this.dropArea = dropArea;
			this.dropAreaGrouping = dropArea.parent as ListDropAreaGrouping;
			this.dropAreaGroupingState = dropAreaGrouping.getChildrenState();
		}

		public function execute():void {
			dropArea.deletion();			
		}
		
		public function undo():Boolean {
			// need the drop area grouping here...
			this.dropAreaGrouping.applyChildState(dropAreaGroupingState);
			this.dropArea.dispatchEvent(new DropAreaUpdateEvent(dropArea.analysisItem));
			return true;
		}
		
	}
}