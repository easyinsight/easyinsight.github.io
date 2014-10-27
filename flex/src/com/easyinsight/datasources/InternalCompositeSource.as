package com.easyinsight.datasources {
import com.easyinsight.customupload.InternalDataSourceCreation;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.internal.InternalCompositeSource")]
public class InternalCompositeSource extends CompositeServerDataSource {


    public function InternalCompositeSource() {
        super();
        feedName = "Internal";
    }

    override public function getFeedType():int {
        return DataSourceType.INTERNAL;
    }

    override public function createAdminPages():ArrayCollection {
        return new ArrayCollection();
    }

    override public function configClass():Class {
        return InternalDataSourceCreation;
    }
}
}