package com.easyinsight.skin {
import mx.core.IFactory;
public class CustomChartRendererFactory implements IFactory {

    private var _selectedFill:Class;
    private var _rolloverFill:Class;

    public function CustomChartRendererFactory(selectedFill:Class, rolloverFill:Class) {
        _selectedFill = selectedFill;
        _rolloverFill = rolloverFill;
    }

    public function newInstance():* {
        var renderer:CustomDropShadowRenderer = new CustomDropShadowRenderer();
        renderer.rolloverFill = _rolloverFill;
        renderer.selectedFill = _selectedFill;
        return renderer;
    }
}
}