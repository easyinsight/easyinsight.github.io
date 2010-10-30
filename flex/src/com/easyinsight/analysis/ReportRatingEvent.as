package com.easyinsight.analysis {
import flash.events.Event;

public class ReportRatingEvent extends Event {

    public static const REPORT_RATING:String = "reportRating";

    public var newRating:Number;

    public function ReportRatingEvent(newRating:Number) {
        super(REPORT_RATING);
        this.newRating = newRating;
    }

    override public function clone():Event {
        return new ReportRatingEvent(newRating);
    }
}
}