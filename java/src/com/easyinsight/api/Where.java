package com.easyinsight.api;

import com.easyinsight.api.NumberWhere;

/**
 * User: James Boe
 * Date: Jan 15, 2009
 * Time: 2:41:29 PM
 */
public class Where {
    private StringWhere[] stringWheres;
    private NumberWhere[] numberWheres;
    private DateWhere[] dateWheres;

    public StringWhere[] getStringWheres() {
        return stringWheres;
    }

    public void setStringWheres(StringWhere[] stringWheres) {
        this.stringWheres = stringWheres;
    }

    public NumberWhere[] getNumberWheres() {
        return numberWheres;
    }

    public void setNumberWheres(NumberWhere[] numberWheres) {
        this.numberWheres = numberWheres;
    }

    public DateWhere[] getDateWheres() {
        return dateWheres;
    }

    public void setDateWheres(DateWhere[] dateWheres) {
        this.dateWheres = dateWheres;
    }
}
