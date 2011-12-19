/**
 * Created by IntelliJ IDEA.
 * User: jamesboe
 * Date: 12/16/11
 * Time: 9:35 PM
 * To change this template use File | Settings | File Templates.
 */
package com.easyinsight.reportviews {
import mx.collections.ArrayCollection;

public class TreeLevel {
    
    public var label:String;
    public var children:ArrayCollection;
    
    public function TreeLevel(label:String,  children:ArrayCollection) {
        this.label = label;
        this.children = children;
    }
}
}
