package com.easyinsight.administration.feed {
import flash.events.KeyboardEvent;
import flash.events.MouseEvent;
import flash.ui.Keyboard;

import mx.binding.utils.BindingUtils;
import mx.events.FlexEvent;
import mx.collections.ArrayCollection;
import mx.controls.Label;
import mx.containers.HBox;
import mx.controls.TextInput;
import mx.controls.List;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import mx.containers.VBox;
import mx.validators.StringValidator;
import mx.validators.Validator;

public class HierarchyAdminBox extends VBox {
    private var _analysisHierarchyItem:AnalysisHierarchyItem;
    private var nameInput:TextInput;
    private var _levels:ArrayCollection;
    public var list:List;
    private var nameValidator:StringValidator;
    
    public function HierarchyAdminBox() {
        nameInput = new TextInput();
    }

    override protected function createChildren():void {
        super.createChildren();
        addEventListener(KeyboardEvent.KEY_UP, onKey);
        list = new List();
        list.dragEnabled = true;
        list.dropEnabled = true;
        list.dragMoveEnabled = true;
        list.width = 200;
        list.percentHeight = 100;
        BindingUtils.bindProperty(list, "dataProvider", this, "levels");
        list.labelField = "display";
        addChild(list);
        var hBox:HBox = new HBox();
        var nameLabel:Label = new Label();
        nameLabel.text = "Name: ";
        hBox.addChild(nameLabel);
        hBox.addChild(nameInput);
        addChild(hBox);
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        nameValidator = new StringValidator();
        nameValidator.source = nameInput;
        nameValidator.property = "text";
        nameValidator.minLength = 3;
    }

    private function onKey(event:KeyboardEvent):void {
        if (event.keyCode == Keyboard.DELETE || event.keyCode == Keyboard.BACKSPACE) {
            if (list.selectedItem != null) {
                list.dataProvider.removeItemAt(list.dataProvider.getItemIndex(list.selectedItem));
            }
        }
    }

    public function getName():String {
        return nameInput.text;
    }

    public function validate():Boolean {
        var results:Array = Validator.validateAll([nameValidator]);
        if (results.length > 0) {
            nameInput.setFocus();
            nameInput.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
            return false;
        }
        if (list.dataProvider.length < 2) {
            list.errorString = "You must specify at least two fields in the hierarchy.";
            list.setFocus();
            list.dispatchEvent(new MouseEvent(MouseEvent.MOUSE_OVER));
            return false;
        }
        return true;
    }

    public function set analysisHierarchyItem(val:AnalysisHierarchyItem):void {
        _analysisHierarchyItem = val;
        nameInput.text = _analysisHierarchyItem.key.createString();
        if (list != null) {
            list.dataProvider = val.hierarchyLevels;
        }
        invalidateProperties();
    }

    override protected function commitProperties():void {
        super.commitProperties();
        levels = _analysisHierarchyItem.hierarchyLevels;
    }

    public function get analysisHierarchyItem():AnalysisHierarchyItem {
        return _analysisHierarchyItem;
    }
    [Bindable]
    public function get levels():ArrayCollection {
        return _levels;
    }
    public function set levels(val:ArrayCollection):void {
        _levels = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }
}
}