package com.easyinsight.analysis {
import flash.display.InteractiveObject;

public class PopupMenuFactory {

    public static var menuFactory:IMenuFactory;

    public function PopupMenuFactory() {
    }

    public static function assignMenu(interactiveObject:InteractiveObject, menuItems:Array):void {
        menuFactory.assignMenu(interactiveObject, menuItems);
    }}
}