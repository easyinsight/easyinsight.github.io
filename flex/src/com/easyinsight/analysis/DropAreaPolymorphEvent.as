package com.easyinsight.analysis
{
	import flash.events.Event;

	public class DropAreaPolymorphEvent extends Event
	{
		public static const DROP_AREA_POLYMORPH:String = "dropAreaPolymorph";
		public var oldDropArea:DropArea;
		public var newDropArea:DropArea;
		
		public function DropAreaPolymorphEvent(oldDropArea:DropArea, newDropArea:DropArea)
		{
			super(DROP_AREA_POLYMORPH);
			this.oldDropArea = oldDropArea;
			this.newDropArea = newDropArea;
		}
		
	}
}