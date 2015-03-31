package com.easyinsight.administration.feed
{
import com.easyinsight.customupload.GoogleSpreadsheetConfiguration;
import com.easyinsight.customupload.GoogleSpreadsheetSourceCreation;
import com.easyinsight.datasources.GoogleSpreadsheetSheetRetrieval;
import com.easyinsight.datasources.IPostOAuth;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.google.GoogleFeedDefinition")]
public class GoogleFeedDefinition extends ServerDataSourceDefinition
{
    public var worksheetURL:String;
    public var tokenKey:String;
    public var tokenSecret:String;
    public var pin:String;

    public function GoogleFeedDefinition()
    {
        super();
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        var config:GoogleSpreadsheetConfiguration = new GoogleSpreadsheetConfiguration();
        config.label = "Google Configuration";
        pages.addItem(config);
        return pages;
    }

    override public function configClass():Class {
        return GoogleSpreadsheetSourceCreation;
    }

    override public function requiresMoreSetupAfterAuth():Boolean {
        return true;
    }

    override public function moreSetup():IPostOAuth {
        var picker:GoogleSpreadsheetSheetRetrieval = new GoogleSpreadsheetSheetRetrieval();
        picker.dataSource = this;
        picker.newSource = true;
        return picker;
    }
}
}