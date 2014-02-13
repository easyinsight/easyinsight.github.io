/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.MySQLDataSourceConfiguration;
import com.easyinsight.customupload.MySQLDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.database.MySQLDatabaseConnection")]
public class MySQLDatabaseConnection extends ServerDatabaseConnection {

    public var host:String;
    public var port:int = 3306;
    public var databaseName:String;
    public var dbUserName:String;
    public var dbPassword:String;

    public function MySQLDatabaseConnection() {
    }

    override public function getFeedType():int {
        return DataSourceType.MYSQL_SERVER;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:MySQLDataSourceConfiguration = new MySQLDataSourceConfiguration();

        config.label = "MySQL Server Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return MySQLDataSourceCreation;
    }
}
}
