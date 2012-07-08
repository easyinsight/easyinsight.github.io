package com.easyinsight.export;

import java.util.Calendar;

/**
* User: jamesboe
* Date: 7/6/12
* Time: 11:30 AM
*/
class ExportMetadata {
    int dateFormat;
    String currencySymbol;
    Calendar cal;

    ExportMetadata(int dateFormat, String currencySymbol, Calendar cal) {
        this.dateFormat = dateFormat;
        this.currencySymbol = currencySymbol;
        this.cal = cal;
    }
}
