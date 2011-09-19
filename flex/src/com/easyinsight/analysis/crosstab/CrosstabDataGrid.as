/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 9/13/11
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.analysis.crosstab {
import flash.display.DisplayObject;
import flash.display.GradientType;
import flash.display.Graphics;
import flash.display.Shape;
import flash.display.Sprite;
import flash.geom.Matrix;

import mx.controls.AdvancedDataGrid;
import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
import mx.core.FlexShape;
import mx.core.IFlexDisplayObject;
import mx.core.UIComponent;
import mx.styles.ISimpleStyleClient;
import mx.styles.StyleManager;

public class CrosstabDataGrid extends AdvancedDataGrid {

    private var _headerRows:int = 1;

    public function CrosstabDataGrid() {
    }

    public function set headerRows(value:int):void {
        _headerRows = value;
    }

    /*override protected function drawHorizontalLine(s:Sprite, rowIndex:int, color:uint, y:Number):void
    {
        var g:Graphics = s.graphics;

        var offset:Number = 0;

        for ( var i:int = 0; i < _headerRows; i++)
            offset += columns[i].width;

        if (lockedRowCount > 0 && rowIndex == lockedRowCount-1)
            g.lineStyle(1, 0);
        else
            g.lineStyle(1, color);

        g.moveTo(offset, y);
        g.lineTo(unscaledWidth - viewMetrics.left - viewMetrics.right, y);
    }*/

    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
        super.updateDisplayList(unscaledWidth, unscaledHeight);

        if (!columns || columns.length == 0) {
            return;
        }

        var headerBG:UIComponent =
            UIComponent(listContent.getChildByName("rowHeaderBG"));

        if (!headerBG)
        {
            headerBG = new UIComponent();
            headerBG.name = "rowHeaderBG";

            listContent.addChildAt(DisplayObject(headerBG), listContent.getChildIndex(selectionLayer));

            var headerBGSkinClass:Class = getStyle("headerBackgroundSkin");
            var headerBGSkin:IFlexDisplayObject = new headerBGSkinClass();

            if (headerBGSkin is ISimpleStyleClient)
                ISimpleStyleClient(headerBGSkin).styleName = this;
            headerBG.addChild(DisplayObject(headerBGSkin));
        }
        drawRowHeaderBackground(headerBG);
    }

    protected function drawRowHeaderBackground(headerBG:UIComponent):void
    {
        var tot:Number = unscaledHeight - viewMetrics.top - viewMetrics.bottom;
        var hh:Number = 0;
        var hhh:Number = rowHeight * 2;
        /*if(headerVisible)

        {*/
            /*hh = headerRowInfo.length ? headerRowInfo[0].height : headerHeight;
            tot -= hh;*/
        //}

        var g:Graphics = headerBG.graphics;
        g.clear();
        var colors:Array /* of int */= getStyle("headerColors");
        StyleManager.getColorNames(colors);

        colors = [ colors[0], colors[0], colors[1] ];
        var ratios:Array /* of int */ = [ 0, 60, 255 ];
        var alphas:Array  /* of Number */= [ 1.0, 1.0, 1.0 ];

        var ww:Number = 0;
        for ( var i:int = 0; i < _headerRows; i++)
            ww += columns[i].width;

        g.beginFill(0x333333, 1);
        g.lineStyle(0, 0x000000, 0);

        /*g.moveTo(0, 0.5);
        g.lineTo(0, tot);
        g.lineTo(ww - 0.5, tot);
        g.lineStyle(0, getStyle("borderColor"), 100);
        g.lineTo(ww-0.5, 0.5);*/
        g.moveTo(0, 0);
        g.lineTo(0, tot);
        g.lineTo(ww, tot);
        g.lineStyle(0, getStyle("borderColor"), 100);
        g.lineTo(ww, 0);

        g.endFill();

        var rowHeaderWidth:int = 0;
        for (var j:int = _headerRows; j < columns.length; j++) {
            var col:AdvancedDataGridColumn = columns[j];
            rowHeaderWidth += col.width;
        }
        g.beginFill(0x333333, 1);
        g.drawRect(ww, 0, rowHeaderWidth, hhh);

        g.endFill();

        var summaryColumnWidth:int = columns[columns.length - 1].width;
        g.beginFill(0x555555, 1);
        g.drawRect(rowHeaderWidth + ww - summaryColumnWidth, hhh, summaryColumnWidth, tot - hhh);

        g.endFill();

        g.beginFill(0x555555, 1);
        g.drawRect(ww, tot - rowHeight, rowHeaderWidth, rowHeight);
        g.endFill();
    }

    /*override protected function drawVerticalLine(s:Sprite, colIndex:int, color:uint, x:Number):void
    {
        //draw our vertical lines
        var g:Graphics = s.graphics;
        if (lockedColumnCount > 0 && colIndex == lockedColumnCount - 1)
            g.lineStyle(1, color, 100);
        else
            super.drawVerticalLine(s, colIndex, color, x);
    }*/

    private function getRowHeadersWidth():Number
    {
        var offset:Number = 0;//getAdjustedXPos(0)* (-1);

        for ( var i:int = 0; i < _headerRows; i++)
            offset += columns[i].width;

        return offset;
    }

    override protected function drawRowBackground(s:Sprite, rowIndex:int,
                                         y:Number, height:Number, color:uint, dataIndex:int):void
    {
        var background:Shape;
        if (rowIndex < s.numChildren)
        {
            background = Shape(s.getChildAt(rowIndex));
        }
        else
        {
            background = new FlexShape();
            background.name = "background";
            s.addChild(background);
        }

        background.y = y;
        background.x = getRowHeadersWidth();

        // Height is usually as tall is the items in the row, but not if
        // it would extend below the bottom of listContent
        var height:Number = Math.min(height,
                                     listContent.height -
                                     y);

        var g:Graphics = background.graphics;
        g.clear();
        g.beginFill(color, getStyle("backgroundAlpha"));
        g.drawRect(0, 0, unscaledWidth - viewMetrics.left - viewMetrics.right, height);
        g.endFill();
    }
}
}
