package com.easyinsight.genredata {
import com.easyinsight.analysis.ReportRating;

public class ExchangeRatingRenderer extends ReportRating{

    private var reportExchangeItem:ReportExchangeItem;

    public function ExchangeRatingRenderer() {
        super();
        rateable = true;
    }

    override public function set data(val:Object):void {
        reportExchangeItem = val as ReportExchangeItem;
        reportID = reportExchangeItem.id;
        score = reportExchangeItem.ratingAverage;
    }

    override public function get data():Object {
        return reportExchangeItem;
    }
}
}