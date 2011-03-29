/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/20/11
 * Time: 8:28 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.datasources {
public class DataSourceBehavior {
    public function DataSourceBehavior() {
    }

    public static function pullDataSource(dataSourceType:int):Boolean {
        switch (dataSourceType) {
            case DataSourceType.STATIC:
            case DataSourceType.EMPTY:
            case DataSourceType.BASECAMP:
            case DataSourceType.HIGHRISE:
            case DataSourceType.PIVOTAL_TRACKER:
            case DataSourceType.WHOLE_FOODS:
            case DataSourceType.LINKEDIN:
            case DataSourceType.CONSTANT_CONTACT:
            case DataSourceType.BATCHBOOK:
            case DataSourceType.CAMPAIGN_MONITOR:
            case DataSourceType.ZENDESK:
                return true;
        }
        return false;
    }

    public static function sizeLabel(dataSourceType:int, size:int):String {
        var text:String = "";
        switch (dataSourceType) {
            case DataSourceType.ANALYSIS:
                    text = "( Derived )";
                    break;
                case DataSourceType.SALESFORCE:
                case DataSourceType.GOOGLE_ANALYTICS:
                case DataSourceType.FRESHBOOKS:
                case DataSourceType.SENDGRID:
                case DataSourceType.GOOGLE:
                case DataSourceType.TWITTER:
                case DataSourceType.CLOUD_WATCH:
                case DataSourceType.REDIRECT:
                case DataSourceType.QUICKBASE:
                case DataSourceType.CLEARDB:
                    text = "( Live )";
                    break;
                case DataSourceType.COMPOSITE:
                case DataSourceType.FEDERATED:
                    text = "( Composite )";
                    break;
                default:
                    text = String(size);
                    break;
        }
        return text;
    }
}
}
