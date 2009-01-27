package com.easyinsight.collaboration
{
	import flash.events.Event;

	public class CollaborationSetupEvent extends Event
	{
		public static const START_SESSION:String = "startSession";
		
		public var sessionName:String;
		
		public function CollaborationSetupEvent(sessionName:String)
		{
			super(START_SESSION);
			this.sessionName = sessionName;
		}
		
	}
}