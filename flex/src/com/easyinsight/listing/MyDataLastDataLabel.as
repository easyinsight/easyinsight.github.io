package com.easyinsight.listing
{

import mx.controls.Label;
import mx.formatters.DateFormatter;

	public class MyDataLastDataLabel extends Label
	{
		private var object:Object;
        private static var formatter:DateFormatter;
		
		public function MyDataLastDataLabel()
		{
			super();
            if (formatter == null) {
                formatter = new DateFormatter();
                formatter.formatString = "MM/DD/YYYY HH:NN";
            }
		}
		
		override public function set data(value:Object):void {
			this.object = value;
			if (value != null) {
				if (value is DataFeedDescriptor) {
					var descriptor:DataFeedDescriptor = value as DataFeedDescriptor;
                    if (descriptor.lastDataTime == null) {
                        this.text = "( No Data )";
                    } else {
					    this.text = formatter.format(descriptor.lastDataTime);
                    }
				} else {
					this.text = "";
				}
			}			
		}
		
		override public function get data():Object {
			return this.object;
		}		
	}
}