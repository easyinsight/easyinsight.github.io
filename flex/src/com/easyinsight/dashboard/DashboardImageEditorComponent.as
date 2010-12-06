package com.easyinsight.dashboard {
import com.easyinsight.skin.ImageBox;

import mx.containers.VBox;
import mx.controls.Label;

public class DashboardImageEditorComponent extends VBox implements IDashboardEditorComponent {

    public var image:DashboardImage;

    private var imageBox:ImageBox;

    public function DashboardImageEditorComponent() {
        super();
        setStyle("verticalAlign", "middle");
        setStyle("horizontalAlign", "center");
        this.percentWidth = 100;
        this.percentHeight = 100;
    }

    protected override function createChildren():void {
        super.createChildren();
        imageBox = new ImageBox();
        imageBox.imageDescriptor = image.imageDescriptor;
        addChild(imageBox);
    }

    public function save():void {
        image.imageDescriptor = imageBox.imageDescriptor;
    }

    public function validate():Boolean {
        return image.imageDescriptor != null;
    }

    public function edit():void {
    }
}
}