package com.easyinsight.exchange;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Sep 14, 2009
 * Time: 7:18:12 PM
 */
public class ReportExchangeItem extends ExchangeItem {
    
    private byte[] image;
    private ExchangeData exchangeData;

    public ReportExchangeItem() {
    }

    public ReportExchangeItem(String name, long id, String attribution, double ratingAverage,
                              double ratingCount, Date dateAdded, String description, String author,
                              ExchangeData exchangeData) {
        super(name, id, attribution, ratingAverage, ratingCount, dateAdded, description, author);
        this.exchangeData = exchangeData;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public ExchangeData getExchangeData() {
        return exchangeData;
    }

    public void setExchangeData(ExchangeData exchangeData) {
        this.exchangeData = exchangeData;
    }    
}
