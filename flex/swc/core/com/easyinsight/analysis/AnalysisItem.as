package com.easyinsight.analysis {


import com.easyinsight.analysis.formatter.FormattingConfiguration;

import com.easyinsight.filtering.FilterDefinition;

import mx.collections.ArrayCollection;

import mx.formatters.Formatter;
import mx.utils.ObjectUtil;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.AnalysisItem")]
public class AnalysisItem {

    public var key:Key;
    public var analysisItemID:int;
    public var hidden:Boolean = false;
    public var formattingType:int;
    public var origin:FieldDataSourceOrigin;
    public var sort:int = 0;
    public var sortSequence:int = 0;
    public var displayName:String;
    public var width:int = 0;
    public var concrete:Boolean;
    public var links:ArrayCollection = new ArrayCollection();
    public var itemPosition:int = 0;
    public var filters:ArrayCollection = new ArrayCollection();
    public var lookupTableID:int = 0;
    public var originalDisplayName:String;
    public var tooltip:String;
    public var reportFieldExtension:ReportFieldExtension;
    public var sortItem:AnalysisItem;
    public var keyColumn:Boolean;
    public var fromField:AnalysisItem;
    public var unqualifiedDisplayName:String;
    public var kpi:Boolean;
    public var fieldType:int;
    //public var flexID:int = flexIDCtr++;
    public var reloadFromDataSource:Boolean;
    public var parentItemID:int;
    public var basedOnReportField:int;
    public var tags:ArrayCollection;

    //public var fieldListField:Boolean;

    public function AnalysisItem() {
        super();
    }

    public function get unqualifiedDisplay():String {
        if (unqualifiedDisplayName != null) {
            return unqualifiedDisplayName;
        }
        return display;
    }

    public function get display():String {
        if (displayName != null) {
            return displayName;
        }
        return key.createString();
    }

    public function matches(analysisItem:AnalysisItem):Boolean {
        if (analysisItemID != 0 && analysisItemID == analysisItem.analysisItemID) {
            return true;
        }
        return qualifiedName() == analysisItem.qualifiedName() && getType() == analysisItem.getType();
        //return (flexID != 0 && flexID == analysisItem.flexID);
    }

    public function updateFromSaved(analysisItem:AnalysisItem):void {
        this.analysisItemID = analysisItem.analysisItemID;
        if (filters != null && analysisItem.filters != null) {
            for each (var itemFilter:FilterDefinition in this.filters) {
                for each (var savedItemFilter:FilterDefinition in analysisItem.filters) {
                    if (itemFilter.matches(savedItemFilter)) {
                        itemFilter.updateFromSaved(savedItemFilter);
                    }
                }
            }
        }
        if (sortItem != null) {
            sortItem.updateFromSaved(analysisItem.sortItem);
        }
        if (fromField != null) {
            fromField.updateFromSaved(analysisItem.fromField);
        }
        /*if (links != null && analysisItem.links != null) {
         for each (var link:Link in this.links) {
         for each (var savedLink:Link in analysisItem.links) {
         if (link.matches(savedLink)) {

         }
         }
         }
         }*/
        if (reportFieldExtension != null) {
            reportFieldExtension.updateFromSaved(analysisItem.reportFieldExtension);
        }
    }

    public function qualifiedName():String {
        return key.internalString() + getQualifiedSuffix();
    }

    public function get qualifiedProperty():String {
        return qualifiedName();
    }

    protected function getQualifiedSuffix():String {
        return String(getType());
    }

    public function getType():int {
        return 0;
    }

    public function hasType(type:int):Boolean {
        return (getType() & type) == type;
    }

    public function getFormatter():Formatter {
        return FormattingConfiguration.getFormatter(2, 0, formattingType);
    }

    public function getSortFunction():Function {
        return null;
    }

    public function defaultLink():Link {
        var defaultLink:Link = null;
        if (links != null && links.length > 0) {
            for each (var link:Link in links) {
                if (link.defaultLink) {
                    defaultLink = link;
                    break;
                }
            }
        }
        return defaultLink;
    }

    protected function createObject():AnalysisItem {
        return null;
    }

    /*public function get otherID():int {
        if (analysisItemID > 0) {
            return analysisItemID;
        }
        return flexID;
    }
*/
    /*public var reportTemporaryItem:AnalysisItem;

    public var locked:Boolean;*/

    public function copy():AnalysisItem {
        return this;
    }/*
        var analysisItem:AnalysisItem = ObjectUtil.copy(this) as AnalysisItem;
        if (this.parentItemID != 0) {
            analysisItem.parentItemID = this.parentItemID;
        } else if (this.analysisItemID != 0) {
            analysisItem.parentItemID = this.analysisItemID;
        } else {
            analysisItem.reportTemporaryItem = this;
        }

        analysisItem.analysisItemID = 0;
        analysisItem.flexID = flexIDCtr++;

        if (sortItem != null) {
            analysisItem.sortItem = sortItem.copy();
        }
        if (fromField != null) {
            analysisItem.fromField = fromField.copy();
        }

        var formatting:FormattingConfiguration = new FormattingConfiguration();
        formatting.formattingType = formattingConfiguration.formattingType;
        analysisItem.formattingConfiguration = formatting;

        var links:ArrayCollection = new ArrayCollection();
        for each (var link:Link in this.links) {
            links.addItem(link.clone());
        }
        analysisItem.links = links;

        var filters:ArrayCollection = new ArrayCollection();
        for each (var filter:FilterDefinition in this.filters) {
            filters.addItem(filter.clone());
        }
        analysisItem.filters = filters;

        if (reportFieldExtension != null) {
            analysisItem.reportFieldExtension = reportFieldExtension.clone();
        }

        createSubclassCopy(analysisItem);

        return analysisItem;
    }*/

    protected function createSubclassCopy(analysisItem:AnalysisItem):void {
    }

    public function updateFromParent(parent:AnalysisItem):void {

    }
}


}