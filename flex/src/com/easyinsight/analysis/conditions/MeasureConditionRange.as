package com.easyinsight.analysis.conditions
{
	[Bindable]
	[RemoteClass(alias="com.easyinsight.conditions.MeasureConditionRange")]
	public class MeasureConditionRange
	{
		public static const FIXED_VALUE:int = 1;
		public static const PERCENTAGE:int = 2;
		public static const LOGARITHM:int = 3;
		
		public var lowCondition:MeasureCondition = new MeasureCondition();		
		public var highCondition:MeasureCondition = new MeasureCondition();
		public var valueRangeType:int = PERCENTAGE;
		public var measureConditionRangeID:int;			
		
		public function MeasureConditionRange()
		{
		}
		
		public function createValueRange():ValueRange {
			var valueRange:ValueRange;
			if (valueRangeType == FIXED_VALUE) {
				valueRange = new FixedValueRange(lowCondition.lowValue, highCondition.highValue);
			} else if (valueRangeType == PERCENTAGE) {
				valueRange = new PercentageValueRange();
			} else if (valueRangeType == LOGARITHM) {
				valueRange = new LogarithmicValueRange();
			}
			return valueRange;
		}			

		public function getColor(value:Number, valueRange:ValueRange):uint {
			var transformed:Number = valueRange.getRangeValue(value);
			trace("transformed number = " + transformed);
			var measureCondition:MeasureCondition = getCondition(transformed);
			return measureCondition.getColor(transformed);
		}
		
		private function getCondition(value:Number):MeasureCondition {
			if (lowCondition == null && highCondition != null) {
				return highCondition;
			} else if (lowCondition != null && highCondition == null) {
				return lowCondition;
			}			
			return lowCondition.accepts(value) ? lowCondition : highCondition;
		}
		
		public function clone():MeasureConditionRange {
			var newRange:MeasureConditionRange = new MeasureConditionRange();
			newRange.valueRangeType = this.valueRangeType;
			newRange.lowCondition = this.lowCondition.clone();
			newRange.highCondition = this.highCondition.clone();
			return newRange;
		}
	}
}