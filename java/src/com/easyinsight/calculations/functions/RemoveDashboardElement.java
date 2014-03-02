package com.easyinsight.calculations.functions;

import com.easyinsight.calculations.Function;
import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dashboard.*;

import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 9/19/11
 * Time: 3:27 PM
 */
public class RemoveDashboardElement extends Function {
    public Value evaluate() {
        if (calculationMetadata.getDashboard() != null) {
            final String target = minusQuotes(0);

            final String toRemove = minusQuotes(1);

            if ("".equals(target)) {
                Dashboard dashboard = calculationMetadata.getDashboard();
                DashboardStack dashboardStack = (DashboardStack) dashboard.getRootElement();
                Iterator<DashboardStackItem> iter = dashboardStack.getGridItems().iterator();
                while (iter.hasNext()) {
                    DashboardStackItem child = iter.next();
                    DashboardElement dashboardElement = child.getDashboardElement();
                    if (toRemove.equals(dashboardElement.getLabel())) {
                        iter.remove();
                    } else if (dashboardElement instanceof DashboardReport) {
                        DashboardReport dashboardReport = (DashboardReport) dashboardElement;
                        if (toRemove.equals(dashboardReport.getReport().getName())) {
                            iter.remove();
                        }
                    }
                }
            } else {
                IDashboardVisitor findStackVisitor = new IDashboardVisitor() {
                    public void accept(DashboardElement dashboardElement) {
                        if (target.equals(dashboardElement.getLabel())) {
                            DashboardStack dashboardStack = (DashboardStack) dashboardElement;
                            Iterator<DashboardStackItem> iter = dashboardStack.getGridItems().iterator();
                            while (iter.hasNext()) {
                                DashboardStackItem dashboardStackItem = iter.next();
                                if (toRemove.equals(dashboardStackItem.getDashboardElement().getLabel())) {
                                    iter.remove();
                                }
                            }
                        }
                    }
                };
                calculationMetadata.getDashboard().visit(findStackVisitor);
            }

        }
        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }
}
