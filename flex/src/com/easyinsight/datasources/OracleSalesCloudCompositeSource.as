package com.easyinsight.datasources {

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.oracle.OracleDataSource")]
public class OracleSalesCloudCompositeSource extends CompositeServerDataSource {

    public var oracleUserName:String;
    public var oraclePassword:String;
    public var oracleAccount:String;
    public var historyUserName:String;
    public var historyPassword:String;
    public var historyReport:String = "/Custom/Customer Relationship Management/OppHistory.xdo";

    public function OracleSalesCloudCompositeSource() {
        super();
        this.feedName = "Oracle Salescloud";
    }

    override public function getFeedType():int {
        return DataSourceType.ORACLE_SALESCLOUD;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return OracleSalesCloudSourceCreation;
    }
}
}