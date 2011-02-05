package com.easyinsight.groups
{
	import flash.events.Event;

	public class GroupSelectedEvent extends Event
	{
		public static const GROUP_SELECTED:String = "groupSelected";
		public static const GROUP_DELETE:String = "groupDelete";
		public var groupID:int;
		
		public function GroupSelectedEvent(type:String, groupID:int)
		{
			super(type, true);
			this.groupID = groupID;
		}
		
		override public function clone():Event {
			return new GroupSelectedEvent(type, groupID);
		}
	}
}