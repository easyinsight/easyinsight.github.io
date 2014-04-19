package com.easyinsight.analysis
{


import com.easyinsight.analysis.list.ListDefinition;
import com.easyinsight.filtering.FilterValueDefinition;
import com.easyinsight.pseudocontext.PseudoContextWindow;
import com.easyinsight.pseudocontext.StandardContextWindow;
import com.easyinsight.report.ReportNavigationEvent;
import com.easyinsight.solutions.InsightDescriptor;

import flash.events.Event;
import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.collections.ArrayCollection;

import mx.controls.Alert;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.UIComponentGlobals;
import mx.core.UITextField;
import mx.core.UITextFormat;
import mx.core.mx_internal;
import mx.events.FlexEvent;
import mx.formatters.Formatter;
import mx.managers.CursorManager;
import mx.managers.ILayoutManagerClient;
import mx.managers.PopUpManager;
use namespace mx_internal;

public class AnalysisCellRenderer extends UITextField implements IListItemRenderer, ILayoutManagerClient
{
    private var _data:Object;
    private var _analysisItem:AnalysisItem;
    private var _selectionEnabled:Boolean;
    private var _report:AnalysisDefinition;
    private var _rolloverIcon:Class;

    private var hyperlinked:Boolean;

    public function AnalysisCellRenderer() {
        super();
        this.percentWidth = 100;
    }

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function set rolloverIcon(value:Class):void {
        _rolloverIcon = value;
    }

    private function onClick(event:MouseEvent):void {
        if (defaultLink != null) {
            if (defaultLink is URLLink) {
                var urlLink:URLLink = defaultLink as URLLink;
                var url:String = data[_analysisItem.qualifiedName() + "_link"];
                try {
                    navigateToURL(new URLRequest(url), "_blank");
                } catch (e:Error) {
                    Alert.show(e.message);
                }
            } else if (defaultLink is DrillThrough) {
                var values:ArrayCollection = null;
                var drillThrough:DrillThrough = defaultLink as DrillThrough;
                var executor:DrillThroughExecutor = new DrillThroughExecutor(drillThrough, data, analysisItem, _report, null, values);
                executor.addEventListener(DrillThroughEvent.DRILL_THROUGH, onDrill);
                executor.send();
            }
        }
    }

    private function onDrill(event:DrillThroughEvent):void {
        if (event.drillThrough.miniWindow) {
            dispatchEvent(new ReportWindowEvent(event.drillThroughResponse.descriptor.id, 0, 0, event.drillThroughResponse.filters, InsightDescriptor(event.drillThroughResponse.descriptor).dataFeedID,
                    InsightDescriptor(event.drillThroughResponse.descriptor).reportType));
        } else {
            dispatchEvent(new ReportNavigationEvent(ReportNavigationEvent.TO_REPORT, event.drillThroughResponse.descriptor, event.drillThroughResponse.filters,
                event.drillThroughResponse.additionalFields));
        }
    }

    private function onRollOver(event:MouseEvent):void {
        if (_rolloverIcon && !_selectionEnabled) {
            CursorManager.setCursor(_rolloverIcon);
        }
        if (hyperlinked) {
            if (utf != null) {
                setTextFormat(hyperlinkedUTF);
                invalidateProperties();
            }
        }
    }

    private var hyperlinkedUTF:UITextFormat;
    private var utf:UITextFormat;

    private function onRollOut(event:MouseEvent):void {
        if (_rolloverIcon && !_selectionEnabled) {
            CursorManager.removeAllCursors();
        }
        if (hyperlinked) {
            if (utf != null) {
                setTextFormat(utf);
                invalidateProperties();
            }
        }
    }

    private function passThrough(event:Event):void {
        dispatchEvent(event);
    }

    public function get analysisItem():AnalysisItem {
        return _analysisItem;
    }

    private var defaultLink:Link;

    public function set analysisItem(val:AnalysisItem):void {
        _analysisItem = val;
        if (_analysisItem != null) {
            toolTip = _analysisItem.tooltip;
            if (_analysisItem.links != null) {
                for each (var link:Link in _analysisItem.links) {
                    if (link.defaultLink) {
                        defaultLink = link;
                        break;
                    }
                }
            }
        }
    }

    public function validateProperties():void {
        validateNow();
    }

    public function validateSize(recursive:Boolean = false):void {
        validateNow();
    }

    public function validateDisplayList():void {
        validateNow();
    }

    private var newColor:uint;
    private var explicitColor:uint;

    private var _changed:Boolean;
    private var _valText:String;
    private var _format:UITextFormat;

    override public function validateNow():void {

        if (data && parent) {
            if (_changed) {
                _changed = false;
                setText(_valText);
                setTextFormat(_format);
            }
            if (newColor != explicitColor)
            {
                styleChangedFlag = true;
                explicitColor = newColor;
                invalidateDisplayList();
            }
        }
        super.validateNow();

    }



    public function set data(value:Object):void {
        _data = value;
        var color:uint = 0;
        var backgroundColor:uint = 0xFFFFFF;
        var bold:Object = null;
        var text:String;
        if (value != null) {
            var field:String = analysisItem.qualifiedName();
            var formatter:Formatter = analysisItem.getFormatter();
            if (value[field] is Value) {
                var objVal:Value = value[field];
                if (objVal == null) {
                    text = "";
                } else {
                    text = formatter.format(objVal.getValue());
                }
                if (_report is ListDefinition) {
                    var listDefinition:ListDefinition = _report as ListDefinition;
                    if (objVal.summary) {
                        color = listDefinition.summaryRowTextColor;
                    } else {
                        if (objVal.valueExtension != null) {
                            var ext:TextValueExtension = objVal.valueExtension as TextValueExtension;
                            color = ext.color;
                            if (ext.bold) {
                                bold = true;
                            }
                            backgroundColor = ext.backgroundColor;
                        } else {
                            color = listDefinition.textColor;
                        }
                    }
                }
                if (defaultLink != null && objVal != null && objVal.type() != Value.EMPTY) {
                    hyperlinked = true;
                }
            } else {
                hyperlinked = false;
                if (value[field] != null) {
                    text = formatter.format(value[field]);
                } else {
                    text = "";
                }

            }
        } else {
            text = "";
        }

        //setText(text);
        _valText = text;

        var rext:TextReportFieldExtension = analysisItem.reportFieldExtension as TextReportFieldExtension;
        var align:String = "left";
        if (rext != null && rext.align != null) {
            align = rext.align.toLowerCase();
        }
        var fontName:String = _report.getFont();
        if (_report.getFont() == "Open Sans" && !bold) {
            styleName = "myFontStyle";
        } else if (_report.getFont() == "Open Sans" && bold) {
            styleName = "boldStyle";
            fontName = "Open Sans Bold";
        } else if (_report.useCustomFontFamily) {
            fontName = null;
        }
        this.newColor = color;
        utf = new UITextFormat(this.systemManager, fontName, _report.fontSize, color, bold, null, false);
        utf.align = align;
        if (hyperlinked) {
            hyperlinkedUTF = new UITextFormat(this.systemManager, fontName, _report.fontSize, color, bold, null, true);
            hyperlinkedUTF.align = align;
        }
        if (hyperlinked && !hasLinks) {
            hasLinks = true;
            addEventListener(MouseEvent.ROLL_OVER, onRollOver);
            addEventListener(MouseEvent.ROLL_OUT, onRollOut);
            addEventListener(MouseEvent.CLICK, onClick);
        } else if (!hyperlinked && hasLinks) {
            hasLinks = false;
            removeEventListener(MouseEvent.ROLL_OVER, onRollOver);
            removeEventListener(MouseEvent.ROLL_OUT, onRollOut);
            removeEventListener(MouseEvent.CLICK, onClick);
        }

        _format = utf;
        _changed = true;

        if (backgroundColor != 0xFFFFFF) {
            this.backgroundColor = backgroundColor;
            this.background = true;
        } else {
            this.background = false;
        }
        new StandardContextWindow(analysisItem, passThrough, this, value, _report);
        UIComponentGlobals.layoutManager.invalidateProperties(this);
        invalidateSize();
        invalidateProperties();
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    private var hasLinks:Boolean;

    protected function setText(text:String):void {
        this.text = text;
    }

    public function get data():Object {
        return _data;
    }

}
}