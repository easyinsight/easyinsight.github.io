package com.easyinsight.customupload {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.custom.CustomDataSource")]
public class CustomDataSource extends ServerDataSourceDefinition {

    public var wsdl:String;

    public function CustomDataSource() {
        super();
    }

    override public function configClass():Class {
        return CustomDataSourceCreation;
    }
}
}