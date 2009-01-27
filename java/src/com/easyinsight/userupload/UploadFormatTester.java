package com.easyinsight.userupload;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;

/**
 * User: James Boe
 * Date: Jun 30, 2008
 * Time: 9:04:19 PM
 */
public class UploadFormatTester {

    private static String patterns[] = new String [] { ",", "|", " ", "\t"};
    private static String escapedPatterns[] = new String [] { ",", "\\|", " ", "\t"};

    public UploadFormat determineFormat(byte[] data) {
        UploadFormat uploadFormat = isFlatFile(data);
        if (uploadFormat == null) {
            uploadFormat = isExcel(data);
        }
        return uploadFormat;
    }

    private UploadFormat isExcel(byte[] data) {
        UploadFormat uploadFormat;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook wb = new HSSFWorkbook(bais);
            HSSFSheet sheet = wb.getSheetAt(0);
            sheet.getTopRow();
            uploadFormat = new ExcelUploadFormat();
        } catch (Exception e) {
            uploadFormat = null;
        }
        return uploadFormat;
    }

    private UploadFormat isFlatFile(byte[] data) {
        String delimiter = null;
        String dataString = new String(data);
        Pattern pattern = Pattern.compile("\r\n");

        String[] lines = pattern.split(dataString);
        if (lines.length == 1) {
            lines = dataString.split("\n");
            if (lines.length == 1) {
                lines = dataString.split("\r");
            }
        }
        if (lines.length > 1) {
            String headerLine = lines[0];
            String firstDataLine = lines[1];
            for (int i = 0; i < escapedPatterns.length; i++) {
                String delimiterPattern = escapedPatterns[i];
                String [] headerResult = headerLine.split(delimiterPattern);
                String [] firstDataLineResult = firstDataLine.split(delimiterPattern);
                if (headerResult.length > 1 && headerResult.length == firstDataLineResult.length) {
                    delimiter = patterns[i];
                    break;
                }
            }
        }
        UploadFormat uploadFormat = null;
        if (delimiter != null) {
            uploadFormat = new FlatFileUploadFormat(delimiter, "");
        }
        return uploadFormat;
    }
}
