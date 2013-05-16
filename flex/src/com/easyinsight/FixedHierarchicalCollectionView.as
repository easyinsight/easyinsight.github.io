/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 5/16/13
 * Time: 2:26 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight {
import mx.core.mx_internal;

use namespace mx_internal;
import mx.collections.HierarchicalCollectionView;
import mx.collections.IHierarchicalData;
import mx.collections.IViewCursor;

public class FixedHierarchicalCollectionView extends HierarchicalCollectionView
{
    public function FixedHierarchicalCollectionView(hierarchicalData:IHierarchicalData=null, argOpenNodes:Object=null)
    {
        super(hierarchicalData, argOpenNodes);
    }


    override public function createCursor() : IViewCursor
    {
        return new FixedHierarchicalCollectionViewCursor(this, treeData, this.source);
    }
}
}
