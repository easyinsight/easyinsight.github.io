/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.customupload.YouTrackDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.youtrack.YouTrackCompositeSource")]
public class YouTrackCompositeSource extends CompositeServerDataSource {

    public var ytUserName:String;
    public var ytPassword:String;
    public var url:String;
    public var cookie:String;

    public function YouTrackCompositeSource() {
    }

    override public function getFeedType():int {
        return DataSourceType.YOUTRACK;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return YouTrackDataSourceCreation;
    }
}
}
