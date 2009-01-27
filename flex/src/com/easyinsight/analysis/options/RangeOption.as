package com.easyinsight.analysis.options
{
import analysis.AnalysisItem;
import analysis.AnalysisRangeDimension;

	public class RangeOption extends AnalysisItemOption
	{
		public function RangeOption()
		{
			super();
		}

        override protected function getLabelValue():String {
			return "Range";
		}

		override public function createAnalysisItem(key:String):AnalysisItem {
			var analysisDimension:AnalysisRangeDimension = new AnalysisRangeDimension();
			analysisDimension.key = key;
			analysisDimension.group = true;
			return analysisDimension;
		}
    }
}