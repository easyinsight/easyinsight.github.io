package com.easyinsight.analysis {
import com.easyinsight.skin.ImageBox;
import com.easyinsight.skin.ImageDescriptor;

public class ImageReportFormItem extends ReportFormItem {

    private var imageBox:ImageBox;

    public function ImageReportFormItem(label:String, property:String, value:Object, report:Object) {
        super(label, property, value, report);
    }

    protected override function createChildren():void {
        super.createChildren();
        imageBox = new ImageBox();
        if (this.value != null) imageBox.imageDescriptor = ImageDescriptor(this.value);
        addChild(imageBox);
    }

    override protected function getValue():Object {
        return imageBox.imageDescriptor;
    }
}
}