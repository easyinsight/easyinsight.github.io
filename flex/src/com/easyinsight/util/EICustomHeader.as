package com.easyinsight.util {
import com.easyinsight.groups.*;

import mx.controls.Label;
public class EICustomHeader extends Label {
    public function EICustomHeader() {
        setStyle("fontWeight", "bold");
        setStyle("fontSize", 13);
        setStyle("fontFamily", "Tahoma");
        setStyle("color", 0x323232);
        setStyle("textAlign", "center");
    }
}
}