package com.easyinsight.skin {

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.ApplicationSkin")]
public class ApplicationSkinTO {

    public var id:int;

    public var coreAppBackgroundImage:ImageDescriptor;
    public var coreAppBackgroundImageEnabled:Boolean = false;
    public var coreAppBackgroundColor:uint = 0x818285;
    public var coreAppBackgroundColorEnabled:Boolean = false;
    public var coreAppBackgroundSize:String = "100%";
    public var coreAppBackgroundSizeEnabled:Boolean = false;
    public var headerBarBackgroundColor:uint = 0xF0F0F0;
    public var headerBarBackgroundColorEnabled:Boolean = false;
    public var headerBarLogo:ImageDescriptor;
    public var headerBarLogoEnabled:Boolean = false;
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


    public function ApplicationSkinTO() {
    }



    
}
}