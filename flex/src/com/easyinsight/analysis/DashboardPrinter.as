package com.easyinsight.analysis {
import com.adobe.images.JPGEncoder;
import com.easyinsight.dashboard.Dashboard;
import com.easyinsight.util.ProgressAlert;

import flash.display.Bitmap;

import flash.display.BitmapData;
import flash.display.DisplayObject;
import flash.display.Sprite;
import flash.geom.Matrix;
import flash.geom.Point;
import flash.geom.Rectangle;
import flash.net.URLRequest;
import flash.net.navigateToURL;
import flash.printing.PrintJob;
import flash.printing.PrintJobOptions;
import flash.printing.PrintJobOrientation;
import flash.utils.ByteArray;

import mx.collections.ArrayCollection;
import mx.core.UIComponent;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.RemoteObject;

public class DashboardPrinter {

    private var upload:RemoteObject;

    public function DashboardPrinter() {
        upload = new RemoteObject();
        upload.destination = "exportService";
        upload.exportDashboardToPDF.addEventListener(ResultEvent.RESULT, gotExcelID);
    }

    private function gotExcelID(event:ResultEvent):void {
        var url:URLRequest = new URLRequest("/app/pdf");
        navigateToURL(url, "_blank");
    }

    public function exportReportToPDF(dashboard:Dashboard, parent:UIComponent, coreView:DisplayObject):void {

        var printJob:PrintJob = new PrintJob();
        printJob.start();

        var pageWidth:int = Math.min(printJob.pageWidth, coreView.width);
        var pageHeight:int = printJob.pageHeight;



        var pageList:ArrayCollection = new ArrayCollection();


        var encoder:JPGEncoder = new JPGEncoder(95);

        var master:BitmapData = new BitmapData(coreView.width, coreView.height);
        master.draw(coreView);

        var scale:Number = coreView.width > pageWidth ? (pageWidth / coreView.width) : 1;

        var scaleHeight:int = pageHeight / scale;

        var pages:int = Math.ceil(coreView.height / scaleHeight);

        var options:PrintJobOptions = new PrintJobOptions(true);
        
        for (var i:int = 0; i < pages; i++) {
            var bdPage:BitmapData;
            var bytes:ByteArray;
            var result:BitmapData;
            if (scale < 1) {
                var copyWidth:int = pageWidth / scale;
                var copyHeight:int = pageHeight / scale;
                bdPage = new BitmapData(copyWidth, copyHeight);
                bdPage.copyPixels(master, new Rectangle(0, i * copyHeight, copyWidth, copyHeight), new Point(0, 0));
                result = resizeImage(bdPage, pageWidth, pageHeight);
            } else {
                bdPage = new BitmapData(pageWidth, pageHeight);
                bdPage.copyPixels(master, new Rectangle(0, i * pageHeight, pageWidth, pageHeight), new Point(0, 0));
                result = bdPage;
            }
            var bm:Bitmap = new Bitmap(result);
            var tmp:Sprite = new Sprite();
            tmp.addChild(bm);
            printJob.addPage(tmp, null, options);
        }


        printJob.send();

        //var bd:BitmapData = new BitmapData(coreView.width, coreView.height);

        /*var snapshot:ImageSnapshot = ImageSnapshot.captureImage(coreView, 0, new JPEGEncoder(85));
        var bytes:ByteArray = snapshot.data;*/
        /*ProgressAlert.alert(parent, "Generating the PDF...", null, upload.exportDashboardToPDF);
        upload.exportDashboardToPDF.send(dashboard, pageList);*/
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