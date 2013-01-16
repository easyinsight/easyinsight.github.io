/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.customupload.SQLServerDataSourceConfiguration;
import com.easyinsight.customupload.SQLServerDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.database.SQLServerDatabaseConnection")]
public class SQLServerDatabaseConnection extends ServerDatabaseConnection {

    public var host:String;
    public var port:int;
    public var databaseName:String;
    public var dbUserName:String;
    public var dbPassword:String;

    public function SQLServerDatabaseConnection() {
    }

    override public function getFeedType():int {
        return DataSourceType.SERVER_SQL_SERVER;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:SQLServerDataSourceConfiguration = new SQLServerDataSourceConfiguration();
        config.dataSourceDefinition = this;
        config.label = "SQL Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return SQLServerDataSourceCreation;
    }
}
}
