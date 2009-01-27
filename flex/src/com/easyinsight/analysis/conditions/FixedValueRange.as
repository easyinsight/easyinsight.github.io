package com.easyinsight.analysis.conditions
{
	public class FixedValueRange implements ValueRange
	{		
		private var lowValue:Number;
		private var highValue:Number;
		private var multiplyFactor:Number;
		
		public function FixedValueRange(lowValue:Number, highValue:Number) {
			this.lowValue = lowValue;
			this.highValue = highValue;
			multiplyFactor = 100 / highValue;
		}

		public function addValue(object:Object):void
		{
		}
		
		public function getRangeValue(dataPoint:Number):Number
		{
			// if the range here is 0 to 10
			// we pass in 4
			// that should translate into 40
			// if the range is 3 to 7
			// we pass in 6
			// that should be 75
			// subtract 3, we get 0 to 4
			// that set our min boundary
			// then 100 / 4 = 25, our multiplier
			// 3 * 25 = 75
			
			if (dataPoint < lowValue) {
				dataPoint = lowValue;
			} else if (dataPoint > highValue) {
				dataPoint = highValue;
			}
			
			/*var adjusted:Number = dataPoint - lowValue;
			return adjusted * multiplyFactor;*/
			return dataPoint;			
		}
		
	}
}