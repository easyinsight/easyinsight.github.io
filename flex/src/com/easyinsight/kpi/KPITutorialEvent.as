package com.easyinsight.kpi {
import flash.events.Event;

public class KPITutorialEvent extends Event {

    public static const NEXT:String = "kpiTutorialNext";
    public static const PREVIOUS:String = "kpiTutorialPrevious";
    public static const CANCEL:String = "kpiTutorialCancel";
    public static const FINISH:String = "kpiTutorialFinish";

    public function KPITutorialEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new KPITutorialEvent(type);
    }
}
}