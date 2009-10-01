package com.easyinsight.solutions {
import com.easyinsight.quicksearch.EIDescriptor;
public class EmptyReportDescriptor extends EIDescriptor {

    public var feedID:int;
    public var feedName:String;

    public function EmptyReportDescriptor(feedID:int, feedName:String) {
        this.feedID = feedID;
        this.feedName = feedName;
    }

    override public function getType():int {
        return EIDescriptor.EMPTY;
    }
}
}