package com.easyinsight.filtering
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterRangeDefinition")]
	public class FilterRangeDefinition extends FilterDefinition
	{
		public var startValue:Number;
		public var startValueDefined:Boolean;
		public var endValue:Number;
		public var endValueDefined:Boolean;
		
		public function FilterRangeDefinition()
		{
			super();
            applyBeforeAggregation = false;
		}
		
		override public function getType():int {
			return FilterDefinition.RANGE;
		}
	}
}