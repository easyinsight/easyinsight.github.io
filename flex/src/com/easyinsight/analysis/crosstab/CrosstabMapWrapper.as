/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 12:51 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {
import flash.utils.Dictionary;

[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.CrosstabMapWrapper")]
public class CrosstabMapWrapper {

    public var map:Object;

    public function CrosstabMapWrapper() {
    }
}
}
