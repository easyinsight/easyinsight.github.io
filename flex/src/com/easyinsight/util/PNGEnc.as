package com.easyinsight.util
{
import flash.display.*;
import flash.geom.*;
import flash.utils.*;

public class PNGEnc {

    public static function encode(img:BitmapData):ByteArray {
        // Create output byte array
        var png:ByteArray = new ByteArray();
        // Write PNG signature
        png.writeUnsignedInt(0x89504e47);
        png.writeUnsignedInt(0x0D0A1A0A);
        // Build IHDR chunk
        var IHDR:ByteArray = new ByteArray();
        IHDR.writeInt(img.width);
        IHDR.writeInt(img.height);
        IHDR.writeUnsignedInt(0x08060000); // 32bit RGBA
        IHDR.writeByte(0);
        writeChunk(png,0x49484452,IHDR);
        // Build IDAT chunk
        var IDAT:ByteArray= new ByteArray();
        for(var i:int=0;i < img.height;i++) {
            // no filter
            IDAT.writeByte(0);
            var p:uint;
            if ( !img.transparent ) {
                for(var j:int=0;j < img.width;j++) {
                    p = img.getPixel(j,i);
                    IDAT.writeUnsignedInt(
                        uint(((p&0xFFFFFF) << 8)|0xFF));
                }
            } else {
                for(var k:int=0;k < img.width;k++) {
                    p = img.getPixel32(k,i);
                    IDAT.writeUnsignedInt(
                        uint(((p&0xFFFFFF) << 8)|
                        ((p>>>24))));
                }
            }
        }
        IDAT.compress();
        writeChunk(png,0x49444154,IDAT);
        // Build IEND chunk
        writeChunk(png,0x49454E44,null);
        // return PNG
        return png;
    }

    private static var crcTable:Array;
    private static var crcTableComputed:Boolean = false;

    private static function writeChunk(png:ByteArray, 
            type:uint, data:ByteArray):void {
        if (!crcTableComputed) {
            crcTableComputed = true;
            crcTable = [];
            for (var n:uint = 0; n < 256; n++) {
                var crc:uint = n;
                for (var k:uint = 0; k < 8; k++) {
                    if (crc & 1) {
                        crc = uint(uint(0xedb88320) ^ 
                            uint(crc >>> 1));
                    } else {
                        crc = uint(crc >>> 1);
                    }
                }
                crcTable[n] = crc;
            }
        }
        var len:uint = 0;
        if (data != null) {
            len = data.length;
        }
        png.writeUnsignedInt(len);
        var p:uint = png.position;
        png.writeUnsignedInt(type);
        if ( data != null ) {
            png.writeBytes(data);
        }
        var e:uint = png.position;
        png.position = p;
        var c:uint = 0xffffffff;
        for (var i:int = 0; i < (e-p); i++) {
            c = uint(crcTable[
                (c ^ png.readUnsignedByte()) & 
                uint(0xff)] ^ uint(c >>> 8));
        }
        c = uint(c^uint(0xffffffff));
        png.position = e;
        png.writeUnsignedInt(c);
    }

    public static function getResizedImage(bmd:BitmapData,newWidth:int , newHeight:int):BitmapData
{
                        if(bmd == null)
                                return null;

                        var newBitMapData:BitmapData = new BitmapData( newWidth , newHeight,bmd.transparent);

                        var xFactor:Number = bmd.width / newWidth;
                        var yFactor:Number = bmd.height / newHeight;
                        for (var x:int = 0; x < newWidth; x++)
                        {
                            for (var y:int = 0; y < newHeight; y++)
                            {
                                newBitMapData.setPixel(x, y, getPixelBilinear(bmd,x * xFactor, y * yFactor));
                            }
                        }

                        return newBitMapData;
                }

//the two methods below from http://clevrlib.riaforge.org/

           /**
              Computes the value of a pixel that is not on a pixel boundary.

              @param theX The sub-pixel precision x-coordinate.
              @param theY The sub-pixel precision y-coordinate.
           */
                private static function getPixelBilinear(bmd:BitmapData,theX:Number, theY:Number): Number
                {

                        var x:int;
                        var y:int;
                        var x_ratio:Number;
                        var y_ratio:Number;
                        var y_opposite:Number;
                        var x_opposite:Number;
                        var a:int;
                        var be:int;
                        var c:int;
                        var d:int;
                        var red:int;
                        var green:int;
                        var blue:int;

                        x = Math.floor(theX);
                        y = Math.floor(theY);

                        if((x < 1) || (y < 1) || ((x + 2) >= bmd.width) || ((y + 2) >= bmd.height))
                                return bmd.getPixel(x, y);

                        x_ratio = theX - x;
                        y_ratio = theY - y;
                        x_opposite = 1 - x_ratio;
                        y_opposite = 1 - y_ratio;

                        a       = bmd.getPixel(x, y);
                        be      =bmd.getPixel(x + 1, y);
                        c       = bmd.getPixel(x, y + 1);
                        d       = bmd.getPixel(x + 1, y + 1);
                        red     = (r(a)  * x_opposite  + r(be)   * x_ratio) * y_opposite + (r(c) * x_opposite  + r(d) * x_ratio) * y_ratio;
                        green   = (g(a)  * x_opposite  + g(be)   * x_ratio) * y_opposite + (g(c) * x_opposite  + g(d) * x_ratio) * y_ratio;
                        blue    = (b(a)  * x_opposite  + b(be)   * x_ratio) * y_opposite + (b(c) * x_opposite  + b(d) * x_ratio) * y_ratio;
                                /*red = r(a);
                                green = g(a);
                                blue = b(a);*/

                        if(red < 0)
                                red = 0;
                        else if(red > 255)
                                red = 255;
                        if(green < 0)

                                green = 0;
                        else if(green > 255)
                                green = 255;
                        if(blue < 0)
                                blue = 0;
                        else if(blue > 255)
                                blue = 255;

                        return (red << 16) | (green << 8) | (blue << 0);
                }

                /**
                *       RGB convenience methods
                */

                private static function r(rgb:int):int {
                        return (rgb >> 16) & 0x0FF;
                }

                private static function g(rgb:int):int {
                        return (rgb >> 8) & 0x0FF;
                }

                private static function b(rgb:int):int {
                        return (rgb >> 0) & 0x0FF;
                }
}
}