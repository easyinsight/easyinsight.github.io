package com.easyinsight.dbservice;

import com.easyinsight.dbservice.validated.*;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.rmi.RemoteException;

/**
 * User: James Boe
 * Date: Feb 4, 2009
 * Time: 1:27:12 PM
 */
public class QueryValidatedPublish {
    private QueryConfiguration queryConfiguration;
    private BasicAuthValidatedPublish service;

    public QueryValidatedPublish(QueryConfiguration queryConfiguration, BasicAuthValidatedPublish service) {
        this.queryConfiguration = queryConfiguration;
        this.service = service;
    }

    public void execute(DBConfiguration dbConfiguration) throws ClassNotFoundException, SQLException, RemoteException {
        String dataSourceKey = queryConfiguration.getDataSource();
        Connection conn = dbConfiguration.getConnection();
        try {
            Row[] rows = createRows(queryConfiguration.getQuery(), conn);
            if (queryConfiguration.getPublishMode() == QueryConfiguration.ADD) {
                service.addRows(dataSourceKey, rows);
            } else if (queryConfiguration.getPublishMode() == QueryConfiguration.REPLACE) {
                service.replaceRows(dataSourceKey, rows);
            } else if (queryConfiguration.getPublishMode() == QueryConfiguration.ADD_EXCLUSIVE_TODAY) {
                Calendar cal = Calendar.getInstance();
                DatePair datePair = new DatePair();
                datePair.setKey("Date");
                datePair.setValue(cal);
                DayWhere dayWhere = new DayWhere();
                dayWhere.setKey("Date");
                dayWhere.setDayOfYear(cal.get(Calendar.DAY_OF_YEAR));
                dayWhere.setYear(cal.get(Calendar.YEAR));
                for (Row row : rows) {
                    DatePair[] datePairs = row.getDatePairs();
                    if (datePairs == null) {
                        datePairs = new DatePair[] { datePair };
                    } else {
                        List<DatePair> datePairList = new ArrayList<DatePair>(Arrays.asList(datePairs));
                        datePairList.add(datePair);
                        datePairs = new DatePair[datePairList.size()];
                        datePairList.toArray(datePairs);
                    }
                    row.setDatePairs(datePairs);
                }
                Where where = new Where();
                where.setDayWheres(new DayWhere[] { dayWhere });
                service.updateRows(dataSourceKey, rows, where);
            }
        } finally {
            conn.close();
        }
    }

    private Row[] createRows(String query, Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(query);
        ResultSetMetaData rsMetadata = rs.getMetaData();
        int columns = rsMetadata.getColumnCount();
        List<Row> rows = new ArrayList<Row>();
        while (rs.next()) {
            Row row = new Row();
            List<StringPair> stringPairs = new ArrayList<StringPair>();
            List<NumberPair> numberPairs = new ArrayList<NumberPair>();
            List<DatePair> datePairs = new ArrayList<DatePair>();
            for (int i = 0; i < columns; i++) {
                String columnName = rsMetadata.getColumnName(i + 1);
                Object object = rs.getObject(i + 1);
                if (object instanceof Number) {
                    Number number = (Number) object;
                    NumberPair numberPair = new NumberPair();
                    numberPair.setKey(columnName);
                    numberPair.setValue(number.doubleValue());
                    numberPairs.add(numberPair);
                } else if (object instanceof String) {
                    String string = (String) object;
                    StringPair stringPair = new StringPair();
                    stringPair.setKey(columnName);
                    stringPair.setValue(string);
                    stringPairs.add(stringPair);
                } else if (object instanceof Date) {
                    Date date = (Date) object;
                    DatePair datePair = new DatePair();
                    datePair.setKey(columnName);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    datePair.setValue(calendar);
                    datePairs.add(datePair);
                } else if (object == null) {
                    StringPair stringPair = new StringPair();
                    stringPair.setKey(columnName);
                    stringPair.setValue("");
                    stringPairs.add(stringPair);
                }
            }
            StringPair[] stringPairArray = new StringPair[stringPairs.size()];
            stringPairs.toArray(stringPairArray);
            NumberPair[] numberPairArray = new NumberPair[numberPairs.size()];
            numberPairs.toArray(numberPairArray);
            DatePair[] datePairArray = new DatePair[datePairs.size()];
            datePairs.toArray(datePairArray);
            row.setDatePairs(datePairArray);
            row.setStringPairs(stringPairArray);
            row.setNumberPairs(numberPairArray);
            rows.add(row);
        }
        Row[] rowArray = new Row[rows.size()];
        rows.toArray(rowArray);
        return rowArray;
    }
}
