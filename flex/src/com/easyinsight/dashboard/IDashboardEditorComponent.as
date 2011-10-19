package com.easyinsight.dashboard {
public interface IDashboardEditorComponent extends IDashboardViewComponent {
    function save():void;
    function validate():Boolean;
    function edit():void;
}
}