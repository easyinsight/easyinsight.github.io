package com.easyinsight.groups
{
	import com.easyinsight.administration.feed.FeedConsumer;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.groups.GroupDescriptor")]
	public class GroupDescriptor extends FeedConsumer
	{
		public var groupID:int;
        public var description:String;
        public var groupMembers:int;
		
		public function GroupDescriptor()
		{
		}


        public function get displayName():String {
            return name;
        }
	}
}