package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.StringValue")]
	public class StringValue extends Value
	{
		public var value:String;
		
		public function StringValue(value:String = null)
		{
			super();
            this.value = value;
		}

        override public function type():int {
            return Value.STRING;
        }

        override public function toString():String {
            return value;
        }
		
		override public function getValue():Object {
			return value;
		}				
	}
}