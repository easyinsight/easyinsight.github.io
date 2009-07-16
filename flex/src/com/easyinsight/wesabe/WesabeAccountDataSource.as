package com.easyinsight.wesabe {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wesabe.WesabeAccountDataSource")]
public class WesabeAccountDataSource extends ServerDataSourceDefinition{
    public function WesabeAccountDataSource() {
        super();
        feedName = "Accounts";
    }
}
}