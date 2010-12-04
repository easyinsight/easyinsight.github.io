package com.easyinsight.analysis.formatter {
import mx.formatters.Formatter;

public class QuarterFormatter extends Formatter {
    public function QuarterFormatter() {
        super();
    }

    override public function format(value:Object):String {
        if (value is Date) {
            var date:Date = value as Date;
            var year:Number = date.getFullYear();
            var month:Number = date.getMonth();
            var quarter:int = month / 3;
            return "Q" + quarter + " " + year;
        } else {
            return "";
        }
    }
}
}