package com.easyinsight.analysis
{
	import commands.ICommand;

	public class AggregationTypeChangedCommand implements ICommand
	{
		private var dropArea:DropArea;
		private var previousAggregationType:String;
		private var newAggregationType:String;
		
		public function AggregationTypeChangedCommand(dropArea:DropArea, previousAggregationType:String, 
			newAggregationType:String) {
			this.dropArea = dropArea;
			this.previousAggregationType = previousAggregationType;
			this.newAggregationType = newAggregationType;				
		}

		public function execute():void {
			this.dropArea.aggregationType = this.newAggregationType;
			this.dropArea.dispatchEvent(new DropAreaUpdateEvent());
		}
		
		public function undo():Boolean {
			this.dropArea.aggregationType = this.previousAggregationType;
			this.dropArea.dispatchEvent(new DropAreaUpdateEvent());
			return true;
		}
		
	}
}