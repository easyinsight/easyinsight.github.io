package com.easyinsight.analysis.conditions
{
	public class PercentageValueRange implements ValueRange
	{
		private var min:Number = Number.MAX_VALUE;
		private var max:Number = Number.MIN_VALUE;
		private var multiplier:Number;
		
		public function PercentageValueRange()
		{
		}

		public function addValue(object:Object):void {
			if (object is Number) {
				var num:Number = object as Number;
				if (num > max) {
					max = num;
				} else if (num < min) {
					min = num;
				}				
			} 
		}
		
		public function getRangeValue(dataPoint:Number):Number {
			multiplier = 100 / (max - min);			
			return (dataPoint - min) * multiplier; 	    						 		
		}
		
	}
}