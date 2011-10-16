package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.TrendOutcome;
import com.easyinsight.kpi.KPIOutcome;

import mx.controls.Image;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.events.FlexEvent;

public class TrendStatusRenderer extends UIComponent implements IListItemRenderer  {

    [Bindable]
    [Embed(source="../../../../../assets/32x32/arrow2_up_green.png")]
    private static var positiveUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/32x32/arrow2_down_green.png")]
    private static var positiveDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/32x32/arrow2_up_red.png")]
    private static var negativeUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/32x32/arrow2_down_red.png")]
    private static var negativeDownIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/32x32/bullet_ball_blue.png")]
    private static var neutralIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/32x32/arrow2_up_blue.png")]
    private static var neutralUpIcon:Class;

    [Bindable]
    [Embed(source="../../../../../assets/32x32/arrow2_down_blue.png")]
    private static var neutralDownIcon:Class;

    private var image:Image;

    public function TrendStatusRenderer() {
        super();
        image = new Image();
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(image);
    }
    
    private var _kpi:TrendOutcome;

    [Bindable("dataChange")]
    public function set data(val:Object):void {
        this._kpi = val as TrendOutcome;
        if (_kpi != null) {
            image.source = iconForKPI(_kpi);
            dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
        }
    }

    public static function iconForKPI(kpi:TrendOutcome):Class {
        var trendImage:Class;
        switch (kpi.outcome) {
            case KPIOutcome.EXCEEDING_GOAL:
            case KPIOutcome.POSITIVE:
                if (kpi.direction == KPIOutcome.UP_DIRECTION) {
                    trendImage = positiveUpIcon;
                } else {
                    trendImage = positiveDownIcon;
                }
                break;
            case KPIOutcome.NEGATIVE:
                if (kpi.direction == KPIOutcome.UP_DIRECTION) {
                    trendImage = negativeUpIcon;
                } else {
                    trendImage = negativeDownIcon;
                }
                break;
            case KPIOutcome.NEUTRAL:
                if (kpi.direction == KPIOutcome.UP_DIRECTION) {
                    trendImage = neutralUpIcon;
                } else if (kpi.direction == KPIOutcome.DOWN_DIRECTION) {
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
        return trendImage;
    }

    public function get data():Object {
        return this._kpi;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        var vMargin:int = (this.unscaledHeight - 16) / 2;
        var hMargin:int = (this.unscaledWidth - 16) / 2;
        image.move(hMargin, vMargin);
        image.setActualSize(16, 16);
    }
}
}