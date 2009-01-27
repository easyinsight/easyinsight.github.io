package com.easyinsight.analysis.scrub
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.scrubbing.DataScrub")]
	public class DataScrub
	{
		public var dataScrubID:Number;
		
		public function DataScrub()
		{
		}

		public function windowClass():Class {
			return null;
		}
	}
}