package com.easyinsight.analysis
{
	
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