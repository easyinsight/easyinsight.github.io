/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/5/12
 * Time: 2:07 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.summary {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.tree.TreeDefinition;

import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.WSSummaryDefinition")]
public class SummaryDefinition extends TreeDefinition {


    public var summaryDefinitionID:int;

    public function SummaryDefinition() {
        super();
    }

    override public function supportsEmbeddedFonts():Boolean {
        return true;
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.summaryDefinitionID = SummaryDefinition(savedDef).summaryDefinitionID;
    }

    override public function get type():int {
        return AnalysisDefinition.SUMMARY;
    }

    override public function getFields():ArrayCollection {
        var fields:ArrayCollection = new ArrayCollection([ hierarchy ]);
        for each (var item:AnalysisItem in items) {
            fields.addItem(item);
        }
        return fields;
    }

    override public function populate(fields:ArrayCollection):void {
        var hierarchies:ArrayCollection = findItems(fields, AnalysisItemTypes.HIERARCHY);
        if (hierarchies.length > 0) {
            hierarchy = hierarchies.getItemAt(0) as AnalysisHierarchyItem;
        }
        for each (var item:AnalysisItem in fields) {
            if (item != null) {
                if (!item.hasType(AnalysisItemTypes.HIERARCHY)) {
                    items.addItem(item);
                }
            }
        }
    }
}
}