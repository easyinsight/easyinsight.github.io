package com.easyinsight.customupload.api
{

	
	import flash.events.Event;

	public class ServiceDefinitionEvent extends Event
	{
		public static const SERVICE_DEFINED:String = "serviceDefined";
		public static const SERVICE_REMOVED:String = "serviceRemoved";
		
		public var descriptor:DataFeedDescriptor;		
		
		public function ServiceDefinitionEvent(type:String, descriptor:DataFeedDescriptor)
		{
			super(type, true);
			this.descriptor = descriptor;
		}
		
		override public function clone():Event {
			return new ServiceDefinitionEvent(type, descriptor);
		}
	}
}