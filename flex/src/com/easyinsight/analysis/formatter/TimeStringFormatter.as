package com.easyinsight.analysis.formatter {
import mx.formatters.Formatter;

public class TimeStringFormatter extends Formatter{
    public function TimeStringFormatter() {
        super();
    }

    override public function format(value:Object):String {
        var resultString:String;
        var num:Number = Number(value);
        if (num < 1000) {
            // < 1 second, display milliseconds
            resultString = num + " ms";
        } else if (num < 60000) {
            // < 1 minute
            num = num / 1000;
            var seconds:uint = num / 1000;
            var milliseconds:uint = num % 1000;
            resultString = seconds + ":" + milliseconds;
        } else if (num < 3600000) {
            // < 1 hour
            num = num / 1000 * 60;
            var minutes:uint = num / 60;
            var secondsRemainder:uint = num % 60;
            resultString = minutes + ":" + seconds;
        } else if (num < 36000000) {
            // < 24 hours
            var hours:uint = num / 24;
            var minutesRemainder:uint = num % 24;
        }
        return super.format(value);
    }
}
}