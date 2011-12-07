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
        } else if (s.equals("firstvalue")) {
            return new FirstValueFunction();
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
        } else if (s.equals("round")) {
            return new Round();
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
        } else if (s.equals("notnull")) {
            return new IfNotNull();
        } else if (s.equals("greaterthan")) {
            return new GreaterThan();
        } else if (s.equals("equalto")) {
            return new EqualTo();
        } else if (s.equals("replacefields")) {
            return new ReplaceFields();
        } else if (s.equals("copyfields")) {
            return new CopyFields();
        } else if (s.equals("replacecalculation")) {
            return new ReplaceCalculation();
        } else if (s.equals("equals")) {
            return new IsOnly();
        } else if (s.equals("addfield")) {
            return new AddReportField();
        } else if (s.equals("removefield")) {
            return new RemoveField();
        } else if (s.equals("addfieldtofilter")) {
            return new AddFieldToFilter();
        } else if (s.equals("removefieldfromfilter")) {
            return new RemoveFieldFromFilter();
        } else if (s.equals("removedashboardelement")) {
            return new RemoveDashboardElement();
        } else if (s.equals("tags")) {
            return new TagsFunction();
        } else if (s.equals("firsttag")) {
            return new FirstTagFunction();
        } else if (s.equals("originaltags")) {
            return new OriginalTags();
        } else if (s.equals("colortext")) {
            return new ColorText();
        } else if (s.equals("colortextall")) {
            return new ColorTextAll();
        } else if (s.equals("invalidatedata")) {
            return new InvalidateData();
        } else if (s.equals("aggregatefield")) {
            return new AggregateField();
        } else if (s.equals("notin")) {
            return new NotIn();
        } else if (s.equals("persona")) {
            return new PersonaEquals();
        } else if (s.equals("dateformat")) {
            return new EIDateFormatFunction();
        } else {
            return null;
        }
    }
}
