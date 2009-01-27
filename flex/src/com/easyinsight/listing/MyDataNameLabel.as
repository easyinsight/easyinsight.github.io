package com.easyinsight.listing
{
	import com.easyinsight.analysis.AnalysisDefinition;
	
	import mx.controls.advancedDataGridClasses.AdvancedDataGridItemRenderer;

	public class MyDataNameLabel extends AdvancedDataGridItemRenderer
	{
		private var object:Object;
		
		public function MyDataNameLabel()
		{
			super();
		}
		
		override public function set data(value:Object):void {
			this.object = value;
			if (value != null) {
				if (value is DataFeedDescriptor) {
					var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;				
					this.text = descriptor.name;
				} else {
					var analysisDefinition:AnalysisDefinition = value as AnalysisDefinition;
					this.text = analysisDefinition.name; 
				}
			}			
		}
		
		override public function get data():Object {
			return this.object;
		}		
	}
}