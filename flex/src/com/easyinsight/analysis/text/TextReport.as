/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 3/20/13
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.text {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSTextDefinition")]
public class TextReport extends AnalysisDefinition {

    public var textReportID:int;
    public var columns:ArrayCollection;
    public var fontColor:uint = 0x333333;
    public var text:String;

    public function TextReport() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.textReportID = TextReport(savedDef).textReportID;
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function getFields():ArrayCollection {
        return columns;
    }

    override public function getFont():String {
        if (customFontFamily != null && customFontFamily != "" && useCustomFontFamily) {
            return customFontFamily;
        }
        if (fontName == "Lucida Grande" || fontName == "Open Sans") {
            return fontName;
        } else {
            return "Lucida Grande";
        }
    }

    override public function populate(fields:ArrayCollection):void {
        columns = new ArrayCollection();
        for each (var field:AnalysisItem in fields) {
            if (field != null) {
                columns.addItem(field);
            }
        }
    }

    override public function get type():int {
        return AnalysisDefinition.TEXT;
    }
}
}
