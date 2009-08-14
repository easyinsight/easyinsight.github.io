package com.easyinsight.administration.feed
{
import com.easyinsight.framework.Credentials;

[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.CredentialsResponse")]
		
	public class CredentialsResponse
	{
		public var successful:Boolean;
		public var failureMessage:String;
        public var encryptedResponse:Credentials;
		
		public function CredentialsResponse()
		{
		}

	}
}