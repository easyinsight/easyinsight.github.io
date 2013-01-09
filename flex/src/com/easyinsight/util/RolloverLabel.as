/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 11/13/12
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.events.MouseEvent;

import mx.controls.advancedDataGridClasses.AdvancedDataGridItemRenderer;

public class RolloverLabel extends AdvancedDataGridItemRenderer {

    public function RolloverLabel() {
        super();
    }

    private function onRollover(event:MouseEvent):void {
        if (parent) {
            setStyle("textDecoration", "underline");
        }
    }

    private function onRollout(event:MouseEvent):void {
        if (parent) {
            setStyle("textDecoration", "none");
        }
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new RolloverLabelEvent(obj));
    }

    private var obj:Object;

    private var hasLinks:Boolean = false;

    protected function isLinkable(value:Object):Boolean {
        return false;
    }

    override public function get data():Object {
        return this.obj;
    }

    override public function set data(value:Object):void {
        super.data = value;
        var linkable:Boolean = isLinkable(value);
        if (linkable && !hasLinks) {
            hasLinks = true;
            addEventListener(MouseEvent.ROLL_OVER, onRollover);
            addEventListener(MouseEvent.ROLL_OUT, onRollout);
            addEventListener(MouseEvent.CLICK, onClick);
        } else if (!linkable && hasLinks) {
            hasLinks = false;
            removeEventListener(MouseEvent.ROLL_OVER, onRollover);
            removeEventListener(MouseEvent.ROLL_OUT, onRollout);
            removeEventListener(MouseEvent.CLICK, onClick);
        }
        this.obj = value;
    }
}
}
