package com.easyinsight.analysis {
import flash.display.Sprite;

import flash.events.MouseEvent;
import flash.utils.Dictionary;

import mx.collections.ArrayCollection;
import mx.collections.CursorBookmark;
import mx.collections.errors.ItemPendingError;
import mx.controls.AdvancedDataGrid;
import mx.controls.Alert;
import mx.controls.advancedDataGridClasses.AdvancedDataGridColumn;
import mx.controls.listClasses.IListItemRenderer;
import mx.controls.listClasses.ListRowInfo;
import mx.core.mx_internal;

use namespace mx_internal;

public class ListViewGrid extends AdvancedDataGrid {

    private var _rowColorFunction:Function;

    public function ListViewGrid() { super(); }

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
        if (this.dataProvider is ArrayCollection) {
            if (this.rowColorFunction != null)
            {
                if (dataIndex < (this.dataProvider as ArrayCollection).length)
                {
                    var item:Object = (this.dataProvider as ArrayCollection).getItemAt(dataIndex);
                    color = this.rowColorFunction.call(this, item, color);
                }
            }
        }

        super.drawRowBackground(s, rowIndex, y, height, color, dataIndex);
    }


    mx_internal override function measureHeightOfItemsUptoMaxHeight(index:int = -1, count:int = 0, maxHeight:Number = -1):Number
    {
        if (!columns.length)
            return rowHeight * count;

        var h:Number = 0;

        var item:IListItemRenderer;
        var c:AdvancedDataGridColumn;
        var ch:Number = 0;
        var n:int;
        var j:int;

        var paddingTop:Number = getStyle("paddingTop");
        var paddingBottom:Number = getStyle("paddingBottom");

        if (!measuringObjects)
            measuringObjects = new Dictionary(false);

        var lockedCount:int = lockedRowCount;

        if (headerVisible && count > 0 && index == -1)
        {
            h = calculateHeaderHeight();

            if (maxHeight != -1 && h > maxHeight)
            {
                setRowCount(0);
                return 0;
            }

            // trace(this + " header preferredHeight = " + h);
        }

        var bookmark:CursorBookmark = (iterator) ? iterator.bookmark : null;

        var bMore:Boolean = iterator != null;
        if (index != -1 && iterator)
        {
            try
            {
                iterator.seek(CursorBookmark.FIRST, index);
            }
            catch (e:ItemPendingError)
            {
                bMore = false;
            }
        }

        if (lockedCount > 0 && collectionIterator)
        {
            try
            {
                collectionIterator.seek(CursorBookmark.FIRST,0);
            }
            catch (e:ItemPendingError)
            {
                bMore = false;
            }
        }

        for (var i:int = 0; i < count; i++)
        {
            var data:Object;
            if (bMore)
            {
                data = (lockedCount > 0) ? collectionIterator.current : iterator.current;
                ch = 0;
                n = columns.length;
                for (j = 0; j < n; j++)
                {
                    c = columns[j];

                    if (!c.visible)
                        continue;

                    item = getMeasuringRenderer(c, false,data);
                    setupRendererFromData(c, item, data);
                    ch = Math.max(ch, variableRowHeight ? Math.ceil(item.getExplicitOrMeasuredHeight()) + paddingBottom + paddingTop : rowHeight);
                }
            }

            if (maxHeight != -1 && (h + ch > maxHeight || !bMore))
            {
                try
                {
                    if (iterator)
                        iterator.seek(bookmark, 0);
                }
                catch (e:ItemPendingError)
                {
                    // we don't recover here since we'd only get here if the first seek failed.
                }
                count = i;
                setRowCount(count);
                return h;
            }

            h += ch;
            if (iterator)
            {
                try
                {
                    bMore = iterator.moveNext();
                    if (lockedCount > 0)
                    {
                        collectionIterator.moveNext();
                        lockedCount--;
                    }
                }
                catch (e:ItemPendingError)
                {
                    // if we run out of data, assume all remaining rows are the size of the previous row
                    bMore = false;
                }
            }
        }

        if (iterator)
        {
            try
            {
                iterator.seek(bookmark, 0);
            }
            catch (e:ItemPendingError)
            {
                // we don't recover here since we'd only get here if the first seek failed.
            }
        }

        // trace("calcheight = " + h);
        return h;
    }
}
}