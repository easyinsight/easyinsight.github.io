package com.easyinsight.skin {
import flash.events.Event;
import flash.events.EventDispatcher;

public class ApplicationSkin extends EventDispatcher {

    private var _coreAppBackgroundImage:Object;
    private var _coreAppBackgroundColor:uint = 0x818285;
    private var _coreAppBackgroundSize:String = "auto";
    private var _headerBarBackgroundColor:uint = 0xF0F0F0;
    private var _headerBarLogo:Object;
    private var _headerBarDividerColor:uint = 0xD42525;
    private var _centerCanvasBackgroundColor:uint = 0xFFFFFF;
    private var _centerCanvasBackgroundAlpha:Number = 1;
    [Embed(source="../../../../assets/background2.JPG")]
    private var defaultReportBackground:Class;
    private var _reportBackground:Object = defaultReportBackground;
    private var _reportBackgroundSize:String = "100%";

    private var _myDataName:Boolean;
    private var _myDataSize:Boolean;
    private var _myDataOwner:Boolean;
    private var _myDataCreationDate:Boolean;
    private var _myDataLastTime:Boolean;
    private var _myDataCombine:Boolean;
    private var _myDataNewScorecard:Boolean;
    private var _myDataNewKPITree:Boolean;
    private var _myDataNewDashboard:Boolean;
    private var _myDataLookupTable:Boolean;

    public function ApplicationSkin() {
        super();
    }

    private static var _instance:ApplicationSkin;

    public static function instance():ApplicationSkin {
        return _instance;
    }

    public static function initialize():void {
        _instance = new ApplicationSkin();
    }

    public function applyUserSettings(appSkin:ApplicationSkinTO):void {
        if (appSkin.coreAppBackgroundImage != null) {
            var loader:ImageLoader = new ImageLoader();
            loader.addEventListener(ImageLoadEvent.IMAGE_LOADED, function(event:ImageLoadEvent):void {
                coreAppBackgroundImage = event.bitmap;
            });
            loader.load(appSkin.coreAppBackgroundImage.id);
        }
        headerBarBackgroundColor = appSkin.headerBarBackgroundColor;
        headerBarDividerColor = appSkin.headerBarDividerColor;
        centerCanvasBackgroundColor = appSkin.centerCanvasBackgroundColor;
        centerCanvasBackgroundAlpha = appSkin.centerCanvasBackgroundAlpha;
        coreAppBackgroundColor = appSkin.coreAppBackgroundColor;
        coreAppBackgroundSize = appSkin.coreAppBackgroundSize;
        if (appSkin.reportBackground != null) {
            var reportLoader:ImageLoader = new ImageLoader();
            reportLoader.addEventListener(ImageLoadEvent.IMAGE_LOADED, function(event:ImageLoadEvent):void {
                reportBackground = event.bitmap;
            });
            reportLoader.load(appSkin.reportBackground.id);
            reportBackgroundSize = appSkin.reportBackgroundSize;
        }
        if (appSkin.headerBarLogo != null) {
            var headerBarLoader:ImageLoader = new ImageLoader();
            headerBarLoader.addEventListener(ImageLoadEvent.IMAGE_LOADED, function(event:ImageLoadEvent):void {
                headerBarLogo = event.bitmap;
            });
            headerBarLoader.load(appSkin.headerBarLogo.id);
        }
        myDataName = appSkin.myDataName;
        myDataSize = appSkin.myDataSize;
        myDataOwner = appSkin.myDataOwner;
        myDataCreationDate = appSkin.myDataCreationDate;
        myDataLastTime = appSkin.myDataLastTime;
        myDataCombine = appSkin.myDataCombine;
        myDataNewScorecard = appSkin.myDataNewScorecard;
        myDataNewKPITree = appSkin.myDataNewKPITree;
        myDataNewDashboard = appSkin.myDataNewDashboard;
        myDataLookupTable = appSkin.myDataLookupTable;
    }

    [Bindable(event="reportBackgroundChanged")]
    public function get reportBackground():Object {
        return _reportBackground;
    }

    public function set reportBackground(value:Object):void {
        if (_reportBackground == value) return;
        _reportBackground = value;
        dispatchEvent(new Event("reportBackgroundChanged"));
    }

    [Bindable(event="reportBackgroundSizeChanged")]
    public function get reportBackgroundSize():String {
        return _reportBackgroundSize;
    }

    public function set reportBackgroundSize(value:String):void {
        if (_reportBackgroundSize == value) return;
        _reportBackgroundSize = value;
        dispatchEvent(new Event("reportBackgroundSizeChanged"));
    }

    [Bindable(event="coreAppBackgroundSizeChanged")]
    public function get coreAppBackgroundSize():String {
        return _coreAppBackgroundSize;
    }

    public function set coreAppBackgroundSize(value:String):void {
        if (_coreAppBackgroundSize == value) return;
        _coreAppBackgroundSize = value;
        dispatchEvent(new Event("coreAppBackgroundSizeChanged"));
    }

    [Bindable(event="centerCanvasBackgroundColorChanged")]
    public function get centerCanvasBackgroundColor():uint {
        return _centerCanvasBackgroundColor;
    }

    public function set centerCanvasBackgroundColor(value:uint):void {
        if (_centerCanvasBackgroundColor == value) return;
        _centerCanvasBackgroundColor = value;
        dispatchEvent(new Event("centerCanvasBackgroundColorChanged"));
    }

    [Bindable(event="centerCanvasBackgroundAlphaChanged")]
    public function get centerCanvasBackgroundAlpha():Number {
        return _centerCanvasBackgroundAlpha;
    }

    public function set centerCanvasBackgroundAlpha(value:Number):void {
        if (_centerCanvasBackgroundAlpha == value) return;
        _centerCanvasBackgroundAlpha = value;
        dispatchEvent(new Event("centerCanvasBackgroundAlphaChanged"));
    }

    [Bindable(event="headerBarBackgroundColorChanged")]
    public function get headerBarBackgroundColor():uint {
        return _headerBarBackgroundColor;
    }

    public function set headerBarBackgroundColor(value:uint):void {
        if (_headerBarBackgroundColor == value) return;
        _headerBarBackgroundColor = value;
        dispatchEvent(new Event("headerBarBackgroundColorChanged"));
    }

    [Bindable(event="headerBarLogoChanged")]
    public function get headerBarLogo():Object {
        return _headerBarLogo;
    }

    public function set headerBarLogo(value:Object):void {
        if (_headerBarLogo == value) return;
        _headerBarLogo = value;
        dispatchEvent(new Event("headerBarLogoChanged"));
    }

    [Bindable(event="headerBarDividerColorChanged")]
    public function get headerBarDividerColor():uint {
        return _headerBarDividerColor;
    }

    public function set headerBarDividerColor(value:uint):void {
        if (_headerBarDividerColor == value) return;
        _headerBarDividerColor = value;
        dispatchEvent(new Event("headerBarDividerColorChanged"));
    }

    [Bindable(event="coreAppBackgroundImageChanged")]
    public function get coreAppBackgroundImage():Object {
        return _coreAppBackgroundImage;
    }

    public function set coreAppBackgroundImage(value:Object):void {
        if (_coreAppBackgroundImage == value) return;
        _coreAppBackgroundImage = value;
        dispatchEvent(new Event("coreAppBackgroundImageChanged"));
    }

    [Bindable(event="coreAppBackgroundColorChanged")]
    public function get coreAppBackgroundColor():uint {
        return _coreAppBackgroundColor;
    }

    public function set coreAppBackgroundColor(value:uint):void {
        if (_coreAppBackgroundColor == value) return;
        _coreAppBackgroundColor = value;
        dispatchEvent(new Event("coreAppBackgroundColorChanged"));
    }


    [Bindable(event="myDataNameChanged")]
    public function get myDataName():Boolean {
        return _myDataName;
    }

    public function set myDataName(value:Boolean):void {
        if (_myDataName == value) return;
        _myDataName = value;
        dispatchEvent(new Event("myDataNameChanged"));
    }

    [Bindable(event="myDataSizeChanged")]
    public function get myDataSize():Boolean {
        return _myDataSize;
    }

    public function set myDataSize(value:Boolean):void {
        if (_myDataSize == value) return;
        _myDataSize = value;
        dispatchEvent(new Event("myDataSizeChanged"));
    }

    [Bindable(event="myDataOwnerChanged")]
    public function get myDataOwner():Boolean {
        return _myDataOwner;
    }

    public function set myDataOwner(value:Boolean):void {
        if (_myDataOwner == value) return;
        _myDataOwner = value;
        dispatchEvent(new Event("myDataOwnerChanged"));
    }

    [Bindable(event="myDataCreationDateChanged")]
    public function get myDataCreationDate():Boolean {
        return _myDataCreationDate;
    }

    public function set myDataCreationDate(value:Boolean):void {
        if (_myDataCreationDate == value) return;
        _myDataCreationDate = value;
        dispatchEvent(new Event("myDataCreationDateChanged"));
    }

    [Bindable(event="myDataLastTimeChanged")]
    public function get myDataLastTime():Boolean {
        return _myDataLastTime;
    }

    public function set myDataLastTime(value:Boolean):void {
        if (_myDataLastTime == value) return;
        _myDataLastTime = value;
        dispatchEvent(new Event("myDataLastTimeChanged"));
    }

    [Bindable(event="myDataCombineChanged")]
    public function get myDataCombine():Boolean {
        return _myDataCombine;
    }

    public function set myDataCombine(value:Boolean):void {
        if (_myDataCombine == value) return;
        _myDataCombine = value;
        dispatchEvent(new Event("myDataCombineChanged"));
    }

    [Bindable(event="myDataNewScorecardChanged")]
    public function get myDataNewScorecard():Boolean {
        return _myDataNewScorecard;
    }

    public function set myDataNewScorecard(value:Boolean):void {
        if (_myDataNewScorecard == value) return;
        _myDataNewScorecard = value;
        dispatchEvent(new Event("myDataNewScorecardChanged"));
    }

    [Bindable(event="myDataNewKPITreeChanged")]
    public function get myDataNewKPITree():Boolean {
        return _myDataNewKPITree;
    }

    public function set myDataNewKPITree(value:Boolean):void {
        if (_myDataNewKPITree == value) return;
        _myDataNewKPITree = value;
        dispatchEvent(new Event("myDataNewKPITreeChanged"));
    }

    [Bindable(event="myDataNewDashboardChanged")]
    public function get myDataNewDashboard():Boolean {
        return _myDataNewDashboard;
    }

    public function set myDataNewDashboard(value:Boolean):void {
        if (_myDataNewDashboard == value) return;
        _myDataNewDashboard = value;
        dispatchEvent(new Event("myDataNewDashboardChanged"));
    }

    [Bindable(event="myDataLookupTableChanged")]
    public function get myDataLookupTable():Boolean {
        return _myDataLookupTable;
    }

    public function set myDataLookupTable(value:Boolean):void {
        if (_myDataLookupTable == value) return;
        _myDataLookupTable = value;
        dispatchEvent(new Event("myDataLookupTableChanged"));
    }
}
}