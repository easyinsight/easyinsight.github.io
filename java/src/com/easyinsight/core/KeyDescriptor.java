package com.easyinsight.core;

/**
 * User: jamesboe
 * Date: 1/16/12
 * Time: 10:24 AM
 */
public class KeyDescriptor extends EIDescriptor {
    @Override
    public int getType() {
        return EIDescriptor.KEY;
    }
    
    public KeyDescriptor(Key key) {
        super(key.toKeyString(), key.getKeyID(), false);
    }
}
