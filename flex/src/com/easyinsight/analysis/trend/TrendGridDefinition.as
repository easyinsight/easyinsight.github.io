/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/26/11
 * Time: 12:29 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.trend {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.KPIDefinition;
import com.easyinsight.analysis.TrendReportFieldExtension;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSTrendGridDefinition")]
public class TrendGridDefinition extends KPIDefinition {

    public var trendReportID:int;
    public var sortIndex:int = 3;
    public var sortAscending:Boolean = false;
    public var showKPIName:Boolean = false;
    public var rowColor1:int = 0xF7F7F7;
    public var rowColor2:int = 0xFFFFFF;
    public var headerColor1:int = 0xFFFFFF;
    public var headerColor2:int = 0xEFEFEF;
    public var textColor:int = 0x000000;
    public var headerTextColor:int = 0x000000;
    public var summaryRowTextColor:int = 0x000000;
    public var summaryRowBackgroundColor:int = 0x6699ff;
    public var maxRecords:int;

    public function TrendGridDefinition() {
    }

    override public function supportsEmbeddedFonts():Boolean {
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

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.trendReportID = TrendGridDefinition(savedDef).trendReportID;
    }

    override public function get type():int {
        return AnalysisDefinition.TREND_GRID;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection();
    }

    override public function populate(fields:ArrayCollection):void {
        measures = new ArrayCollection();
        for each (var field:AnalysisItem in fields) {
            if (field != null && field.hasType(AnalysisItemTypes.MEASURE)) {
                measures.addItem(field);
            } else if (field.hasType(AnalysisItemTypes.DIMENSION) && !field.hasType(AnalysisItemTypes.DATE) && !field.hasType(AnalysisItemTypes.TEXT)) {
                if (groupings.length == 0) {
                    groupings.addItem(field);
                }
            }
        }
    }
}
}
