package com.easyinsight.analysis
{
	import mx.formatters.DateFormatter;
	import mx.formatters.Formatter;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisDateDimension")]
	public class AnalysisDateDimension extends AnalysisDimension
	{
		public var dateLevel:int;
		public var customDateFormat:String;
		
		public function AnalysisDateDimension(dateLevel:int = AnalysisItemTypes.YEAR_LEVEL, 
			customDateFormat:String = "yyyy-MM-dd") {				
			super();
			this.dateLevel = dateLevel;
			this.customDateFormat = customDateFormat;
		}
		
		override public function getType():int {
			return super.getType() | AnalysisItemTypes.DATE;	
		}
		
		override public function getFormatter():Formatter {
			var dateFormatter:DateFormatter = new DateFormatter();
			switch (this.dateLevel) {
				case AnalysisItemTypes.YEAR_LEVEL:
					dateFormatter.formatString = "YYYY";
					break;
				case AnalysisItemTypes.MONTH_LEVEL:
					dateFormatter.formatString = "MM/YYYY";
					break;
				case AnalysisItemTypes.DAY_LEVEL:
					dateFormatter.formatString = "MM/DD/YYYY";
					break;
			}
			return dateFormatter;
		}
	}
}