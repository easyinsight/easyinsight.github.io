package com.easyinsight.dashboard {
public interface IDashboardEditorComponent extends IDashboardViewComponent {
    function save():void;
    function validate(results:Array):void;
    function edit():void;
}
}