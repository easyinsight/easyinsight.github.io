/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/28/11
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.framework.DataFolder;
import com.easyinsight.quicksearch.EIDescriptor;

import flash.events.Event;

public class ChangeFolderEvent extends Event {

    public static const CHANGE_FOLDER:String = "changeFolder";

    public var movingItem:EIDescriptor;
    public var targetFolder:DataFolder;

    public function ChangeFolderEvent(movingItem:EIDescriptor, targetFolder:DataFolder) {
        super(CHANGE_FOLDER, true);
        this.movingItem = movingItem;
        this.targetFolder = targetFolder;
    }

    override public function clone():Event {
        return new ChangeFolderEvent(movingItem, targetFolder);
    }
}
}
