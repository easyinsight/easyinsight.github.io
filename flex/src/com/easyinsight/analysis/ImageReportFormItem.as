package com.easyinsight.analysis {
import com.easyinsight.skin.ImageBox;
import com.easyinsight.skin.ImageDescriptor;

public class ImageReportFormItem extends ReportFormItem {

    private var imageBox:ImageBox;

    private var publicImage:Boolean;

    public function ImageReportFormItem(label:String, property:String, value:Object, report:Object, enabledProperty:String = null, publicImage:Boolean = false) {
        super(label, property, value, report, enabledProperty);
        this.publicImage = publicImage;
    }

    protected override function createChildren():void {
        super.createChildren();
        imageBox = new ImageBox();
        imageBox.publicImage = this.publicImage;
        if (this.value != null) imageBox.imageDescriptor = ImageDescriptor(this.value);
        addChild(imageBox);
    }

    override protected function getValue():Object {
        return imageBox.imageDescriptor;
    }
}
}