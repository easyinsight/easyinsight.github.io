package com.easyinsight.analysis.formatter
{
import com.easyinsight.framework.User;

import mx.formatters.CurrencyFormatter;

    import mx.formatters.Formatter;
import mx.formatters.NumberBaseRoundType;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FormattingConfiguration")]	
	public class FormattingConfiguration
	{
		public static const NUMBER:int = 1;
    	public static const CURRENCY:int = 2;
    	public static const PERCENTAGE:int = 3;
        public static const MILLISECONDS:int = 4;
        public static const SECONDS:int = 5;
        public static const BYTES:int = 6;
		
		public function FormattingConfiguration()
		{
		}

		public static function getFormatter(precision:int, minPrecision:int, formattingType:int):Formatter {
			var formatter:Formatter;
			switch (formattingType) {
				case NUMBER:
					var numberFormatter:FlexibleNumberFormatter = new FlexibleNumberFormatter();
                    if (User.getInstance() != null) {
                        numberFormatter.decimalSeparatorTo = User.getInstance().decimalSeperator;
                        numberFormatter.thousandsSeparatorTo = User.getInstance().thousandsSeperator;
                    }
					numberFormatter.precision = precision;
                    numberFormatter.minPrecision = minPrecision;
                    numberFormatter.rounding = NumberBaseRoundType.NEAREST;
					formatter = numberFormatter;
					break;
				case CURRENCY:
					var currencyFormatter:CurrencyFormatter = new EICurrencyFormatter();
                    if (User.getInstance() != null) {
                        currencyFormatter.decimalSeparatorTo = User.getInstance().decimalSeperator;
                        currencyFormatter.thousandsSeparatorTo = User.getInstance().thousandsSeperator;
                    }
					currencyFormatter.precision = precision;
                    currencyFormatter.currencySymbol = User.getInstance() == null ? "$" : User.getInstance().currencySymbol;
                    currencyFormatter.rounding = NumberBaseRoundType.NEAREST;
					formatter = currencyFormatter;
					break;
				case PERCENTAGE:
                    var percentageFormatter:PercentageNumberFormatter = new PercentageNumberFormatter();
                    if (User.getInstance() != null) {
                        percentageFormatter.decimalSeparatorTo = User.getInstance().decimalSeperator;
                        percentageFormatter.thousandsSeparatorTo = User.getInstance().thousandsSeperator;
                    }
                    percentageFormatter.precision = precision;
                    percentageFormatter.minPrecision = minPrecision;
                    percentageFormatter.rounding = NumberBaseRoundType.NEAREST;
                    formatter = percentageFormatter;
                    break;
                case MILLISECONDS:
                    formatter = new TimeStringFormatter(1, precision);
                    break;
                case SECONDS:
                    formatter = new TimeStringFormatter(1000, precision);
                    break;
                case BYTES:
                    formatter = new ByteFormatter();
                    break;
				default:
					var defaultFormatter:FlexibleNumberFormatter = new FlexibleNumberFormatter();
                    if (User.getInstance() != null) {
                        defaultFormatter.decimalSeparatorTo = User.getInstance().decimalSeperator;
                        defaultFormatter.thousandsSeparatorTo = User.getInstance().thousandsSeperator;
                    }
					defaultFormatter.precision = precision;
                    defaultFormatter.rounding = NumberBaseRoundType.NEAREST;
					formatter = defaultFormatter;
					break;				
			}
			return formatter;
		}
	}
}