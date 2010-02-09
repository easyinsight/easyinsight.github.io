package com.easyinsight.analysis
{
import com.easyinsight.analysis.range.RangeOption;

import mx.collections.ArrayCollection;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisRangeDimension")]
	public class AnalysisRangeDimension extends AnalysisDimension
	{
        public var explicitOptions:ArrayCollection = new ArrayCollection();
        public var lowerBound:RangeOption;
        public var upperBound:RangeOption;
        public var aggregationType:int;

		public function AnalysisRangeDimension()
		{
			super();
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.RANGE;	
		}

        override public function getSortFunction():Function {
            return null;
        }
	}
}