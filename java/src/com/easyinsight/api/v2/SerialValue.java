package com.easyinsight.api.v2;

import java.io.Serializable;

/**
 * User: jamesboe
 * Date: Sep 3, 2010
 * Time: 12:47:21 PM
 */
public class SerialValue implements Serializable {
    private String key;
    private static final long serialVersionUID = -2194446225686851986L;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
