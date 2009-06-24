package com.easyinsight.util {
import com.easyinsight.quicksearch.EIDescriptor;

public interface IAsyncScreenFactory {
    function createScreen(descriptor:EIDescriptor):IAsyncScreen;
}
}