package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.NumericValue")]
	public class NumericValue extends Value
	{
		public var value:Number;
		
		public function NumericValue()
		{
			super();
		}
		
		override public function getValue():Object {
			return value;
		}
	}
}