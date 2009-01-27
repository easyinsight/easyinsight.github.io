package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.EmptyValue")]
	public class EmptyValue extends Value
	{
		public function EmptyValue()
		{
			super();
		}
		
		override public function getValue():Object {
			return "";
		}
	}
}