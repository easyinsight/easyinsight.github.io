package com.easyinsight.analysis.service {
import flash.events.Event;

public class ReportRetrievalFault extends Event{

    public static const RETRIEVAL_FAULT:String = "retrievalFault";

    public var message:String;

    public function ReportRetrievalFault(message:String) {
        super(RETRIEVAL_FAULT, true);
        this.message = message;
    }
}
}