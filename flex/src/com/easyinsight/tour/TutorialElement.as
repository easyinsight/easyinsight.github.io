package com.easyinsight.tour {
import flash.display.DisplayObjectContainer;

import mx.core.Application;
import mx.core.Container;
import mx.core.UIComponent;

public class TutorialElement extends UIComponent {

    private var _anchor:String;

    private var _baseObject:Container;

    public function TutorialElement() {
        super();
    }

    public function get anchor():String {
        return _anchor;
    }

    public function set anchor(value:String):void {
        _anchor = value;
    }

    public function forwardExecute():void {
        
    }

    public function backwardExecute():void {
        
    }

    public function destroyNote():void {
        
    }

    public function staysOnScreen():Boolean {
        return false;
    }

    protected function findComponent(id:String, container:Container = null):UIComponent {
        if (container == null) {
            container = Application.application as Container;
        }
        for each (var uiComponent:UIComponent in container.getChildren()) {
            if (uiComponent.id == id) {
                return uiComponent;
            }
            if (uiComponent is Container) {
                var test:UIComponent = findComponent(id, Container(uiComponent));
                if (test != null) {
                    return test;
                }
            }
        }
        return null;
    }
}
}