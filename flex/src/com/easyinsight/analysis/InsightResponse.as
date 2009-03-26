package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightResponse")]
	public class InsightResponse
	{
        public static const SUCCESS:int = 1;
        public static const NEED_LOGIN:int = 2;
        public static const REJECTED:int = 3;
        
		public var status:int;
		public var definition:AnalysisDefinition;
		
		public function InsightResponse()
		{
		}

	}
}