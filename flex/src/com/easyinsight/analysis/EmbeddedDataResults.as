package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.EmbeddedDataResults")]
	public class EmbeddedDataResults
	{
		public var headers:Array;
		public var rows:Array;
        public var definition:AnalysisDefinition;

		public function EmbeddedDataResults()
			{
			super();
		}

	}
}