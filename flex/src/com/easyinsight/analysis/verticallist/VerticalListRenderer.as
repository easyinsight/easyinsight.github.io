/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/11
 * Time: 11:27 AM
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

public class VerticalListRenderer extends UIComponent implements IListItemRenderer {

    private var text:UITextField;

    public function VerticalListRenderer() {
        super();
        /*var tf:UITextFormat = new UITextFormat(Application(Application.application).systemManager, "Lucida Grande", 12);
        tf.align = "right";
        setTextFormat(tf);*/
        text = new UITextField();
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    private var _qualifiedName:String;

    private var _report:AnalysisDefinition;

    public function set report(value:AnalysisDefinition):void {
        _report = value;
    }

    public function set qualifiedName(value:String):void {
        _qualifiedName = value;
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

    public function set data(value:Object):void {
        this.value = value;
        var measure:AnalysisMeasure = value["baseMeasure"] as AnalysisMeasure;
        var color:uint = value[_qualifiedName + "color"] as uint;
        var asSet:Boolean = false;
        if (measure != null && measure.reportFieldExtension != null && measure.reportFieldExtension is YTDReportFieldExtension) {
            var testExt:YTDReportFieldExtension = measure.reportFieldExtension as YTDReportFieldExtension;
            if (testExt.alwaysShow) {
                asSet = true;
                _valText = "";
            }
        } else if (measure != null && measure.reportFieldExtension != null && measure.reportFieldExtension is VerticalListReportExtension) {
            var vertExt:VerticalListReportExtension = measure.reportFieldExtension as VerticalListReportExtension;
            if (vertExt.alwaysShow) {
                asSet = true;
                _valText = "";
            }
        }
        if (!asSet) {
            _valText = value[_qualifiedName];
        }
        //text.text = value[_qualifiedName];
        var bold:Boolean = false;

        if (_report.getFont() == "Open Sans" && !bold) {
            text.styleName = "myFontStyle";
        } else if (_report.getFont() == "Open Sans" && bold) {
            text.styleName = "boldStyle";
        }
        var tf:UITextFormat = new UITextFormat(Application(Application.application).systemManager, _report.getFont(), 10, color);
        tf.align = "right";
        _format = tf;
        _changed = true;
        invalidateProperties();
        invalidateDisplayList();
    }

    public function get data():Object {
        return this.value;
    }
}
}
