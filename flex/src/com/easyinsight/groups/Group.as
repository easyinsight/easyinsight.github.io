package com.easyinsight.groups
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.groups.Group")]
	public class Group
	{
		public var groupID:int;
		public var name:String;
		public var description:String;
		public var tags:ArrayCollection;
        public var groupUsers:ArrayCollection;
        public var urlKey:String;
        public var selected:Boolean;
		
		public function Group()
		{
		}

	}
}