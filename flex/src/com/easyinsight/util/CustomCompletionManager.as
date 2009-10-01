package com.easyinsight.util {

import mx.controls.TextInput;

public class CustomCompletionManager extends AutoCompleteManager{

    public function CustomCompletionManager() {
        super();
    }

    public function forceOpen():void {
        display(true, target as TextInput);
    }
}
}