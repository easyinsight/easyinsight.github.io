package com.easyinsight.administration.feed
{
import com.easyinsight.analysis.ReportFault;
import com.easyinsight.framework.Credentials;

[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.CredentialsResponse")]
		
	public class CredentialsResponse
	{
		public var successful:Boolean;
		public var failureMessage:String;
        public var encryptedResponse:Credentials;
        public var dataSourceID:int;
        public var reportFault:ReportFault;
        public var callDataID:String;
		
		public function CredentialsResponse()
		{
		}

	}
}