package com.easyinsight.analysis
{
	import com.easyinsight.analysis.options.AverageOption;
	import com.easyinsight.analysis.options.CountOption;
	import com.easyinsight.analysis.options.MaxOption;
	import com.easyinsight.analysis.options.MinOption;
	import com.easyinsight.analysis.options.SumOption;
	
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	
	public class MeasureDropArea extends DropArea
	{
		private var aggregationBox:ComboBox;
		
		public function MeasureDropArea()
		{
			super();		
		}
		
		override public function getDropAreaType():String {
			return "Measure";
		}
		
		override public function getItemEditorClass():Class {
			return MeasureItemEditor;
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Measure Here";
		}
	}
}