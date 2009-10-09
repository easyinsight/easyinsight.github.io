package com.easyinsight.util {
import com.adobe.ac.mxeffects.CubeRotate;
import com.easyinsight.framework.DataServiceLoadingEvent;

import com.easyinsight.quicksearch.EIDescriptor;

import mx.containers.ViewStack;
import mx.controls.Alert;
import mx.core.Container;
[Event(name="asyncLoadStart", type="com.easyinsight.util.AsyncLoadEvent")]
[Event(name="asyncLoadSuccess", type="com.easyinsight.util.AsyncLoadEvent")]
[Event(name="asyncLoadFailure", type="com.easyinsight.util.AsyncLoadEvent")]
public class AsyncViewStack extends ViewStack{

    private var loadingScreen:IAsyncScreen;

    private var descriptorMap:Object = new Object();

    private var _screenRenderer:IAsyncScreenFactory;

    private var selectedDescriptor:EIDescriptor;

    private var selectedDescriptorChanged:Boolean;

    public function AsyncViewStack() {
        super();
    }

    public function set screenRenderer(value:IAsyncScreenFactory):void {
        _screenRenderer = value;
    }

    public function set targetDescriptor(descriptor:EIDescriptor):void {
        if (selectedDescriptor != descriptor) {
            selectedDescriptorChanged = true;
            selectedDescriptor = descriptor;
            invalidateProperties();
        }
    }

    

    private function gotInitialData(event:DataServiceLoadingEvent):void {
        var screen:IAsyncScreen = loadingScreen;
        var dataViewPanel:Container = screen.getContainer();
        dataViewPanel.removeEventListener(DataServiceLoadingEvent.LOADING_STOPPED, gotInitialData);
        loadingScreen = null;
        if (getChildren().length == 1) {
            selectedChild = dataViewPanel;
        } else {
            var targetIndex:int = getChildIndex(dataViewPanel);
            //Alert.show("target index = " + targetIndex + " , selected index = " + selectedIndex);
            // TODO: make effect pluggable
            var cubeRotate:CubeRotate = new CubeRotate();
            if (targetIndex > selectedIndex) {
                cubeRotate.direction = "RIGHT";
            } else {
                cubeRotate.direction = "LEFT";
            }
            cubeRotate.duration = 1000;
            cubeRotate.target = selectedChild;
            cubeRotate.siblings = [ dataViewPanel ];
            selectedChild = dataViewPanel;
            cubeRotate.play();
        }
        dispatchEvent(new AsyncLoadEvent(AsyncLoadEvent.LOAD_SUCCESS, screen));
    }

    override protected function commitProperties():void {
        super.commitProperties();
        if (selectedDescriptorChanged) {
            selectedDescriptorChanged = false;
            var idString:String = selectedDescriptor.getType() + "-" + selectedDescriptor.id;
            var screen:IAsyncScreen = descriptorMap[idString];
            var newScreen:Boolean = false;
            if (screen == null) {
                //Alert.show("creating new!");
                newScreen = true;
                screen = _screenRenderer.createScreen(selectedDescriptor);
                descriptorMap[idString] = screen;
            }
            loadingScreen = screen;
            var container:Container = screen.getContainer();
            container.addEventListener(DataServiceLoadingEvent.LOADING_STOPPED, gotInitialData);
            dispatchEvent(new AsyncLoadEvent(AsyncLoadEvent.LOAD_START));
            if (newScreen) {
                addChild(container);
            } else {
                var event:DataServiceLoadingEvent = new DataServiceLoadingEvent(DataServiceLoadingEvent.LOADING_STOPPED);
                gotInitialData(event);
            }
        }
    }
}
}