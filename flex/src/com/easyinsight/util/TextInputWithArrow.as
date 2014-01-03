/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 6/18/13
 * Time: 10:35 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.display.Shape;
import flash.events.Event;
import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;

import mx.collections.ArrayCollection;

import mx.containers.Canvas;

import mx.controls.TextInput;
import mx.core.UIComponent;

public class TextInputWithArrow extends Canvas {
    public function TextInputWithArrow() {
    }

    private var triangle:UIComponent;
    public var textInput:TextInput = new TextInput();

    private var autoCompleteManager:AutoCompleteManager;

    private var _dataProvider:ArrayCollection;
    private var _filteredProvider:ArrayCollection;

    [Bindable(event="dataProviderChanged")]
    public function get dataProvider():ArrayCollection {
        return _dataProvider;
    }

    public function set dataProvider(value:ArrayCollection):void {
        if (_dataProvider == value) return;
        _dataProvider = value;
        if (value != null) {
            filteredProvider = new ArrayCollection(value.toArray());
        }
        dispatchEvent(new Event("dataProviderChanged"));
    }

    [Bindable(event="filteredProviderChanged")]
    public function get filteredProvider():ArrayCollection {
        return _filteredProvider;
    }

    public function set filteredProvider(value:ArrayCollection):void {
        if (_filteredProvider == value) return;
        _filteredProvider = value;
        dispatchEvent(new Event("filteredProviderChanged"));
    }

    private function onClick(event:MouseEvent):void {
        autoCompleteManager.openDropdownForTarget(textInput);
    }

    override protected function createChildren():void {
        super.createChildren();
        autoCompleteManager = new AutoCompleteManager();
        BindingUtils.bindProperty(autoCompleteManager, "dataProvider", this, "filteredProvider");
        autoCompleteManager.minCharsForCompletion = 0;
        autoCompleteManager.labelField = labelField;
        autoCompleteManager.maxRowCount = 12;
        autoCompleteManager.target = textInput;
        textInput.width = 300;
        textInput.addEventListener(MouseEvent.CLICK, onClick);
        addEventListener(MouseEvent.CLICK, onClick);
        addChild(textInput);
        var shape:Shape = new Shape();
        shape.graphics.beginFill(0);
        shape.graphics.moveTo(0, 0);
        shape.graphics.lineTo(4, 4);
        shape.graphics.lineTo(8, 0);
        shape.graphics.lineTo(0, 0);
        shape.graphics.endFill();
        var uic:UIComponent = new UIComponent();
        uic.addChild(shape);
        this.triangle = uic;
        triangle.x = 285;
        triangle.y = 9;
        addChild(triangle);
    }

    public var labelField:String = "displayName";

    public function get selectedItem():Object {
        var text:String = textInput.text;
        for each (var f:Object in dataProvider) {
            if (f[labelField] == text) {
                return f;
            }
        }
        textInput.errorString = "You need to select a valid entry.";
        textInput.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
        return null;
    }
}
}
