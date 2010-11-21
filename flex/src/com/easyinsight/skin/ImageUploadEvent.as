package com.easyinsight.skin {
import flash.events.Event;

public class ImageUploadEvent extends Event {

    public static const IMAGE_UPLOAD:String = "imageUpload";

    public var imageDescriptor:ImageDescriptor;

    public function ImageUploadEvent(imageDescriptor:ImageDescriptor) {
        super(IMAGE_UPLOAD);
        this.imageDescriptor = imageDescriptor;
    }

    override public function clone():Event {
        return new ImageUploadEvent(imageDescriptor);
    }
}
}