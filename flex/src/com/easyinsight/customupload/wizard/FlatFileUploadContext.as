package com.easyinsight.customupload.wizard {
[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.FlatFileUploadContext")]
public class FlatFileUploadContext extends UploadContext {

    public var uploadID:int;

    public function FlatFileUploadContext() {
        super();
    }
}
}