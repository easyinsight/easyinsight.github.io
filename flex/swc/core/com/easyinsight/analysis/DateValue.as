package com.easyinsight.analysis
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.core.DateValue")]
	
	public class DateValue extends Value
	{
		public var year:int;
        public var month:int;
        public var day:int;
        public var hour:int;
        public var minute:int;

        public var cachedDate:Date;
				
		public function DateValue()
		{
			super();
		}

        private function getDate():Date {
            if (cachedDate == null) {
                cachedDate = new Date();
                cachedDate.setFullYear(year, month, day);
                cachedDate.setHours(hour, minute);
            }
            return cachedDate;
        }

        override public function type():int {
            return Value.DATE;
        }

        override public function toString():String {
            return String(getDate());
        }

        override public function toDate():Date {
            return getDate();
        }
		
		override public function getValue():Object {
			return getDate();
		}
	}
}