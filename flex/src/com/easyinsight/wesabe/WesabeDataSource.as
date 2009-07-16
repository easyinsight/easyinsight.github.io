package com.easyinsight.wesabe {
import com.easyinsight.datasources.CompositeServerDataSource;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.wesabe.WesabeDataSource")]
public class WesabeDataSource extends CompositeServerDataSource{
    public function WesabeDataSource() {
        super();
        feedName = "Wesabe";
    }


    override public function configClass():Class {
        return WesabeDataSourceCreation;
    }

    private function blah():void {
        var account:WesabeAccountDataSource;
        var txn:WesabeTransactionDataSource;
    }
}
}