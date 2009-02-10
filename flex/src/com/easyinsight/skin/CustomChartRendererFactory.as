package com.easyinsight.skin {
import mx.core.IFactory;
public class CustomChartRendererFactory implements IFactory {
    public function CustomChartRendererFactory() {
    }

    public function newInstance():* {
        var renderer:CustomDropShadowRenderer = new CustomDropShadowRenderer();
        
        return renderer;
    }
}
}