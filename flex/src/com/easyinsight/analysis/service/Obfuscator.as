package com.easyinsight.analysis.service {
import com.easyinsight.analysis.AnalysisItem;
import com.easyinsight.analysis.AnalysisItemTypes;
import com.easyinsight.analysis.ListDataResults;
import com.easyinsight.analysis.StringValue;
import com.easyinsight.analysis.Value;

public class Obfuscator {
    public function Obfuscator() {
    }

    public function obfuscate(results:ListDataResults):void {
        var rows:Array = results.rows;
        var headers:Array = results.headers;
        var obfuscationMap:Object = new Object();
        for (var i:int = 0; i < rows.length; i++) {
            var row:Object = rows[i];
            var values:Array = row.values as Array;
            var endObject:Object = new Object();
            for (var j:int = 0; j < headers.length; j++) {
                var headerItem:AnalysisItem = headers[j];
                if (headerItem.getType() == AnalysisItemTypes.DIMENSION) {
                    var typeMap:Object = obfuscationMap[headerItem.qualifiedName()];
                    if (typeMap == null) {
                        typeMap = new Object();
                        obfuscationMap[headerItem.qualifiedName()] = typeMap;
                    }
                    var value:Value = values[j];
                    var string:String = String(value.getValue());
                    var obString:StringValue = typeMap[string];
                    if (obString == null) {
                        var str:String = headerItem.display + " " + int(Math.random() * 1000);
                        obString = new StringValue();
                        obString.value = str;
                        typeMap[string] = obString;
                    }
                    values[j] = obString;
                }
            }
        }
    }
}
}