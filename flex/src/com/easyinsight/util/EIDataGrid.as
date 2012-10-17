/**
 * Created with IntelliJ IDEA.
 * User: jamesboe
 * Date: 10/17/12
 * Time: 11:57 AM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.util {
import flash.display.Sprite;

import mx.collections.ArrayCollection;

import mx.controls.DataGrid;
import mx.controls.listClasses.ListRowInfo;

public class EIDataGrid extends DataGrid {

    private var _rowColorFunction:Function;

    public function EIDataGrid() {
        super();
    }

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
}
}
