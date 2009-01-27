package com.easyinsight.collaboration
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.collaboration.Participant")]
	public class Participant
	{
		public var accountID:int;
		public var name:String;
		public var role:int;
		
		public function Participant()
		{
		}

	}
}