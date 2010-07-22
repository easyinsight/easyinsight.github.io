package com.easyinsight.analysis;

import com.easyinsight.core.StringValue;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.dataset.DataSet;
import com.easyinsight.logging.LogClass;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * User: jamesboe
 * Date: Mar 26, 2010
 * Time: 3:44:11 PM
 */
public class ZipGeocodeCache {

    private Map<String, Point> pointMap = new WeakHashMap<String, Point>();

    public void blah(DataSet dataSet, AnalysisZipCode zip, AnalysisLongitude analysisLongitude, AnalysisLatitude analysisLatitude) {
        EIConnection conn = Database.instance().getConnection();
        try {
            PreparedStatement queryStmt = conn.prepareStatement("SELECT LONGITUDE, LATITUDE FROM ZIP_CODE_GEOCODE WHERE ZIP_CODE = ?");
            for (IRow row : dataSet.getRows()) {
                Value zipValue = row.getValue(zip.createAggregateKey());
                String zipCode = null;
                if (zipValue.type() == Value.STRING) {
                    zipCode = zipValue.toString().trim();
                    if (zipCode.length() > 5) {
                        zipCode = zipCode.substring(0, 5);
                    }
                }
                if (zipCode != null) {
                    Point point = pointMap.get(zipCode);
                    if (point == null) {
                        queryStmt.setString(1, zipCode);
                        ResultSet rs = queryStmt.executeQuery();
                        if (rs.next()) {
                            double longitude = rs.getDouble(1);
                            double latitude = rs.getDouble(2);

                            point = new Point(String.valueOf(longitude), String.valueOf(latitude));
                            pointMap.put(zipCode, point);
                        }
                    }
                    if (point != null) {
                        if (analysisLongitude != null) {
                            row.addValue(analysisLongitude.createAggregateKey(), new StringValue(point.getLongitude()));
                        }
                        if (analysisLatitude != null) {
                            row.addValue(analysisLatitude.createAggregateKey(), new StringValue(point.getLatitude()));
                        }
                    }
                }
            }
        } catch (SQLException e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        } finally {
            Database.closeConnection(conn);
        }
    }



    public void saveFile(byte[] data) throws IOException {
        EIConnection conn = Database.instance().getConnection();
        try {
            conn.setAutoCommit(false);
            PreparedStatement clearStmt = conn.prepareStatement("DELETE FROM zip_code_geocode");
            clearStmt.executeUpdate();
            PreparedStatement addRowStmt = conn.prepareStatement("INSERT INTO zip_code_geocode (zip_code, longitude, latitude) values (?, ?, ?)");
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook wb = new HSSFWorkbook(bais);
            HSSFSheet sheet = wb.getSheetAt(0);
            Iterator<org.apache.poi.ss.usermodel.Row> rit = sheet.rowIterator();
            rit.next();
            for (; rit.hasNext(); ) {
                org.apache.poi.ss.usermodel.Row row = rit.next();
                Cell zipCell = row.getCell(2);
                int zipCode = 0;
                if (zipCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                    zipCode = (int) zipCell.getNumericCellValue();
                } else if (zipCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                    String zipCodeString = zipCell.getStringCellValue();
                    try {
                        zipCode = Integer.parseInt(zipCodeString);
                    } catch (NumberFormatException e) {
                        zipCode = 0;
                    }
                }

                if (zipCode > 0) {
                    Cell latCell = row.getCell(6);
                    if (latCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                        double latitude = latCell.getNumericCellValue();
                        Cell longCell = row.getCell(5);
                        double longitude = longCell.getNumericCellValue();
                        NumberFormat numberFormat = new DecimalFormat("00000");
                        addRowStmt.setString(1, numberFormat.format(zipCode));
                        addRowStmt.setDouble(2, longitude);
                        addRowStmt.setDouble(3, latitude);
                        addRowStmt.execute();
                    }
                }
            }
            conn.commit();
        } catch (Exception e) {
            LogClass.error(e);
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            conn.setAutoCommit(true);
            Database.closeConnection(conn);
        }
    }

    public static void main(String[] args) throws IOException {
        Database.initialize();
        File file = new File("/postalcodes.csv.xls");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        new ZipGeocodeCache().saveFile(bytes);
    }
}
