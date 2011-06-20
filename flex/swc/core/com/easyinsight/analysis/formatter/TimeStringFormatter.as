package com.easyinsight.analysis.formatter {
import mx.formatters.Formatter;

public class TimeStringFormatter extends Formatter{

    private var baseInterval:int = 1;

    public function TimeStringFormatter(baseInterval:int) {
        super();
        this.baseInterval = baseInterval;
    }

    override public function format(value:Object):String {
        var resultString:String;
        var num:Number = Number(value);
        num = num * baseInterval;
        var pos:Boolean = num >= 0;
        num = Math.abs(num);
        if (num == 0) {
            resultString = "";
        } else if (num < 1000) {
            // < 1 second, display milliseconds
            resultString = num + " ms";
        } else if (num < 60000) {
            // < 1 minute
            var seconds:uint = num / 1000;
            var milliseconds:uint = num % 1000;
            if (milliseconds == 0) {
                resultString = seconds + "s";
            } else {
                resultString = seconds + "s:" + milliseconds+"ms";
            }
        } else if (num < (60000 * 60)) {
            // < 1 hour
            var minutes:uint = num / (60 * 1000);
            var secondsRemainder:uint = (num / 1000) % 60;
            if (secondsRemainder == 0) {
                resultString = minutes + "m";
            } else {
                resultString = minutes + "m:" + secondsRemainder + "s";
            }
        } else if (num < (60000 * 60 * 24)) {
            // < 24 hours
            var hours:uint = num / (60000 * 60);
            var minutesRemainder:uint = num % 24;
            if (minutesRemainder == 0) {
                resultString = hours + "h";
            } else {
                resultString = hours + "h:" + minutesRemainder + "m";
            }
        } else {      
            var dayVal:uint = (60000 * 60 * 24);
            var days:uint = num / dayVal;
            var dayRemainder:uint = (num / (60000 * 60)) % 24;
            if (dayRemainder == 0) {
                resultString = days + "d";
            } else {
                resultString = days + "d:" + dayRemainder + "h";
            }
        }
        if (!pos) {
            resultString = "(" + resultString + ")";
        }
        return resultString;
    }
}
}