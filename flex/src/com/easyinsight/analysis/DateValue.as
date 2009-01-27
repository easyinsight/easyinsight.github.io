package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.DateValue")]
	
	public class DateValue extends Value
	{
		public var date:Date;
				
		public function DateValue()
		{
			super();
		}
		
		override public function getValue():Object {
			return date;
		}
	}
}