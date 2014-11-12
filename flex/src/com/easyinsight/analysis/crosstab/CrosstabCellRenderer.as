/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:22 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.DrillThrough;
import com.easyinsight.analysis.DrillThroughEvent;
import com.easyinsight.analysis.DrillThroughExecutor;
import com.easyinsight.analysis.Link;
import com.easyinsight.analysis.ReportWindowEvent;
import com.easyinsight.analysis.TextValueExtension;
import com.easyinsight.analysis.URLLink;
import com.easyinsight.pseudocontext.StandardContextWindow;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import flash.text.TextFormat;

import mx.controls.Alert;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;

public class CrosstabCellRenderer extends UIComponent implements IListItemRenderer {

    private var _cellProperty:String;

    private var text:UITextField;

    private var _report:CrosstabDefinition;

    public function CrosstabCellRenderer() {
        super();
        text = new UITextField();
        text.alpha = 1;
        percentHeight = 100;
        percentWidth = 100;
        addEventListener(MouseEvent.ROLL_OVER, onRollOver);
        addEventListener(MouseEvent.ROLL_OUT, onRollOut);
        addEventListener(MouseEvent.CLICK, onClick);
        mouseChildren = false;
        mouseEnabled = true;
    }

    private function onClick(event:MouseEvent):void {
        if (defaultLink != null) {
            if (defaultLink is URLLink) {
                var urlLink:URLLink = defaultLink as URLLink;
                var url:String = data[urlLink.label + "_link"];
                try {
                    navigateToURL(new URLRequest(url), "_blank");
                } catch (e:Error) {
                    Alert.show(e.message);
                }
            } else if (defaultLink is DrillThrough) {
                var drillThrough:DrillThrough = defaultLink as DrillThrough;
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough, crosstabValue.dtMap, analysisItem, _report);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            }
        }
    }

    private var analysisItem:AnalysisItem;

    private function onDrill(event:DrillThroughEvent):void {
        if (event.drillThrough.miniWindow) {
            dispatchEvent(new ReportWindowEvent(event.drillThroughResponse.descriptor.id, 0, 0, event.drillThroughResponse.filters, InsightDescriptor(event.drillThroughResponse.descriptor).dataFeedID,
                    InsightDescriptor(event.drillThroughResponse.descriptor).reportType));
        } else {
            dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.drillThroughResponse.descriptor, event.drillThroughResponse.filters));
        }
    }

    private var hyperlinked:Boolean;

    private function onRollOver(event:MouseEvent):void {
        if (hyperlinked) {
            text.setTextFormat(new TextFormat(text.getTextFormat().font, text.getTextFormat().size, text.getTextFormat().color,
                text.getTextFormat().bold, text.getTextFormat().italic, true));
        }
    }

    private function onRollOut(event:MouseEvent):void {
        if (hyperlinked) {
            text.setTextFormat(new TextFormat(text.getTextFormat().font, text.getTextFormat().size, text.getTextFormat().color,
                    text.getTextFormat().bold, text.getTextFormat().italic, false));
        }
    }

    private var defaultLink:Link;

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

    public function set cellProperty(value:String):void {
        _cellProperty = value;
    }

    public function get data():Object {
        return null;
    }

    override protected function commitProperties():void {
        super.commitProperties();
        if (crosstabValue != null) {
            if (_changed) {
                _changed = false;
                text.text = _valText;
                text.setTextFormat(_format);
                if (backgroundColor != 0xFFFFFF) {
                    text.backgroundColor = backgroundColor;
                    text.background = true;
                } else {
                    text.background = false;
                }
            }
        }
    }

    private var backgroundColor:uint = 0xFFFFFF;

    private var _changed:Boolean;
    private var _valText:String;
    private var _format:TextFormat;

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    private var crosstabValue:CrosstabValue;

    public function set data(value:Object):void {
        crosstabValue = value[_cellProperty];
        if (crosstabValue != null) {
            if (crosstabValue.dtMap != null) {
                hyperlinked = true;
                defaultLink = crosstabValue.measure.links.getItemAt(0) as Link;
            } else {
                hyperlinked = false;
                defaultLink = null;
            }
            if (crosstabValue.header == null) {
                if (crosstabValue.measure != null) {
                    _valText = crosstabValue.measure.getFormatter().format(crosstabValue.value);
                    analysisItem = crosstabValue.measure;
                } else {
                    _valText = "";
                }
            } else {
                if (crosstabValue.headerLabel) {
                    _valText = String(crosstabValue.value.getValue());
                } else {
                    _valText = crosstabValue.header.getFormatter().format(crosstabValue.value.getValue());
                }
            }
            var fontName:String = _report.getFont();
            if (_report.customFontFamily) {
                fontName = null;
            } else if (_report.getFont() == "Open Sans") {
                text.styleName = "myFontStyle";
            }
            backgroundColor = 0xFFFFFF;
            if (crosstabValue.header == null) {
                if (crosstabValue.summaryValue) {
                    var summaryColor:uint = _report.summaryTextColor;
                    _format = new TextFormat(fontName, 11, summaryColor, null);
                    _format.align = _report.align;
                } else {
                    var color:uint;
                    if (crosstabValue.value.valueExtension != null) {
                        var ext:TextValueExtension = crosstabValue.value.valueExtension as TextValueExtension;
                        color = ext.color;
                        backgroundColor = ext.backgroundColor;
                    } else {
                        color = 0x0;
                    }
                    _format = new TextFormat(fontName, 11, color, null);
                    _format.align = _report.align;
                }
            } else {
                if (crosstabValue.headerLabel) {
                    new StandardContextWindow(crosstabValue.header, passThrough, this, crosstabValue.value, _report);

                    var headerColor:uint;
                    if (crosstabValue.value.valueExtension != null) {
                        var ext2:TextValueExtension = crosstabValue.value.valueExtension as TextValueExtension;
                        if (ext2.color > 0) {
                            headerColor = ext2.color;
                        } else {
                            headerColor = _report.headerTextColor;
                        }
                    } else {
                        headerColor = _report.headerTextColor;
                    }
                    _format = new TextFormat(fontName, 12, headerColor, null);
                    _format.align = _report.align;
                } else {
                    _format = new TextFormat(fontName, 11, _report.headerTextColor, null);
                    _format.align = _report.align;
                }
            }
        } else {
            text.text = "";
        }
        _changed = true;
        invalidateProperties();
        invalidateDisplayList();
    }
}
}
