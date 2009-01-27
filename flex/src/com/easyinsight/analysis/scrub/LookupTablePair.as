package com.easyinsight.analysis.scrub
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.scrubbing.LookupTablePair")]
	public class LookupTablePair
	{
		public var pairID:int;
		public var sourceValue:String;
		public var replaceValue:String;
		
		public function LookupTablePair()
		{
		}

	}
}