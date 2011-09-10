package com.easyinsight.analysis
{
	import com.easyinsight.analysis.formatter.DimensionValueFormatter;
	
	import mx.formatters.Formatter;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.DerivedAnalysisDimension")]
	public class DerivedAnalysisDimension extends AnalysisDimension
	{
		public var derivationCode:String;
        public var wordWrap:Boolean;
        public var html:Boolean;
		
		public function DerivedAnalysisDimension(group:Boolean = true)
		{
			super(group);
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.DERIVED_GROUPING;
		}
	}
}