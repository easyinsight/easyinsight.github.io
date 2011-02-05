package com.easyinsight.framework {
public class PerspectiveInfo {

    public static const REPORT_EDITOR:int = 1;
    public static const REPORT_VIEW:int = 2;

    public static const SPREADSHEET_WIZARD:int = 4;
    public static const KPI_TREE_ADMIN:int = 5;
    public static const KPI_TREE_VIEW:int = 6;
    public static const DATA_SOURCE_ADMIN:int = 7;
    public static const POST_CONNECTION_INSTALL:int = 8;

    public static const LOOKUP_TABLE:int = 10;

    public static const GOAL_DETAIL:int = 12;
    public static const COMPOSITE_WORKSPACE:int = 13;
    public static const ACCOUNT_CREATION:int = 14;
    public static const HOME_PAGE:int = 15;
    /*public static const SCORECARDS:int = 16;*/
    public static const MY_DATA:int = 17;
    public static const CONNECTIONS:int = 18;
    public static const API:int = 19;
    public static const ACCOUNT:int = 20;
    public static const EXCHANGE:int = 21;
    public static const HELP:int = 22;
    public static const GROUPS:int = 24;
    public static const CONNECTION_DETAIL:int = 25;
    public static const KPI_WINDOW:int = 26;
    public static const ANALYSIS_ITEM_EDITOR:int = 27;
    public static const DASHBOARD_EDITOR:int = 28;
    public static const DASHBOARD_VIEW:int = 29;
    public static const SCHEDULING:int = 30;
    public static const SCORECARD_EDITOR:int = 31;
    public static const SCORECARD_VIEW:int = 32;

    public var perspectiveType:int;
    public var properties:Object = null;

    public function PerspectiveInfo(perspectiveType:int, properties:Object = null) {
        this.perspectiveType = perspectiveType;
        this.properties = properties;
    }
}
}