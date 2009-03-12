package com.easyinsight.google {
import mx.collections.ArrayCollection;
[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.google.Spreadsheet")]
public class Spreadsheet {

    public var title:String;
    public var children:ArrayCollection;

    public function Spreadsheet() {
    }
}
}