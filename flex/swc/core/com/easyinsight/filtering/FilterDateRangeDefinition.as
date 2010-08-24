package com.easyinsight.filtering
{
import com.easyinsight.analysis.AnalysisDateDimension;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterDateRangeDefinition")]
	public class FilterDateRangeDefinition extends FilterDefinition
	{
		public var startDate:Date;
		public var startDateDimension:AnalysisDateDimension;
        public var boundingStartDate:Date;
		public var endDate:Date;
		public var endDateDimension:AnalysisDateDimension;
        public var boundingEndDate:Date;
        public var sliding:Boolean;
		
		public function FilterDateRangeDefinition()
		{
			super();
		}
		
		override public function getType():int {
			return FilterDefinition.DATE;
		}
	}
}