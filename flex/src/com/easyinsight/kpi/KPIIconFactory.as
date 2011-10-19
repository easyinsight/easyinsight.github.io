package com.easyinsight.kpi {
public class KPIIconFactory {

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_green.png")]
    private static var positiveUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_green.png")]
    private static var positiveDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_red.png")]
    private static var negativeUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_red.png")]
    private static var negativeDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_ball_blue.png")]
    private static var neutralIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_blue.png")]
    private static var neutralUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_blue.png")]
    private static var neutralDownIcon:Class;

    public function KPIIconFactory() {
    }

    public static function iconForKPI(kpi:KPI):Class {
        var trendImage:Class;
        if (kpi.kpiOutcome != null) {
            switch (kpi.kpiOutcome.outcomeState) {
                case KPIOutcome.EXCEEDING_GOAL:
                case KPIOutcome.POSITIVE:
                    if (kpi.kpiOutcome.direction == KPIOutcome.UP_DIRECTION) {
                        trendImage = positiveUpIcon;
                    } else if (kpi.kpiOutcome.direction == KPIOutcome.DOWN_DIRECTION) {
                        trendImage = positiveDownIcon;
                    } else {
                        trendImage = positiveUpIcon;
                    }
                    break;
                case KPIOutcome.NEGATIVE:
                    if (kpi.kpiOutcome.direction == KPIOutcome.UP_DIRECTION) {
                        trendImage = negativeUpIcon;
                    } else if (kpi.kpiOutcome.direction == KPIOutcome.DOWN_DIRECTION) {
                        trendImage = negativeDownIcon;
                    } else {
                        trendImage = negativeDownIcon;
                    }
                    break;
                case KPIOutcome.NEUTRAL:
                    if (kpi.kpiOutcome.direction == KPIOutcome.UP_DIRECTION) {
                        trendImage = neutralUpIcon;
                    } else if (kpi.kpiOutcome.direction == KPIOutcome.DOWN_DIRECTION) {
                        trendImage = neutralDownIcon;
                    } else {
                        trendImage = neutralIcon;
                    }
                    break;
                case KPIOutcome.NO_DATA:
                default:
                    trendImage = neutralIcon;
                    break;
            }
        }
        return trendImage;
    }
}
}