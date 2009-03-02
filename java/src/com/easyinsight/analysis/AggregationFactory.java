package com.easyinsight.analysis;

/**
 * User: James Boe
 * Date: Oct 17, 2008
 * Time: 10:42:22 AM
 */
public class AggregationFactory {

    private AnalysisMeasure analysisMeasure;
    private IAggregationState aggregationState;

    public AggregationFactory(AnalysisMeasure analysisMeasure) {
        this.analysisMeasure = analysisMeasure;
        switch (analysisMeasure.getAggregation()) {
            case AggregationTypes.NORMALS:
                aggregationState = new NormalsAggregationState();
                break;
        }
    }

    public Aggregation getAggregation() {
        return getAggregation(analysisMeasure.getAggregation());
    }

    private Aggregation getAggregation(int value) {
        Aggregation aggregation;
        switch (value) {
            case AggregationTypes.SUM:
                aggregation = new SumAggregation();
                break;
            case AggregationTypes.AVERAGE:
                aggregation = new AverageAggregation();
                break;
            case AggregationTypes.MAX:
                aggregation = new MaxAggregation();
                break;
            case AggregationTypes.MIN:
                aggregation = new MinAggregation();
                break;
            case AggregationTypes.COUNT:
                aggregation = new CountAggregation();
                break;
            case AggregationTypes.NORMALS:
                ComplexAnalysisMeasure complexAnalysisMeasure = (ComplexAnalysisMeasure) analysisMeasure;
                aggregation = new NormalsAggregation(aggregationState, getAggregation(complexAnalysisMeasure.getWrappedAggregation()));
                aggregationState.addAggregation(aggregation);
                break;
            case AggregationTypes.LAST_VALUE:
                aggregation = new SumAggregation();
                break;
            default:
                throw new RuntimeException("unknown value " + value);
        }
        return aggregation;
    }
}
