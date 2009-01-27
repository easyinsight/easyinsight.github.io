package com.easyinsight.framework
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.users.Permission")]
	public class Permission
	{
		public var name:String;
		public var permissionID:Number;
		
		public function Permission()
		{
		}

	}
}