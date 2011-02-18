package com.easyinsight.listing
{
import com.easyinsight.framework.User;

import com.easyinsight.quicksearch.EIDescriptor;

import mx.controls.Label;
import mx.formatters.DateFormatter;

	public class MyDataCreationDateDataLabel extends Label
	{
		private var object:Object;
        private static var formatter:DateFormatter;
		
		public function MyDataCreationDateDataLabel()
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
				if (value is EIDescriptor) {
                    this.text = formatter.format(EIDescriptor(value).creationDate);
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