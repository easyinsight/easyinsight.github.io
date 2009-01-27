package com.easyinsight.analysis
{
	import mx.collections.ArrayCollection;
	
	public class MeasureItemEditor extends AnalysisItemEditor
	{
		public function MeasureItemEditor()
		{
			super();
		}
		
		override protected function getAggregationTypeObjects():ArrayCollection {
			return new ArrayCollection([ "Sum", "Average", "Min", "Max","Count"] );	
		}
	}
}