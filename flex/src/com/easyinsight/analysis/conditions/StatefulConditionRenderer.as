package com.easyinsight.analysis.conditions
{
	public class StatefulConditionRenderer extends ConditionRenderer
	{
		private var measureConditionRange:MeasureConditionRange;
		private var valueRange:ValueRange;
		
		public function StatefulConditionRenderer(measureConditionRange:MeasureConditionRange) {
			super();
			this.measureConditionRange = measureConditionRange;
			this.valueRange = measureConditionRange.createValueRange();
		}
		
		override public function getColor(value:Object):uint {
			if (value is Number) {
				return this.measureConditionRange.getColor(value as Number, valueRange);
			} else {
				return 0;
			}
		}
		
		override public function hasCustomColor():Boolean {
			return true;
		}
		
		override public function addValue(object:Object):void {
			valueRange.addValue(object);
		}
	}
}