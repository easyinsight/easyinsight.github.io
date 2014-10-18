package com.easyinsight.datasources {

import com.easyinsight.customupload.MailChimpDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.mailchimp.MailchimpCompositeSource")]
public class MailChimpCompositeSource extends CompositeServerDataSource {

    public var mailchimpApiKey:String;

    public function MailChimpCompositeSource() {
        super();
        feedName = "MailChimp";
    }

    override public function getFeedType():int {
        return DataSourceType.MAILCHIMP;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        return pages;
    }

    override public function configClass():Class {
        return MailChimpDataSourceCreation;
    }
}
}