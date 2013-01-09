/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/31/12
 * Time: 1:33 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.events.MouseEvent;

import mx.core.mx_internal;

use namespace mx_internal;
import mx.controls.PopUpMenuButton;

public class ArghButton extends PopUpMenuButton {
    public function ArghButton() {
    }

    mx_internal override function overArrowButton(event:MouseEvent):Boolean {
        return true;
    }
}
}
