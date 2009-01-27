package com.easyinsight.administration.feed
{
	import com.easyinsight.groups.GroupDescriptor;
	
	import flash.events.Event;

	public class GroupLinkEvent extends Event
	{
		public static const DELETE_GROUP_LINK:String = "deleteGroupLink";
		public static const ADD_GROUP_LINK:String = "addGroupLink";
		
		public var groupDescriptor:GroupDescriptor;
		
		public function GroupLinkEvent(type:String, groupDescriptor:GroupDescriptor)
		{
			super(type);
			this.groupDescriptor = groupDescriptor;
		}
		
		override public function clone():Event {
			return new GroupLinkEvent(this.type, this.groupDescriptor);
		}
		
	}
}