package com.easyinsight.genredata {
import mx.controls.Label;
import mx.formatters.DateFormatter;

public class ReportDateRenderer extends Label {

    private var item:ReportExchangeItem;
    private var dateFormatter:DateFormatter;

    public function ReportDateRenderer() {
        super();
        dateFormatter = new DateFormatter();
        dateFormatter.formatString = "MM/DD/YYYY";
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