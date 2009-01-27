package com.easyinsight.analysis
{
	public class CombinedGroupController
	{
		private var measureCount:int = 0;
		private var dimensionCount:int = 0;
		private var maxMeasures:int = 0;
		private var maxDimensions:int = 0;		
		public var measureGroup:ListDropAreaGrouping;
		public var dimensionGroup:ListDropAreaGrouping;
		
		public function CombinedGroupController(maxMeasures:int = 1, maxDimensions:int = 1)
		{
			this.maxMeasures = maxMeasures;
			this.maxDimensions = maxDimensions;
		}
		
		public function reset():void {
			this.measureCount = 0;
			this.dimensionCount = 0;
			measureGroup.reset();
			dimensionGroup.reset();
			measureGroup.canDoMore();
			dimensionGroup.canDoMore();
		}
		
		public function set maxMeasureCount(maxMeasures:int):void {
			this.maxMeasures = maxMeasures;
			if (measureCount > maxMeasures) {
				measureGroup.destroyLast();
			} else if (measureCount < maxMeasures && dimensionCount < 2) {
				measureGroup.canDoMore();
			}
		}
		
		public function set maxDimensionCount(maxDimensions:int):void {
			this.maxDimensions = maxDimensions;
			if (dimensionCount > maxDimensions) {
				dimensionGroup.destroyLast();
			} else if (dimensionCount < maxDimensions && measureCount < 2){
				dimensionGroup.canDoMore();
			}
		}

		public function onMeasureAdd():void {
			measureCount++;
			if (measureCount >= maxMeasures) {
				if (measureCount > maxMeasures) {
					measureGroup.destroyLast();
					measureCount--;
				}
				measureGroup.noMore();
			}
			if (measureCount == 2) {
				dimensionGroup.noMore();
			}
		}
		
		public function onDimensionAdd():void {
			dimensionCount++;
			if (dimensionCount >= maxDimensions) {
				if (dimensionCount > maxDimensions) {
					dimensionGroup.destroyLast();
					dimensionCount--;
				}
				dimensionGroup.noMore();
			}
			if (dimensionCount == 2) {
				measureGroup.noMore();
			}
		}
		
		public function onMeasureRemove():void {
			measureCount--;
			if (measureCount < maxMeasures) {
				measureGroup.canDoMore();
			}
			if (measureCount == 1) {
				dimensionGroup.canDoMore();
			}
		}
		
		public function onDimensionRemove():void {
			dimensionCount--;
			if (dimensionCount < maxDimensions) {
				dimensionGroup.canDoMore();				
			}
			if (dimensionCount == 1) {
				measureGroup.canDoMore();
			}
		}
	}
}