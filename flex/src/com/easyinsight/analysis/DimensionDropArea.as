package com.easyinsight.analysis
{
	import com.easyinsight.analysis.options.DateOption;
	import com.easyinsight.analysis.options.GroupingOption;
	import com.easyinsight.analysis.options.RangeOption;
	
	import mx.collections.ArrayCollection;
	
	public class DimensionDropArea extends DropArea
	{
		public function DimensionDropArea()
		{
			super();
		}
		
		override public function getDropAreaType():String {
			return "Dimension";
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Dimension Here";
		}
		
		override public function getItemEditorClass():Class {
			return DimensionItemEditor;
		}
	}
}