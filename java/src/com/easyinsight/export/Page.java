package com.easyinsight.export;

/**
 * User: jamesboe
 * Date: 1/9/12
 * Time: 5:09 PM
 */
public class Page {
    private byte[] bytes;
    private int width;
    private int height;

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
