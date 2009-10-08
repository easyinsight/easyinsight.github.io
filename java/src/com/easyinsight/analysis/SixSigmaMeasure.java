package com.easyinsight.analysis;

import com.easyinsight.core.Value;
import com.easyinsight.core.NumericValue;
import com.easyinsight.dataset.DataSet;

import javax.persistence.*;
import java.util.*;

import org.hibernate.Session;

/**
 * User: James Boe
 * Date: Apr 20, 2009
 * Time: 9:57:19 AM
 */
@Entity
@Table(name="six_sigma_measure")
@PrimaryKeyJoinColumn(name="analysis_item_id")
public class SixSigmaMeasure extends AnalysisMeasure {

    public static final int DEFECTS_PER_MILLION_OPPS = 1;
    public static final int DEFECT_PERCENTAGE = 2;
    public static final int YIELD_PERCENTAGE = 3;
    public static final int PROCESS_SIGMA = 4;

    @Column(name="sigma_type")
    private int sigmaType;

    @OneToOne
    @JoinColumn(name="defects_measure_id")
    private AnalysisMeasure totalDefectsMeasure;

    @OneToOne
    @JoinColumn(name="opportunities_measure_id")
    private AnalysisMeasure totalOpportunitiesMeasure;

    @Override
    public void updateIDs(Map<Long, AnalysisItem> replacementMap) {
        super.updateIDs(replacementMap);
        if (totalDefectsMeasure != null) {
            totalDefectsMeasure = (AnalysisMeasure) replacementMap.get(totalDefectsMeasure.getAnalysisItemID());
        }
        if (totalOpportunitiesMeasure != null) {
            totalOpportunitiesMeasure = (AnalysisMeasure) replacementMap.get(totalOpportunitiesMeasure.getAnalysisItemID());
        }
    }

    public int getSigmaType() {
        return sigmaType;
    }

    public void setSigmaType(int sigmaType) {
        this.sigmaType = sigmaType;
    }

    public AnalysisMeasure getTotalDefectsMeasure() {
        return totalDefectsMeasure;
    }

    public void setTotalDefectsMeasure(AnalysisMeasure totalDefectsMeasure) {
        this.totalDefectsMeasure = totalDefectsMeasure;
    }

    public AnalysisMeasure getTotalOpportunitiesMeasure() {
        return totalOpportunitiesMeasure;
    }

    public void setTotalOpportunitiesMeasure(AnalysisMeasure totalOpportunitiesMeasure) {
        this.totalOpportunitiesMeasure = totalOpportunitiesMeasure;
    }

    public boolean isCalculated() {
        return true;
    }

    @Override
    public List<AnalysisItem> getAnalysisItems(List<AnalysisItem> allItems, Collection<AnalysisItem> insightItems, boolean getEverything) {
        List<AnalysisItem> objectList = new ArrayList<AnalysisItem>();
        objectList.addAll(Arrays.asList(totalDefectsMeasure, totalOpportunitiesMeasure));
        return objectList;
    }

    public Value calculate(DataSet dataSet, IRow row) {
        Value defectsValue = row.getValue(totalDefectsMeasure.createAggregateKey());
        Value opportunitiesValue = row.getValue(totalOpportunitiesMeasure.createAggregateKey());
        double defectsNum = defectsValue.toDouble();
        double opportunitiesNum = opportunitiesValue.toDouble();
        double resultNum;
        if (opportunitiesNum > 0) {
            switch (sigmaType) {
                case DEFECTS_PER_MILLION_OPPS:
                    resultNum = (defectsNum / opportunitiesNum) * 1000000;
                    break;
                case DEFECT_PERCENTAGE:
                    resultNum = (defectsNum / opportunitiesNum) * 100;
                    break;
                case YIELD_PERCENTAGE:
                    resultNum = 100 - ((defectsNum / opportunitiesNum) * 100);
                    break;
                case PROCESS_SIGMA:
                    resultNum = sixSigma(1 - (defectsNum / opportunitiesNum)) + 1.5;
                    break;
                default:
                    throw new RuntimeException("Unknown sigma type configured");
            }
        } else {
            resultNum = 0;
        }
        return new NumericValue(resultNum);
    }

    private double sixSigma(double p) {
        double[] a = {-3.969683028665376e+01, 2.209460984245205e+02, -2.759285104469687e+02, 1.383577518672690e+02, -3.066479806614716e+01, 2.506628277459239e+00};
        double[] b = {-5.447609879822406e+01, 1.615858368580409e+02, -1.556989798598866e+02, 6.680131188771972e+01, -1.328068155288572e+01};
        double[] c = {-7.784894002430293e-03, -3.223964580411365e-01, -2.400758277161838e+00, -2.549732539343734e+00, 4.374664141464968e+00, 2.938163982698783e+00};
        double[] d = {7.784695709041462e-03, 3.224671290700398e-01, 2.445134137142996e+00, 3.754408661907416e+00};

        // Define break-points.
        double plow = 0.02425;
        double phigh = 1 - plow;

        //# Rational approximation for lower region:
        if (p < plow) {
            double q = Math.sqrt(-2 * Math.log(p));
            return (((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5]) / ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        }

        // # Rational approximation for upper region:
        if (phigh < p) {
            double q = Math.sqrt(-2 * Math.log(1 - p));
            return -(((((c[0] * q + c[1]) * q + c[2]) * q + c[3]) * q + c[4]) * q + c[5]) / ((((d[0] * q + d[1]) * q + d[2]) * q + d[3]) * q + 1);
        }

        // # Rational approximation for central region:
        double q = p - 0.5;
        double r = q * q;
        return (((((a[0] * r + a[1]) * r + a[2]) * r + a[3]) * r + a[4]) * r + a[5]) * q / (((((b[0] * r + b[1]) * r + b[2]) * r + b[3]) * r + b[4]) * r + 1);
    }


    @Override
    public int getType() {
        return super.getType() | AnalysisItemTypes.SIX_SIGMA_MEASURE;
    }

    @Override
    public boolean isDerived() {
        return true;
    }

    @Override
    public List<AnalysisItem> getDerivedItems() {
        List<AnalysisItem> items = new ArrayList<AnalysisItem>();
        items.add(this);
        return items;
    }

    @Override
    public boolean isValid() {
        return getTotalDefectsMeasure() != null && getTotalOpportunitiesMeasure() != null;
    }

    public void reportSave(Session session) {
        if (totalDefectsMeasure.getAnalysisItemID() == 0) {
            session.save(totalDefectsMeasure);
        }
        if (totalOpportunitiesMeasure.getAnalysisItemID() == 0) {
            session.save(totalOpportunitiesMeasure);
        }
    }
}
