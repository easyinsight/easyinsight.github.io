package com.easyinsight.analysis.conditions
{
	public class LogarithmicValueRange implements ValueRange
	{
		private var min:Number = Number.MAX_VALUE;
		private var max:Number = Number.MIN_VALUE;
		private var multiplier:Number;
		
		public function LogarithmicValueRange()
		{
		}

		public function addValue(object:Object):void {
			if (object is Number) {
				var num:Number = object as Number;
				if (num > 0) {
					num = Math.log(num);
				} else {
					num = 0;
				}
				if (num > max) {
					max = num;
				} else if (num < min) {
					min = num;
				}				
			} 
		}
		
		public function getRangeValue(dataPoint:Number):Number {
			if (dataPoint > 0) {
				dataPoint = Math.log(dataPoint);
			} else {
				dataPoint = 0;
			}
			multiplier = 100 / (max - min);			
			return (dataPoint - min) * multiplier; 	    						 		
		}
		
	}
}