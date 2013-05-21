/**
 * Created with IntelliJ IDEA.
 * User: Alan
 * Date: 5/16/13
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight {
import mx.collections.HierarchicalCollectionView;
import mx.collections.HierarchicalCollectionViewCursor;
import mx.collections.ICollectionView;
import mx.collections.IHierarchicalData;
import mx.collections.errors.CursorError;

public class FixedHierarchicalCollectionViewCursor extends HierarchicalCollectionViewCursor {
    public function FixedHierarchicalCollectionViewCursor(collection:HierarchicalCollectionView, model:ICollectionView, hierarchicalData:IHierarchicalData) {
        super(collection, model, hierarchicalData);
    }


    override public function get current():Object {
        // original HierarchicalCollectionViewCursor class fails to catch the "bookmark no
        // longer valid" Error, which is thrown as a CollectionViewError instance in ListCollectionView,
        // but transformed to a CursorError within the same class
        try {
            var result:Object = super.current;
        }
        catch (e:CursorError) {
            result = null;
        }

        // done
        return result;
    }

}
}
