package com.easyinsight.analysis
{
	import com.easyinsight.analysis.formatter.DimensionValueFormatter;
	
	import mx.formatters.Formatter;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.DerivedAnalysisDateDimension")]
	public class DerivedAnalysisDateDimension extends AnalysisDateDimension
	{
		public var derivationCode:String;
		
		public function DerivedAnalysisDateDimension()
		{
			super();
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.DERIVED_DATE;
		}
	}
}