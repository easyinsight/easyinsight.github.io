package com.easyinsight.analysis.heatmap {
import flash.display.Sprite;
import flash.text.TextField;

public class CustomPointWindow extends Sprite {

    private var _point:Object;
    private var _heatMap:HeatMapDefinition;

    public function CustomPointWindow() {
        super();
        this.height = 40;
        this.width = 40;
    }

    public function set point(value:Object):void {
        _point = value;
        updateContent();
    }

    public function set heatMap(value:HeatMapDefinition):void {
        _heatMap = value;
        updateContent();
    }

    private function updateContent():void {
        if (_point != null && _heatMap != null) {
            var text:TextField = new TextField();
            text.text = _point[_heatMap.measure.qualifiedName()];
            addChild(text);
        }
    }
}
}