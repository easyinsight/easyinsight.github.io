package com.easyinsight.analysis.conditions
{

	public class MeasureConditionRange
	{
		public static const FIXED_VALUE:int = 1;
		public static const PERCENTAGE:int = 2;
		public static const LOGARITHM:int = 3;
		
		public var condition:MeasureCondition = new MeasureCondition();
		public var valueRangeType:int = PERCENTAGE;	
		
		public function MeasureConditionRange()
		{
		}
		
		public function createValueRange():ValueRange {
			var valueRange:ValueRange;
			if (valueRangeType == FIXED_VALUE) {
				valueRange = new FixedValueRange(condition.lowValue, condition.highValue);
			} else if (valueRangeType == PERCENTAGE || valueRangeType == LOGARITHM) {
				valueRange = new PercentageValueRange();
			}/* else if (valueRangeType == LOGARITHM) {
				valueRange = new LogarithmicValueRange();
			}*/
			return valueRange;
		}

		public function getColor(value:Number, valueRange:ValueRange):uint {
			var transformed:Number = valueRange.getRangeValue(value);
			return condition.getColor(transformed);
		}
	}
}