/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {

import com.easyinsight.analysis.TextValueExtension;

import flash.text.TextFormat;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.formatters.Formatter;

public class CrosstabCellRenderer extends UIComponent implements IListItemRenderer {

    private var _formatter:Formatter;
    private var _cellProperty:String;

    private var text:UITextField;

    private var _report:CrosstabDefinition;

    public function CrosstabCellRenderer() {
        text = new UITextField();
        text.alpha = 1;
        percentHeight = 100;
        percentWidth = 100;
    }

    public function set report(value:CrosstabDefinition):void {
        _report = value;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (text != null) {
            text.setActualSize(this.width, this.height);
        }
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

    public function get data():Object {
        return null;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        if (crosstabValue != null) {
            if (crosstabValue.header == null) {
                if (crosstabValue.summaryValue) {
                    text.setTextFormat(new TextFormat(_report.getFont(), 11, 0xFFFFFF, null));
                } else {
                    var color:uint;
                    if (crosstabValue.value.valueExtension != null) {
                        var ext:TextValueExtension = crosstabValue.value.valueExtension as TextValueExtension;
                        color = ext.color;
                    } else {
                        color = 0x0;
                    }
                    text.setTextFormat(new TextFormat(_report.getFont(), 11, color, null));
                }
            } else {
                if (crosstabValue.headerLabel) {
                    text.setTextFormat(new TextFormat(_report.getFont(), 12, 0xFFFFFF, null));
                } else {
                    text.setTextFormat(new TextFormat(_report.getFont(), 11, 0xFFFFFF, null));
                }
            }
        }
    }

    private var crosstabValue:CrosstabValue;

    public function set data(value:Object):void {
        crosstabValue = value[_cellProperty];
        if (crosstabValue != null) {
            if (crosstabValue.header == null) {
                text.text = _formatter.format(crosstabValue.value);
            } else {
                if (crosstabValue.headerLabel) {
                    text.text = String(crosstabValue.value.getValue());
                } else {
                    text.text = crosstabValue.header.getFormatter().format(crosstabValue.value.getValue());
                }
            }
        } else {
            text.text = "";
        }
        invalidateProperties();
        invalidateDisplayList();
    }
}
}
