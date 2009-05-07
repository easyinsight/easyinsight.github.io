package com.easyinsight.analysis.treemap {
import com.easyinsight.analysis.CustomChangeEvent;

public class ColorSchemeEvent extends CustomChangeEvent {

    public var colorScheme:int;

    public function ColorSchemeEvent(colorScheme:int) {
        super();
        this.colorScheme = colorScheme;
    }
}
}