/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 8/1/11
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.preferences {
import com.easyinsight.filtering.FilterDefinition;

[Bindable]
[RemoteClass(alias="com.easyinsight.preferences.UserDLSFilter")]
public class UserDLSFilter {

    public var filterDefinition:FilterDefinition;
    public var originalFilterID:int;

    public function UserDLSFilter() {
    }
}
}
