package com.easyinsight.analysis.maps {
import com.easyinsight.analysis.conditions.ConditionRenderer;

import mx.containers.VBox;
import mx.controls.Alert;
import mx.formatters.Formatter;

public class ColorRangeLegend extends VBox {

    private var _points:int;
    private var _minValue:Number;
    private var _maxValue:Number;
    private var _conditionRenderer:ConditionRenderer;
    private var _formatter:Formatter;

    public function ColorRangeLegend() {
        super();
    }

    public function recreate():void {
        removeAllChildren();
        if (_points > 0) {            
            var delta:Number = (_maxValue - _minValue) / Number((_points - 1));
            var currentVal:Number = _minValue;

            for (var i:int = 0; i < _points; i++) {
                var colorItem:ColorRangeItem = new ColorRangeItem();
                colorItem.valueColor = _conditionRenderer.getColor(currentVal);;
                colorItem.valueString = _formatter.format(currentVal);

                addChild(colorItem);
                currentVal += delta;
            }
        }
    }


    public function set formatter(value:Formatter):void {
        _formatter = value;
    }

    public function set points(value:int):void {
        _points = value;
    }

    public function set minValue(value:Number):void {
        _minValue = value;
    }

    public function set maxValue(value:Number):void {
        _maxValue = value;
    }

    public function set conditionRenderer(value:ConditionRenderer):void {
        _conditionRenderer = value;
    }
}
}