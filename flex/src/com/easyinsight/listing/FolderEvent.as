/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/15/12
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.solutions.CustomFolder;

import flash.events.Event;

public class FolderEvent extends Event {

    public static const RENAME_FOLDER:String = "renameFolder";
    public static const RENAMED_FOLDER:String = "renamedFolder";
    public static const DELETE_FOLDER:String = "deleteFolder";

    public var folder:CustomFolder;

    public function FolderEvent(type:String, customFolder:CustomFolder) {
        super(type, true);
        this.folder = customFolder;
    }

    override public function clone():Event {
        return new FolderEvent(type, folder);
    }
}
}
