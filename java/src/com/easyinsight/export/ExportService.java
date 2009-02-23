package com.easyinsight.export;

import com.easyinsight.analysis.DataService;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.security.SecurityUtil;
import com.easyinsight.database.Database;
import com.easyinsight.logging.LogClass;
import com.easyinsight.core.Value;
import com.easyinsight.core.StringValue;
import com.easyinsight.core.NumericValue;
import com.easyinsight.core.DateValue;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.ListRow;
import com.easyinsight.analysis.CrossTabDataResults;
import com.easyinsight.analysis.*;

import org.apache.poi.hssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;
import java.util.HashMap;

/**
 * User: James Boe
 * Date: Jun 2, 2008
 * Time: 4:26:26 PM
 */
public class ExportService {

    public static final String CURRENCY_STYLE = "currency";
    public static final String GENERIC_STYLE = "generic";

    public long exportToExcel(WSAnalysisDefinition analysisDefinition) {
        long exportID;
        try {
            if (analysisDefinition instanceof WSListDefinition) {
                WSListDefinition listDefinition = (WSListDefinition) analysisDefinition;
                ListDataResults listDataResults = new DataService().list(listDefinition, new InsightRequestMetadata());
                HSSFWorkbook workbook = createWorkbookFromList(listDefinition, listDataResults);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                workbook.write(baos);
                byte[] bytes = baos.toByteArray();
                baos.close();
                exportID = saveBytes(bytes);
            } else {
                WSCrosstabDefinition crosstabDefinition = (WSCrosstabDefinition) analysisDefinition;
                CrossTabDataResults results = new DataService().pivot(crosstabDefinition, new InsightRequestMetadata());
                HSSFWorkbook workbook = createWorkbookFromCrosstab(results);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                workbook.write(baos);
                byte[] bytes = baos.toByteArray();
                baos.close();
                exportID = saveBytes(bytes);
            }
        } catch (Exception e) {
            LogClass.error(e);
            throw new RuntimeException(e);
        }
        return exportID;
    }

    private HSSFWorkbook createWorkbookFromCrosstab(CrossTabDataResults results) {
        throw new UnsupportedOperationException();
    }

    private HSSFWorkbook createWorkbookFromList(WSListDefinition listDefinition, ListDataResults listDataResults) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Map<String, HSSFCellStyle> styleMap = new HashMap<String, HSSFCellStyle>();
        HSSFCellStyle currencyStyle = workbook.createCellStyle();
        currencyStyle.setDataFormat(workbook.createDataFormat().getFormat("$##,##0.00"));
        styleMap.put(CURRENCY_STYLE, currencyStyle);
        HSSFCellStyle genericStyle = workbook.createCellStyle();
        styleMap.put(GENERIC_STYLE, genericStyle);
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Data");
        HSSFRow headerRow = sheet.createRow(0);
        Map<AnalysisItem, Short> positionMap = new HashMap<AnalysisItem, Short>();
        for (short i = 0; i < listDefinition.getColumns().size(); i++) {
            AnalysisItem analysisItem = listDefinition.getColumns().get(i);
            positionMap.put(analysisItem, i);
        }
        for (AnalysisItem analysisItem : listDataResults.getHeaders()) {
            short headerPosition = positionMap.get(analysisItem);
            sheet.setColumnWidth(headerPosition, (short) (analysisItem.getWidth() / 10 * 256));
            HSSFCell headerCell = headerRow.createCell(headerPosition);
            String displayName;
            if (analysisItem.getDisplayName() == null) {
                displayName = analysisItem.getKey().toDisplayName();
            } else {
                displayName = analysisItem.getDisplayName();
            }
            headerCell.setCellValue(new HSSFRichTextString(displayName));
        }
        int i = 1;
        for (ListRow listRow : listDataResults.getRows()) {
            HSSFRow row = sheet.createRow(i);
            Value[] values = listRow.getValues();
            short cellIndex = 0;
            for (Value value : values) {
                AnalysisItem analysisItem = listDataResults.getHeaders()[cellIndex];
                short translatedIndex = positionMap.get(analysisItem);
                HSSFCellStyle style = getStyle(styleMap, analysisItem);
                populateCell(row, translatedIndex, value, style);
                cellIndex++;
            }
            i++;
        }
        return workbook;
    }

    private long saveBytes(byte[] bytes) throws SQLException {
        long exportID;
        Connection conn = Database.instance().getConnection();
        try {
            PreparedStatement insertStmt = conn.prepareStatement("INSERT INTO EXCEL_EXPORT (excel_file, user_id) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            insertStmt.setBytes(1, bytes);
            insertStmt.setLong(2, SecurityUtil.getUserID());
            insertStmt.execute();
            exportID = Database.instance().getAutoGenKey(insertStmt);
        } finally {
            Database.instance().closeConnection(conn);
        }
        return exportID;
    }

    private HSSFCellStyle getStyle(Map<String, HSSFCellStyle> styleMap, AnalysisItem analysisItem) {
        HSSFCellStyle style;
        if (analysisItem.hasType(AnalysisItemTypes.MEASURE)) {
            FormattingConfiguration formattingConfiguration = analysisItem.getFormattingConfiguration();
            switch (formattingConfiguration.getFormattingType()) {
                case FormattingConfiguration.CURRENCY:
                    style = styleMap.get(CURRENCY_STYLE);
                    break;
                default:
                    style = styleMap.get(GENERIC_STYLE);
                    break;
            }
        } else {
            style = styleMap.get(GENERIC_STYLE);
        }
        return style;
    }

    private void populateCell(HSSFRow row, short cellIndex, Value value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(cellIndex);
        cell.setCellStyle(style);
        if (value.type() == Value.STRING) {
            StringValue stringValue = (StringValue) value;
            HSSFRichTextString richText = new HSSFRichTextString(stringValue.getValue());
            cell.setCellValue(richText);
        } else if (value.type() == Value.NUMBER) {
            NumericValue numericValue = (NumericValue) value;
            cell.setCellValue(numericValue.toDouble());
        } else if (value.type() == Value.DATE) {
            DateValue dateValue = (DateValue) value;
            cell.setCellValue(dateValue.getDate());
        }
    }
}
