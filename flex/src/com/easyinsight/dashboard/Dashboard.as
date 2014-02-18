package com.easyinsight.dashboard {
import com.easyinsight.analysis.Link;
import com.easyinsight.datasources.DataSourceInfo;
import com.easyinsight.quicksearch.EIDescriptor;
import com.easyinsight.skin.ImageDescriptor;

import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.dashboard.Dashboard")]
public class Dashboard {

    public var name:String;
    public var id:int;
    public var urlKey:String;
    public var accountVisible:Boolean;
    public var rootElement:DashboardElement;
    public var administrators:ArrayCollection;
    public var dataSourceID:int;
    public var filters:ArrayCollection;
    public var exchangeVisible:Boolean;
    public var publicVisible:Boolean;
    public var description:String;
    public var authorName:String;
    public var creationDate:Date;
    public var updateDate:Date;
    public var overridenFilters:Object;

    public var backgroundColor:uint = 0xCCCCCC;
    public var padding:int = 3;
    public var borderColor:uint = 0x000000;
    public var borderThickness:int = 1;
    public var colorSet:String = "Primary";

    public var headerStyle:int;
    public var recommendedExchange:Boolean;
    public var dataSourceInfo:DataSourceInfo;
    public var role:int;
    public var marmotScript:String;
    public var folder:int = EIDescriptor.MAIN_VIEWS_FOLDER;

    public var absoluteSizing:Boolean = true;
    public var reportAccessProblem:Boolean;

    public var fillStackHeaders:Boolean = true;
    public var stackFill1Start:int = 0x848080;
    public var stackFill1SEnd:int = 0x545050;
    public var stackFill2Start:int = 0x494444;
    public var stackFill2End:int = 0x343030;
    public var reportHorizontalPadding:int = 20;

    public var enableLocalStorage:Boolean = false;

    public var headerImage:ImageDescriptor;
    public var headerTextColor:int;
    public var headerBackgroundColor:int;
    public var imageFullHeader:Boolean;

    public var configurations:ArrayCollection;

    public var publicWithKey:Boolean;

    public var defaultDrillthrough:Link;

    public var tabletVersion:int;
    public var phoneVersion:int;

    public var version:int;

    public function Dashboard() {
    }
}
}