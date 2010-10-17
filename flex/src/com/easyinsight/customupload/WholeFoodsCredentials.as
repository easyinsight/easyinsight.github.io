package com.easyinsight.customupload {
import com.easyinsight.framework.Credentials;

import flash.utils.ByteArray;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wholefoods.WholeFoodsCredentials")]
public class WholeFoodsCredentials extends Credentials {

    public var bytes:ByteArray;

    public function WholeFoodsCredentials() {
        super();
    }
}
}