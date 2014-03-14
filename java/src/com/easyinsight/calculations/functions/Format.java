package com.easyinsight.calculations.functions;

import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.calculations.Function;
import com.easyinsight.core.*;
import com.easyinsight.export.ExportMetadata;
import com.easyinsight.export.ExportService;
import com.easyinsight.logging.LogClass;
import com.easyinsight.security.SecurityUtil;

import java.util.Calendar;

/**
 * User: jamesboe
 * Date: 1/27/12
 * Time: 9:40 AM
 */
public class Format extends Function {
    public Value evaluate() {
        Value value = getParameter(0);
        AnalysisItem field = findDataSourceItem(0);
        try {
            ExportMetadata exportMetadata = ExportService.createExportMetadata(SecurityUtil.getAccountID(false), calculationMetadata.getConnection(), calculationMetadata.getInsightRequestMetadata());
            return new StringValue(ExportService.createValue(1, field, value, Calendar.getInstance(), exportMetadata.currencySymbol, exportMetadata.locale, true));
        } catch (Exception e) {
            LogClass.error(e);
            return new EmptyValue();
        }
    }

    public int getParameterCount() {
        return 1;
    }

    @Override
    public boolean onDemand() {
        return true;
    }
}
