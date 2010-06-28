package com.easyinsight.genredata {
import com.easyinsight.framework.User;

import mx.controls.Label;
import mx.formatters.DateFormatter;

public class ReportDateRenderer extends Label {

    private var item:ReportExchangeItem;
    private var dateFormatter:DateFormatter;

    public function ReportDateRenderer() {
        super();
        dateFormatter = new DateFormatter();
        switch (User.getInstance().dateFormat) {
            case 0:
                dateFormatter.formatString = "MM/DD/YYYY";
                break;
            case 1:
                dateFormatter.formatString = "YYYY-MM-DD";
                break;
            case 2:
                dateFormatter.formatString = "DD-MM-YYYY";
                break;
            case 3:
                dateFormatter.formatString = "DD/MM/YYYY";
                break;
            case 4:
                dateFormatter.formatString = "DD.MM.YYYY";
                break;
        }
    }

    override public function set data(val:Object):void {
        this.item = val as ReportExchangeItem;
        this.text = dateFormatter.format(item.dateAdded);
    }

    override public function get data():Object {
        return item;
    }
}
}