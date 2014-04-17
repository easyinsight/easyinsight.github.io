/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 1/23/14
 * Time: 11:19 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.administration.feed.BlankDataSourceConfiguration;
import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.BlankDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.blank.BlankDataSource")]
public class BlankDataSource extends ServerDataSourceDefinition {

    public function BlankDataSource() {
        super();
    }

    override public function configClass():Class {
        return BlankDataSourceCreation;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:BlankDataSourceConfiguration = new BlankDataSourceConfiguration();
        pages.addItem(config);
        return pages;
    }

    override public function getFeedType():int {
        return DataSourceType.BLANK;
    }

    override public function allowFieldEdit():Boolean {
        return true;
    }

    override public function additionalSetup():Boolean {
        return true;
    }
}
}
