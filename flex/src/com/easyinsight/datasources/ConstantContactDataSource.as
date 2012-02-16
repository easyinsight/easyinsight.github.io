package com.easyinsight.datasources {
import com.easyinsight.customupload.ConstantContactDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.constantcontact.ConstantContactCompositeSource")]
public class ConstantContactDataSource extends CompositeServerDataSource {

    public var pin:String;
    public var ccUserName:String;
    public var tokenKey:String;
    public var tokenSecret:String;
    public var briefMode:Boolean;

    public function ConstantContactDataSource() {
        super();
        feedName = "Constant Contact";
    }

    override public function getFeedType():int {
        return DataSourceType.CONSTANT_CONTACT;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return ConstantContactDataSourceCreation;
    }
}
}