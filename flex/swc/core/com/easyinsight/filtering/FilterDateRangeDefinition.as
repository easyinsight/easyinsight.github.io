package com.easyinsight.filtering
{

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterDateRangeDefinition")]
	public class FilterDateRangeDefinition extends FilterDefinition
	{
		public var startDate:Date;
    public var endDateEnabled:Boolean = true;
    public var startDateEnabled:Boolean = true;
    public var sliderRange:Boolean = true;
        public var boundingStartDate:Date;
		public var endDate:Date;
        public var boundingEndDate:Date;
        public var sliding:Boolean;
		
		public function FilterDateRangeDefinition()
		{
			super();
		}
		
		override public function getType():int {
			return FilterDefinition.DATE;
		}

        override public function getSaveValue():Object {
            var obj:Object = new Object();
            obj["start"] = startDate;
            obj["end"] = endDate;
            return obj;
        }

        override public function loadFromSharedObject(value:Object):void {
            if (value != null) {
                var date1:Date = value["start"];
                var date2:Date = value["end"];
                if (date1 != null) {
                    startDate = date1;
                }
                if (date2 != null) {
                    endDate = date2;
                }
            }
        }
	}
}