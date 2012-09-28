package com.easyinsight.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* User: jamesboe
* Date: 9/28/12
* Time: 10:48 AM
*/
public class Update {
    private List<WhereClause> whereClauses;

    public List<WhereClause> getWhereClauses() {
        return whereClauses;
    }

    public void setWhereClauses(WhereClause... whereClauses) {
        this.whereClauses = Arrays.asList(whereClauses);
    }

    private List<DataRow> rows = new ArrayList<DataRow>();

    public DataRow newRow() {
        DataRow dataRow = new DataRow();
        rows.add(dataRow);
        return dataRow;
    }

    public String toXML() {
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("<update>");
        updateBuilder.append("<rows>");
        for (DataRow row : rows) {
            updateBuilder.append(row.toXML());
        }
        updateBuilder.append("</rows>");
        updateBuilder.append("<wheres>");
        for (WhereClause whereClause : whereClauses) {
            updateBuilder.append(whereClause.toXML());
        }
        updateBuilder.append("</wheres>");
        updateBuilder.append("</update>");
        return updateBuilder.toString();
    }
}
