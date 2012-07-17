package com.easyinsight.filtering
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterRangeDefinition")]
	public class FilterRangeDefinition extends FilterDefinition
	{
        public static const LESS_THAN:int = 1;
        public static const LESS_THAN_EQUAL_TO:int = 2;

		public var startValue:Number;
		public var startValueDefined:Boolean;
		public var endValue:Number;
		public var endValueDefined:Boolean;
        public var currentStartValue:Number;
        public var currentEndValue:Number;
        public var currentStartValueDefined:Boolean;
        public var currentEndValueDefined:Boolean;
        public var lowerOperator:int;
        public var upperOperator:int;


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