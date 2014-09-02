/**
 * Created by jamesboe on 8/29/14.
 */
package com.easyinsight {
import mx.core.UIComponent;

public interface IWindowManagement {
    function addWindow(window:UIComponent):void;
    function restoreReport():void;
    function hideReport():void;
}
}