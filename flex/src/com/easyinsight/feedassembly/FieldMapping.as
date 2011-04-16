/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/1/11
 * Time: 11:45 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.feedassembly {
import com.easyinsight.administration.feed.FeedDefinitionData;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.composite.FieldMapping")]
public class FieldMapping {

    public var federatedKey:String;
    public var sourceKey:String;

    public var dataSource:FeedDefinitionData;

    public function FieldMapping() {
    }
}
}
