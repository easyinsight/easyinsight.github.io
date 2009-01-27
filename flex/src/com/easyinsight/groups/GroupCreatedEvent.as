package com.easyinsight.groups
{
	import flash.events.Event;

	public class GroupCreatedEvent extends Event
	{
		public static const GROUP_CREATED:String = "groupCreated";
		public static const GROUP_UPDATED:String = "groupUpdated";
		public static const GROUP_DELETED:String = "groupDeleted";		
		
		public function GroupCreatedEvent(type:String)
		{
			super(type);
		}
		
		override public function clone():Event {
			return new GroupCreatedEvent(type);
		}
	}
}