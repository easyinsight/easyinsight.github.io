package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.datafeeds.CredentialsDefinition")]
	public class CredentialsDefinition
	{		
		public static const NO_CREDENTIALS:int = 0;
		public static const STANDARD_USERNAME_PW:int = 1;
		public static const SALESFORCE:int = 2;
		
		public var credentialsName:String;
		
		
		public function CredentialsDefinition()
		{
		}

	}
}