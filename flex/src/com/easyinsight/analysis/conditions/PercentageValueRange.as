package com.easyinsight.analysis.conditions
{
import mx.controls.Alert;

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
				}
                if (num < min) {
					min = num;
				}				
			} 
		}
		
		public function getRangeValue(dataPoint:Number):Number {
            // max = 44
            // min = 0
            // multiplier = 100 / 44 = 2.2
			multiplier = 100 / (max - min);
            // 0 * 2.2 = 0
			return (dataPoint - min) * multiplier;
		}
		
	}
}