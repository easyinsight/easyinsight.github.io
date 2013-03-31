/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 2/13/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis {
import flash.display.BitmapData;
import flash.display.DisplayObject;
import flash.geom.Matrix;
import flash.geom.Point;
import flash.geom.Rectangle;
import flash.utils.ByteArray;

import mx.collections.ArrayCollection;

import mx.core.UIComponent;

import mx.graphics.codec.PNGEncoder;

public class MultiPagePDFCreator {
    public function MultiPagePDFCreator() {
    }

    private static const PDF_X:int = 1100;
    private static const PDF_Y:int = 700;

    public static function exportReportToPDF(coreView:DisplayObject, landscape:Boolean, headerObj:UIComponent = null):ArrayCollection {

        var pageWidth:int;
        var pageHeight:int;

        if (landscape) {
            pageWidth = Math.min(PDF_X, coreView.width);
            pageHeight = PDF_Y;
        } else {
            pageWidth = Math.min(PDF_Y, coreView.width);
            pageHeight = PDF_X;
        }


        var pageList:ArrayCollection = new ArrayCollection();


        var encoder:PNGEncoder = new PNGEncoder();

        var height:int;
        if (headerObj == null) {
            height = coreView.height;
        } else {
            height = coreView.height + headerObj.height + 10;
        }
        var master:BitmapData = new BitmapData(coreView.width, height);
        var reportBitmap:BitmapData = new BitmapData(coreView.width, coreView.height);
        reportBitmap.draw(coreView);
        if (headerObj != null) {
            var headerBitmap:BitmapData = new BitmapData(coreView.width, headerObj.height);
            headerBitmap.draw(headerObj);
            master.copyPixels(headerBitmap, new Rectangle(0, 0, coreView.width, headerObj.height), new Point(0, 0));
            master.copyPixels(reportBitmap, new Rectangle(0, 0, coreView.width, coreView.height), new Point(0, headerObj.height + 10));
        } else {
            master.copyPixels(reportBitmap, new Rectangle(0, 0, coreView.width, coreView.height), new Point(0, 0));
        }

        //var scale:Number = coreView.width > pageWidth ? (pageWidth / coreView.width) : 1;

        //var scaleHeight:int = pageHeight / scale;

        var pages:int = Math.ceil(height / pageHeight);

        for (var i:int = 0; i < pages; i++) {
            var bdPage:BitmapData;
            var bytes:ByteArray;
            /*if (scale < 1) {
                var copyWidth:int = pageWidth / scale;
                var copyHeight:int = pageHeight / scale;
                bdPage = new BitmapData(copyWidth, copyHeight + 10);
                bdPage.copyPixels(master, new Rectangle(0, i * copyHeight, copyWidth, copyHeight), new Point(0, 0));
                //var data:BitmapData = resizeImage(bdPage, pageWidth, pageHeight);
                bytes = encoder.encode(bdPage);
            } else {*/
                bdPage = new BitmapData(coreView.width, pageHeight + 10);
                bdPage.copyPixels(master, new Rectangle(0, i * pageHeight, coreView.width, pageHeight), new Point(0, 0));
                bytes = encoder.encode(bdPage);
            //}

            var page:Page = new Page();
            page.bytes = bytes;
            page.width = coreView.width;
            page.height = pageHeight;
            pageList.addItem(page);
        }
        return pageList;
    }

    private static const IDEAL_RESIZE_PERCENT:Number = .5;

    public static function resizeImage(source:BitmapData, width:uint, height:uint, resizeStyle:String = "constrainProportions"):BitmapData
    {
        var bitmapData:BitmapData;
        var crop:Boolean = false;
        var fill:Boolean = false;
        var constrain:Boolean = false;
        switch (resizeStyle) {
            case ResizeStyle.CROP: // these are supposed to not have break; statements
                crop = true;
            case ResizeStyle.CENTER:
                fill = true;
            case ResizeStyle.CONSTRAIN_PROPORTIONS:
                constrain = true;
                break;
            case ResizeStyle.STRETCH:
                fill = true;
                break;
            default:
                throw new ArgumentError("Invalid resizeStyle provided. Use options available on the ImageResizeStyle lookup class");
        }

        // Find the scale to reach the final size
        var scaleX:Number = width/source.width;
        var scaleY:Number = height/source.height;

        if (width == 0 && height == 0) {
            scaleX = scaleY = 1;
            width = source.width;
            height = source.height;
        } else if (width == 0) {
            scaleX = scaleY;
            width = scaleX * source.width;
        } else if (height == 0) {
            scaleY = scaleX;
            height = scaleY * source.height;
        }

        if (crop) {
            if (scaleX < scaleY) scaleX = scaleY;
            else scaleY = scaleX;
        } else if (constrain) {
            if (scaleX > scaleY) scaleX = scaleY;
            else scaleY = scaleX;
        }

        var originalWidth:uint = source.width;
        var originalHeight:uint = source.height;
        var originalX:int = 0;
        var originalY:int = 0;
        var finalWidth:uint = Math.round(source.width*scaleX);
        var finalHeight:uint = Math.round(source.height*scaleY);

        if (fill) {
            originalWidth = Math.round(width/scaleX);
            originalHeight = Math.round(height/scaleY);
            originalX = Math.round((originalWidth - source.width)/2);
            originalY = Math.round((originalHeight - source.height)/2);
            finalWidth = width;
            finalHeight = height;
        }

        if (scaleX >= 1 && scaleY >= 1) {
            try {
                bitmapData = new BitmapData(finalWidth, finalHeight, true, 0);
            } catch (error:ArgumentError) {
                error.message += " Invalid width and height: " + finalWidth + "x" + finalHeight + ".";
                throw error;
            }
            bitmapData.draw(source, new Matrix(scaleX, 0, 0, scaleY, originalX*scaleX, originalY*scaleY), null, null, null, true);
            return bitmapData;
        }

        // scale it by the IDEAL for best quality
        var nextScaleX:Number = scaleX;
        var nextScaleY:Number = scaleY;
        while (nextScaleX < 1) nextScaleX /= IDEAL_RESIZE_PERCENT;
        while (nextScaleY < 1) nextScaleY /= IDEAL_RESIZE_PERCENT;

        if (scaleX < IDEAL_RESIZE_PERCENT) nextScaleX *= IDEAL_RESIZE_PERCENT;
        if (scaleY < IDEAL_RESIZE_PERCENT) nextScaleY *= IDEAL_RESIZE_PERCENT;

        bitmapData = new BitmapData(Math.round(originalWidth*nextScaleX), Math.round(originalHeight*nextScaleY), true, 0);
        bitmapData.draw(source, new Matrix(nextScaleX, 0, 0, nextScaleY, originalX*nextScaleX, originalY*nextScaleY), null, null, null, true);

        nextScaleX *= IDEAL_RESIZE_PERCENT;
        nextScaleY *= IDEAL_RESIZE_PERCENT;

        while (nextScaleX >= scaleX || nextScaleY >= scaleY) {
            var actualScaleX:Number = nextScaleX >= scaleX ? IDEAL_RESIZE_PERCENT : 1;
            var actualScaleY:Number = nextScaleY >= scaleY ? IDEAL_RESIZE_PERCENT : 1;
            var temp:BitmapData = new BitmapData(Math.round(bitmapData.width*actualScaleX), Math.round(bitmapData.height*actualScaleY), true, 0);
            temp.draw(bitmapData, new Matrix(actualScaleX, 0, 0, actualScaleY), null, null, null, true);
            bitmapData.dispose();
            nextScaleX *= IDEAL_RESIZE_PERCENT;
            nextScaleY *= IDEAL_RESIZE_PERCENT;
            bitmapData = temp;
        }

        return bitmapData;
    }
}
}
