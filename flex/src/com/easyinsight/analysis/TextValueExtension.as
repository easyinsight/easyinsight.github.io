/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/6/11
 * Time: 12:23 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
[Bindable]
[RemoteClass(alias="com.easyinsight.analysis.TextValueExtension")]
public class TextValueExtension extends ValueExtension {

    public var color:uint;
    public var bold:Boolean;
    public var italic:Boolean;

    public function TextValueExtension() {
    }
}
}
