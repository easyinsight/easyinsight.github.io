package com.easyinsight.intention;

/**
 * User: jamesboe
 * Date: 9/24/11
 * Time: 12:43 PM
 */
public class NewHierarchyIntention extends Intention {

    public static final int HIERARCHY = 1;
    public static final int CUSTOMIZE_JOINS = 2;

    private int variant;

    public NewHierarchyIntention() {
    }

    public NewHierarchyIntention(int variant) {
        this.variant = variant;
    }

    public int getVariant() {
        return variant;
    }

    public void setVariant(int variant) {
        this.variant = variant;
    }
}
