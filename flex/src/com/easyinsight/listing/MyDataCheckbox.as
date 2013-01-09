/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 4/23/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.quicksearch.EIDescriptor;

import mx.containers.HBox;
import mx.controls.CheckBox;

public class MyDataCheckbox extends HBox {

    private var checkbox:CheckBox = new CheckBox();

    public function MyDataCheckbox() {
    }

    override protected function createChildren():void {
        super.createChildren();
        if (checkbox == null) {
            checkbox = new CheckBox();
            addChild(checkbox);
        }
    }

    private var eiDesc:EIDescriptor;

    override public function set data(val:Object):void {
        if (val is EIDescriptor) {
            eiDesc.selected = val as Boolean;
        }

    }

    override public function get data():Object {
        return eiDesc.selected;
    }
}
}
