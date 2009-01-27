package com.easyinsight.customupload.api
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.api.DynamicServiceDescriptor")]
	public class DynamicServiceDescriptor
	{
		public var feedID:int;
		public var feedName:String;
		public var wsdlURL:String;
		public var serviceID:int;
        public var dynamicServiceDefinition:DynamicServiceDefinition;
		
		public function DynamicServiceDescriptor()
		{
		}

	}
}