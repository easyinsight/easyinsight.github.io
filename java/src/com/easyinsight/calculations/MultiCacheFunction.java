package com.easyinsight.calculations;

import com.easyinsight.analysis.AnalysisCalculation;
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.core.Value;
import com.easyinsight.database.Database;
import com.easyinsight.database.EIConnection;
import com.easyinsight.datafeeds.FeedDefinition;
import com.easyinsight.datafeeds.FeedStorage;
import com.easyinsight.logging.LogClass;
import com.easyinsight.storage.CacheDataTransform;
import com.easyinsight.storage.MultiCacheDataTransform;
import com.easyinsight.storage.MultiCacheInfo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: jamesboe
 * Date: 4/3/12
 * Time: 11:26 AM
 */
public class MultiCacheFunction extends Function {
    public Value evaluate() {
        EIConnection conn = Database.instance().getConnection();
        try {
            if (!(calculationMetadata instanceof DataSourceCalculationMetadata)) {
                return null;
            }
            DataSourceCalculationMetadata dataSourceCalculationMetadata = (DataSourceCalculationMetadata) calculationMetadata;
            FeedDefinition baseSource = dataSourceCalculationMetadata.getDataSource();
            Collection<MultiCacheInfo> infos = new ArrayList<MultiCacheInfo>();
            for (int i = 0; i < params.size(); i += 2) {

                String sourceCalculationFieldName = minusQuotes(1);
                AnalysisCalculation sourceCalculation = null;
                for (AnalysisItem field : baseSource.getFields()) {
                    if (sourceCalculationFieldName.equals(field.toDisplay())) {
                        sourceCalculation = (AnalysisCalculation) field;
                        break;
                    }
                }
                String targetFieldName = minusQuotes(2);
                AnalysisItem targetField = null;
                for (AnalysisItem field : baseSource.getFields()) {
                    if (targetFieldName.equals(field.toDisplay())) {
                        targetField = field;
                        break;
                    }
                }
                MultiCacheInfo multiCacheInfo = new MultiCacheInfo(sourceCalculation, targetField);
                infos.add(multiCacheInfo);
            }


            dataSourceCalculationMetadata.getTransforms().add(new MultiCacheDataTransform(infos, baseSource));
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        } catch (Exception e) {
            LogClass.error(e);
            throw new FunctionException(e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
    }

    public int getParameterCount() {
        return -1;
    }
}
