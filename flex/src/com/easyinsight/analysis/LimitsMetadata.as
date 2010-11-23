package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.LimitsMetadata")]
	public class LimitsMetadata
	{
		public var top:Boolean;
		public var number:int;
		public var limitsMetadataID:int;
        public var limitEnabled:Boolean;
		
		public function LimitsMetadata()
		{
		}
	}
}