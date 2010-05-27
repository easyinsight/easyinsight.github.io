package com.easyinsight.analysis
{
import com.easyinsight.analysis.formatter.DimensionValueFormatter;
import com.easyinsight.framework.User;

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
                        switch (User.getInstance().dateFormat) {
                            case 0:
                            case 3:
                                dateFormatter.formatString = "MM/YYYY";
                                break;
                            case 1:
                                dateFormatter.formatString = "YYYY-MM";
                                break;
                            case 2:
                                dateFormatter.formatString = "MM-YYYY";
                                break;
                            case 4:
                                dateFormatter.formatString = "MM.YYYY";
                                break;
                        }
                        break;
                    case AnalysisItemTypes.WEEK_LEVEL:
                    case AnalysisItemTypes.DAY_LEVEL:
                        switch (User.getInstance().dateFormat) {
                            case 0:
                                dateFormatter.formatString = "MM/DD/YYYY";
                                break;
                            case 1:
                                dateFormatter.formatString = "YYYY-MM-DD";
                                break;
                            case 2:
                                dateFormatter.formatString = "DD-MM-YYYY";
                                break;
                            case 3:
                                dateFormatter.formatString = "DD/MM/YYYY";
                                break;
                            case 4:
                                dateFormatter.formatString = "DD.MM.YYYY";
                                break;
                        }
                        break;
                    case AnalysisItemTypes.HOUR_LEVEL:
                        switch (User.getInstance().dateFormat) {
                            case 0:
                                dateFormatter.formatString = "MM/DD/YYYY HH:00";
                                break;
                            case 1:
                                dateFormatter.formatString = "YYYY-MM-DD HH:00";
                                break;
                            case 2:
                                dateFormatter.formatString = "DD-MM-YYYY HH:00";
                                break;
                            case 3:
                                dateFormatter.formatString = "DD/MM/YYYY HH:00";
                                break;
                            case 4:
                                dateFormatter.formatString = "DD.MM.YYYY HH:00";
                                break;
                        }
                        break;
                    case AnalysisItemTypes.MINUTE_LEVEL:
                        switch (User.getInstance().dateFormat) {
                            case 0:
                                dateFormatter.formatString = "MM/DD/YYYY HH:MM";
                                break;
                            case 1:
                                dateFormatter.formatString = "YYYY-MM-DD HH:MM";
                                break;
                            case 2:
                                dateFormatter.formatString = "DD-MM-YYYY HH:MM";
                                break;
                            case 3:
                                dateFormatter.formatString = "DD/MM/YYYY HH:MM";
                                break;
                            case 4:
                                dateFormatter.formatString = "DD.MM.YYYY HH:MM";
                                break;
                        }
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