package com.easyinsight.core;

/**
 * User: James Boe
 * Date: Mar 30, 2009
 * Time: 2:31:58 PM
 */
public class DataSourceDescriptor extends EIDescriptor {
    @Override
    public int getType() {
        return EIDescriptor.DATA_SOURCE;
    }

    public DataSourceDescriptor() {
    }

    public DataSourceDescriptor(String name, long id) {
        super(name, id);
    }
}
