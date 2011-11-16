package com.easyinsight.userupload;

import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.io.IOException;
import java.util.regex.Pattern;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * User: James Boe
 * Date: Jun 30, 2008
 * Time: 9:04:19 PM
 */
public class UploadFormatTester {

    private static String patterns[] = new String [] { ",", "|", "\t"};
    private static String escapedPatterns[] = new String [] { ",", "\\|", "\t"};

    public UploadFormat determineFormat(byte[] data) throws InvalidFormatException {
        UploadFormat uploadFormat;
        uploadFormat = isExcel(data);
        if (uploadFormat == null) {
            uploadFormat = isCsv(data);
            if(uploadFormat == null) {
                uploadFormat = isFlatFile(data);
            }
        }
        return uploadFormat;
    }

    private UploadFormat isExcel(byte[] data) throws InvalidFormatException {
        UploadFormat uploadFormat;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook wb = new HSSFWorkbook(bais);
            HSSFSheet sheet = wb.getSheetAt(0);
            sheet.getTopRow();
            uploadFormat = new ExcelUploadFormat();
        } catch (OldExcelFormatException oefe) {
            throw new InvalidFormatException("It looks like you tried to upload an Excel format earlier than 1997. Easy Insight does not support these older formats.");
        } catch (OfficeXmlFileException oxfe) {
            try {
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                XSSFWorkbook xb = new XSSFWorkbook(bais);
                xb.getSheetAt(0).getRow(0).getLastCellNum();
                uploadFormat = new XSSFExcelUploadFormat();
            } catch (Exception e) {
                uploadFormat = null;
            }
        } catch (Exception e) {
            uploadFormat = null;
        }
        return uploadFormat;
    }

    private UploadFormat isCsv(byte[] data) {
        UploadFormat uploadFormat;
        try {
            uploadFormat = new CsvFileUploadFormat();
            if (uploadFormat.test(data)) {
                return uploadFormat;
            } else {
                uploadFormat = null;
            }
        } catch(Exception e) {
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
                if (headerResult.length > 1 && headerResult.length >= firstDataLineResult.length) {
                    delimiter = patterns[i];
                    break;
                }
            }
        }
        UploadFormat uploadFormat = null;
        if (delimiter != null && !delimiter.equals(",")) {
            uploadFormat = new FlatFileUploadFormat(delimiter, "");
        }
        else if(delimiter != null && delimiter.equals(",")) {
            uploadFormat = new CsvFileUploadFormat();
        }
        return uploadFormat;
    }
}
