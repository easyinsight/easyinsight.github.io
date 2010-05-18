package com.easyinsight.analysis
{
import com.easyinsight.analysis.formatter.DimensionValueFormatter;
import mx.formatters.DateFormatter;
	import mx.formatters.Formatter;
import mx.formatters.NumberFormatter;
	
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
            var formatter:Formatter;
            if (dateLevel <= AnalysisItemTypes.WEEK_LEVEL) {
                var dateFormatter:DateFormatter = new DateFormatter();
                switch (this.dateLevel) {
                    case AnalysisItemTypes.YEAR_LEVEL:
                        dateFormatter.formatString = "YYYY";
                        break;
                    case AnalysisItemTypes.MONTH_LEVEL:
                        dateFormatter.formatString = "MM/YYYY";
                        break;
                    case AnalysisItemTypes.WEEK_LEVEL:
                        dateFormatter.formatString = "MM/DD/YYYY";
                        break;
                    case AnalysisItemTypes.DAY_LEVEL:
                        dateFormatter.formatString = "MM/DD/YYYY";
                        break;
                    case AnalysisItemTypes.HOUR_LEVEL:
                        dateFormatter.formatString = "MM/DD/YYYY HH:00";
                        break;
                    case AnalysisItemTypes.MINUTE_LEVEL:
                        dateFormatter.formatString = "MM/DD/YYYY HH:NN";
                        break;
                }
                formatter = dateFormatter;
            } else if (dateLevel == AnalysisItemTypes.DAY_OF_WEEK_FLAT || dateLevel == AnalysisItemTypes.MONTH_FLAT) {
                formatter = new DimensionValueFormatter();
            } else {
                var numberFormatter:NumberFormatter = new NumberFormatter();
			    numberFormatter.precision = 0;
			    formatter = numberFormatter;
            }
			return formatter;
		}
	}
}