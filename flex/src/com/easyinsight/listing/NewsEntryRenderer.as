/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/24/12
 * Time: 8:43 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.listing {
import com.easyinsight.account.NewsEntry;
import com.easyinsight.util.AutoSizeTextArea;
import com.easyinsight.util.AutoSizeTextArea;

import mx.containers.HBox;

import mx.containers.VBox;
import mx.controls.Label;
import mx.controls.Spacer;

import mx.controls.TextArea;
import mx.formatters.DateFormatter;

public class NewsEntryRenderer extends VBox {

    private var newsEntry:NewsEntry;

    private var textArea:AutoSizeTextArea;
    private var titleBar:HBox;
    private var titleLabel:Label;
    private var dateLabel:Label;

    public function NewsEntryRenderer() {
        textArea = new AutoSizeTextArea();
        textArea.editable = false;
        textArea.width = 600;
        titleLabel = new Label();
        titleLabel.setStyle("fontSize", 16);
        dateLabel = new Label();
        dateLabel.setStyle("fontSize", 16);
    }

    override protected function createChildren():void {
        super.createChildren();
        titleBar = new HBox();
        titleBar.addChild(titleLabel);
        var spacer:Spacer = new Spacer();
        spacer.percentWidth = 100;
        titleBar.addChild(spacer);
        titleBar.addChild(dateLabel);
        addChild(titleBar);
        addChild(textArea);
    }

    override public function set data(val:Object):void {
        newsEntry = val as NewsEntry;
        if (newsEntry != null) {
            titleLabel.text = newsEntry.title != null ? newsEntry.title : "";
            var df:DateFormatter = new DateFormatter();
            df.formatString = "YYYY-MM-DD";
            dateLabel.text = df.format(newsEntry.date);
            textArea.htmlText = newsEntry.news;
        }
    }
}
}
