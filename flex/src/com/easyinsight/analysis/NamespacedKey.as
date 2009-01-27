package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.NamespacedKey")]
	public class NamespacedKey extends NamedKey
	{
		public var namespaceField:String;
		public var field:String;
		
		public function NamespacedKey()
		{
			super();
		}
		
	}
}