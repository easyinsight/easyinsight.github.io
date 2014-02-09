/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/8/14
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
package {
import mx.core.UIComponent;

public interface IEasyInsightComponent {
    function go():void;
    function createView():UIComponent;
    function childSetup():void;
}
}
