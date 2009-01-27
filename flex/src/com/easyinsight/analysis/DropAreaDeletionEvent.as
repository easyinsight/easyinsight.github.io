package com.easyinsight.analysis
{
	import flash.events.Event;

	public class DropAreaDeletionEvent extends Event
	{
		public static const DROP_AREA_DELETE:String = "dropAreaDelete";
		public var dropArea:DropArea;
		
		public function DropAreaDeletionEvent(dropArea:DropArea)
		{
			super(DROP_AREA_DELETE);
			this.dropArea = dropArea;
		}
		
	}
}