package com.easyinsight.analysis.formatter
{
	import mx.formatters.CurrencyFormatter;
	import mx.formatters.Formatter;
	
	[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FormattingConfiguration")]	
	public class FormattingConfiguration
	{
		public static const NUMBER:int = 1;
    	public static const CURRENCY:int = 2;
    	public static const PERCENTAGE:int = 3;
    	public static const TEXT_UOM:int = 4;
    
		public var formattingConfigurationID:int;
		public var formattingType:int;
		public var textUom:String;
		
		public function FormattingConfiguration()
		{
		}

		public function getFormatter():Formatter {
			var formatter:Formatter;
			switch (formattingType) {
				case NUMBER:
					var numberFormatter:FlexibleNumberFormatter = new FlexibleNumberFormatter();
					numberFormatter.precision = 2;
					formatter = numberFormatter;
					break;
				case CURRENCY:
					var currencyFormatter:CurrencyFormatter = new CurrencyFormatter();
					currencyFormatter.precision = 2;
					formatter = currencyFormatter;
					break;
				case PERCENTAGE:
				case TEXT_UOM:
				default:
					var defaultFormatter:FlexibleNumberFormatter = new FlexibleNumberFormatter();
					defaultFormatter.precision = 2;
					formatter = defaultFormatter;
					break;				
			}
			return formatter;
		}
	}
}