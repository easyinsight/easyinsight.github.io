/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/15/11
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
package mx.core {
import flash.display.DisplayObject;

public class Application {
    public function Application() {
    }

    public static function get application():DisplayObject {
        return FlexGlobals.topLevelApplication as DisplayObject;
    }
}
}
