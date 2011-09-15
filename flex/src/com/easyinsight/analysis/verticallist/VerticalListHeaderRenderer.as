/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/11
 * Time: 12:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import mx.controls.Label;

public class VerticalListHeaderRenderer extends Label {

    private var _headerText:String;

    public function VerticalListHeaderRenderer() {
        setStyle("textAlign", "center");
        setStyle("color", 0xFFFFFF);
        setStyle("fontSize", 11);
        this.percentWidth = 100;
    }

    public function set headerText(value:String):void {
        _headerText = value;
        this.text = _headerText;
    }
}
}
