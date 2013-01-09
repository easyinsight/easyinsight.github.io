package com.easyinsight.dashboard {
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

    public var backgroundColor:uint = 0xCCCCCC;
    public var padding:int = 3;
    public var borderColor:uint = 0x000000;
    public var borderThickness:int = 1;

    public var headerStyle:int;
    public var recommendedExchange:Boolean;
    public var dataSourceInfo:DataSourceInfo;
    public var role:int;
    public var marmotScript:String;
    public var folder:int = EIDescriptor.MAIN_VIEWS_FOLDER;

    public var absoluteSizing:Boolean;
    public var reportAccessProblem:Boolean;

    public var fillStackHeaders:Boolean;
    public var stackFill1Start:int;
    public var stackFill1SEnd:int;
    public var stackFill2Start:int;
    public var stackFill2End:int;

    public function Dashboard() {
    }
}
}