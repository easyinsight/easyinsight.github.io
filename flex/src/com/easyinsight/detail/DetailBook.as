package com.easyinsight.detail {
import flash.events.MouseEvent;

import mx.collections.ArrayCollection;
import mx.containers.HBox;
import mx.containers.TitleWindow;

import mx.containers.VBox;
import mx.controls.Button;
import mx.controls.Label;
import mx.controls.Spacer;
import mx.events.CloseEvent;

import mx.managers.PopUpManager;

import qs.controls.FlexBook;
import qs.controls.flexBookClasses.FlexBookEvent;

public class DetailBook extends TitleWindow{

    private var _details:ArrayCollection;
    private var book:FlexBook;

    private var prevButton:Button;
    private var nextButton:Button;

    public function DetailBook() {
        super();
        this.width = 800;
        this.height = 600;
        this.showCloseButton = true;
        this.addEventListener(CloseEvent.CLOSE, onClose);
    }

    private function onClose(event:CloseEvent):void {
        PopUpManager.removePopUp(this);
    }

    public function set details(val:ArrayCollection):void {
        _details = val;
    }

    private function onTurnEnd(event:FlexBookEvent):void {
        if (event.index == 0) {
            prevButton.enabled = false;
        }
        if (event.index > 0) {
            prevButton.enabled = true;
        }
        if (event.index == _details.length) {
            nextButton.enabled = false;
        }
        if (event.index < _details.length && false) {

        }

    }

    private function nextPage(event:MouseEvent):void {

    }

    private function previousPage(event:MouseEvent):void {

    }

    override protected function createChildren():void {
        super.createChildren();
        var box:VBox = new VBox();
        box.percentWidth = 100;
        box.percentHeight = 100;
        var titleBox:HBox = new HBox();
        titleBox.setStyle("horizontalAlign", "center");
        titleBox.percentWidth = 100;
        var titleLabel:Label = new Label();
        titleLabel.text = "Showing " + _details.length + " individual records. Click the page corners to see other records.";
        titleBox.addChild(titleLabel);
        box.addChild(titleBox);
        if (book == null) {
            book = new FlexBook();
            book.setStyle("activeGrabArea", "corner");
            book.itemSize = "page";
            book.setStyle("edgeAndCornerSize", 80);
            book.animateCurrentPageIndex = true;
            book.percentWidth = 100;
            book.percentHeight = 100;
            book.addEventListener(FlexBookEvent.TURN_END, onTurnEnd);
        }
        var pages:Array = [];
        for each (var item:Object in _details) {
            var detailPage:DetailPage = new DetailPage();
            detailPage.detailItem = item;
            //book.addChild(detailPage);
            pages.push(detailPage);
        }
        book.content = pages;
        box.addChild(book);
        var controlsBox:HBox = new HBox();
        controlsBox.percentWidth = 100;
        prevButton = new Button();
        prevButton.label = "Previous Page";
        prevButton.enabled = false;
        nextButton = new Button();
        nextButton.label = "Next Page";
        nextButton.enabled = _details.length > 2;
        var spacer:Spacer = new Spacer();
        spacer.percentWidth = 100;
        controlsBox.addChild(prevButton);
        controlsBox.addChild(spacer);
        controlsBox.addChild(nextButton);
        //box.addChild(controlsBox);
        addChild(box);
    }
}
}