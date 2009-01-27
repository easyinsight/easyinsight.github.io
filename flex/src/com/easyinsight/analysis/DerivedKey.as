package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.DerivedKey")]
	public class DerivedKey extends Key
	{
		public var parentKey:Key;
		public var feedID:int;
		
		public function DerivedKey()
		{
			super();
		}
		
		override public function createString():String {
			return parentKey.createString();
		}				
	}
}