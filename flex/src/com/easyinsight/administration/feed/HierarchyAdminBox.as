package com.easyinsight.administration.feed {
import com.easyinsight.analysis.AnalysisItem;
import mx.managers.DragManager;
import mx.events.DragEvent;
import com.easyinsight.analysis.HierarchyLevel;
import mx.binding.utils.BindingUtils;
import mx.events.FlexEvent;
import mx.collections.ArrayCollection;
import com.easyinsight.analysis.NamedKey;
import flash.events.Event;
import mx.controls.Label;
import mx.containers.HBox;
import mx.controls.TextInput;
import mx.controls.List;
import com.easyinsight.analysis.AnalysisHierarchyItem;
import mx.containers.VBox;
import mx.validators.StringValidator;

public class HierarchyAdminBox extends VBox {
    private var _analysisHierarchyItem:AnalysisHierarchyItem;
    private var nameInput:TextInput;
    private var _levels:ArrayCollection;
    public var list:List;
    private var validators:Array;
    
    public function HierarchyAdminBox() {
        nameInput = new TextInput();
    }

    override protected function createChildren():void {
        super.createChildren();
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
        nameInput.addEventListener(Event.CHANGE, onNameChange);
        addChild(hBox);
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        var validator:StringValidator = new StringValidator();
        validator.source = nameInput;
        validator.property = "text";
        validator.minLength = 3;
        validators = [ validator ];
    }

    private function onNameChange(event:Event):void {
        var namedKey:NamedKey = _analysisHierarchyItem.key as NamedKey;
        namedKey.nameValue = nameInput.text;
    }

    public function set analysisHierarchyItem(val:AnalysisHierarchyItem):void {
        _analysisHierarchyItem = val;
        nameInput.text = _analysisHierarchyItem.key.createString();
        levels = _analysisHierarchyItem.hierarchyLevels;
    }


    public function get analysisHierarchyItem():AnalysisHierarchyItem {
        return _analysisHierarchyItem;
    }
    public function get levels():ArrayCollection {
        return _levels;
    }
    public function set levels(val:ArrayCollection):void {
        _levels = val;
        dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }
}
}