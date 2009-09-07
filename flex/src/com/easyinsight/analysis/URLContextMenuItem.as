package com.easyinsight.analysis {
import flash.ui.ContextMenuItem;

public class URLContextMenuItem extends ContextMenuItem {

    public var urlLink:URLLink = new URLLink();

    public function URLContextMenuItem(label:String, blah:Boolean, urlLink:URLLink) {
        super(label, blah);
        this.urlLink = urlLink;
    }
}
}