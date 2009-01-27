package com.easyinsight.analysis
{
	public class AnalysisItemTypes
	{
		public static const MEASURE:int = 1;
		public static const DIMENSION:int = 2;
		public static const RANGE:int = 4;
		public static const DATE:int = 8;		
		public static const LIST:int = 16;
    	public static const CALCULATION:int = 32;
    	public static const TEXT:int = 128;
    	public static const LOOKUP_TABLE:int = 256;
    	public static const TEMPORAL_MEASURE:int = 512;
    	public static const COMPLEX_MEASURE:int = 1024;
        public static const HIERARCHY:int = 2048;
		
		public static const YEAR_LEVEL:int = 1;
		public static const MONTH_LEVEL:int = 2;
		public static const DAY_LEVEL:int = 3;
		public static const HOUR_LEVEL:int = 4;
		public static const MINUTE_LEVEL:int = 5;
		
		public function AnalysisItemTypes()
			{
			super();
		}

	}
}