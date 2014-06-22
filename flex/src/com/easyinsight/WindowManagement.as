/**
 * Created by jamesboe on 6/17/14.
 */
package com.easyinsight {
import com.easyinsight.analysis.DataViewFactory;

import flash.events.Event;

import mx.controls.Alert;

import mx.core.UIComponent;

public class WindowManagement {

    public var factory:DataViewFactory;
    public var openWindow:UIComponent;

    public static var manager:WindowManagement = new WindowManagement();

    public function WindowManagement() {
    }

    public function clearFactory():void {
        this.factory = null;
    }

    public function registerFactory(factory:DataViewFactory):void {
        if (this.factory != null) {
            Alert.show("more than one factory registered");
        } else {
            this.factory = factory;
        }
    }

    public function hideReport():void {
        factory.hideReport();
    }

    public function restoreReport():void {
        factory.restoreReport();
    }

    public function addWindow(window:UIComponent):void {
        if (openWindow != null) {
            openWindow.removeEventListener(Event.REMOVED_FROM_STAGE, onEvent);
            openWindow = null;
        }

        openWindow = window;
        factory.hideReport();
        window.addEventListener(Event.REMOVED_FROM_STAGE, onEvent);

    }

    private function onEvent(event:Event):void {
        if (openWindow != null) {
            openWindow.removeEventListener(Event.REMOVED_FROM_STAGE, onEvent);
            openWindow = null;
        }
        if (factory != null) {
            factory.restoreReport();
        }
    }
}
}
