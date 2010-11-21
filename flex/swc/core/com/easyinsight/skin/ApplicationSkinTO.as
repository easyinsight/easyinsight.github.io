package com.easyinsight.skin {

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.ApplicationSkin")]
public class ApplicationSkinTO {

    public var id:int;

    public var coreAppBackgroundImage:ImageDescriptor;
    public var coreAppBackgroundColor:uint = 0x818285;
    public var coreAppBackgroundSize:String = "100%";
    public var headerBarBackgroundColor:uint = 0xF0F0F0;
    public var headerBarLogo:ImageDescriptor;
    public var headerBarDividerColor:uint = 0xD42525;
    public var centerCanvasBackgroundColor:uint = 0xFFFFFF;
    public var centerCanvasBackgroundAlpha:Number = 1;
    public var reportBackground:ImageDescriptor;
    public var reportBackgroundSize:String;

    public function ApplicationSkinTO() {
    }



    
}
}