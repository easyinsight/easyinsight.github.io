package com.easyinsight.analysis.conditions
{
import mx.controls.Alert;

public class MeasureCondition
	{		
		public var lowColor:uint;
		public var lowValue:int;
		public var highColor:uint;
		public var highValue:int;
		public var measureConditionID:int;
		
		public function MeasureCondition()
		{
		}
		
		public function getColor(transformed:Number):uint {
			
			// -12.6 on a scale of -15 to 0 should become roughly 20%
			// so...
			
			var operatingLowValue:int = lowValue;
			var operatingHighValue:int = highValue;
			if (operatingLowValue < 0) {
				operatingLowValue = 0;
				operatingHighValue = highValue - lowValue;
				transformed = transformed - lowValue;	
			}
			
			var multiplyFactor:Number = 100 / operatingHighValue;
			var adjusted:Number = transformed - operatingLowValue;
			var upperWeight:Number = adjusted * multiplyFactor;
			var lowerWeight:Number = 100 - upperWeight;			
		
			var red1:uint = Math.floor (lowColor / 65536);
			var green1:uint = Math.floor ((lowColor - red1 * 65536) / 256);
			var blue1:uint = lowColor - red1 * 65536 - green1 * 256;			
			
			var red2:uint = Math.floor (highColor / 65536);
			var green2:uint = Math.floor ((highColor - red2 * 65536) / 256);
			var blue2:uint = highColor - red2 * 65536 - green2 * 256;							
						
			var mixedRed:uint = ((red2 * upperWeight) + (red1 * lowerWeight)) / 100;
			var mixedGreen:uint = ((green2 * upperWeight) + (green1 * lowerWeight)) / 100;
			var mixedBlue:uint = ((blue2 * upperWeight) + (blue1 * lowerWeight)) / 100;									
			
			var newColor:uint = mixedRed << 16 | mixedGreen << 8 | mixedBlue;
            //Alert.show("new color = " + newColor + " while high color = " + highColor + " and low color = " + lowColor);
            if (highColor > lowColor) {
                if (newColor > highColor) {
                    newColor = highColor;
                } else if (newColor < lowColor) {
                    newColor = lowColor;
                }
            } else {

                if (newColor < highColor) {

                    newColor = highColor;
                } else if (newColor > lowColor) {
                    newColor = lowColor;
                }
            }
            
			return newColor;
		}
	}
}