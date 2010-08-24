package com.easyinsight.analysis
{
	import com.easyinsight.analysis.formatter.DimensionValueFormatter;
	
	import mx.formatters.Formatter;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisDimension")]
	public class AnalysisDimension extends AnalysisItem
	{
		public var group:Boolean = true;
		public var analysisDimensionID:int;
        public var keyDimension:AnalysisDimension;
        public var summary:Boolean = false;
		
		public function AnalysisDimension(group:Boolean = true)
		{
			super();
			this.group = group;
		}
		
		override public function getType():int {
			return AnalysisItemTypes.DIMENSION;	
		}
		
		override public function getFormatter():Formatter {
			return new DimensionValueFormatter();			
		}
	}
}