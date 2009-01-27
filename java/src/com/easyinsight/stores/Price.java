package com.easyinsight.stores;

/**
 * User: James Boe
 * Date: May 27, 2008
 * Time: 10:59:08 PM
 */
public class Price {
    private Long priceID;
    private double cost;

    public Long getPriceID() {
        return priceID;
    }

    public void setPriceID(Long priceID) {
        this.priceID = priceID;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
