/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.customupload.PostgresDataSourceConfiguration;
import com.easyinsight.customupload.PostgresDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.database.PostgresDatabaseConnection")]
public class PostgresDatabaseConnection extends ServerDatabaseConnection {

    public var host:String;
    public var port:int = 5432;
    public var databaseName:String;
    public var dbUserName:String;
    public var dbPassword:String;

    public function PostgresDatabaseConnection() {
    }

    override public function getFeedType():int {
        return DataSourceType.MYSQL_SERVER;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:PostgresDataSourceConfiguration = new PostgresDataSourceConfiguration();

        config.label = "PostgreSQL Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return PostgresDataSourceCreation;
    }
}
}
