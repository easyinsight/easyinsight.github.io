/**
 * Created by ${PRODUCT_NAME}.
 * User: jamesboe
 * Date: 12/14/10
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.google {
import flash.events.Event;

import mx.collections.ArrayCollection;

public class GoogleSpreadsheetEvent extends Event {

    public static const GOOGLE_SPREADSHEET:String = "googleSpreadsheet";

    public var spreadsheets:ArrayCollection;

    public function GoogleSpreadsheetEvent(spreadsheets:ArrayCollection) {
        super(GOOGLE_SPREADSHEET);
        this.spreadsheets = spreadsheets;
    }

    override public function clone():Event {
        return new GoogleSpreadsheetEvent(spreadsheets);
    }
}
}
