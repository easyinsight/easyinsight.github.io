package com.easyinsight.dashboard {
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
    public var paddingLeft:int = 10;
    public var paddingRight:int = 10;
    public var filterBorderStyle:String = "solid";
    public var filterBorderColor:uint = 0xCCCCCC;
    public var filterBackgroundColor:uint = 0xFFFFFF;
    public var filterBackgroundAlpha:Number = 0;
    public var headerStyle:int;

    public function Dashboard() {
    }
}
}