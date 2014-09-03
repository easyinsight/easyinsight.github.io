/**
 * Created by jamesboe on 6/17/14.
 */
package com.easyinsight {
import com.easyinsight.analysis.DataViewFactory;
import com.easyinsight.analysis.EmbeddedViewFactory;

import flash.events.Event;

import mx.collections.ArrayCollection;

import mx.controls.Alert;

import mx.core.UIComponent;

public class WindowManagement implements IWindowManagement {

    public var factory:DataViewFactory;
    public var endUserFactories:ArrayCollection = new ArrayCollection();
    public var openWindow:UIComponent;

    public static var manager:WindowManagement = new WindowManagement();

    public function WindowManagement() {
    }

    public function clearFactory():void {
        this.factory = null;
        endUserFactories = new ArrayCollection();
    }

    public static function getManager():WindowManagement {
        return manager;
    }

    public function registerFactory(factory:DataViewFactory):void {
        if (this.factory != null || this.endUserFactories.length > 0) {
            Alert.show("more than one factory registered");
        } else {
            this.factory = factory;
        }
    }

    public function registerEndUserFactory(factory:EmbeddedViewFactory):void {
        if (this.factory != null) {
            Alert.show("more than one factory registered");
        } else {
            endUserFactories.addItem(factory);
        }
    }

    public function hideReport():void {
        if (factory != null) {
            factory.hideReport();
        } else if (endUserFactories.length > 0) {
            for each (var f:EmbeddedViewFactory in endUserFactories) {
                f.hideReport();
            }
        }
    }

    public function restoreReport():void {
        if (factory != null) {
            factory.restoreReport();
        } else if (endUserFactories.length > 0) {
            for each (var f:EmbeddedViewFactory in endUserFactories) {
                f.restoreReport();
            }
        }
    }

    public function addWindow(window:UIComponent):void {
        if (openWindow != null) {
            openWindow.removeEventListener(Event.REMOVED_FROM_STAGE, onEvent);
            openWindow = null;
        }

        if (window != null) {
            openWindow = window;
            if (factory != null) {
                factory.hideReport();
            } else if (endUserFactories.length > 0) {
                for each (var f:EmbeddedViewFactory in endUserFactories) {
                    f.hideReport();
                }
            }
            window.addEventListener(Event.REMOVED_FROM_STAGE, onEvent);
        }
    }

    private function onEvent(event:Event):void {
        if (openWindow != null) {
            openWindow.removeEventListener(Event.REMOVED_FROM_STAGE, onEvent);
            openWindow = null;
        }
        if (factory != null) {
            factory.restoreReport();
        }
        else if (endUserFactories.length > 0) {
            for each (var f:EmbeddedViewFactory in endUserFactories) {
                f.restoreReport();
            }
        }
    }
}
}
