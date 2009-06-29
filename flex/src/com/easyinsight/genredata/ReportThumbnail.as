package com.easyinsight.genredata {
import com.easyinsight.solutions.InsightDescriptor;

import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.ReportThumbnail")]
public class ReportThumbnail {

    public var insightDescriptor:InsightDescriptor;
    public var image:ByteArray;

    public function ReportThumbnail() {
    }

}
}