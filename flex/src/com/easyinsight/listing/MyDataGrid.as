/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 1/19/11
 * Time: 12:19 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import flash.ui.ContextMenu;

import mx.controls.AdvancedDataGrid;

public class MyDataGrid extends AdvancedDataGrid {
    public function MyDataGrid() {
        var menu:ContextMenu = new ContextMenu();
        menu.hideBuiltInItems();
        //menu.customItems = items;
        this.contextMenu = menu;
    }
}
}
