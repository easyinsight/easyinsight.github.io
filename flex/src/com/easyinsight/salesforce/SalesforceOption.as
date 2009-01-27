package com.easyinsight.salesforce
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.dataproviders.salesforce.SalesforceOption")]
	
	public class SalesforceOption
	{
		public var name:String;
		public var dataFeedID:int;
		
		public function SalesforceOption()
			{
			super();
		}

	}
}