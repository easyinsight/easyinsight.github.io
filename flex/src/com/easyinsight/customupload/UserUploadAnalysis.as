package com.easyinsight.customupload
{
	import mx.collections.ArrayCollection;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.userupload.UserUploadAnalysis")]
	public class UserUploadAnalysis
	{
		public var successful:Boolean;
		public var failureMessage:String;
		public var fields:ArrayCollection;
		public var size:int;		
		
		public function UserUploadAnalysis()
			{
			super();
		}

	}
}