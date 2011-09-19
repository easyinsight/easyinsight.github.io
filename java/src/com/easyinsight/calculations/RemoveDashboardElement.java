package com.easyinsight.calculations;

import com.easyinsight.core.EmptyValue;
import com.easyinsight.core.Value;
import com.easyinsight.dashboard.DashboardElement;
import com.easyinsight.dashboard.DashboardStack;
import com.easyinsight.dashboard.DashboardStackItem;
import com.easyinsight.dashboard.IDashboardVisitor;

import java.util.Iterator;

/**
 * User: jamesboe
 * Date: 9/19/11
 * Time: 3:27 PM
 */
public class RemoveDashboardElement extends Function {
    public Value evaluate() {
        final String target = minusQuotes(0);

        final String toRemove = minusQuotes(1);

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
        return new EmptyValue();
    }

    public int getParameterCount() {
        return -1;
    }
}
