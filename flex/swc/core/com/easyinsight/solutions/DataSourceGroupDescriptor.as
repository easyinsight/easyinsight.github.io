/**
 * Created by jamesboe on 9/2/14.
 */
package com.easyinsight.solutions {
[Bindable]
[RemoteClass(alias="com.easyinsight.core.DataSourceGroupDescriptor")]
public class DataSourceGroupDescriptor extends DataSourceDescriptor {

    public var partOfGroup:Boolean;

    public function DataSourceGroupDescriptor() {
    }
}
}
