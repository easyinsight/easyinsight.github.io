package com.easyinsight.analysis {
import flash.display.Sprite;
import flash.events.MouseEvent;
import flash.utils.Dictionary;

import mx.collections.IHierarchicalData;
import mx.controls.AdvancedDataGrid;
import mx.controls.listClasses.ListRowInfo;
import mx.core.FlexSprite;
import mx.core.mx_internal;
import mx.styles.StyleManager;

use namespace mx_internal;
public class SummaryViewGrid extends AdvancedDataGrid {

    private var _rowColorFunction:Function;

    public function SummaryViewGrid() { super(); }

    override public function findString(str:String):Boolean {
        return false;
    }

    override public function set columns(value:Array):void {
        super.columns = value;
        itemRendererToFactoryMap = new Dictionary(false);
    }

    public function blah():int {
        return measureHeightOfItems(-1, dataProvider.length);
    }

    override protected function mouseMoveHandler(event:MouseEvent):void {
        try {
            super.mouseMoveHandler(event);
        } catch (e:Error) {
            // ignore
        }
    }

    /**
     * A user-defined function that will return the correct color of the
     * row. Usually based on the row data.
     *
     * expected function signature:
     * public function F(item:Object, defaultColor:uint):uint
     **/
    public function set rowColorFunction(f:Function):void
    {
        this._rowColorFunction = f;
    }

    public function get rowColorFunction():Function
    {
        return this._rowColorFunction;
    }


    private var displayWidth:Number; // I wish this was protected, or internal so I didn't have to recalculate it myself.
    override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
    {
        super.updateDisplayList(unscaledWidth, unscaledHeight);
        if (displayWidth != unscaledWidth - viewMetrics.right - viewMetrics.left)
        {
            displayWidth = unscaledWidth - viewMetrics.right - viewMetrics.left;
        }
        var trueHeight:int = 0;
        if (dataProvider && rowInfo) {
            for (var i:int = 0; i < dataProvider.length && i < rowInfo.length; i++) {
                var info:ListRowInfo = rowInfo[i];
                trueHeight += info.height;
            }
        }
        /*if (trueHeight != 0) {
            this.height = trueHeight;
        }*/
        //
    }
    
    public function recalculateHeight():int {
        var trueHeight:int = headerHeight;
        if (dataProvider && rowInfo) {
            for (var i:int = 0; i < dataProvider.length && i < rowInfo.length; i++) {
                var info:ListRowInfo = rowInfo[i];
                trueHeight += info.height;
            }
        }
        return trueHeight;
    }


    /**
     *  Draws a row background
     *  at the position and height specified using the
     *  color specified.  This implementation creates a Shape as a
     *  child of the input Sprite and fills it with the appropriate color.
     *  This method also uses the <code>backgroundAlpha</code> style property
     *  setting to determine the transparency of the background color.
     *
     *  @param s A Sprite that will contain a display object
     *  that contains the graphics for that row.
     *
     *  @param rowIndex The row's index in the set of displayed rows.  The
     *  header does not count, the top most visible row has a row index of 0.
     *  This is used to keep track of the objects used for drawing
     *  backgrounds so a particular row can re-use the same display object
     *  even though the index of the item that row is rendering has changed.
     *
     *  @param y The suggested y position for the background
     *
     *  @param height The suggested height for the indicator
     *
     *  @param color The suggested color for the indicator
     *
     *  @param dataIndex The index of the item for that row in the
     *  data provider.  This can be used to color the 10th item differently
     *  for example.
     */
    override protected function drawRowBackground(s:Sprite, rowIndex:int,
                                                  y:Number, height:Number, color:uint, dataIndex:int):void
    {
        //if (this.dataProvider is ArrayCollection) {
            if (this.rowColorFunction != null)
            {
                var item:Object = listItems[rowIndex][0].data;

                /*if (dataIndex < (this.dataProvider as ArrayCollection).length)
                {*/
                    //var item:Object = (this.dataProvider as ArrayCollection).getItemAt(dataIndex);
                    color = this.rowColorFunction.call(this, item, color);
                //}
            }
        //}

        super.drawRowBackground(s, rowIndex, y, height, color, dataIndex);
    }
    
    private var _summaryColor:Number = NaN;

    public function set summaryColor(value:uint):void {
        _summaryColor = value;
    }

    override protected function drawRowBackgrounds():void
    {
        var colors:Array;
        colors = getStyle("depthColors");

        if (!(_rootModel is IHierarchicalData) || !colors)
        {
            super.drawRowBackgrounds();
            return;
        }

        var rowBGs:Sprite = Sprite(listContent.getChildByName("rowBGs"));
        if (!rowBGs)
        {
            rowBGs = new FlexSprite();
            rowBGs.mouseEnabled = false;
            rowBGs.name = "rowBGs";
            listContent.addChildAt(rowBGs, 0);
        }

        var color:Object;
        var depthColors:Boolean = false;

        if (colors)
        {
            depthColors = true;
        }
        else
        {
            colors = getStyle("alternatingItemColors");
        }

        color = getStyle("backgroundColor");
        if (!colors || colors.length == 0)
            return;

        StyleManager.getColorNames(colors);

        var curRow:int = 0;

        var i:int = 0;
        var actualRow:int = verticalScrollPosition;
        var n:int = listItems.length;

        while (curRow < n)
        {
            if (depthColors)
            {
                try {
                    if (listItems[curRow][0])
                    {
                        var d:int = getItemDepth(listItems[curRow][0].data, curRow);
                        drawRowBackground(rowBGs, i++, rowInfo[curRow].y, rowInfo[curRow].height, colors[d - 1], curRow);
                    }
                    else
                    {
                        drawRowBackground(rowBGs, i++, rowInfo[curRow].y, rowInfo[curRow].height, uint(color), curRow);
                    }
                }
                catch (e:Error)
                {
                }
            }
            else
            {
                drawRowBackground(rowBGs, i++, rowInfo[curRow].y, rowInfo[curRow].height, colors[actualRow % colors.length], actualRow);
            }
            curRow++;
            actualRow++;
        }

        while (rowBGs.numChildren > i)
        {
            rowBGs.removeChildAt(rowBGs.numChildren - 1);
        }
    }
}
}