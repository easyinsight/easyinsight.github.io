package com.easyinsight.analysis.formatter {
import mx.formatters.Formatter;
import mx.formatters.NumberFormatter;

public class RoundedCurrencyFormatter extends Formatter {

    private var numberFormatter:NumberFormatter = new NumberFormatter();
    public var currencySymbol:String;

    public function RoundedCurrencyFormatter(currencySymbol:String, precision:int) {
        super();
        numberFormatter.precision = precision;
        this.currencySymbol = currencySymbol;
    }

    override public function format(value:Object):String {
        var num:Number = Number(value);
        var negative:Boolean = false;
        if (num < 0) {
            negative = true;
        }
        num = Math.abs(num);
        var resultString:String;
        if (num < 1000) {
            resultString = String(numberFormatter.format(num));
        } else if (num < 1000000) {
            resultString = String(numberFormatter.format(num / 1000)) + "k";
        } else if (num < 1000000000) {
            resultString = String(numberFormatter.format(num / 1000000)) + "m";
        } else {
            resultString = String(numberFormatter.format(num / 1000000000)) + "b";
        }
        resultString = currencySymbol + resultString;
        if (negative) {
            resultString = "(" + resultString + ")";
        }
        return resultString;
    }
}
}