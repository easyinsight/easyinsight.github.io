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

        override public function type():int {
            return Value.DATE;
        }

        override public function toString():String {
            return String(date);
        }

        override public function toDate():Date {
            return date;
        }
		
		override public function getValue():Object {
			return date;
		}
	}
}