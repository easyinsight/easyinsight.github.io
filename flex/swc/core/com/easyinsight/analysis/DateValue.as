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

        public var format:String;

        public var dateTime:Boolean;

        public var cachedDate:Date;

        public var date:Date;
				
		public function DateValue(date:Date = null)
		{
			super();
            this.date = date;
		}

        private function getDate():Date {
            if (cachedDate == null) {
                cachedDate = new Date();
                /*if (dateTime) {
                    cachedDate.setUTCFullYear(year, month, day);
                    cachedDate.setUTCHours(hour, minute);
                } else {*/
                    cachedDate.setFullYear(year, month, day);
                    cachedDate.setHours(hour, minute);
                //}

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