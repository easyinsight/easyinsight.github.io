/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/21/11
 * Time: 10:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import com.easyinsight.analysis.crosstab.CrosstabValue;

import mx.formatters.Formatter;

import spark.components.gridClasses.GridItemRenderer;
import spark.components.supportClasses.StyleableTextField;

public class SparkCrosstabCellRenderer extends GridItemRenderer {

    private var text:StyleableTextField;

    public function SparkCrosstabCellRenderer() {
        text = new StyleableTextField();
        text.setStyle("textAlign", "right");
    }

    private var crosstabValue:CrosstabValue;

    private var _cellProperty:String;

    override protected function createChildren():void {
        super.createChildren();
        addElement(text);
    }

    public function set cellProperty(value:String):void {
        _cellProperty = value;
    }

    private var _formatter:Formatter;

    public function set formatter(value:Formatter):void {
        _formatter = value;
    }

    private var _colorChanged:Boolean = false;

    private var _lastCol:Boolean;

    public function set lastCol(value:Boolean):void {
        _lastCol = value;
    }

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (_colorChanged) {
            text.width = this.width;
            _colorChanged = false;
            if (crosstabValue != null) {
                if (crosstabValue.header == null) {
                    if (crosstabValue.summaryValue) {
                        graphics.beginFill(0x555555, 1.0);
                        graphics.drawRect(0, 0, this.width, this.height);
                        graphics.endFill();
                        text.textColor = 0xFFFFFF;
                        text.setStyle("fontWeight", "normal");
                    } else {
                        graphics.beginFill(0xFFFFFF, 1.0);
                        graphics.drawRect(0, 0, this.width, this.height);
                        graphics.endFill();
                        text.textColor = 0x000000;
                        text.setStyle("fontWeight", "normal");
                    }
                } else {
                    if (crosstabValue.headerLabel) {
                        graphics.beginFill(0x333333, 1.0);
                        graphics.drawRect(0, 0, this.width, this.height);
                        graphics.endFill();
                        text.textColor = 0xFFFFFF;
                        text.setStyle("fontWeight", "bold");
                    } else {
                        graphics.beginFill(0x333333, 1.0);
                        graphics.drawRect(0, 0, this.width, this.height);
                        graphics.endFill();
                        text.textColor = 0xFFFFFF;
                        text.setStyle("fontWeight", "normal");
                    }
                }
            } else {
                if (rowIndex < 2 || columnIndex == 0) {
                    graphics.beginFill(0x333333, 1.0);
                    graphics.drawRect(0, 0, this.width, this.height);
                    graphics.endFill();
                } else if (_lastCol) {
                    graphics.beginFill(0x555555, 1.0);
                    graphics.drawRect(0, 0, this.width, this.height);
                    graphics.endFill();
                }
            }
        }
    }

    override public function set data(value:Object):void {
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
        _colorChanged = true;
        invalidateDisplayList();
    }
}
}
