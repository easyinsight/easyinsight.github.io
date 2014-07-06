package com.easyinsight.export;

/**
 * User: jamesboe
 * Date: 6/30/14
 * Time: 12:50 PM
 */
public class PDFImageData {
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
