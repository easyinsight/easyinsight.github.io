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
        if (aggregationMap.containsKey(s)) {
            return new CastFunction(aggregationMap.get(s), s);
        }
        if (s.equals("ln")) {
            return new NaturalLog();
        } else if (s.equals("now")) {
            return new Now();
        } else if (s.equals("sin")) {
            return new Sine();
        } else if (s.equals("sqrt")) {
            return new SquareRoot();
        } else if (s.equals("ceil")) {
            return new Ceiling();
        } else if (s.equals("floor")) {
            return new Floor();
        } else if (s.equals("cos")) {
            return new Cosine();
        } else if (s.equals("tan")) {
            return new Tangent();
        } else if (s.equals("asin")) {
            return new ArcSine();
        } else if (s.equals("acos")) {
            return new ArcCosine();
        } else if (s.equals("atan")) {
            return new ArcTan();
        } else if (s.equals("nconcat")) {
            return new NConcat();
        } else if (s.equals("bracketvalue")) {
            return new BracketValueFunction();
        } else if (s.equals("namedbracketvalue")) {
            return new NamedBracketValueFunction();
        } else if (s.equals("upper")) {
            return new UpperCaseFunction();
        } else if (s.equals("lower")) {
            return new LowerCaseFunction();
        } else if (s.equals("abs")) {
            return new AbsoluteValue();
        } else if (s.equals("min")) {
            return new Minimum();
        } else if (s.equals("max")) {
            return new Maximum();
        } else if (s.equals("substring")) {
            return new SubstringFunction();
        } else if (s.equals("concat")) {
            return new Concat();
        } else if (s.equals("parseint")) {
            return new ParseIntFunction();
        } else if (s.equals("namedbracketdate")) {
            return new NamedBracketDateFunction();
        } else if (s.equals("replace")) {
            return new Replace();
        } else if (s.equals("blah")) {
            return new BlahFunction();
        } else {
            return null;
        }
    }
}
