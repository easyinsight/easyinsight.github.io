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

    public static const USD:int = 1;
    public static const EURO:int = 2;
    public static const GBP:int = 3;

    public var color:uint;
    public var backgroundColor:uint = 0xFFFFFF;
    public var bold:Boolean;
    public var italic:Boolean;
    public var currency:int;
    public var currencyString;

    public function TextValueExtension() {
    }
}
}

