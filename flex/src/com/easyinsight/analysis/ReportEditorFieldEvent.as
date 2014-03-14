package com.easyinsight.analysis {
import com.easyinsight.listing.Tag;

import flash.events.Event;

public class ReportEditorFieldEvent extends Event {

    public static const ITEM_COPY:String = "reportItemCopy";
    public static const ITEM_DELETE:String = "reportItemDelete";
    public static const ITEM_EDIT:String = "reportItemEdit";
    public static const ITEM_REFACTOR_TO_DATA_SOURCE:String = "reportItemRefactorToDataSource";
    public static const ITEM_ADD_TO_REPORT:String = "reportItemAddToReport";
    public static const ITEM_FILTER:String = "reportItemFilter";
    public static const TAG_ITEM:String = "reportItemTag";

    public var item:AnalysisItemWrapper;
    public var x:int;
    public var y:int;
    public var tag:Tag;

    public function ReportEditorFieldEvent(type:String, item:AnalysisItemWrapper, x:int = 0, y:int = 0, tag:Tag = null) {
        super(type, true);
        this.item = item;
        this.x = x;
        this.y = y;
        this.tag = tag;
    }

    override public function clone():Event {
        return new ReportEditorFieldEvent(type, item, x, y, tag);
    }
}
}