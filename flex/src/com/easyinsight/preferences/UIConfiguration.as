package com.easyinsight.preferences {
import mx.collections.ArrayCollection;
import mx.controls.Alert;

public class UIConfiguration {

    public static const SHOW_COMBINE_SOURCES:String = "SHOW_COMBINE_SOURCES";
    public static const SHOW_CREATE_PACKAGE:String = "SHOW_CREATE_PACKAGE";
    public static const SHOW_DESKTOP_WIDGET:String = "SHOW_DESKTOP_WIDGET";
    public static const SHOW_ADMIN_DATA_SOURCES:String = "SHOW_ADMIN_DATA_SOURCES";
    public static const SHOW_COPY_DATA_SOURCES:String = "SHOW_COPY_DATA_SOURCES";

    public static const SHOW_CONNECTIONS:String = "SHOW_CONNECTIONS";
    public static const SHOW_EXCHANGE:String = "SHOW_EXCHANGE";
    public static const SHOW_KPIS:String = "SHOW_KPIS";
    public static const SHOW_APIS:String = "SHOW_API";
    public static const SHOW_GROUPS:String = "SHOW_GROUPS";
    public static const SHOW_HOME:String = "SHOW_HOME";
    public static const SHOW_MY_DATA:String = "SHOW_MY_DATA";
    public static const SHOW_ACCOUNT:String = "SHOW_ACCOUNT";
    public static const SHOW_DATA_TAB:String = "SHOW_DATA_TAB";
    public static const SHOW_SCRUB_TAB:String = "SHOW_SCRUB_TAB";

    public var configMap:Object = new Object();

    public var ownScorecard:Boolean = false;

    public function UIConfiguration() {
    }

    public static function fromUISettings(settings:UISettings):UIConfiguration {
        var uiConfiguration:UIConfiguration = new UIConfiguration();
        var options:ArrayCollection = uiConfiguration.getNodes();
        uiConfiguration.recurseOptions(options);
        for each (var visibilitySetting:UIVisibilitySetting in settings.visibilitySettings) {
            var option:UIOption = uiConfiguration.configMap[visibilitySetting.key];
            if (option != null) {                
                option.selected = visibilitySetting.selected;
            }
        }
        uiConfiguration.ownScorecard = settings.useCustomScorecard;
        return uiConfiguration;
    }

    private var roots:Array;

    public function getRoots():ArrayCollection {
        if (roots == null) {
            getNodes();
        }
        return new ArrayCollection(roots);
    }

    public function getNodes():ArrayCollection {
        var showCombineSources:UIOption = new UIOption(SHOW_COMBINE_SOURCES, "Combine Sources", []);
        var showCreatePackage:UIOption = new UIOption(SHOW_CREATE_PACKAGE, "Create Package", []);
        var showDesktopWidget:UIOption = new UIOption(SHOW_DESKTOP_WIDGET, "Desktop Widget", []);
        var showAdminDataSource:UIOption = new UIOption(SHOW_ADMIN_DATA_SOURCES, "Administer Data Sources", []);
        var showCopy:UIOption = new UIOption(SHOW_COPY_DATA_SOURCES, "Copy Data Sources", []);

        var showConnections:UIOption = new UIOption(SHOW_CONNECTIONS, "Connections", []);
        var showExchange:UIOption = new UIOption(SHOW_EXCHANGE, "Exchange", []);
        var showKPIs:UIOption = new UIOption(SHOW_KPIS, "KPIs", []);
        var showAPI:UIOption = new UIOption(SHOW_APIS, "API", []);
        var showGroups:UIOption = new UIOption(SHOW_GROUPS, "Groups", []);
        var showHome:UIOption = new UIOption(SHOW_HOME, "Home", []);
        var showMyData:UIOption = new UIOption(SHOW_MY_DATA, "My Data", [
            showCombineSources, showCreatePackage, showDesktopWidget, showAdminDataSource, showCopy
        ]);
        var showAccount:UIOption = new UIOption(SHOW_ACCOUNT, "Account", []);

        var headerConfiguration:UIOption = new UIOption(null, "Header Configuration", [
            showHome, showMyData, showConnections, showExchange, showGroups, showKPIs, showAPI, showAccount
        ]);

        var showDataTab:UIOption = new UIOption(SHOW_DATA_TAB, "Data Tab", []);
        var showScrubTab:UIOption = new UIOption(SHOW_SCRUB_TAB, "Scrub Tab", []);

        var reportEditorConfiguration:UIOption = new UIOption(null, "Report Editor", [
            showDataTab, showScrubTab
        ]);

        roots = [ headerConfiguration, reportEditorConfiguration ];

        return new ArrayCollection(roots);
    }

    private function recurseOptions(options:ArrayCollection):void {
        for each (var option:UIOption in options) {
            configMap[option.key] = option;
            recurseOptions(option.children);
        }
    }

    public function getConfiguration(key:String):UIOption {
        return configMap[key];        
    }}
}
