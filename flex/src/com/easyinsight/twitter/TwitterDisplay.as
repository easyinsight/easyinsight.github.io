package com.easyinsight.twitter {
import flash.events.Event;

import flash.events.MouseEvent;

import flash.net.URLRequest;

import mx.binding.utils.BindingUtils;
import mx.collections.ArrayCollection;
import mx.containers.Box;
import mx.containers.HBox;
import mx.containers.VBox;
import mx.controls.Alert;
import mx.controls.Label;
import mx.controls.LinkButton;
import mx.controls.List;
import mx.core.ClassFactory;
import mx.rpc.events.FaultEvent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;
import mx.states.AddChild;
import mx.states.RemoveChild;
import mx.states.State;

public class TwitterDisplay extends VBox{

    private var updatesLabel:Label;
    private var twitter:VBox;
    private var followMeLink:LinkButton;

    private var _messages:ArrayCollection;

    private var _person:String;

    private var twitterService:RemoteObject;

    public function TwitterDisplay() {
        super();
        this.width = 300;
    }

    override protected function createChildren():void {
        super.createChildren();
        var failureState:State = new State();
        failureState.name = "NoTwitter";
        var addChildOp:AddChild = new AddChild();
        var failure:Box = new Box();
        failure.percentHeight = 100;
        failure.percentWidth = 100;
        failure.setStyle("horizontalAlign", "center");
        failure.setStyle("verticalAlign", "middle");
        var failureLabel:Label = new Label();
        failureLabel.text = "Can't connect to Twitter at the moment";
        failure.addChild(failureLabel);
        addChildOp.target = failure;

        var coreBox:VBox = new VBox();
        coreBox.percentWidth = 100;

        addChildOp.relativeTo = coreBox;

        if (updatesLabel == null) {
            updatesLabel = new Label();
            updatesLabel.text = "TWITTER UPDATES";
            updatesLabel.setStyle("fontSize", 10);
            updatesLabel.setStyle("color", 0x222222);
        }
        addChild(updatesLabel);
        if (twitter == null) {
            twitter = new VBox();
            /*twitter = new List();
            twitter.rowCount = 3;
            twitter.itemRenderer = new ClassFactory(TwitterItemRenderer);
            twitter.setStyle("backgroundAlpha", 0);
            BindingUtils.bindProperty(twitter, "dataProvider", this, "messages");*/
            twitter.percentWidth = 100;
        }

        coreBox.addChild(twitter);
        addChild(coreBox);
        if (followMeLink == null) {
            followMeLink = new LinkButton();
            followMeLink.setStyle("color", 0x222222);
            followMeLink.label = "follow Easy Insight on Twitter";
            followMeLink.addEventListener(MouseEvent.CLICK, followMe);
        }
        var box:HBox = new HBox();
        box.percentWidth = 100;
        box.setStyle("horizontalAlign", "right");
        box.addChild(followMeLink);
        addChild(box);

        if (twitterService == null) {
            twitterService = new RemoteObject();
            twitterService.destination = "twitter";
            twitterService.getTweets.addEventListener(ResultEvent.RESULT, onResult);
            twitterService.getTweets.addEventListener(FaultEvent.FAULT, onFault);
        }

        var removeChildOp:RemoveChild = new RemoveChild();
        removeChildOp.target = twitter;

        failureState.overrides = [ removeChildOp, addChildOp ];

        states = [ failureState ];
        twitterService.getTweets.send();
    }

    private function followMe(event:MouseEvent):void {
        flash.net.navigateToURL(new URLRequest("http://twitter.com/easyinsight"), "_blank");
    }

    private function onFault(event:FaultEvent):void {
        currentState = "NoTwitter";
    }

    private function onResult(event:ResultEvent):void {
        twitter.removeAllChildren();
        messages = twitterService.getTweets.lastResult as ArrayCollection;
        for each (var message:Tweet in messages) {
            var twitterRenderer:TwitterItemRenderer = new TwitterItemRenderer();
            twitterRenderer.data = message;
            twitter.addChild(twitterRenderer);
        }
    }


    [Bindable(event="messagesChanged")]
    public function get messages():ArrayCollection {
        return _messages;
    }

    public function set messages(value:ArrayCollection):void {
        if (_messages == value) return;
        _messages = value;
        dispatchEvent(new Event("messagesChanged"));
    }

    [Bindable(event="personChanged")]
    public function get person():String {
        return _person;
    }

    public function set person(value:String):void {
        if (_person == value) return;
        _person = value;
        dispatchEvent(new Event("personChanged"));
    }
}
}