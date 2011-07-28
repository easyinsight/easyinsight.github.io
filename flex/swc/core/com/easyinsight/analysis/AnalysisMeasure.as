package com.easyinsight.analysis
{
import com.easyinsight.analysis.formatter.FormattingConfiguration;

import mx.formatters.Formatter;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.AnalysisMeasure")]
	public class AnalysisMeasure extends AnalysisItem
	{
		public var aggregation:int;
		public var analysisMeasureID:int;
        public var rowCountField:Boolean;
    public var precision:int = 2;
    public var minPrecision:int;
    public var underline:Boolean;
		
		public function AnalysisMeasure(aggregation:int = 1, formattingType:int = 0)
		{
			super();
			this.aggregation = aggregation;
            this.formattingConfiguration.formattingType = formattingType;
		}
		
		override protected function getQualifiedSuffix():String {
			return getType() + ":" + aggregation + ":" + display;
		}

        override public function getFormatter():Formatter {
			return formattingConfiguration.getFormatter(precision, minPrecision);
		}
		
		override public function getType():int {
			return AnalysisItemTypes.MEASURE;	
		}
	}
}