package com.easyinsight.calculations;

import com.easyinsight.analysis.AggregationTypes;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Alan
 * Date: Jul 11, 2008
 * Time: 9:06:50 PM
 */
public class FunctionFactory {
    private static Map<String, Integer> aggregationMap;
    static {
        aggregationMap = new HashMap<String, Integer>();
        aggregationMap.put("castsum", AggregationTypes.SUM);
        aggregationMap.put("castaverage", AggregationTypes.AVERAGE);
        aggregationMap.put("castmax", AggregationTypes.MAX);
        aggregationMap.put("castmin", AggregationTypes.MIN);
        aggregationMap.put("castcount", AggregationTypes.COUNT);
        aggregationMap.put("castmedian", AggregationTypes.MEDIAN);
        aggregationMap.put("castvariance", AggregationTypes.VARIANCE);
    }

    public IFunction createFunction(String s) {
        if(aggregationMap.containsKey(s)) {
            return new CastFunction(aggregationMap.get(s));
        }
        if(s.equals("ln")) {
            return new NaturalLog(); 
        }
        else if(s.equals("nconcat")) {
            return new NConcat();
        }
        else if(s.equals("abs")) {
            return new AbsoluteValue();
        }
        else if(s.equals("min")) {
            return new Minimum();
        }
        else if(s.equals("max")) {
            return new Maximum();
        }
        else {
            return null;
        }
    }
}
