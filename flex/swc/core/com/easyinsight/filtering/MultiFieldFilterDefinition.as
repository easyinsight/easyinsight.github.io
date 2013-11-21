/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.MultiFieldFilterDefinition")]
public class MultiFieldFilterDefinition extends FilterDefinition {

    public var selectedItems:ArrayCollection = new ArrayCollection();
    public var availableItems:ArrayCollection = new ArrayCollection();
    public var availableTags:ArrayCollection = new ArrayCollection();
    public var fieldOrdering:ArrayCollection = new ArrayCollection();
    public var all:Boolean = true;

    public function MultiFieldFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.MULTI_FIELD;
    }

    override public function updateFromSaved(savedItemFilter:FilterDefinition):void {
        super.updateFromSaved(savedItemFilter);
        /*var aFilter:MultiFieldFilterDefinition = savedItemFilter as MultiFieldFilterDefinition;
        if (availableItems != null) {
            for each (var aItem:AnalysisItem in availableItems) {
                for each (var eItem:AnalysisItem in aFilter.availableItems) {
                    if (aItem.matches(eItem)) {
                        aItem.updateFromSaved(eItem);
                    }
                }
            }
        }
        if (selectedItems != null) {
            for each (var aItem:AnalysisItem in selectedItems) {
                for each (var eItem:AnalysisItem in aFilter.selectedItems) {
                    if (aItem.matches(eItem)) {
                        aItem.updateFromSaved(eItem);
                    }
                }
            }
        }*/
    }

    override public function updateFromReportView(filter:FilterDefinition):void {
        super.updateFromReportView(filter);
        var aFilter:MultiFieldFilterDefinition = filter as MultiFieldFilterDefinition;
        availableItems = aFilter.availableItems;
        selectedItems = aFilter.selectedItems;
    }

    override public function getSaveValue():Object {
        var o:Object = new Object();
        o["availableItems"] = availableItems;
        o["selectedItems"] = selectedItems;
        //return targetItem.analysisItemID;
        return null;
    }

    override public function loadFromSharedObject(value:Object):void {
        var a:ArrayCollection = value["availableItems"];
        if (a != null) {
            availableItems = a;
        }
        var s:ArrayCollection = value["selectedItems"];
        if (s != null) {
            selectedItems = s;
        }
    }
}
}
