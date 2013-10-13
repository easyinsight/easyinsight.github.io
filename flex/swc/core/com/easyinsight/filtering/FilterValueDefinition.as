package com.easyinsight.filtering {
import com.easyinsight.analysis.AnalysisItemResultMetadata;

import flash.net.SharedObject;

import mx.collections.ArrayCollection;
import mx.utils.ObjectUtil;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.FilterValueDefinition")]
public class FilterValueDefinition extends FilterDefinition {
    public var filteredValues:ArrayCollection = new ArrayCollection();
    public var inclusive:Boolean;
    public var singleValue:Boolean;
    public var autoComplete:Boolean;
    public var excludeEmpty:Boolean;
    public var allOption:Boolean;
    public var cachedValues:AnalysisItemResultMetadata;
    public var newType:Boolean;

    public function FilterValueDefinition() {
        super();
    }

    override public function getType():int {
        return FilterDefinition.VALUE;
    }

    override protected function subclassClone(filter:FilterDefinition):void {
        var values:ArrayCollection = new ArrayCollection();
        for each (var obj:Object in this.filteredValues) {
            values.addItem(ObjectUtil.copy(obj));
        }
        FilterValueDefinition(filter).filteredValues = values;
    }

    override public function getSaveValue():Object {
        return filteredValues;
    }

    override public function loadFromSharedObject(value:Object):void {
        filteredValues = value as ArrayCollection;
    }
}
}