package com.easyinsight.analysis.formatter {
import mx.formatters.Formatter;
import mx.formatters.NumberFormatter;

public class ByteFormatter extends Formatter {

    private var numberFormatter:NumberFormatter = new NumberFormatter();

    public function ByteFormatter() {
        super();
        numberFormatter.precision = 2;
    }

    override public function format(value:Object):String {
        var num:Number = Number(value);
        var resultString:String;
        if (num < 1000) {
            resultString = String(numberFormatter.format(num));
        } else if (num < 1000000) {
            resultString = String(numberFormatter.format(num / 1000)) + " kb";
        } else if (num < 1000000000) {
            resultString = String(numberFormatter.format(num / 1000000)) + " mb";
        } else if (num < 1000000000000) {
            resultString = String(numberFormatter.format(num / 1000000000)) + " gb";
        } else {
            resultString = numberFormatter.format(num / 1000000000000) + " tb";
        }
        return resultString;
    }
}
}