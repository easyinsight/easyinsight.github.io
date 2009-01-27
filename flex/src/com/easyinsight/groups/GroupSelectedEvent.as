package com.easyinsight.groups
{
	import flash.events.Event;

	public class GroupSelectedEvent extends Event
	{
		public static const GROUP_SELECTED:String = "groupSelected";
		public var groupID:int;
		
		public function GroupSelectedEvent(groupID:int)
		{
			super(GROUP_SELECTED, true);
			this.groupID = groupID;
		}
		
		override public function clone():Event {
			return new GroupSelectedEvent(groupID);
		}
	}
}