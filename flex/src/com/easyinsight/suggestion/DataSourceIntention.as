/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/20/11
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.suggestion {
import com.easyinsight.datasources.DataSourceRefreshExecutor;

[Bindable]
[RemoteClass(alias="com.easyinsight.intention.DataSourceIntention")]
public class DataSourceIntention extends Intention {

    public var refreshData:Boolean;
    public var adminData:Boolean;

    public function DataSourceIntention() {
    }

    override public function apply(suggestionMetadata:SuggestionMetadata):void {
        if (refreshData) {
            var executor:DataSourceRefreshExecutor = new DataSourceRefreshExecutor();
            executor.refresh(suggestionMetadata.dataSource);
        } else if (adminData) {
            dispatchEvent(new IntentionTriggerEvent(IntentionTriggerEvent.INTENTION_TRIGGER, true));
        }
    }
}
}
