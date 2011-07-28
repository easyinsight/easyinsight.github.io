/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 5/23/11
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.verticallist {
import com.easyinsight.analysis.AnalysisDefinition;
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSCombinedVerticalListDefinition")]
public class CombinedVerticalListDefinition extends AnalysisDefinition {

    public var reports:ArrayCollection = new ArrayCollection();
    public var combinedVerticalListID:int;

    public function CombinedVerticalListDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.combinedVerticalListID = CombinedVerticalListDefinition(savedDef).combinedVerticalListID;
    }

    override public function get type():int {
        return AnalysisDefinition.COMBINED_VERTICAL_LIST;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection();
    }

    override public function populate(fields:ArrayCollection):void {
    }
}
}
