package com.easyinsight.solutions {
import flash.display.GradientType;
import flash.geom.Matrix;

import mx.core.UIComponent;

public class HeaderShadeArea extends UIComponent {

    private var _leftColor:int;
    private var _rightColor:int;

    public function HeaderShadeArea() {
        super();
    }


    public function set leftColor(value:int):void {
        _leftColor = value;
        invalidateDisplayList();
    }

    public function set rightColor(value:int):void {
        _rightColor = value;
        invalidateDisplayList();
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        graphics.clear();
        var gradientBoxMatrix:Matrix = new Matrix();
        gradientBoxMatrix.createGradientBox(this.width, this.height, 0, 0, 0);
        graphics.beginGradientFill(GradientType.LINEAR, [_leftColor, _rightColor], [1, 1], [0, 255], gradientBoxMatrix);
        graphics.drawRect(0, 0, this.width, this.height);
        graphics.endFill();
    }
}
}