package com.easyinsight.salesforce
{
	import com.easyinsight.listing.DataFeedDescriptor;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.salesforce.SalesforceCreationResponse")]
	
	public class SalesforceCreationResponse
	{
		public var successful:Boolean;
		public var failureMessage:String;
		public var descriptor:DataFeedDescriptor;
		
		public function SalesforceCreationResponse()
		{
		}

	}
}