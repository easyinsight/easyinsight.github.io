/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/5/12
 * Time: 7:38 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
import com.easyinsight.customupload.InsightlyDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.insightly.InsightlyCompositeSource")]
public class InsightlyCompositeSource extends CompositeServerDataSource {

    public var insightlyApiKey:String;

    public function InsightlyCompositeSource() {
    }

    override public function getFeedType():int {
        return DataSourceType.INSIGHTLY;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return InsightlyDataSourceCreation;
    }
}
}
