/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/13/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.MouseEvent;

import mx.controls.dataGridClasses.DataGridItemRenderer;

public class RolloverLabelGridColumn extends DataGridItemRenderer {
    public function RolloverLabelGridColumn() {
        addEventListener(MouseEvent.ROLL_OVER, onRollover);
        addEventListener(MouseEvent.ROLL_OUT, onRollout);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onRollover(event:MouseEvent):void {
        setStyle("textDecoration", "underline");
    }

    private function onRollout(event:MouseEvent):void {
        setStyle("textDecoration", "none");
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new RolloverLabelEvent(obj));
    }

    private var obj:Object;

    override public function set data(value:Object):void {
        super.data = value;
        this.obj = value;
    }
}
}
