/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/3/11
 * Time: 9:23 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import com.easyinsight.administration.feed.ServerDataSourceDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.datafeeds.quickbase.QuickbaseDatabaseSource")]
public class QuickbaseDatabaseSource extends ServerDataSourceDefinition {

    public var databaseID:String;
    public var indexEnabled:Boolean;
    public var weightsID:int;

    public function QuickbaseDatabaseSource() {
    }
}
}
