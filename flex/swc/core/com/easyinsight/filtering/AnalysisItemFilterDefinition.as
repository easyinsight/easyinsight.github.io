/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemHandle;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisItemFilterDefinition")]
public class AnalysisItemFilterDefinition extends FilterDefinition {

    public var targetItem:AnalysisItem;
    public var availableItems:ArrayCollection  = new ArrayCollection();
    public var availableHandles:ArrayCollection = new ArrayCollection();
    public var availableTags:ArrayCollection = new ArrayCollection();
    public var useFullyQualifiedNames:Boolean;
    public var selectedName:String;
    public var selectedFQN:String;
    public var expandDates:int;

    public function AnalysisItemFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.ANALYSIS_ITEM;
    }

    override public function updateFromSaved(savedItemFilter:FilterDefinition):void {
        super.updateFromSaved(savedItemFilter);
        var aFilter:AnalysisItemFilterDefinition = savedItemFilter as AnalysisItemFilterDefinition;
        if (targetItem != null) {
            targetItem.updateFromSaved(aFilter.targetItem);
        }
        if (availableItems != null) {
            for each (var aItem:AnalysisItem in availableItems) {
                for each (var eItem:AnalysisItem in aFilter.availableItems) {
                    if (aItem.matches(eItem)) {
                        aItem.updateFromSaved(eItem);
                    }
                }
            }
        }
    }

    override public function updateFromReportView(filter:FilterDefinition):void {
        super.updateFromReportView(filter);
        var aFilter:AnalysisItemFilterDefinition = filter as AnalysisItemFilterDefinition;
        targetItem = aFilter.targetItem;
        availableItems = aFilter.availableItems;
    }

    override public function getSaveValue():Object {
        return {display: targetItem.unqualifiedDisplay, fqn: targetItem.display};
    }

    override public function loadFromSharedObject(value:Object):void {
        if (value != null) {
            selectedName = value.display;
            selectedFQN = value.fqn;
        }
    }
}
}
