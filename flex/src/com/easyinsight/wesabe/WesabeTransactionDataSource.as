package com.easyinsight.wesabe {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wesabe.WesabeTransactionDataSource")]
public class WesabeTransactionDataSource extends ServerDataSourceDefinition{
    public function WesabeTransactionDataSource() {
        super();
        feedName = "Accounts";
    }
}
}