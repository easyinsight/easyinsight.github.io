package com.easyinsight.datafeeds.wholefoods;

import com.easyinsight.users.Credentials;

/**
 * User: jamesboe
 * Date: Oct 16, 2010
 * Time: 9:51:43 PM
 */
public class WholeFoodsCredentials extends Credentials {
    private byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
