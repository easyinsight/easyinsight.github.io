/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {

import mx.containers.Box;
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.formatters.Formatter;

public class CrosstabCellRenderer2 extends Box {

    private var _formatter:Formatter;
    private var _cellProperty:String;

    private var text:Label;

    public function CrosstabCellRenderer2() {
        text = new Label();
        percentHeight = 100;
        percentWidth = 100;
    }

    override protected function createChildren():void {
        super.createChildren();
        addChild(text);
    }

    public function set formatter(value:Formatter):void {
        _formatter = value;
    }

    public function set cellProperty(value:String):void {
        _cellProperty = value;
    }

    override public function get data():Object {
        return null;
    }

    private var backgroundColor:uint;

    override public function set data(value:Object):void {
        Alert.show("blah");
        var crosstabValue:CrosstabValue = value[_cellProperty];
        if (crosstabValue != null) {
            if (crosstabValue.header == null) {
                //setStyle("backgroundColor", 0xFFFFFF);
                //text.setStyle("color", 0);
                text.text = _formatter.format(crosstabValue.value);
            } else {
                //setStyle("backgroundColor", 0x333333);
                //text.setStyle("color", 0xFFFFFF);
                text.text = crosstabValue.header.getFormatter().format(crosstabValue.value);
            }
        } else {
            backgroundColor = 0xFFFFFF;
            text.text = "";
        }
        invalidateDisplayList();
    }
}
}
