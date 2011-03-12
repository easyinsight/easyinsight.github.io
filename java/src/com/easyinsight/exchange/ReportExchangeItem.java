package com.easyinsight.exchange;

import com.easyinsight.core.EIDescriptor;

import java.util.Date;

/**
 * User: jamesboe
 * Date: Sep 14, 2009
 * Time: 7:18:12 PM
 */
public class ReportExchangeItem extends ExchangeItem {

    private EIDescriptor descriptor;

    public ReportExchangeItem() {
    }

    public ReportExchangeItem(String name, long id, int installs, Date dateAdded, String description, String author,
                              EIDescriptor descriptor) {
        super(name, id, installs, dateAdded, description, author);
        this.descriptor = descriptor;
    }

    public EIDescriptor getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(EIDescriptor descriptor) {
        this.descriptor = descriptor;
    }
}
