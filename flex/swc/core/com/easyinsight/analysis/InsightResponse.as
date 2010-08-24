package com.easyinsight.analysis
{
import com.easyinsight.solutions.InsightDescriptor;
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.InsightResponse")]
	public class InsightResponse
	{
        public static const SUCCESS:int = 1;
        public static const NEED_LOGIN:int = 2;
        public static const REJECTED:int = 3;
        
		public var status:int;
		public var insightDescriptor:InsightDescriptor;

		public function InsightResponse()
		{
		}

	}
}