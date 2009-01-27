package com.easyinsight.filtering
{

	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterDateRangeDefinition")]
	public class FilterDateRangeDefinition extends FilterDefinition
	{
		public var startDate:Date;
		public var endDate:Date;
		
		public function FilterDateRangeDefinition()
		{
			super();
		}
		
		override public function getType():int {
			return FilterDefinition.DATE;
		}
	}
}