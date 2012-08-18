/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/18/12
 * Time: 2:03 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.formatter {
import mx.formatters.DateFormatter;

public class EIDateFormatter extends DateFormatter {
    public function EIDateFormatter() {
    }

    override public function format(value:Object):String {
        if (value is String) {
            return value as String;
        }
        return super.format(value);
    }
}
}
