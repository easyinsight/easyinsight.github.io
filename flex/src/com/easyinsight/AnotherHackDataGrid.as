/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/27/11
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight {
import mx.controls.AdvancedDataGrid;
import mx.events.DragEvent;

public class AnotherHackDataGrid extends AdvancedDataGrid {
    public function AnotherHackDataGrid() {
    }

    override protected function dragStartHandler(event:DragEvent):void {
        dispatchEvent(event);
        super.dragStartHandler(event);
    }
}
}
