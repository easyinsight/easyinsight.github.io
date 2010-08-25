package com.easyinsight.framework
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.Credentials")]
	public class Credentials
	{
		public static const UNKNOWN:int = 1;
		public static const SUCCESSFUL:int = 2;
		public static const FAILED:int = 3;
		
		public var userName:String;
		public var password:String;
		public var failureState:int = UNKNOWN;
        public var encrypted:Boolean = false;
		
		public function Credentials()
			{
			super();
		}

	}
}