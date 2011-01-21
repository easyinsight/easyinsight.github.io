package com.easyinsight.listing
{
import com.easyinsight.framework.User;

import com.easyinsight.solutions.DataSourceDescriptor;
import com.easyinsight.solutions.InsightDescriptor;

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
                switch (User.getInstance().dateFormat) {
                    case 0:
                        formatter.formatString = "MM/DD/YYYY HH:NN";
                        break;
                    case 1:
                        formatter.formatString = "YYYY-MM-DD HH:NN";
                        break;
                    case 2:
                        formatter.formatString = "DD-MM-YYYY HH:NN";
                        break;
                    case 3:
                        formatter.formatString = "DD/MM/YYYY HH:NN";
                        break;
                    case 4:
                        formatter.formatString = "DD.MM.YYYY HH:NN";
                        break;
                }
            }
		}
		
		override public function set data(value:Object):void {
			this.object = value;
			if (value != null) {
				if (value is DataSourceDescriptor) {
					var descriptor:DataSourceDescriptor = value as DataSourceDescriptor;
                    if (descriptor.lastDataTime == null) {
                        this.text = "( No Data )";
                    } else {
					    this.text = formatter.format(descriptor.lastDataTime);
                    }
                } else if (value is InsightDescriptor) {
                    var insightDescriptor:InsightDescriptor = value as InsightDescriptor;
                    if (insightDescriptor.lastDataTime == null) {
                        this.text = "";
                    } else {
                        this.text = formatter.format(insightDescriptor.lastDataTime);
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