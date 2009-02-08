package com.easyinsight.icons {
import flash.events.MouseEvent;
import mx.containers.HBox;
import mx.controls.Label;
import mx.controls.Image;
import mx.containers.VBox;
[Event(name="iconSelection", type="com.easyinsight.icons.IconSelectionEvent")]
public class IconButton extends VBox {

    private var _iconFile:Icon;
    private var imageField:Image;
    private var labelField:Label;

    public function IconButton() {
        addEventListener(MouseEvent.CLICK, clicked);
        this.horizontalScrollPolicy = "off";
        this.verticalScrollPolicy = "off";
    }

    private function clicked(event:MouseEvent):void {
        dispatchEvent(new IconSelectionEvent(_iconFile));
    }

    override public function set data(val:Object):void {
        _iconFile = val as Icon;
        if (imageField != null) imageField.load("/DMS/assets/icons/32x32/" + _iconFile.path);
        if (labelField != null) labelField.text = _iconFile.name;
    }

    override public function get data():Object {
        return _iconFile;
    }

    override protected function createChildren():void {
        super.createChildren();
        var imageBox:HBox = new HBox();
        imageBox.percentWidth = 100;
        imageBox.setStyle("horizontalAlign", "center");
        imageField = new Image();
        if (_iconFile != null) imageField.load(_iconFile.path);
        imageBox.addChild(imageField);
        addChild(imageBox);
        labelField = new Label();
        labelField.maxWidth = 80;
        labelField.truncateToFit = true;
        if (_iconFile != null) labelField.text = _iconFile.name;
        var hBox:HBox = new HBox();
        hBox.percentWidth = 100;
        hBox.setStyle("horizontalAlign", "center");
        hBox.addChild(labelField);
        addChild(hBox);
    }
}
}