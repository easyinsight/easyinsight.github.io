package com.easyinsight.datasources {
import mx.core.IFactory;

public class DataSourceInfoRendererFactory implements IFactory {
    public function DataSourceInfoRendererFactory() {
    }

    public function newInstance():* {
        return new DataSourceInfoRenderer();
    }
}
}