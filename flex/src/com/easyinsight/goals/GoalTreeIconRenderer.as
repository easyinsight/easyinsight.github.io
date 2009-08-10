package com.easyinsight.goals {

import com.benstucki.utilities.IconUtility;
import com.easyinsight.genredata.AnalyzeEvent;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.Image;
import mx.controls.Label;

public class GoalTreeIconRenderer extends Button {

    private var goalTreeDescriptor:GoalTreeDescriptor;
    //private var _titleText:String;
    //private var titleLabel:Label;
    //private var imageRenderer:Image;
    //private var box:VBox;

    private var backgroundColor:uint = 0xFFFFFF;

    public function GoalTreeIconRenderer() {
        super();
        /*titleLabel = new Label();
        titleLabel.maxWidth = 110;
        BindingUtils.bindProperty(titleLabel, "text", this, "titleText");
        imageRenderer = new Image();
        setStyle("paddingLeft", 0);
        setStyle("paddingRight", 0);
        setStyle("paddingTop", 0);
        setStyle("paddingBottom", 0);
        //setStyle("borderStyle", "solid");
        //setStyle("borderThickness", 1);
        setStyle("backgroundAlpha", 0);
        setStyle("horizontalAlign", "center");
        setStyle("verticalAlign", "middle");
        this.width = 120;
        this.height = 80;*/
        this.width = 120;
        this.height = 60;
        addEventListener(MouseEvent.CLICK, onClick);
        labelPlacement = "bottom";
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new AnalyzeEvent(new GoalDataAnalyzeSource(goalTreeDescriptor.id)));
    }

    /*[Bindable(event="titleTextChanged")]
    public function get titleText():String {
        return _titleText;
    }

    public function set titleText(value:String):void {
        if (_titleText == value) return;
        _titleText = value;
        dispatchEvent(new Event("titleTextChanged"));
    }*/

    override protected function createChildren():void {
        super.createChildren();
        /*box = new VBox();
        box.setStyle("borderStyle", "solid");
        box.setStyle("cornerRadius", 8);
        box.setStyle("dropShadowEnabled", "true");
        box.setStyle("backgroundAlpha", 1);
        box.setStyle("backgroundColor", backgroundColor);
        box.setStyle("horizontalAlign", "center");
        box.setStyle("verticalAlign", "middle");
        addChild(box);
        var titleBox:HBox = new HBox();
        titleBox.percentWidth = 100;
        titleBox.setStyle("horizontalAlign", "center");
        titleBox.addChild(titleLabel);
        box.addChild(imageRenderer);
        box.addChild(titleBox);        */
    }

    override public function set data(val:Object):void {
        goalTreeDescriptor = val as GoalTreeDescriptor;
        if (goalTreeDescriptor.iconName == null) {
            setStyle("icon", null);
            //imageRenderer.visible = false;
        } else {
            setStyle("icon", IconUtility.getClass(this, "/app/assets/icons/32x32/" + goalTreeDescriptor.iconName, 32, 32));
            /*imageRenderer.source = "/app/assets/icons/32x32/" + goalTreeDescriptor.iconName;
            imageRenderer.visible = true;*/
        }
        //invalidateSize();
        label = goalTreeDescriptor.name;
        //titleText = goalTreeDescriptor.name;
        //if (box != null) box.setStyle("backgroundColor", backgroundColor);

    }

    override public function get data():Object {
        return goalTreeDescriptor;
    }
}
}