package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.NumericValue")]
	public class NumericValue extends Value
	{
		public var value:Number;
		
		public function NumericValue(value:Number = 0)
		{
			super();
            this.value = value;
		}

        override public function type():int {
            return Value.NUMBER;
        }

        override public function toString():String {
            return String(value);
        }

        override public function toNumber():Number {
            return value;
        }        
		
		override public function getValue():Object {
			return value;
		}
	}
}