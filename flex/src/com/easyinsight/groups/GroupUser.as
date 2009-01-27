package com.easyinsight.groups
{
	import com.easyinsight.administration.sharing.UserStub;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.groups.GroupUser")]
	public class GroupUser extends UserStub
	{
        public static const OWNER:int = 1;
        public static const EDITOR:int = 2;
        public static const VIEWER:int = 3;

		public var role:int;
		
		public function GroupUser()
		{
		}

	}
}