package com.easyinsight.datasources {

import com.easyinsight.administration.feed.ServerDataSourceDefinition;
import com.easyinsight.customupload.SmartsheetDataSourceCreation;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.smartsheet.SmartsheetTableSource")]
public class SmartsheetTableSource extends ServerDataSourceDefinition {

    public var accessToken:String;
    public var refreshToken:String;
    public var table:String;
    public var rebuildFields:Boolean;

    public function SmartsheetTableSource() {
        super();
    }

    override public function getFeedType():int {
        return DataSourceType.SMARTSHEET;
    }

    override public function createAdminPages():ArrayCollection {
        var pages:ArrayCollection = new ArrayCollection();
        /*var config:SmartsheetConfiguration = new SmartsheetConfiguration();
        config.label = "Smartsheet Server Configuration";
        pages.addItem(config);*/
        return pages;
    }

    override public function configClass():Class {
        return SmartsheetDataSourceCreation;
    }

    override public function requiresMoreSetupAfterAuth():Boolean {
        return true;
    }

    override public function moreSetup():IPostOAuth {
        var picker:SmartsheetAccountRetrieval = new SmartsheetAccountRetrieval();
        picker.dataSource = this;
        return picker;
    }

    override public function allowFieldEdit():Boolean {
        return true;
    }
}
}