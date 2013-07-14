package com.easyinsight.customupload.wizard {
import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.FlatFileUploadContext")]
public class FlatFileUploadContext extends UploadContext {

    public var uploadKey:String;
    public var type:int;

    public function FlatFileUploadContext() {
        super();
    }
}
}