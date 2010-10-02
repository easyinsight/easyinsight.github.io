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
    public static const SHOW_CALCULATION:String = "SHOW_CALCULATION";
    public static const SHOW_HIERARCHY:String = "SHOW_HIERARCHY";
    public static const SHOW_REPORT_EDITOR_API:String = "SHOW_REPORT_EDITOR_API";
    public static const SHOW_TEXT_REPLACES:String = "SHOW_TEXT_REPLACES";
    public static const SHOW_FILTER_BUTTONS:String = "SHOW_FILTER_BUTTONS";
    public static const SHOW_EXPORT:String = "SHOW_EXPORT_TAB";
    public static const SHOW_EMAIL:String = "SHOW_EMAIL";
    public static const SHOW_DLS:String = "SHOW_DLS";
    public static const SHOW_EMBED:String = "SHOW_EMBED";
    public static const SHOW_PNG:String = "SHOW_PNG";
    public static const SHOW_NETVIBES:String = "SHOW_NETVIBES";
    public static const SHOW_IGOOGLE:String = "SHOW_IGOOGLE";
    public static const SHOW_SHARING:String = "SHOW_SHARING";
    public static const SHOW_SHARING_OTHERS:String = "SHOW_SHARING_OTHERS";
    public static const SHOW_SHARING_WORLD:String = "SHOW_SHARING_WORLD";
    public static const SHOW_SHARING_TEMPLATE:String = "SHOW_SHARING_TEMPLATE";
    public static const SHOW_LOOKUP_TABLE:String = "SHOW_LOOKUP_TABLE";
    public static const SHOW_DELETE_DATA:String = "SHOW_DELETE_DATA";
    public static const SHOW_SIZE_DATA:String = "SHOW_SIZE_DATA";
    public static const SHOW_TIME_DATA:String = "SHOW_TIME_DATA";
    public static const SHOW_REPORT_BUG:String = "SHOW_REPORT_BUG";
    public static const SHOW_KPI_TREE_ADD:String = "SHOW_KPI_TREE_ADD";
    public static const SHOW_REFRESH_DATA:String = "SHOW_REFRESH_DATA";
    public static const SHOW_SCORECARD_CONTROLS:String = "SHOW_SCORECARD_CONTROLS";

    public var configMap:Object = new Object();

    public var ownScorecard:Boolean = false;
    public var marketplaceEnabled:Boolean = true;
    public var publicDataEnabled:Boolean = true;
    public var reportSharingEnabled:Boolean = true;

    public function UIConfiguration() {
    }

    public static function fromUISettings(settings:UISettings):UIConfiguration {
        var uiConfiguration:UIConfiguration = new UIConfiguration();
        var options:ArrayCollection = uiConfiguration.getNodes();
        var map:Object = new Object();
        for each (var visibilitySetting:UIVisibilitySetting in settings.visibilitySettings) {
            map[visibilitySetting.key] = visibilitySetting;
        }
        uiConfiguration.recurseOptions(options, map);

        /*for each (var visibilitySetting:UIVisibilitySetting in settings.visibilitySettings) {
            var option:UIOption = uiConfiguration.configMap[visibilitySetting.key];
            if (option != null) {                
                option.selected = visibilitySetting.selected;
            }
        }*/
        uiConfiguration.ownScorecard = settings.useCustomScorecard;
        uiConfiguration.marketplaceEnabled = settings.marketplace;
        uiConfiguration.publicDataEnabled = settings.publicSharing;
        uiConfiguration.reportSharingEnabled = settings.reportSharing;
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
        var showAdminDataSource:UIOption = new UIOption(SHOW_ADMIN_DATA_SOURCES, "Administer Data Sources", []);
        var showCopy:UIOption = new UIOption(SHOW_COPY_DATA_SOURCES, "Copy Data Sources", []);
        var showLookupTables:UIOption = new UIOption(SHOW_LOOKUP_TABLE, "Show Lookup Tables", []);
        var showRefreshData:UIOption = new UIOption(SHOW_REFRESH_DATA, "Refresh Data Sources", []);
        var copyData:UIOption = new UIOption(SHOW_COPY_DATA_SOURCES, "Copy Data Sources", []);
        var deleteData:UIOption = new UIOption(SHOW_DELETE_DATA, "Delete Data Sources", []);
        var sizeData:UIOption = new UIOption(SHOW_SIZE_DATA, "Data Source Size", []);
        var timeData:UIOption = new UIOption(SHOW_TIME_DATA, "Data Source Last Time", []);
        var kpiTreeAdd:UIOption = new UIOption(SHOW_KPI_TREE_ADD, "Add KPI Tree", []);

        var showAPIReportEditorOption:UIOption = new UIOption(SHOW_REPORT_EDITOR_API, "Data Source API Info", []);
        var showTextReplace:UIOption = new UIOption(SHOW_TEXT_REPLACES, "Text Replacement", []);
        var showCreateCalculation:UIOption = new UIOption(SHOW_CALCULATION, "Create Calculation", []);
        var showCreateHierarchy:UIOption = new UIOption(SHOW_HIERARCHY, "Create Hierarchy", []);
        var showFilterButtons:UIOption = new UIOption(SHOW_FILTER_BUTTONS, "Filter Buttons", []);
        var scorecardControls:UIOption = new UIOption(SHOW_SCORECARD_CONTROLS, "Scorecard Controls", []);

        var showDLS:UIOption = new UIOption(SHOW_DLS, "Data Level Security", []);
        var showEmbed:UIOption = new UIOption(SHOW_EMBED, "Embed Report HTML", []);
        var showPNG:UIOption = new UIOption(SHOW_PNG, "Export PNG", []);
        var showNetvibes:UIOption = new UIOption(SHOW_NETVIBES, "Export Netvibes", []);
        var showIGoogle:UIOption = new UIOption(SHOW_IGOOGLE, "Export iGoogle", []);
        var showEmail:UIOption = new UIOption(SHOW_EMAIL, "Email Scheduling", []);

        var showConnections:UIOption = new UIOption(SHOW_CONNECTIONS, "Connections", []);
        var showExchange:UIOption = new UIOption(SHOW_EXCHANGE, "Exchange", []);
        var showAccount:UIOption = new UIOption(SHOW_ACCOUNT, "Account", []);
        var showAPI:UIOption = new UIOption(SHOW_APIS, "API", []);
        var showGroups:UIOption = new UIOption(SHOW_GROUPS, "Groups", []);
        var showReportBug:UIOption = new UIOption(SHOW_REPORT_BUG, "Report Bug", []);
        var showMyData:UIOption = new UIOption(SHOW_MY_DATA, "My Data", [
            showCombineSources, showCreatePackage, showAdminDataSource, copyData, showLookupTables, showRefreshData, deleteData,
                sizeData, timeData, kpiTreeAdd
        ]);

        var headerConfiguration:UIOption = new UIOption(null, "Header Configuration", [
            showMyData, showConnections, showExchange, showGroups, showAccount, showAPI, showReportBug
        ]);

        var scorecardConfiguration:UIOption = new UIOption(null, "Scorecard Configuration", [
            scorecardControls
        ]);

        var showSharing:UIOption = new UIOption(SHOW_SHARING, "Sharing Tab", []);

        var showDataTab:UIOption = new UIOption(SHOW_DATA_TAB, "Data Tab", [ showAPIReportEditorOption, showCreateCalculation,
                showCreateHierarchy, showTextReplace]);

        var showExport:UIOption = new UIOption(SHOW_EXPORT, "Export Tab", [
                showDLS, showEmbed, showPNG, showNetvibes, showIGoogle, showEmail
        ]);


        var reportEditorConfiguration:UIOption = new UIOption(null, "Report Editor", [
            showDataTab, showFilterButtons, showExport, showSharing
        ]);

        roots = [ headerConfiguration, reportEditorConfiguration, scorecardConfiguration ];

        return new ArrayCollection(roots);
    }

    public function toggle(key:String, state:Boolean):void {
        for each (var option:UIOption in roots) {
            if (option.key == key) {
                option.selected = state;
            }
            toggleNodes(key, state, option.children);
        }
    }

    private function toggleNodes(key:String, state:Boolean, nodes:ArrayCollection):void {
        for each (var option:UIOption in nodes) {
            if (option.key == key) {
                option.selected = state;
            }
            toggleNodes(key, state, option.children);
        }
    }

    private function recurseOptions(options:ArrayCollection, map:Object):void {
        for each (var option:UIOption in options) {
            configMap[option.key] = option;
            var setting:UIVisibilitySetting = map[option.key];
            if (setting != null) {
                option.selected = setting.selected;
            } else {
                option.selected = true;
            }
            recurseOptions(option.children, map);
        }
    }

    public function getConfiguration(key:String):UIOption {
        var option:UIOption = configMap[key];
        if (option == null) {
            option = new UIOption(key, "", []);
        }
        return option;
    }}
}
