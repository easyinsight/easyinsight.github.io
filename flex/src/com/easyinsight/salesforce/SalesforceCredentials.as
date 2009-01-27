package com.easyinsight.salesforce
{
	import com.easyinsight.framework.Credentials;

	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.salesforce.SalesforceCredentials")]	
	public class SalesforceCredentials extends Credentials
	{
		public var securityToken:String;
		
		public function SalesforceCredentials()
		{
			super();
		}
		
	}
}