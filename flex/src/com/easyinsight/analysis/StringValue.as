package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.StringValue")]
	public class StringValue extends Value
	{
		public var value:String;
		
		public function StringValue()
		{
			super();
		}
		
		override public function getValue():Object {
			return value;
		}				
	}
}