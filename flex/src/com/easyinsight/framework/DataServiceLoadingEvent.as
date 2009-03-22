package com.easyinsight.framework
{
    import flash.events.Event;

	public class DataServiceLoadingEvent extends Event
	{
		public static const LOADING_STARTED:String = "dataServiceLoadingStarted";
		public static const LOADING_STOPPED:String = "dataServiceLoadingStopped";
		
		public function DataServiceLoadingEvent(type:String)
		{
			super(type);
		}


        override public function clone():Event {
            return new DataServiceLoadingEvent(type);
        }
    }
}