/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 10:21 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.MultiFieldFilterDefinition")]
public class MultiFieldFilterDefinition extends FilterDefinition {

    public var selectedItems:ArrayCollection = new ArrayCollection();
    public var availableHandles:ArrayCollection = new ArrayCollection();
    public var availableTags:ArrayCollection = new ArrayCollection();
    public var fieldOrdering:ArrayCollection = new ArrayCollection();
    public var excludeReportFields:Boolean;
    public var all:Boolean = true;
    public var alphaSort:Boolean = false;
    public var useFullyQualifiedNames:Boolean;
    public var selectedName:String;
    public var selectedFQN:String;
    public var expandDates:int;

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
    }

    override public function getSaveValue():Object {
        var o:Object = new Object();
        o["selectedItems"] = selectedItems;
        o["all"] = all;
        //return targetItem.analysisItemID;
        return o;
    }

    override public function loadFromSharedObject(value:Object):void {
        var s:ArrayCollection = value["selectedItems"];
        if (s != null) {
            selectedItems = s;
        }
        if (value["all"] != null) {
            all = value["all"];
        }
    }
}
}
