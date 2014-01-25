package com.easyinsight.filtering
{
	import com.easyinsight.analysis.AnalysisItem;

import flash.events.EventDispatcher;

import mx.utils.ObjectUtil;

[Bindable]
	[RemoteClass(alias="com.easyinsight.analysis.FilterDefinition")]
	public class FilterDefinition extends EventDispatcher
	{
		public static const VALUE:int = 1;
		public static const RANGE:int = 2;
		public static const DATE:int = 3;
		public static const ROLLING_DATE:int = 4;
		public static const LAST_VALUE:int = 5;
		public static const PATTERN:int = 6;
		public static const FIRST_VALUE:int = 7;
        public static const ORDERED:int = 8;
        public static const OR:int = 9;
        public static const NULL:int = 10;
        public static const NAMED_REF:int = 11;
        public static const FLAT_DATE:int = 12;
        public static const ANALYSIS_ITEM:int = 13;
        public static const MULTI_FLAT_DATE:int = 14;
        public static const MONTH_CUTOFF:int = 15;
        public static const MULTI_FIELD:int = 16;

    public static var flexIDCtr:int = 0;

		public var field:AnalysisItem;
    public var drillthrough:Boolean;
		public var applyBeforeAggregation:Boolean = true;
        public var filterID:int;
        public var intrinsic:Boolean = false;
        public var enabled:Boolean = true;
        public var showOnReportView:Boolean = true;
    public var filterName:String;
    public var templateFilter:Boolean;
    public var toggleEnabled:Boolean;
    public var minimumRole:int = 4;
    public var marmotScript:String;
    public var trendFilter:Boolean;
    public var notCondition:Boolean;
    public var parentFilters:String;
    public var fieldChoiceFilterLabel:String;
    public var customizable:Boolean;
    public var section:int;
    public var flexID:int = flexIDCtr++;
    public var parentChildLabel:String;
    public var childToParentLabel:String;

		public function FilterDefinition()
			{
			super();
		}

    public function loadFromSharedObject(value:Object):void {

    }

    public function retrieveParentFilters():Array {
        if (parentFilters == null || parentFilters == "") {
            return [];
        }
        return parentFilters.split(",");
        //return [ parentFilters ];
    }

    public function matches(filterDefinition:FilterDefinition):Boolean {
        if (filterID != 0 && filterID == filterDefinition.filterID) {
            return true;
        }
        return (flexID != 0 && flexID == filterDefinition.flexID);
    }

    public static function getLabel(filterDefinition:FilterDefinition, field:AnalysisItem):String {
        if (filterDefinition == null) {
            if (field == null) {
                return "";
            } else {
                return field.display + ":";
            }
        }
        if (filterDefinition.filterName != null && filterDefinition.filterName != "") {
            return filterDefinition.filterName + ":";
        }
        if (filterDefinition.field != null) {
            return filterDefinition.field.unqualifiedDisplay + ":";
        }
        return "";
    }

    public static function getLabelWithEnd(filterDefinition:FilterDefinition, field:AnalysisItem, end:String):String {
        if (filterDefinition == null) {
            if (field == null) {
                return "";
            } else {
                return field.display + end;
            }
        }
        if (filterDefinition.filterName != null && filterDefinition.filterName != "") {
            return filterDefinition.filterName + end;
        }
        if (filterDefinition.field != null) {
            return filterDefinition.field.unqualifiedDisplay + end;
        }
        return "";
    }

		public function getType():int {
			return 0;
		}

        public function updateFromSaved(savedItemFilter:FilterDefinition):void {
            filterID = savedItemFilter.filterID;
            if (field != null) {
                field.updateFromSaved(savedItemFilter.field);
            }
        }

        public function updateFromReportView(filter:FilterDefinition):void {
            field = filter.field;
        }

    public function qualifiedName():String {
        if (field != null) {
            return field.qualifiedName() + ":" + getType();
        } else {
            return filterName + ":" + String(getType());
        }
    }

    public function get displayName():String {
        return getLabel(this, field);
    }

    public function clone():FilterDefinition {
        var filter:FilterDefinition = ObjectUtil.copy(this) as FilterDefinition;
        filter.filterID = 0;
        subclassClone(filter);
        return filter;
    }

    protected function subclassClone(filter:FilterDefinition):void {

    }

    public function getSaveValue():Object {
        return null;
    }
}
}