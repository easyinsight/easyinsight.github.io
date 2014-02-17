package com.easyinsight.skin {
import mx.collections.ArrayCollection;

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.ApplicationSkin")]
public class ApplicationSkinTO {

    public var id:int;

    public var coreAppBackgroundImage:ImageDescriptor;
    public var reportHeaderImage:ImageDescriptor;
    public var coreAppBackgroundImageEnabled:Boolean = false;
    public var coreAppBackgroundColor:uint = 0x818285;
    public var coreAppBackgroundColorEnabled:Boolean = false;
    public var coreAppBackgroundSize:String = "100%";
    public var coreAppBackgroundSizeEnabled:Boolean = false;
    public var headerBarBackgroundColor:uint = 0xF0F0F0;
    public var headerBarBackgroundColorEnabled:Boolean = false;
    public var headerBarLogo:ImageDescriptor;
    public var headerBarDividerColor:uint = 0xD42525;
    public var headerBarDividerColorEnabled:Boolean = false;
    public var centerCanvasBackgroundColor:uint = 0xFFFFFF;
    public var centerCanvasBackgroundColorEnabled:Boolean = false;
    public var centerCanvasBackgroundAlpha:Number = 1;
    public var centerCanvasBackgroundAlphaEnabled:Boolean = false;
    public var reportBackground:ImageDescriptor;
    public var reportBackgroundEnabled:Boolean = false;
    public var reportBackgroundSize:String;
    public var reportBackgroundSizeEnabled:Boolean = false;
    public var reportBackgroundColor:uint;
    public var reportTextColor:uint;
    public var reportHeader:Boolean = false;

    public var myDataName:Boolean = true;
    public var myDataSize:Boolean = false;
    public var myDataOwner:Boolean = false;
    public var myDataCreationDate:Boolean = false;
    public var myDataLastTime:Boolean = false;
    public var myDataCombine:Boolean = true;
    public var myDataNewScorecard:Boolean = false;
    public var myDataNewDashboard:Boolean = true;
    public var myDataLookupTable:Boolean = false;
    public var myDataAccountVisible:Boolean = false;

    public var dataSourceTags:Boolean = false;
    public var dataSourceAPIKey:Boolean = false;
    public var reportTags:Boolean = false;
    public var reportType:Boolean = false;
    public var reportAPIKey:Boolean = false;
    public var reportModified:Boolean = false;
    public var reportCached:Boolean = false;
    public var reportPersistedCached:Boolean = false;

    public var customChartColor:int;
    public var customChartColorEnabled:Boolean;
    public var gradientChartColor:int;
    public var gradientChartColorEnabled:Boolean;
    public var secondaryColor:int;
    public var secondaryColorEnabled:Boolean;
    public var tertiaryColor:int;
    public var tertiaryColorEnabled:Boolean;
    public var summaryBackgroundColor:int;
    public var summaryBackgroundColorEnabled:Boolean;
    public var summaryTextColor:int;
    public var summaryTextColorEnabled:Boolean;
    public var crosstabHeaderTextColor:int;
    public var crosstabHeaderTextColorEnabled:Boolean;
    public var crosstabHeaderBackgroundColor:int;
    public var crosstabHeaderBackgroundColorEnabled:Boolean;
    public var headerStart:int;
    public var headerStartEnabled:Boolean;
    public var headerEnd:int;
    public var headerEndEnabled:Boolean;
    public var tableColorStart:int;
    public var tableColorStartEnabled:Boolean;
    public var tableColorEnd:int;
    public var tableColorEndEnabled:Boolean;

    public var dashboardStack1ColorStart:int;
    public var dashboardStack1ColorEnd:int;
    public var dashboardStackColor2Start:int;
    public var dashboardStackColor2End:int;

    public var multiColors:ArrayCollection;
    public var secondaryMultiColors:ArrayCollection;

    public function ApplicationSkinTO() {
    }



    
}
}