/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 7/16/11
 * Time: 1:02 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.filtering {

import com.easyinsight.analysis.AnalysisItemTypes;

import mx.collections.ArrayCollection;
import mx.utils.ObjectUtil;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.MultiFlatDateFilter")]
public class MultiFlatDateFilterDefinition extends FilterDefinition {

    public var levels:ArrayCollection = new ArrayCollection();
    public var endDateProperty:String;
    public var level:int = AnalysisItemTypes.MONTH_FLAT;
    public var cachedValues:ArrayCollection;
    public var unitsBack:int = 20;
    public var unitsForward:int;
    public var includeRelative:Boolean;
    public var allOption:Boolean;

    public function MultiFlatDateFilterDefinition() {
    }

    override public function getType():int {
        return FilterDefinition.MULTI_FLAT_DATE;
    }

    override protected function subclassClone(filter:FilterDefinition):void {
        var levels:ArrayCollection = new ArrayCollection();
        for each (var wrapper:DateLevelWrapper in levels) {
            var copiedWrapper:DateLevelWrapper = ObjectUtil.copy(wrapper) as DateLevelWrapper;
            levels.addItem(copiedWrapper);
        }
        MultiFlatDateFilterDefinition(filter).levels = levels;
    }

    override public function getSaveValue():Object {
        return levels;
    }

    override public function loadFromSharedObject(value:Object):void {
        levels = value as ArrayCollection;
    }

    public function createLabel():String {
        var label:String;
        var firstValue:int = 50000000;
        var lastValue:int = -1;
        var firstWrapper:DateLevelWrapper;
        var lastWrapper:DateLevelWrapper;
        for each (var wrapper:DateLevelWrapper in levels) {
            if (wrapper.dateLevel < firstValue) {
                firstValue = wrapper.dateLevel;
                firstWrapper = wrapper;
            }
            if (wrapper.dateLevel > lastValue) {
                lastValue = wrapper.dateLevel;
                lastWrapper = wrapper;
            }
        }
        if (firstWrapper == lastWrapper) {
            label = firstWrapper.shortDisplay;
        } else {
            label = firstWrapper.shortDisplay + " to " + lastWrapper.shortDisplay;
        }
        return label;
    }
}
}
