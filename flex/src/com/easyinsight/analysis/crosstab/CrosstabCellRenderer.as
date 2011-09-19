/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {

import flash.text.TextFormat;

import mx.controls.Alert;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.formatters.Formatter;

public class CrosstabCellRenderer extends UIComponent implements IListItemRenderer {

    private var _formatter:Formatter;
    private var _cellProperty:String;

    private var text:UITextField;

    //private var background:UIComponent;

    public function CrosstabCellRenderer() {
        text = new UITextField();
        text.alpha = 1;
        /*background = new UIComponent();
        background.percentWidth = 100;
        background.percentHeight = 100;*/
        percentHeight = 100;
        percentWidth = 100;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (unscaledHeight > 24) {
            Alert.show("larger field");
        }
        if (text != null) {
            text.setActualSize(this.width, this.height);
        }
    }

    override protected function createChildren():void {
        super.createChildren();
        /*addChild(background);*/
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
                    text.setTextFormat(new TextFormat("Lucida Grande", 11, 0xFFFFFF, null));
                } else {
                    text.setTextFormat(new TextFormat("Lucida Grande", 11, 0x000000, null));
                }
            } else {
                if (crosstabValue.headerLabel) {
                    text.setTextFormat(new TextFormat("Lucida Grande", 12, 0xFFFFFF, null));
                } else {
                    text.setTextFormat(new TextFormat("Lucida Grande", 11, 0xFFFFFF, null));
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
                text.text = crosstabValue.header.getFormatter().format(crosstabValue.value.getValue());
            }
        } else {
            text.text = "";
        }
        invalidateProperties();
        invalidateDisplayList();
    }
}
}
