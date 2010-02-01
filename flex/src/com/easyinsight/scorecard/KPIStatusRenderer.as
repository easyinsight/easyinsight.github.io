package com.easyinsight.scorecard {
import com.easyinsight.kpi.KPI;
import com.easyinsight.kpi.KPIOutcome;

import mx.containers.HBox;
import mx.controls.Image;

public class KPIStatusRenderer extends HBox {

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_green.png")]
    private var positiveUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_green.png")]
    private var positiveDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_ball_green.png")]
    private var positiveIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_red.png")]
    private var negativeUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_red.png")]
    private var negativeDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_square_glass_red.png")]
    private var negativeIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_ball_blue.png")]
    private var neutralIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_up_blue.png")]
    private var neutralUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/arrow2_down_blue.png")]
    private var neutralDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../assets/bullet_square_grey.png")]
    private var noDataIcon:Class;

    private var image:Image;

    public function KPIStatusRenderer() {
        super();
        image = new Image();
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }
    
    private var _kpi:KPI;

    override public function set data(val:Object):void {
        this._kpi = val as KPI;
        var trendImage:Class;
        if (_kpi.kpiOutcome != null) {
            if (_kpi.kpiOutcome.problemEvaluated) {
                trendImage = negativeIcon;
            } else if (_kpi.problemConditions.length > 0) {
                trendImage = positiveIcon;
            } else {
                switch (_kpi.kpiOutcome.outcomeState) {
                    case KPIOutcome.EXCEEDING_GOAL:
                    case KPIOutcome.POSITIVE:
                        if (_kpi.kpiOutcome.direction == KPIOutcome.UP_DIRECTION) {
                            trendImage = positiveUpIcon;
                        } else if (_kpi.kpiOutcome.direction == KPIOutcome.DOWN_DIRECTION) {
                            trendImage = positiveDownIcon;
                        } else {
                            trendImage = positiveIcon;
                        }
                        break;
                    case KPIOutcome.NEGATIVE:
                        if (_kpi.kpiOutcome.direction == KPIOutcome.UP_DIRECTION) {
                            trendImage = negativeUpIcon;
                        } else if (_kpi.kpiOutcome.direction == KPIOutcome.DOWN_DIRECTION) {
                            trendImage = negativeDownIcon;
                        } else {
                            trendImage = negativeIcon;
                        }
                        break;
                    case KPIOutcome.NEUTRAL:
                        if (_kpi.kpiOutcome.direction == KPIOutcome.UP_DIRECTION) {
                            trendImage = neutralUpIcon;
                        } else if (_kpi.kpiOutcome.direction == KPIOutcome.DOWN_DIRECTION) {
                            trendImage = neutralDownIcon;
                        } else {
                            trendImage = negativeIcon;
                        }
                        break;
                    case KPIOutcome.NO_DATA:
                        trendImage = noDataIcon;
                        break;
                }
            }
        }
        image.source = trendImage;
    }

    override public function get data():Object {
        return this._kpi;
    }
}
}