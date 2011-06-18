package com.easyinsight.datasources {
import com.easyinsight.customupload.SampleDataSourceCreation;

import mx.collections.ArrayCollection;


[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.sample.SampleDataSource")]
public class SampleDataSource extends CompositeServerDataSource {


    public function SampleDataSource() {
        super();
        feedName = "Sample Data";
    }

    override public function getFeedType():int {
        return DataSourceType.SAMPLE;
    }

    override public function createAdminPages():ArrayCollection {
        return new ArrayCollection();
    }

    override public function configClass():Class {
        return SampleDataSourceCreation;
    }
}
}