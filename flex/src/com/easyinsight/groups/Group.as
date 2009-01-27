package com.easyinsight.groups
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.groups.Group")]
	public class Group
	{
		public var groupID:int;
		public var name:String;
		public var publiclyVisible:Boolean;
		public var publiclyJoinable:Boolean;
		public var description:String;
		public var tags:ArrayCollection;
        public var groupUsers:ArrayCollection;
		
		public function Group()
		{
		}

	}
}