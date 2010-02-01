package com.easyinsight.kpi {
import com.easyinsight.kpi.KPI;

import flash.events.Event;

public class KPIEvent extends Event {

    public static const KPI_ADDED:String = "kpiAdded";
    public static const KPI_EDITED:String = "kpiEdited";
    public static const KPI_REMOVED:String = "kpiRemoved";

    public var kpi:KPI;

    public function KPIEvent(type:String, kpi:KPI) {
        super(type, true);
        this.kpi = kpi;
    }

    override public function clone():Event {
        return new KPIEvent(type, kpi);
    }
}
}