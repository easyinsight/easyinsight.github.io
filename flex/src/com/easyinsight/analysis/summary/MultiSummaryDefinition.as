/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSMultiSummaryDefinition")]
public class MultiSummaryDefinition extends AnalysisDefinition {

    public var multiSummaryID:int;
    public var coreItems:ArrayCollection = new ArrayCollection();
    public var key:AnalysisItem;
    public var reports:ArrayCollection = new ArrayCollection();
    public var headerColor1:int = 0xFFFFFF;
    public var headerColor2:int = 0xEEEEEE;
    public var headerTextColor:int;
    public var defaultToExpanded:Boolean;
    public var nestedReportHeaders:Boolean;
    public var nestedReportTitles:Boolean;
    public var nestedFontSize:int;
    public var defaultMeasureAlignment:String;
    public var defaultGroupingAlignnment:String;
    public var defaultDateAlignment:String;

    public function MultiSummaryDefinition() {
        super();
    }


    override public function useHTMLInFlash():Boolean {
        return true;
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

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.multiSummaryID = MultiSummaryDefinition(savedDef).multiSummaryID;
    }

    override public function get type():int {
        return AnalysisDefinition.MULTI_SUMMARY;
    }

    override public function getFields():ArrayCollection {
        var fields:ArrayCollection = new ArrayCollection([ key ]);
        for each (var item:AnalysisItem in coreItems) {
            fields.addItem(item);
        }
        return fields;
    }

    override public function populate(fields:ArrayCollection):void {
        for each (var item:AnalysisItem in fields) {
            if (item != null) {
                coreItems.addItem(item);
            }
        }
    }
}
}