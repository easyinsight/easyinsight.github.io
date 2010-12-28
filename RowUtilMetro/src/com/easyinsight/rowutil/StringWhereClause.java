package com.easyinsight.rowutil;

import com.easyinsight.rowutil.v3web.StringWhere;
import com.easyinsight.rowutil.v3web.Where;

/**
 * User: jamesboe
 * Date: 12/22/10
 * Time: 10:32 AM
 */
public class StringWhereClause extends WhereClause {

    private String value;

    public StringWhereClause(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    void addToWhere(Where where) {
        StringWhere stringWhere = new StringWhere();
        stringWhere.setKey(getKey());
        stringWhere.setValue(value);
        where.getStringWheres().add(stringWhere);
    }
}
