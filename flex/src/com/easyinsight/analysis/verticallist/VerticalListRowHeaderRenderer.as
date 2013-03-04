/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/18/11
 * Time: 2:48 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisMeasure;
import com.easyinsight.analysis.ytd.VerticalListReportExtension;
import com.easyinsight.analysis.ytd.YTDReportFieldExtension;

import mx.controls.listClasses.IListItemRenderer;
import mx.core.Application;
import mx.core.UIComponent;
import mx.core.UITextField;
import mx.core.UITextFormat;

public class VerticalListRowHeaderRenderer extends UIComponent implements IListItemRenderer {

    private var text:UITextField;

    public function VerticalListRowHeaderRenderer() {
        super();
        text = new UITextField();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private var value:Object;

    override protected function createChildren():void {
        super.createChildren();
        addChild(text);
    }

    override protected function commitProperties():void {
        super.commitProperties();

        if (data && parent) {
            if (_changed) {
                _changed = false;
                text.text = _valText;
                text.setTextFormat(_format);
            }
        }
    }

    private var _changed:Boolean;
    private var _valText:String;
    private var _format:UITextFormat;

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (text != null) {
            text.setActualSize(this.width, this.height);
        }
    }

    private var _report:AnalysisDefinition;

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function set data(value:Object):void {
        this.value = value;
        if (value == null) {
            return;
        }
        var align:String = "right";
        var bold:Object = null;
        var _analysisMeasure:AnalysisMeasure = value["baseMeasure"] as AnalysisMeasure;
        if (_analysisMeasure == null) {
            _valText = "";
        } else {
            _valText = _analysisMeasure.display;
            if (_analysisMeasure.reportFieldExtension != null && _analysisMeasure.reportFieldExtension is YTDReportFieldExtension) {
                var testExt:YTDReportFieldExtension = _analysisMeasure.reportFieldExtension as YTDReportFieldExtension;
                bold = testExt.alwaysShow;
                if (testExt.alwaysShow) {
                    align = "left";
                }
            } else if (_analysisMeasure.reportFieldExtension != null && _analysisMeasure.reportFieldExtension is VerticalListReportExtension) {
                var vertExt:VerticalListReportExtension = _analysisMeasure.reportFieldExtension as VerticalListReportExtension;
                bold = vertExt.alwaysShow;
                if (testExt.alwaysShow) {
                    align = "left";
                }
            }
        }
        var fontName:String = _report.getFont();
        if (_report.getFont() == "Open Sans" && !bold) {
            text.styleName = "myFontStyle";
        } else if (_report.getFont() == "Open Sans" && bold) {
            text.styleName = "boldStyle";
            fontName = "Open Sans Bold";
        }

        var tf:UITextFormat = new UITextFormat(Application(Application.application).systemManager, fontName, 10, 0, bold);
        tf.align = align;
        _format = tf;
        _changed = true;
        invalidateProperties();
    }

    public function get data():Object {
        return this.value;
    }
}
}
