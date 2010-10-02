package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.CoordinateValue")]
	public class CoordinateValue extends Value
	{
		public var x:String;
		public var y:String;
        public var zip:String;

		public function CoordinateValue()
		{
			super();
		}

        override public function type():int {
            return Value.STRING;
        }

        override public function toString():String {
            return x + ":" + y;
        }
		
		override public function getValue():Object {
			return this;
		}				
	}
}