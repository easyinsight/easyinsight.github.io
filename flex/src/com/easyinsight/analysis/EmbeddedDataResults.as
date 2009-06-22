package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedDataResults")]
	public class EmbeddedDataResults
	{
		public var headers:Array;
		public var rows:Array;
        public var definition:AnalysisDefinition;
        public var dataSourceAccessible:Boolean;
        public var lastDataTime:Date;
        public var attribution:String;

		public function EmbeddedDataResults()
			{
			super();
		}

	}
}