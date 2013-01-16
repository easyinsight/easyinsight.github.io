/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.customupload.OracleDataSourceConfiguration;
import com.easyinsight.customupload.OracleDataSourceCreation;
import com.easyinsight.customupload.SQLServerDataSourceConfiguration;
import com.easyinsight.customupload.SQLServerDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.database.OracleDatabaseConnection")]
public class OracleDatabaseConnection extends ServerDatabaseConnection {

    public var host:String;
    public var port:int;
    public var sid:String;
    public var dbUserName:String;
    public var dbPassword:String;

    public function OracleDatabaseConnection() {
    }

    override public function getFeedType():int {
        return DataSourceType.SERVER_ORACLE;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:OracleDataSourceConfiguration = new OracleDataSourceConfiguration();
        config.dataSourceDefinition = this;
        config.label = "Oracle Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return OracleDataSourceCreation;
    }
}
}
