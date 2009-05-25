package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterDefinition")]
	public class FilterDefinition
	{
		public static const VALUE:int = 1;
		public static const RANGE:int = 2;
		public static const DATE:int = 3;
		public static const ROLLING_DATE:int = 4;
		public static const LAST_N:int = 5;

		public var field:AnalysisItem;
		public var applyBeforeAggregation:Boolean = true;
        public var filterID:int;
		
		public function FilterDefinition()
			{
			super();
		}

		public function getType():int {
			return 0;
		}
	}
}