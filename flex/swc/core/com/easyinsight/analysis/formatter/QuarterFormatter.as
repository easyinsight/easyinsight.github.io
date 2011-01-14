package com.easyinsight.analysis.formatter {
import mx.formatters.Formatter;

public class QuarterFormatter extends Formatter {
    public function QuarterFormatter() {
        super();
    }

    override public function format(value:Object):String {
        var date:Date;
        if (value == null) {
            return "";
        }
        if (value is String) {
            if (value == "") {
                return "";
            }
            var num:Number = Date.parse(value);
            if (isNaN(num) || !isFinite(num)) {
                return "";
            }
            date = new Date(num);
        } else if (value is Date) {
            date = value as Date;
        } else {
            return "";
        }
        var year:Number = date.getFullYear();
        var month:Number = date.getMonth();
        var quarter:int = month / 3 + 1;
        return "Q" + quarter + " " + year;
    }
}
}