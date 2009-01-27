package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	public class DimensionItemEditor extends AnalysisItemEditor
	{
		public function DimensionItemEditor()
		{
			super();
		}
		
		override protected function getAggregationTypeObjects():ArrayCollection {
			return new ArrayCollection([ "Grouping", "Range", "Date", "List" ] );	
		}
	}
}