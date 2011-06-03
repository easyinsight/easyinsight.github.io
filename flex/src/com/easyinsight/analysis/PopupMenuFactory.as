package com.easyinsight.analysis {
import com.easyinsight.report.IReportContextMenuFactory;

import flash.display.DisplayObject;

import flash.display.InteractiveObject;

public class PopupMenuFactory {

    public static var menuFactory:IMenuFactory;
    public static var reportFactory:IReportContextMenuFactory;

    public function PopupMenuFactory() {
    }

    public static function assignMenu(interactiveObject:InteractiveObject, menuItems:Array):void {
        menuFactory.assignMenu(interactiveObject, menuItems);
    }

    public static function assignReportMenu(factory:IReportContextMenuFactory):void {
        reportFactory = factory;
    }
}
}