/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/17/10
 * Time: 9:31 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.customupload.QuickbaseDataSourceCreation;
import com.easyinsight.datasources.CompositeServerDataSource;
import com.easyinsight.listing.DataFeedDescriptor;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.quickbase.QuickbaseCompositeSource")]
public class QuickbaseCompositeSource extends CompositeServerDataSource {

    public var qbUserName:String;
    public var qbPassword:String;

    public var applicationToken:String;
    public var sessionTicket:String;
    public var host:String;

    public function QuickbaseCompositeSource() {
        super();
        this.feedName = "Quickbase";
    }

    override public function isLiveData():Boolean {
        return true;
    }

    override public function getFeedType():int {
        return DataFeedDescriptor.QUICKBASE;
    }

    override public function configClass():Class {
        return QuickbaseDataSourceCreation;
    }
}
}
