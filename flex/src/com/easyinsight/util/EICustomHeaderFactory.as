package com.easyinsight.util {
import mx.core.IFactory;
public class EICustomHeaderFactory implements IFactory {
    public function EICustomHeaderFactory() {
    }

    public function newInstance():* {
        return new EICustomHeader();
    }
}
}