package com.easyinsight.administration.feed
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.CredentialsResponse")]
		
	public class CredentialsResponse
	{
		public var successful:Boolean;
		public var failureMessage:String;
		
		public function CredentialsResponse()
		{
		}

	}
}