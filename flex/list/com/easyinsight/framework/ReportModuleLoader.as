/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/28/11
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.framework {
import com.easyinsight.analysis.list.ListModule;

import flash.events.Event;
import flash.events.EventDispatcher;

import mx.core.Container;
import mx.core.UIComponent;

public class ReportModuleLoader extends EventDispatcher {

    private var moduleName:String;

    public function ReportModuleLoader() {
    }

    public function loadReportRenderer(_reportRendererModule:String, container:Container):void {
        moduleName = _reportRendererModule;
        dispatchEvent(new Event("moduleLoaded"));
    }

    public function create():Object {
        var module:UIComponent;
        switch (moduleName) {
            case "ListModule.swf":
                module = new ListModule();
                break;
        }
        return module;
    }
}
}
