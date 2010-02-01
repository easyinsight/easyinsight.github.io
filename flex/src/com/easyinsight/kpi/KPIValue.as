package com.easyinsight.kpi {
[Bindable]
[RemoteClass(alias="com.easyinsight.kpi.KPIValue")]
public class KPIValue {

    public var value:Number;
    public var date:Date;
    public var kpiID:int;

    public function KPIValue() {
    }
}
}