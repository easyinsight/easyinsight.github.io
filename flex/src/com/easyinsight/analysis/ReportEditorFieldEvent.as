package com.easyinsight.analysis {
import flash.events.Event;

public class ReportEditorFieldEvent extends Event {

    public static const ITEM_COPY:String = "reportItemCopy";
    public static const ITEM_DELETE:String = "reportItemDelete";
    public static const ITEM_EDIT:String = "reportItemEdit";
    public static const ITEM_REFACTOR_TO_DATA_SOURCE:String = "reportItemRefactorToDataSource";
    public static const ITEM_ADD_TO_REPORT:String = "reportItemAddToReport";
    public static const ITEM_FILTER:String = "reportItemFilter";

    public var item:AnalysisItemWrapper;
    public var x:int;
    public var y:int;

    public function ReportEditorFieldEvent(type:String, item:AnalysisItemWrapper, x:int = 0, y:int = 0) {
        super(type, true);
        this.item = item;
        this.x = x;
        this.y = y;
    }

    override public function clone():Event {
        return new ReportEditorFieldEvent(type, item);
    }
}
}