package com.easyinsight.analysis
{
	
	public class ListDropArea extends DropArea
	{
		
		public function ListDropArea()
		{
			super();
		}
		
		override public function getDropAreaType():String {
			return "List";
		}
		
		override protected function getNoDataLabel():String {
			return "Drop Field Here";
		}
	}
}