package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.UserCapabilities")]
	public class UserCapabilities
	{
		public var feedRole:int;
		public var analysisRole:int;
		public var groupMember:Boolean;
		
		public function UserCapabilities()
		{
		}

	}
}