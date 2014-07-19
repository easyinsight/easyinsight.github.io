/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 4/28/11
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {

import com.easyinsight.framework.User;

import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.navigateToURL;

import mx.controls.LinkButton;

public class WhatsNewButton extends LinkButton {

    public function WhatsNewButton() {

        if (User.getInstance().newsDate != null) {
            if (User.getInstance().newsDismissDate == null || User.getInstance().newsDismissDate.getTime() < User.getInstance().newsDate.getTime()) {
                //if (getChildren().length > 0 && !(getChildAt(0) is NewsBar)) {
/*                    var newsBar:NewsBar = new NewsBar();
                    newsBar.addEventListener("dismissNews", onDismissed);
                    if (topBox == null) {
                        topBox = new HBox();
                        addChildAt(topBox, 0);
                    }
                    topBox.addChildAt(newsBar, 0);*/
                //}
            }
        }

        setStyle("color", 0xFFFFFF);
        setStyle("themeColor", 0xFFFFFF);
        setStyle("fontSize", 14);
        label = "What's New";
        addEventListener(MouseEvent.CLICK, onClick);
    }

    private static function onClick(event:MouseEvent):void {
        navigateToURL(new URLRequest("https://www.easy-insight.com/app/whatsnew.jsp"));
    }
}
}
