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
[RemoteClass(alias="com.easyinsight.analysis.definitions.WSVerticalListDefinition")]
public class VerticalListDefinition extends AnalysisDefinition {

    public var measures:ArrayCollection;
    public var column:AnalysisItem;
    public var verticalListID:int;
    public var headerWidth:int = 140;
    public var columnWidth:int = 73;

    public function VerticalListDefinition() {
    }

    override public function fromSave(savedDef:AnalysisDefinition):void {
        super.fromSave(savedDef);
        this.verticalListID = VerticalListDefinition(savedDef).verticalListID;
    }

    override public function get type():int {
        return AnalysisDefinition.VERTICAL_LIST;
    }

    override public function getFields():ArrayCollection {
        return new ArrayCollection();
    }

    override public function populate(fields:ArrayCollection):void {
    }
}
}
