package com.easyinsight.collaboration
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.collaboration.CollaborationSession")]
	public class CollaborationSession
	{
		public var sessionName:String;
		public var host:String;
		public var participants:ArrayCollection;
		
		public function CollaborationSession()
		{
		}

	}
}