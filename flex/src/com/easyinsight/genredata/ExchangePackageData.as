package com.easyinsight.genredata {
[Bindable]
[RemoteClass(alias="com.easyinsight.exchange.ExchangePackageData")]
public class ExchangePackageData extends ExchangeData {

    public var packageID:int;
    public var packageName:String;

    public function ExchangePackageData() {
        super();
    }
}
}