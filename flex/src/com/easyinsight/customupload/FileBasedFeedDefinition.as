package com.easyinsight.customupload {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.file.FileBasedFeedDefinition")]
public class FileBasedFeedDefinition extends ServerDataSourceDefinition {
    public static const GET:int = 1;
    public static const POST:int = 2;

    public var uploadFormat:UploadFormat;
    public var url:String;

    public var httpMethod:int = GET;

    public var userName:String;

    public var password:String;

    public function FileBasedFeedDefinition() {
        super();
    }

    override public function configClass():Class {
        return FlatFileDataSourceCreation;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:FlatFIleConfiguration = new FlatFIleConfiguration();
        config.dataSourceDefinition = this;
        pages.addItem(config);
        return pages;
    }

    override public function allowFieldEdit():Boolean {
        return true;
    }
}
}