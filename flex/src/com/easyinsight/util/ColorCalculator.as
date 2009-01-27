package com.easyinsight.util
{
	public class ColorCalculator
	{
		public function ColorCalculator()
		{
		}
		
		public function calculate(x:Number):Number {
			var range:Range = findRange(x);
			// what this should be...
			var locationInRange:Number = range.positionOf(x);
		    
		}
		
		public function getColor(lowColor:uint, highColor:uint, locationInRange:Number):uint {
			// each of these is between 0-5
			// so a delta of 
			var redDelta:uint = getRed(lowColor) - getRed(highColor);
			var greenDelta:uint = getGreen(lowColor) - getGreen(highColor);
			var blueDelta:uint = getBlue(lowColor) - getBlue(HighColor);
			// so if we have low = 000000
			// high = FFFFFF
			// those sort of translate into 0, 0, 0 and 5, 5, 5
			// if the range value is 0, it would be just be 0 * 0, 0 * 0, 0 * 0
			// if the range value is 5, it would be...
			// if the range value is .3 and the red set is 1 -> 2
			// that should give us 1
			// if the range value is .6 and the blue set is 1 -> 3
			// it's going to give us a result of (3 - 1) * .6 = 1.2 + 1 = 2.2
			// if it's 3 - 1 
		}
	}
}