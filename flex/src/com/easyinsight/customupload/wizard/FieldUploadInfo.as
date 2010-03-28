package com.easyinsight.customupload.wizard {
import com.easyinsight.analysis.AnalysisItem;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.userupload.FieldUploadInfo")]
public class FieldUploadInfo {

    public var guessedItem:AnalysisItem;
    public var sampleValues:ArrayCollection;

    public function FieldUploadInfo() {
    }
}
}