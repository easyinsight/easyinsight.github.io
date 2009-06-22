package com.easyinsight.solutions {
import com.easyinsight.framework.User;

import flash.events.Event;

import flash.events.MouseEvent;

import mx.binding.utils.BindingUtils;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Label;

public class SolutionView extends VBox{

    private var solution:Solution;
    private var _titleText:String;
    private var titleLabel:Label;
    private var imageRenderer:SolutionImageRenderer;

    public function SolutionView() {
        super();
        titleLabel = new Label();
        BindingUtils.bindProperty(titleLabel, "text", this, "titleText");
        imageRenderer = new SolutionImageRenderer();
        setStyle("paddingLeft", 10);
        setStyle("paddingRight", 10);
        setStyle("paddingTop", 10);
        setStyle("paddingBottom", 10);
        setStyle("backgroundAlpha", 0);
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        dispatchEvent(new SolutionSelectionEvent(solution));
    }

    [Bindable(event="titleTextChanged")]
    public function get titleText():String {
        return _titleText;
    }

    public function set titleText(value:String):void {
        if (_titleText == value) return;
        _titleText = value;
        dispatchEvent(new Event("titleTextChanged"));
    }

    override protected function createChildren():void {
        super.createChildren();
        var box:VBox = new VBox();
        box.setStyle("borderStyle", "solid");
        box.setStyle("cornerRadius", 15);
        box.setStyle("dropShadowEnabled", "true");
        box.setStyle("backgroundAlpha", 1);
        box.setStyle("backgroundColor", 0xFFFFFF);
        addChild(box);
        var titleBox:HBox = new HBox();
        titleBox.percentWidth = 100;
        titleBox.setStyle("horizontalAlign", "center");
        titleBox.addChild(titleLabel);
        box.addChild(titleBox);
        box.addChild(imageRenderer);
    }

    override public function set data(val:Object):void {
        solution = val as Solution;
        imageRenderer.data = solution;
        titleText = solution.name;
        if (User.getInstance() == null) {
            setStyle("alpha", 1);
            toolTip = "Click to look at details about this solution.";
        } else if (solution.accessible) {
            setStyle("alpha", 1);
            toolTip = "Click to look at details about this solution.";
        } else {
            setStyle("alpha", .5);
            toolTip = "This solution is not accessible to your account tier.";
        }
    }

    override public function get data():Object {
        return solution;
    }
}
}