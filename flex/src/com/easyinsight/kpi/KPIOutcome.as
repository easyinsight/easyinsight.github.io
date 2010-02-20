package com.easyinsight.kpi {
[Bindable]
[RemoteClass(alias="com.easyinsight.kpi.KPIOutcome")]
public class KPIOutcome {


    public static const NO_DATA:int = 0;
    public static const EXCEEDING_GOAL:int = 1;
    public static const POSITIVE:int = 2;
    public static const NEUTRAL:int = 3;
    public static const NEGATIVE:int = 4;

    public static const DOWN_DIRECTION:int = 1;
    public static const NO_DIRECTION:int = 2;
    public static const UP_DIRECTION:int = 3;

    public var outcomeState:int;
    public var direction:int;
    public var previousValue:Number;
    public var problemEvaluated:Boolean;
    public var outcomeValue:Number;
    public var evaluationDate:Date;
    public var kpiID:int;
    public var valueDefined:Boolean;
    public var percentChange:Number;
    public var directional:Boolean;

    public function KPIOutcome() {
        
    }
}
}