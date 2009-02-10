package com.easyinsight.collaboration {
[Bindable]
[RemoteClass(alias="com.easyinsight.collaboration.DataSourceAccessMessage")]
public class DataSourceAccessMessage extends NotificationMessage{

    public var dataSourceID:int;
    public var newRole:int;

    public function DataSourceAccessMessage() {
        super();
    }
}
}