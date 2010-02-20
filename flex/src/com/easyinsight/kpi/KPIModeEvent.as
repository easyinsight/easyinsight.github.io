package com.easyinsight.kpi {
import flash.events.Event;

public class KPIModeEvent extends Event {

    public static const EXPERT_MODE:String = "expertMode";
    public static const WIZARD_MODE:String = "wizardMode";
    public static const CANCEL:String = "kpiCancel";

    public function KPIModeEvent(type:String) {
        super(type);
    }

    override public function clone():Event {
        return new KPIModeEvent(type);
    }
}
}