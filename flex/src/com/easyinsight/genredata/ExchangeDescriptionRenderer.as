package com.easyinsight.genredata {
import mx.controls.Text;

public class ExchangeDescriptionRenderer extends Text {

    private var reportExchangeItem:ExchangeItem;

    public function ExchangeDescriptionRenderer() {
        super();
        selectable = false;
        setStyle("fontSize", 11);
    }

    override public function set data(val:Object):void {
        reportExchangeItem = val as ExchangeItem;
        this.text = reportExchangeItem.description;
    }

    override public function get data():Object {
        return reportExchangeItem;
    }
}
}