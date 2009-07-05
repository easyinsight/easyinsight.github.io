package com.easyinsight.filtering
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.RollingFilterDefinition")]
	public class RollingDateRangeFilterDefinition extends FilterDefinition
	{
		public static const DAY:int = 1;
		public static const WEEK:int = 2;
		public static const MONTH:int = 3;
		public static const QUARTER:int = 4;
		public static const YEAR:int = 5;
		public static const DAY_TO_NOW:int = 6;
		public static const WEEK_TO_NOW:int = 7;
		public static const MONTH_TO_NOW:int = 8;
		public static const QUARTER_TO_NOW:int = 9;
		public static const YEAR_TO_NOW:int = 10;        
		public static const LAST_DAY:int = 11;        

		public var interval:int = DAY;
		
		public function RollingDateRangeFilterDefinition()
		{
			super();
		}
		
		override public function getType():int {
			return FilterDefinition.ROLLING_DATE;
		}
	}
}