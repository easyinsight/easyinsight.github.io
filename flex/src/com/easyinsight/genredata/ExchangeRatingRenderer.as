package com.easyinsight.genredata {

import mx.containers.HBox;
import mx.controls.Label;
import mx.formatters.NumberFormatter;

public class ExchangeRatingRenderer extends HBox {

    private var reportExchangeItem:ReportExchangeItem;
    private var ratingLabel:Label;

    public function ExchangeRatingRenderer() {
        super();
        setStyle("horizontalAlign", "center");
        percentWidth = 100;
        ratingLabel = new Label();
    }

    protected override function createChildren():void {
        super.createChildren();
        addChild(ratingLabel);
    }

    override public function set data(val:Object):void {
        reportExchangeItem = val as ReportExchangeItem;
        var nf:NumberFormatter = new NumberFormatter();
        nf.precision = 2;
        ratingLabel.text = nf.format(reportExchangeItem.ratingAverage);
    }

    override public function get data():Object {
        return reportExchangeItem;
    }
}
}